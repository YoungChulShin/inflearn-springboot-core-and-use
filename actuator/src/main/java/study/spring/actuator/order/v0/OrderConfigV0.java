package study.spring.actuator.order.v0;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.actuator.order.OrderService;

@Configuration
public class OrderConfigV0 {

  @Bean
  OrderService orderService() {
    return new OrderServiceV0();
  }

}
