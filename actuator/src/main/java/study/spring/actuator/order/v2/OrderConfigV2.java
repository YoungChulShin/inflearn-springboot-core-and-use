package study.spring.actuator.order.v2;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.actuator.order.OrderService;

@Configuration
public class OrderConfigV2 {

  @Bean
  public OrderService orderService() {
    return new OrderServiceV2();
  }

  @Bean
  public CountedAspect countedAspect(MeterRegistry registry) {
    return new CountedAspect(registry);
  }

}
