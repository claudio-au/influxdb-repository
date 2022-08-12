package com.autonomic.influxdb.template.repository;

import com.autonomic.influxdb.template.annotations.Bucket;
import com.autonomic.influxdb.template.config.InfluxDBConfig;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.QueryReactiveApi;
import com.influxdb.client.reactive.WriteReactiveApi;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.lang.reflect.ParameterizedType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

@Slf4j
public class InfluxDBRepositoryImpl<T> implements InfluxDBRepository<T>{

  @Getter
  @Setter

  @Autowired
  private InfluxDBClientReactive client;
  @Autowired
  private InfluxDBConfig config;


  private Class<T> clazz;
  private String bucket;
  private String measurement;


  private void processAnnotation() {
    if (this.clazz ==null) {
      this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
          .getActualTypeArguments()[0];
    }
    if (this.measurement==null && clazz.isAnnotationPresent(Measurement.class)) {
      this.measurement = clazz.getAnnotation(Measurement.class).name();
    } else {
      throw new BeanInitializationException("Missed Measurement annotation for class "+ clazz.toString());
    }

    if (this.bucket == null &&  clazz.isAnnotationPresent(Bucket.class)) {
      this.bucket = clazz.getAnnotation(Bucket.class).name();
    } else {
      this.bucket = config.getBucket();
    }
  }

  public void save(T measurement) {
    this.processAnnotation();
    WriteReactiveApi writeApi = client.getWriteReactiveApi();

    var publisher = writeApi.writeMeasurement(WritePrecision.NS, measurement);
    Disposable subscriber = Flowable.fromPublisher(publisher)
        .subscribe(success -> log.debug("Successfully written {} measurement", clazz.toString()));
    subscriber.dispose();
  }

  public Flux<T> findByRangeAndLimitPivot(String start, String end, int limit) {
    this.processAnnotation();
    long startTime = System.nanoTime();
    String query = "from(bucket: \""+this.bucket+"\")"
        + "  |> range(start: "+start+", stop: "+end+")"
        + "  |> filter(fn: (r) => r[\"_measurement\"] == \""+this.measurement+"\")"
        + "  |> limit(n: "+limit+") "
        + "  |> pivot(rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
    log.debug(query);
    QueryReactiveApi queryReactiveApi = client.getQueryReactiveApi();
    Publisher<T> publisher =queryReactiveApi.query(query, clazz);

    return Flux.from(publisher)
        .doOnComplete(() -> {
          log.debug("Query time: " + ((System.nanoTime() - startTime) /1000000));
        });
  }

}
