package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvNewsReader {

    private static final String CSV_FILE_PATH = "newsArticles.csv";

    public List<NewsArticle> readNewsFromCsv() {
        List<NewsArticle> newsArticles = new ArrayList<>();

        try (Reader reader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("KEYWORDS", "SUMMARY", "TEXT", "TITLE", "URL").withSkipHeaderRecord())) {

            for (CSVRecord csvRecord : csvParser) {
                String title = csvRecord.get("TITLE");
                String content = csvRecord.get("TEXT");

                NewsArticle newsArticle = NewsArticle.builder()
                        .title(title)
                        .content(content)
                        .build();

                newsArticles.add(newsArticle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsArticles;
    }
}