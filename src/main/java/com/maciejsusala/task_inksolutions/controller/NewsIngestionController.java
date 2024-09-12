package com.maciejsusala.task_inksolutions.controller;

import com.maciejsusala.task_inksolutions.service.impl.NewsIngestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsIngestionController {

    private final NewsIngestionService newsIngestionService;

    public NewsIngestionController(NewsIngestionService newsIngestionService) {
        this.newsIngestionService = newsIngestionService;
    }

    @PostMapping("/ingest")
    public void ingestNews() {
        newsIngestionService.updateAndIngestNews();
    }
}