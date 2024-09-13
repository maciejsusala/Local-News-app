package com.maciejsusala.task_inksolutions.controller;

import com.maciejsusala.task_inksolutions.service.NewsIngestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
public class NewsIngestionController {

    private final NewsIngestionService newsIngestionService;

    public NewsIngestionController(NewsIngestionService newsIngestionService) {
        this.newsIngestionService = newsIngestionService;
    }

    /**
     * Endpoint to ingest news data.
     * <p>
     * This endpoint is left in place to allow for easy updates of news data in the future.
     */

    //TODO secure endpoint with password
    @PostMapping("/ingest")
    public void ingestNews() {
        newsIngestionService.updateAndIngestNews();
    }
}