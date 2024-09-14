package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.CsvNewsReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * I decided to separate the news reading logic this way to easily replace it with another solution in the future,
 * such as a scraper or an external API.
 */

@Slf4j
@Service
public class CsvNewsReaderImpl implements CsvNewsReader {

    @Override
    public List<NewsArticle> readNewsFromCsv() {
        List<NewsArticle> articles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("news.csv").getInputStream()))) {
            Stream<String> lines = reader.lines().skip(1); // Skip header
            List<String> lineList = lines.toList();
            for (int i = 0; i < lineList.size(); i++) {
                log.info("Adding article number: {}", i + 1);
                articles.add(mapToArticle(lineList.get(i)));
            }
        } catch (Exception e) {
            log.error("Failed to import news", e);
            throw new RuntimeException("Failed to import news", e);
        }
        return articles;
    }

    private NewsArticle mapToArticle(String line) {
        String[] fields = line.split(";");
        NewsArticle newsArticle = new NewsArticle();
        newsArticle.setTitle(fields[0]);
        newsArticle.setContent(fields[1]);
        newsArticle.setLocal(false);
        newsArticle.setCity("Global");
        return newsArticle;
    }
}