package ua.com.mcgray.monitoring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

/**
 * @author orezchykov
 * @since 01.12.14
 */

public class ToDoShareGaugeService implements GaugeService {

    private final MetricWriter writer;

    final Map<String, Double> accumulator = new HashMap<>();


    public ToDoShareGaugeService(final MetricWriter metricWriter) {
        this.writer = metricWriter;
    }

    @Override
    public void submit(String metricName, double value) {
        if (metricName!=null && value != 0) {
            double metricValue = value;
            metricValue = updateAccumulator(metricName, metricValue);
            this.writer.set(new Metric<>(metricName, metricValue));
        }
    }

    private double updateAccumulator(String metricName, double metricValue) {
        synchronized (accumulator) {
            if (accumulator.containsKey(metricName)) {
                metricValue += accumulator.get(metricName);
            }
            accumulator.put(metricName, metricValue);
        }
        return metricValue;
    }
}
