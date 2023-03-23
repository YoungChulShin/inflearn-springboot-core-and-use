# 서버 설명
WAS 위에서 동작하는 웹서버를 만들어본다.

# 코드 설명
## JAR
특징
- 'Java Archive'라고 하는 압축 파일. 클래스와 관련 리소스를 압축한 파일
- 직접 실행될 수도 있고, 다른 곳에서 라이브러리로 사용할 수도 있다
   - 예: java -jar test.jar
   - 직접 실행될 때에는 main() 메서드가 필요하다

## WAR
특징
- 'Web Application Archive' 용어이고, WAS에 배포할 때 사용되는 파일
- 웹 애플리케이션 위에서 실행되고, HTML 같은 정적 리소스와 클래스파일을 모두 포함한다

구조
- 'WEB-INF' -> 자바 클래스와 라이브러리, 그리고 설정 정보가 들어간다
   - 'classes': 실행 클래스 모음
   - 'libs': 라이브러리 모음
   - 'web.xml': 웹 서버 정적 배치 설정 (생략 가능)
- 'index.html': 정적 리소스

## 톰캣
시작 및 종료
- 시작: `bin/startup.sh`
- 종료: `bin/shutdown.sh`

war 배포
1. '*.war' 파일을 'webapps' 폴더에 복사한다
2. '*.war' 파일의 이름을 'ROOT.war'로 변경한다
3. 톰캣을 실행한다
4. 톰캣이 ROOT.war 파일의 압축을 푼다

## Servlet
톰캣같은 WAS는 서블릿 스펙을 맞추고 있고, 서블릿 스펙에 맞춰서 동작하도록 구현해야한다. 

Servlet을 컨테이너에 등록 방법
1. `WebServlet` 애노테이션 사용
   ```java
   @WebServlet(urlPatterns = "/test")
   public class TestServlet extends HttpServlet {
   ```
2. 직접 서블릿을 생성해서 등록하는 방법
   1. servlet 생성
      ```java
      public class HelloServlet extends HttpServlet {

         @Override
         protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("HelloServlet.service");
            resp.getWriter().println("hello servlet");
         }
      }
      ```
   2. 생성한 서블릿을 context에 등록하는 코드 작성. Interface가 있어야한다
      ```java
      public class AppInitV1Servlet implements AppInit {

      `  @Override
         public void onStartUp(ServletContext servletContext) {
            // 순수 서블릿 코드 등록
            Dynamic helloServlet = servletContext.addServlet("helloServlet", new HelloServlet());
            helloServlet.addMapping("/hello-servlet");
         }
      }
      ```
   3. 서블릿 컨테이너 초기화 코드에서 앞에서 생성한 서블릿을 컨텍스트에 등록하는 코드 작성
      ```java
      // 이 인터페이스를 구현한 클래스를 'Set<Class<?>> c'에 넘겨준다
      @HandlesTypes(AppInit.class)
      public class MyContainerInitV2 implements ServletContainerInitializer {

      @Override
      public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
         for (Class<?> appInitClass : c) {
            try {
            var appInit = (AppInit)appInitClass.getDeclaredConstructor().newInstance();
            appInit.onStartUp(ctx);
            } catch (Exception e) {
            throw new RuntimeException(e);
            }
         }
      }
      }`
      ```
   4. ServletContainerInitializer가 실행되도록 하기 위해서 'resources/META-INF.services' 경로에 `jakarta.servlet.ServletContainerInitializer` 파일을 만들고, 구현체(여기서는 MyContainerInitV2)를 package 이름 포함해서 등록해준다
## Servlet, Spring
SpringController를 Servlet에 등록하는 방법
1. 컨트롤러 생성 및 Configuration으로 등록
2. 앱 초기화 인터페이스를 구현하는 구현체 생성
3. 구현체에서 스프링 컨테이너 생성
   ```java
   AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
   ```
4. 스프링 컨테이너에 Configuration 등록
   ```java
   appContext.register(HelloConfig.class);
   ```
5. MVC dispatcher servlet 생성 및 스프링 컨터이네를 연결
   ```java
   // appContext가 스프링 컨테이너
   DispatcherServlet dispatcher = new DispatcherServlet(appContext);
   ```
6. dispatcher servlet을 스프링 컨테이너에 등록
   ```java
   Dynamic servlet = servletContext.addServlet("dispatcherV2", dispatcher);
   ```
7. dispatcher servlet에 매핑 등록
   ```java
   // /spring/* 요청이 dispatcher servlet을 통하게 된다
   servlet.addMapping("/spring/*");
   ```
8. 호출 테스트
   ```
   /spring/controller-url 을 입력하면 연결된다
   ```

스프링이 제공하는 방법으로 Servlet 등록
1. 컨트롤러 생성 및 Configuration으로 등록
2. `WebApplicationInitializer` 인터페이스를 구현하는 클래스를 생성한다
3. 내부에 앱 초기화 코드를 넣어준다 (앞에서 설명한 Servlet을 직접 등록하는 3번의 과정부터 진행하면 된다)
- 장점은 서블릿 초기화 코드를 작성하지 않아도 된다는 것이다

WebApplicationInitializer 인터페이스 코드 확인
- `spring-web` 라이브러리를 보면 'META-INF.services'에 'jakarta.servlet.ServletContainerInitializer' 파일이 존재한다
   - 해당 파일에는 'org.springframework.web.SpringServletContainerInitializer'가 등록되어 있다
- SpringServletContainerInitializer 파일을 들어가보면 'HandlesTypes'가 정의되어 있고, ServletContainerInitializer를 구현하고 있다.
   ```java
   @HandlesTypes(WebApplicationInitializer.class)
   public class SpringServletContainerInitializer implements ServletContainerInitializer {
   ```
- 즉, 'WebApplicationInitializer'를 구현하는 클래스를 작성하면, 서블릿 컨테이너 초기화 이후에 앱 초기화과정에서 호출이 된다. 

## 외장 서버, 내장 서버
외장 서버
- WAS(tomcat)에 WAR를 배포하는 방식. WAS를 실행해서 동작한다

내장 서버
- JAR안에 라이브러리와 WAS 라이브러리가 포함되는 방식. main()을 실행해서 동작한다. 

