package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.repository.NewsArticleRepository;
import com.maciejsusala.task_inksolutions.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsArticleRepository newsArticleRepository;

    public NewsServiceImpl(NewsArticleRepository newsArticleRepository) {
        this.newsArticleRepository = newsArticleRepository;
    }

    @Override
    public List<NewsArticle> getNewsByCity(String city) {
        return newsArticleRepository.findByCity(city);
    }
}
