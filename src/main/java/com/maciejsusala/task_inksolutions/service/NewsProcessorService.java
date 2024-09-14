package com.maciejsusala.task_inksolutions.service;

import com.maciejsusala.task_inksolutions.model.NewsArticle;

public interface NewsProcessorService {
    void processNews(NewsArticle newsArticle);
}
