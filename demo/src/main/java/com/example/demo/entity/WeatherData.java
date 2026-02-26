package com.example.demo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "weather")
public class WeatherData {

    @Id
    private String id;

    private LocalDateTime date;
    private double temperature;
    private double humidity;
    private double pressure;
    private double heatIndex;
    private String weatherCondition;
}