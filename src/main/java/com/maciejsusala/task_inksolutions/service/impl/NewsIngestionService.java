package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsIngestionService {

    private final KafkaTemplate<String, NewsArticle> kafkaTemplate;
    private final CsvNewsReader csvNewsReader;

    public NewsIngestionService(KafkaTemplate<String, NewsArticle> kafkaTemplate, CsvNewsReader csvNewsReader) {
        this.kafkaTemplate = kafkaTemplate;
        this.csvNewsReader = csvNewsReader;
    }
    public void updateAndIngestNews() {
        List<NewsArticle> articles = csvNewsReader.readNewsFromCsv();
        for (NewsArticle article : articles) {
            kafkaTemplate.send("raw-news", article);
        }

        //TODO logger
        System.out.println("Ingested " + articles.size() + " news articles");
    }
}
