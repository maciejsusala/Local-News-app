package com.maciejsusala.task_inksolutions.controller;

import com.maciejsusala.task_inksolutions.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Endpoint to import cities data.
     * <p>
     * This endpoint is left in place because there might be a future need for an administrator
     * to update the cities' database. Before deploying the application to production, this
     * endpoint would be secured with a password to prevent unauthorized access.
     */
    //TODO security endpoint with password
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public void importCities() {
        cityService.importCities();
    }
}