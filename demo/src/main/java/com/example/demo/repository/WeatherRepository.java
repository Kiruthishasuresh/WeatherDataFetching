package com.example.demo.repository;

import com.example.demo.entity.WeatherData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends MongoRepository<WeatherData, String> {
    List<WeatherData> findByDate(LocalDate date);
}