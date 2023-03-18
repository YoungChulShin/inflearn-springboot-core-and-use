package study.spring.actuator.order.v1;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.actuator.order.OrderService;

@Configuration
public class OrderConfigV1 {

  @Bean
  OrderService orderService(MeterRegistry registry) {
    return new OrderServiceV1(registry);
  }

}
