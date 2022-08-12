package com.autonomic.influxdb.template;

import com.autonomic.influxdb.template.repository.InfluxDBRepository;
import com.autonomic.influxdb.template.repository.InfluxDBRepositoryImpl;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.QueryReactiveApi;
import java.lang.reflect.ParameterizedType;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class InfluxdbTemplate<T> extends InfluxDBRepositoryImpl<T> {

  @Autowired
  private InfluxDBClientReactive client;

  private Class<T> clazz;

  public Flux<T> query(String query) {
    clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
        getActualTypeArguments()[0];
    long startTime = System.nanoTime();

    QueryReactiveApi queryReactiveApi = this.client.getQueryReactiveApi();
    Publisher<T> publisher = queryReactiveApi.query(query, clazz);

    return Flux.from(publisher)
        .doOnComplete(() -> {
          log.debug("Query time: " + ((System.nanoTime() - startTime) / 1000000));
        });
  }

}
