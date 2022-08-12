package com.autonomic.influxdb.template.controller;

import com.autonomic.influxdb.template.domain.Weather;
import com.autonomic.influxdb.template.domain.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HistoricalController {

  private final WeatherRepository weatherRepository;

  @GetMapping("/historic")
  public Flux<Weather> get(
      @RequestParam(name = "start", required = false, defaultValue = "-1h") String start,
      @RequestParam(name = "stop", required = false, defaultValue = "now()") String end,
      @RequestParam(name = "limit", required = false, defaultValue = "10000") int limit) {

    return this.weatherRepository.query("from(bucket: \"tss\")  "
        + "|> range(start: -30d, stop: now())"
        + "|> filter(fn: (r) => r[\"_field\"] == \"temperature\")"
        + "|> aggregateWindow(every: 1d, fn: mean, createEmpty: false)");
  }

  @GetMapping("/save")
  public void save(){
    Weather w = new Weather();
    w.setCity("test");
    w.setCountry("Canada");
    w.setHumidity(1.0);
    w.setTemperature(2.0);
    w.setLatitude(0.0);
    w.setLongitude(0.0);
    this.weatherRepository.save(w);
  }
}
