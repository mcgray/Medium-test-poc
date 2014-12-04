package ua.com.mcgray.monitoring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonProfilerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GaugeService gaugeService;

    private final CounterService counterService;

    @Autowired
    public CommonProfilerAspect(GaugeService gaugeService, CounterService counterService) {
        this.gaugeService = gaugeService;
        this.counterService = counterService;
    }

    @Pointcut("@annotation(Monitorable)")
    public void monitor(){}

    @Pointcut("@within(SimpleMonitorable) || @annotation(SimpleMonitorable)")
    public void simpleMonitor(){}

    @Around("monitor()")
    public Object profile(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        Object output = proceedingJoinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        String metricsName = proceedingJoinPoint.getSignature().toShortString();
        logger.debug(metricsName + ": Execution time: " + elapsedTime + " ms. (" + elapsedTime / 60000 + " minutes)");
        gaugeService.submit(metricsName + ".time", elapsedTime);
        counterService.increment(metricsName + ".count");
        return output;
    }

    @Around("simpleMonitor()")
    public Object simpleProfile(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object output = proceedingJoinPoint.proceed();
        String metricsName = proceedingJoinPoint.getSignature().toShortString();
        counterService.increment(metricsName + ".count");
        return output;
    }

    @AfterThrowing(value = "monitor() || simpleMonitor()", throwing = "e")
    public void throwingException(JoinPoint joinPoint, Throwable e) {
        String metricsName = joinPoint.getSignature().toShortString();
        logger.warn(metricsName + ": Exception occurred", e);
        counterService.increment(metricsName + ".exception.count");
    }

}
