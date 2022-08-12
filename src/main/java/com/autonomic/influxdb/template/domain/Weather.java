package com.autonomic.influxdb.template.domain;

import com.autonomic.influxdb.template.annotations.Bucket;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Measurement(name="weather")
@Bucket(name="tss")
public class Weather {

  @Column(name="temperature")
  private Double temperature;
  @Column(name="humidity")
  private Double humidity;
  @Column(tag = true)
  private String country;
  @Column(tag = true)
  private String city;
  @Column
  private Double latitude;
  @Column
  private Double longitude;
  @Column(timestamp = true, name = "time")
  private Instant timestamp;

}
