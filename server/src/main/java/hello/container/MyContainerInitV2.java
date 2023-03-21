package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

// 이 인터페이스를 구현한 클래스를 'Set<Class<?>> c'에 넘겨준다
@HandlesTypes(AppInit.class)
public class MyContainerInitV2 implements ServletContainerInitializer {

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    System.out.println("MyContainerInitV2.onStartup");
    System.out.println("MyContainerInitV2 c = " + c);
    System.out.println("MyContainerInitV2 ctx = " + ctx);

    for (Class<?> appInitClass : c) {
      try {
        var appInit = (AppInit)appInitClass.getDeclaredConstructor().newInstance();
        appInit.onStartUp(ctx);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
