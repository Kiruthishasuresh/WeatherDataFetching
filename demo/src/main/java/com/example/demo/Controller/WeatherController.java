package com.example.demo.Controller;

import com.example.demo.dto.TemperatureStatusDTO;
import com.example.demo.dto.WeatherResponseDTO;
import com.example.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // ✅ Verify DB data count
    @GetMapping("/verify")
    public String verifyData() {
        long count = weatherService.getAllWeather().size();
        return "Total weather records in MongoDB: " + count;
    }

    @GetMapping("/all")
    public List<WeatherResponseDTO> getAllWeather() {
        return weatherService.getAllWeather();
    }

    @GetMapping("/date/{date}")
    public List<WeatherResponseDTO> getByDate(@PathVariable String date) {
        return weatherService.getWeatherByDate(LocalDate.parse(date));
    }

    @GetMapping("/month/{month}")
    public List<WeatherResponseDTO> getByMonth(@PathVariable int month) {
        return weatherService.getWeatherByMonth(month);
    }

    @GetMapping("/stats/{year}")
    public List<TemperatureStatusDTO> getMonthlyStats(@PathVariable int year) {
        return weatherService.getMonthlyStats(year);
    }
}