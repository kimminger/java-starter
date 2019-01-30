package com.elderbyte.spring.boot.bootstrap.data.continuation;

import com.elderbyte.commons.data.contiunation.worker.ContinuableBatchWorker;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

public class ContinuableWorkerInstrumented {

    @Autowired
    private MeterRegistry meterRegistry;

    private long totalItemsValue;

    private Counter handledItems;
    private Timer loadingTimer;
    private Timer processingTimer;

    public void instrument(){

        handledItems = meterRegistry.counter("continuable.worker.items.count");

        loadingTimer = Timer.builder("continuable.worker.request")
                .publishPercentiles(0.3, 0.5, 0.95)
                .register(meterRegistry);

        processingTimer = Timer.builder("continuable.worker.processing")
                .publishPercentiles(0.3, 0.5, 0.95)
                .register(meterRegistry);

        Gauge.builder("continuable.worker.items.total", this, (a) -> (double)totalItemsValue)
                //.tags(tags)
                .register(meterRegistry);
    }

    public void instrument(ContinuableBatchWorker<?> worker){
        worker
                .processingMetrics(record -> {

                    totalItemsValue = record.getTotalItems().orElse(0L);

                    handledItems.increment(record.getBatchSize());


                    processingTimer.record(Duration.ofNanos(record.getBatchProcessingTime()));

                })
                .loadingMetrics(r -> {
                    loadingTimer.record(Duration.ofNanos(r.getBatchLoadingTime()));
                });
    }
}
