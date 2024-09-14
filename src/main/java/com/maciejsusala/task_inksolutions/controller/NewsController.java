package com.maciejsusala.task_inksolutions.controller;

import com.maciejsusala.task_inksolutions.model.NewsArticle;
import com.maciejsusala.task_inksolutions.service.NewsIngestionService;
import com.maciejsusala.task_inksolutions.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;
    private final NewsIngestionService newsIngestionService;

    public NewsController(NewsService newsService, NewsIngestionService newsIngestionService) {
        this.newsService = newsService;
        this.newsIngestionService = newsIngestionService;
    }

    /**
     * Endpoint to ingest news data.
     * <p>
     * This endpoint is left in place to allow for easy updates of news data in the future.
     */

    //TODO secure endpoint with password
    @PostMapping("/ingest")
    @ResponseStatus(HttpStatus.OK)
    public void ingestNews() {
        newsIngestionService.updateAndIngestNews();
    }

    @GetMapping("/city")
    @ResponseStatus(HttpStatus.OK)
    public List<NewsArticle> getNewsByCity(@RequestParam String city) {
        return newsService.getNewsByCity(city);
    }
}