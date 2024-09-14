package com.maciejsusala.task_inksolutions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "articles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewsArticle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("title")
    @Column(nullable = false)
    private String title;

    @JsonProperty("content")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isLocal;

    @Column
    private String city;
}