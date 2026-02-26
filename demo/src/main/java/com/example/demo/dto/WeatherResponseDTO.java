package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponseDTO {
    private String weatherCondition;
    private double temperature;
    private double humidity;
    private double pressure;
    private double heatIndex;
}