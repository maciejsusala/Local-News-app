package com.maciejsusala.task_inksolutions.service;

import com.maciejsusala.task_inksolutions.model.NewsArticle;

import java.util.List;

public interface CsvNewsReader {
    List<NewsArticle> readNewsFromCsv();
}
