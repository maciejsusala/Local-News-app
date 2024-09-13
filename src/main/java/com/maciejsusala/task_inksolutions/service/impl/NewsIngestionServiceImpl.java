package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.CsvNewsReader;
import com.maciejsusala.task_inksolutions.service.NewsIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsIngestionServiceImpl implements NewsIngestionService {

    private final KafkaTemplate<String, NewsArticle> kafkaTemplate;
    private final CsvNewsReader csvNewsReader;
    private final static Logger logger = LoggerFactory.getLogger(NewsIngestionServiceImpl.class);


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

        logger.info("Ingested {} news articles", articles.size());
    }
}
