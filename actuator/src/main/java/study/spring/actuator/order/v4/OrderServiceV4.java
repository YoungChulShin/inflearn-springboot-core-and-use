package study.spring.actuator.order.v4;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import study.spring.actuator.order.OrderService;

@Slf4j
public class OrderServiceV4 implements OrderService {

  private AtomicInteger stock = new AtomicInteger(100);

  @Timed("my.order")
  @Override
  public void order() {
    log.info("주문");
    stock.decrementAndGet();
    sleep(500);
  }

  @Timed("my.order")
  @Override
  public void cancel() {
    log.info("취소");
    stock.incrementAndGet();
    sleep(200);
  }

  private static void sleep(int l) {
    try {
      Thread.sleep(l + new Random().nextInt(200));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public AtomicInteger getStock() {
    return stock;
  }
}
