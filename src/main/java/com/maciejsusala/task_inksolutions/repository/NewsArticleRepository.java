package com.maciejsusala.task_inksolutions.repository;


import com.maciejsusala.task_inksolutions.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, String> {
}