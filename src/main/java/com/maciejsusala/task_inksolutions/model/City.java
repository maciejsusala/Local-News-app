package com.maciejsusala.task_inksolutions.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cities")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(name = "state_id", nullable = false)
    private String stateId;

    @Column(name = "county_name", nullable = false)
    private String countyName;

    // Latitude and longitude information may be useful when retrieving the user's actual location in the future.
    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;



}