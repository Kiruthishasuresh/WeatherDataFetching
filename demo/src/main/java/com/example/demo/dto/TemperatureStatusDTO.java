package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemperatureStatusDTO {
    private int month;
    private double maxTemperature;
    private double medianTemperature;
    private double minTemperature;
}