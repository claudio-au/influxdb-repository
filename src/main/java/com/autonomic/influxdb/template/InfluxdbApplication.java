package com.autonomic.influxdb.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class InfluxdbApplication {

  public static void main(String[] args) {
    SpringApplication.run(InfluxdbApplication.class, args);
  }


}
