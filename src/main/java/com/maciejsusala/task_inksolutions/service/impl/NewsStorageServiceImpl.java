package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.repository.NewsArticleRepository;
import com.maciejsusala.task_inksolutions.service.NewsStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NewsStorageServiceImpl implements NewsStorageService {

    private final NewsArticleRepository newsArticleRepository;

    public NewsStorageServiceImpl(NewsArticleRepository newsArticleRepository) {
        this.newsArticleRepository = newsArticleRepository;
    }

    @Override
    @KafkaListener(topics = "processed-news")
    public void storeNews(List<NewsArticle> newsArticles) {
        newsArticleRepository.saveAll(newsArticles);
        log.info("Stored {} news articles", newsArticles.size());
    }
}