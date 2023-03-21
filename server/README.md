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