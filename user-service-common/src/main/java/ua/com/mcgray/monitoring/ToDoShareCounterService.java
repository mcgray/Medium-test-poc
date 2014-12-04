package ua.com.mcgray.monitoring;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Component;

/**
 * @author orezchykov
 * @since 01.12.14
 */

public class ToDoShareCounterService implements CounterService {

    private final MetricWriter writer;

    public ToDoShareCounterService(MetricWriter writer) {
        this.writer = writer;
    }

    @Override
    public void increment(String metricName) {
        this.writer.increment(new Delta<>(metricName, 1L));
    }

    @Override
    public void decrement(String metricName) {
        this.writer.increment(new Delta<>(metricName, -1L));
    }

    @Override
    public void reset(String metricName) {
        this.writer.increment(new Delta<>(metricName, 0L));
    }
}
