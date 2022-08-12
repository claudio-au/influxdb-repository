package com.autonomic.influxdb.template.config;

import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.InfluxDBClientReactiveFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.influxdb")
public class InfluxDBConfig {

  private String host;
  private char[] token;
  private String org;
  private String bucket;

  @Bean
  public InfluxDBClientReactive createInfluxConnection() {
    System.out.println("Creating influx connection");
    InfluxDBClientReactive client = InfluxDBClientReactiveFactory
        .create(this.getHost(), this.getToken(), this.getOrg(), this.getBucket());

    return client;
  }

}
