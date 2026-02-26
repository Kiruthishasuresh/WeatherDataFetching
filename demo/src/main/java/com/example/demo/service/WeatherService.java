package com.example.demo.service;

import com.example.demo.dto.TemperatureStatusDTO;
import com.example.demo.dto.WeatherResponseDTO;
import com.example.demo.entity.WeatherData;
import com.example.demo.repository.WeatherRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository repository;

    // ✅ Load CSV data automatically on startup
    @PostConstruct
    public void init() {
        loadCSVData();
    }



    public void loadCSVData() {
        try {
            if (repository.count() > 0) {
                System.out.println("CSV data already loaded.");
                return;
            }

            ClassPathResource resource = new ClassPathResource("testset.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            br.readLine(); // skip header

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm");

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                LocalDateTime dateTime = LocalDateTime.parse(data[0], formatter);
                String condition = data[1];

                // Handle empty numeric fields safely
                double temperature = data[2].isEmpty() ? 0.0 : Double.parseDouble(data[2]);
                double humidity = data[3].isEmpty() ? 0.0 : Double.parseDouble(data[3]);
                double pressure = data[4].isEmpty() ? 0.0 : Double.parseDouble(data[4]);
                double heatIndex = data[5].isEmpty() ? 0.0 : Double.parseDouble(data[5]);

                WeatherData weather = new WeatherData(
                        null,
                        dateTime,
                        temperature,
                        humidity,
                        pressure,
                        heatIndex,
                        condition
                );
                repository.save(weather);
            }
            br.close();
            System.out.println("CSV data loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Get all weather records
    public List<WeatherResponseDTO> getAllWeather() {
        List<WeatherData> allData = repository.findAll();
        List<WeatherResponseDTO> response = new ArrayList<>();
        for (WeatherData w : allData) {
            response.add(new WeatherResponseDTO(
                    w.getWeatherCondition(),
                    w.getTemperature(),
                    w.getHumidity(),
                    w.getPressure(),
                    w.getHeatIndex()
            ));
        }
        return response;
    }

    // ✅ Get weather records by date
    public List<WeatherResponseDTO> getWeatherByDate(LocalDate date) {
        List<WeatherData> weatherList = repository.findAll();
        List<WeatherResponseDTO> response = new ArrayList<>();
        for (WeatherData w : weatherList) {
            if (w.getDate().toLocalDate().equals(date)) {
                response.add(new WeatherResponseDTO(
                        w.getWeatherCondition(),
                        w.getTemperature(),
                        w.getHumidity(),
                        w.getPressure(),
                        w.getHeatIndex()
                ));
            }
        }
        return response;
    }

    // ✅ Get weather records by month (any year)
    public List<WeatherResponseDTO> getWeatherByMonth(int month) {
        List<WeatherData> allData = repository.findAll();
        List<WeatherResponseDTO> response = new ArrayList<>();
        for (WeatherData w : allData) {
            if (w.getDate().getMonthValue() == month) {
                response.add(new WeatherResponseDTO(
                        w.getWeatherCondition(),
                        w.getTemperature(),
                        w.getHumidity(),
                        w.getPressure(),
                        w.getHeatIndex()
                ));
            }
        }
        return response;
    }

    // ✅ Get monthly temperature stats for a year
    public List<TemperatureStatusDTO> getMonthlyStats(int year) {
        List<WeatherData> allData = repository.findAll();
        List<TemperatureStatusDTO> stats = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            List<Double> temps = new ArrayList<>();
            for (WeatherData w : allData) {
                if (w.getDate().getYear() == year && w.getDate().getMonthValue() == month) {
                    temps.add(w.getTemperature());
                }
            }
            if (temps.isEmpty()) continue;

            Collections.sort(temps);
            double min = temps.get(0);
            double max = temps.get(temps.size() - 1);
            double median;
            int size = temps.size();
            if (size % 2 == 0) {
                median = (temps.get(size / 2 - 1) + temps.get(size / 2)) / 2;
            } else {
                median = temps.get(size / 2);
            }

            stats.add(new TemperatureStatusDTO(month, max, median, min));
        }

        return stats;
    }
}