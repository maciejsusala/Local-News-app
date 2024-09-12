package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.repository.NewsArticleRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsStorageService {

    private final NewsArticleRepository newsArticleRepository;

    public NewsStorageService(NewsArticleRepository newsArticleRepository) {
        this.newsArticleRepository = newsArticleRepository;
    }

    @KafkaListener(topics = "processed-news")
    public void storeNews(List<NewsArticle> newsArticles) {
        newsArticleRepository.saveAll(newsArticles);
        //TODO logger
        System.out.println("Stored " + newsArticles.size() + " news articles");
    }
}