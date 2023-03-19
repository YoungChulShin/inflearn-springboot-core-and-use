package study.spring.actuator.order.v3;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.actuator.order.OrderService;
import study.spring.actuator.order.v0.OrderServiceV0;

@Configuration
public class OrderConfigV3 {

  @Bean
  OrderService orderService(MeterRegistry registry) {
    return new OrderServiceV3(registry);
  }

}
