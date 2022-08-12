package com.autonomic.influxdb.template.domain.repository;

import com.autonomic.influxdb.template.InfluxdbTemplate;
import com.autonomic.influxdb.template.domain.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WeatherRepository extends InfluxdbTemplate<Weather> {
}