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

# 빌드
Jar에 Main 클래스를 지정하는 방법
- Jar task에 `Main-Class` attribute를 추가해준다
   ```
   manifest {
        attributes 'Main-Class': 'hello.embed.EmbedTomcatSpringMain'
   }
   ```
- 생성된 Jar를 풀어보면 `META-INF/MANIFEST.MF` 파일이 있다
   ```
   Manifest-Version: 1.0
   Main-Class: hello.embed.EmbedTomcatSpringMain
   ```

FatJar, UberJar
- 벼경
   - 일반적인 Jar는 Jar를 포함할 수 없다. 이로 인해서 일반적으로 생성된 Jar 파일은 라이브러리를 포함할 수가 없어서 코드를 실행할 수 없다
- FatJar 해결
   - FatJar는 라이브러리의 클래스 파일을 모두 확인해서 Jar 파일에 넣어주는 방법으로 해결한다.
- 문제
   - 모두 클래스로 풀려있어서 라이브러리가 포함되어 있는지 확인하기 어렵다
   - 파일명 중복을 해결할 수 없다