package com.maciejsusala.task_inksolutions.repository;

import com.maciejsusala.task_inksolutions.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}