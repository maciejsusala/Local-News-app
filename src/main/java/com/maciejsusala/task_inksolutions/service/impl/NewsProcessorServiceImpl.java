package com.maciejsusala.task_inksolutions.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.NewsProcessorService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
@Service
public class NewsProcessorServiceImpl implements NewsProcessorService {

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
        String prompt = createPrompt(newsArticle);
        String response = getGpt4Response(prompt);
        return parseResponse(response, newsArticle);
    }

    private String createPrompt(NewsArticle newsArticle) {
        return String.format(
                "Analyze the following news article and determine if it's local or global news. " +
                        "If the news is about a specific city in the USA, set 'local' to true and 'city' to the city name only (without state, country, etc.). " +
                        "If the news is about a city outside the USA or does not mention a specific city, set 'local' to false and 'city' to 'Global'. " +
                        "If the news mentions a district or borough (e.g., Manhattan), map it to the corresponding city (e.g., New York). " +
                        "Return the result in JSON format with fields 'local' and 'city'. " +
                        "Title: %s\n" +
                        "Article: %s",
                newsArticle.getTitle(),
                newsArticle.getContent()
        );
    }

    private String getGpt4Response(String prompt) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(List.of(new ChatMessage("user", prompt)))
                .model("gpt-4o-mini")
                .maxTokens(100)
                .build();

        String response = openAiService.createChatCompletion(completionRequest).getChoices().getFirst().getMessage().getContent();
        log.info("Chat response: {}", response);

        return response.replaceAll("```json", "").replaceAll("```", "").trim();
    }

    private NewsArticle parseResponse(String response, NewsArticle newsArticle) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            newsArticle.setLocal(jsonNode.get("local").asBoolean());
            newsArticle.setCity(jsonNode.get("city").asText());
        } catch (JsonProcessingException e) {
            log.error("Error parsing response: {}", e.getMessage());
            newsArticle.setLocal(false);
            newsArticle.setCity("Global");
        }
        return newsArticle;
    }
}