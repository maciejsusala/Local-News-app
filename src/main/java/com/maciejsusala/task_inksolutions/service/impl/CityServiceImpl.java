package com.maciejsusala.task_inksolutions.service.impl;

import com.maciejsusala.task_inksolutions.model.City;
import com.maciejsusala.task_inksolutions.repository.CityRepository;
import com.maciejsusala.task_inksolutions.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private static final int BATCH_SIZE = 1000;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void importCities() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("cities.csv").getInputStream()))) {
            List<City> cities = new ArrayList<>();
            Stream<String> lines = reader.lines().skip(1); // Skip header
            List<String> lineList = lines.toList();
            for (int i = 0; i < lineList.size(); i++) {
                log.info("Adding city number: {}", i + 1);
                cities.add(mapToCity(lineList.get(i)));
                if (cities.size() == BATCH_SIZE) {
                    cityRepository.saveAll(cities);
                    cities.clear();
                }
            }
            if (!cities.isEmpty()) {
                cityRepository.saveAll(cities);
            }
        } catch (Exception e) {
            log.error("Failed to import cities", e);
            throw new RuntimeException("Failed to import cities", e);
        }
    }

    private City mapToCity(String line) {
        String[] fields = line.split(",");
        if (fields.length < 8) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }
        City city = new City();
        city.setCity(fields[0].replace("\"", "").trim());
        city.setStateId(fields[2].replace("\"", "").trim());
        city.setCountyName(fields[5].replace("\"", "").trim());
        city.setLat(Double.parseDouble(fields[6].replace("\"", "").trim()));
        city.setLng(Double.parseDouble(fields[7].replace("\"", "").trim()));
        return city;
    }
}