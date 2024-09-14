package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.CsvNewsReader;
import com.maciejsusala.task_inksolutions.service.NewsIngestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NewsIngestionServiceImpl implements NewsIngestionService {

    private final KafkaTemplate<String, NewsArticle> kafkaTemplate;
    private final CsvNewsReader csvNewsReader;


    public NewsIngestionServiceImpl(KafkaTemplate<String, NewsArticle> kafkaTemplate, CsvNewsReader csvNewsReader) {
        this.kafkaTemplate = kafkaTemplate;
        this.csvNewsReader = csvNewsReader;
    }

    @Override
    public void updateAndIngestNews() {
        List<NewsArticle> articles = csvNewsReader.readNewsFromCsv();
        for (NewsArticle article : articles) {
            kafkaTemplate.send("raw-news", article);
        }

        log.info("Ingested {} news articles", articles.size());
    }
}
