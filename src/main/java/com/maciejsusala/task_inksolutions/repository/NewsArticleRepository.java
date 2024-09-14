package com.maciejsusala.task_inksolutions.repository;


import com.maciejsusala.task_inksolutions.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, String> {
    List<NewsArticle> findByCity(String city);
}