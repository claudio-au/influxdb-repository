package com.autonomic.influxdb.template.repository;

import reactor.core.publisher.Flux;

public interface InfluxDBRepository<T> {

  void save(T measurement);
  Flux<T> findByRangeAndLimitPivot(String start, String end, int limit);

}
