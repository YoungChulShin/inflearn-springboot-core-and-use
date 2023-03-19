package study.spring.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import study.spring.actuator.order.gauge.v1.StockConfigV1;
import study.spring.actuator.order.gauge.v1.StockConfigV2;
import study.spring.actuator.order.v0.OrderConfigV0;
import study.spring.actuator.order.v1.OrderConfigV1;
import study.spring.actuator.order.v2.OrderConfigV2;
import study.spring.actuator.order.v3.OrderConfigV3;
import study.spring.actuator.order.v4.OrderConfigV4;

//@Import(OrderConfigV0.class)
//@Import(OrderConfigV1.class)
//@Import(OrderConfigV2.class)
//@Import(OrderConfigV3.class)
//@Import({OrderConfigV4.class, StockConfigV1.class})
@Import({OrderConfigV4.class, StockConfigV2.class})
@SpringBootApplication(scanBasePackages = "study.spring.actuator.controller")
public class ActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActuatorApplication.class, args);
	}

}
