package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.CsvNewsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CsvNewsReaderImpl implements CsvNewsReader {

    private static final Logger logger = LoggerFactory.getLogger(CsvNewsReaderImpl.class);

    @Override
    public List<NewsArticle> readNewsFromCsv() {
        List<NewsArticle> articles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("news.csv").getInputStream()))) {
            Stream<String> lines = reader.lines().skip(1); // Skip header
            List<String> lineList = lines.toList();
            for (int i = 0; i < lineList.size(); i++) {
                logger.info("Adding article number: {}", i + 1);
                articles.add(mapToArticle(lineList.get(i)));
            }
        } catch (Exception e) {
            logger.error("Failed to import news", e);
            throw new RuntimeException("Failed to import news", e);
        }
        return articles;
    }

    private NewsArticle mapToArticle(String line) {
        String[] fields = line.split(",");
        NewsArticle newsArticle = new NewsArticle();
        newsArticle.setTitle(fields[0]);
        newsArticle.setContent(fields[1]);
        newsArticle.setLocal(false);
        newsArticle.setLocation("Global");
        return newsArticle;
    }
}