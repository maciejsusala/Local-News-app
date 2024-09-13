package com.maciejsusala.task_inksolutions.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.NewsProcessorService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class NewsProcessorServiceImpl implements NewsProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(NewsProcessorServiceImpl.class);

    private final OpenAiService openAiService;
    private final KafkaTemplate<String, NewsArticle> kafkaTemplate;


    public NewsProcessorServiceImpl(KafkaTemplate<String, NewsArticle> kafkaTemplate, @Value("${OPEN_AI_KEY}") String openAiApiKey) {
        this.kafkaTemplate = kafkaTemplate;
        this.openAiService = new OpenAiService(openAiApiKey);
    }

    @Override
    @KafkaListener(topics = "raw-news")
    public void processNews(NewsArticle newsArticle) {
        Mono.fromCallable(() -> processWithGpt4(newsArticle))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        processedArticle -> kafkaTemplate.send("processed-news", processedArticle),
                        error -> System.err.println("Error processing news: " + error.getMessage())
                );
    }

    private NewsArticle processWithGpt4(NewsArticle newsArticle) {
        String prompt = String.format(
                "Analyze the following news article and determine if it's local or global news. " +
                        "If the news is about a specific city in the USA, set 'local' to true and 'location' to the city name only (without state, country, etc.). " +
                        "If the news is about a city outside the USA or does not mention a specific city, set 'local' to false and 'location' to 'Global'. " +
                        "Return the result in JSON format with fields 'local' and 'location'. " +
                        "Article: %s",
                newsArticle.getContent()
        );

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(List.of(new ChatMessage("user", prompt)))
                .model("gpt-4o-mini")
                .maxTokens(100)
                .build();

        String response = openAiService.createChatCompletion(completionRequest).getChoices().getFirst().getMessage().getContent();
        logger.info("Chat response: {}", response);

        response = response.replaceAll("```json", "").replaceAll("```", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            newsArticle.setLocal(jsonNode.get("local").asBoolean());
            newsArticle.setLocation(jsonNode.get("location").asText());
        } catch (JsonProcessingException e) {
            logger.error("Error parsing response: {}", e.getMessage());
            newsArticle.setLocal(false);
            newsArticle.setLocation("Global");
        }

        return newsArticle;
    }
}