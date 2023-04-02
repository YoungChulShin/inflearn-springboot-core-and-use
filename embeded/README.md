# 저장소 설명
내장 톰캣 사용 실습 저장소

# 내장톰캣을 이용한 컨테이너 설정 및 연결
방법
1. 톰캣을 설정을 한다
2. 스프링 컨테이너를 설정하고, 컴포넌트를 등록한다
   ```java
   AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
   appContext.register(HelloConfig.class);
   ```
3. 디스패처 서블릿을 생성하고 컨테이너와 연결한다
   ```java
   DispatcherServlet dispatcher = new DispatcherServlet(appContext);
   ```
4. 디스패처 서블릿을 톰캣 컨텍스트에 추가한다
   ```java
   Context context = tomcat.addContext("", "/");
   tomcat.addServlet("", "dispatcher", dispatcher);
   context.addServletMappingDecoded("/", "dispatcher");
   ```
5. 톰캣을 실행한다

