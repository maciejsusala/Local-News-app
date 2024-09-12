package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.NewsProcessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class NewsProcessorServiceImpl implements NewsProcessorService {

    private final OpenAiService openAiService;
    private final KafkaTemplate<String, NewsArticle> kafkaTemplate;



    public NewsProcessorServiceImpl(KafkaTemplate<String, NewsArticle> kafkaTemplate, @Value("${OPEN_AI_KEY}") String openAiApiKey) {
        this.kafkaTemplate = kafkaTemplate;
        this.openAiService = new OpenAiService(openAiApiKey);
    }

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
                        "If it's local, specify the location it belongs to. Article: %s",
                newsArticle.getContent()
        );

        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("gpt-4o-mini")
                .prompt(prompt)
                .maxTokens(100)
                .build();

        String response = openAiService.createCompletion(completionRequest).getChoices().get(0).getText();

        if(response.toLowerCase().contains("local")) {
            newsArticle.setLocal(true);
            newsArticle.setLocation(extractLocation(response));
        } else {
            newsArticle.setLocal(false);
        }
        return newsArticle;
    }

    private String extractLocation(String response) {
        // TODO Implement location extraction logic here
        return "";
    }
}