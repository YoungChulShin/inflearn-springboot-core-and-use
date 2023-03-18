package study.spring.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import study.spring.actuator.order.v0.OrderConfigV0;
import study.spring.actuator.order.v1.OrderConfigV1;

//@Import(OrderConfigV0.class)
@Import(OrderConfigV1.class)
@SpringBootApplication(scanBasePackages = "study.spring.actuator.controller")
public class ActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActuatorApplication.class, args);
	}

}
