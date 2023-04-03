# 저장소 설명
스프링부트의 의존성 관리 설명

# 의존성 관리 
`io.spring.dependency-management` 플러그인을 통해서 의존성 관리를 자동을 할 수 있다

사용하는 버전 정보
- https://github.com/spring-projects/spring-boot/blob/3.0.x/spring-boot-project/spring-boot-dependencies/build.gradle
- https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html#appendix.dependency-versions

# 스프링부트 스타터
특정 기능을 사용하기 편하게 다양한 라이브러리를 모아놓은 스타터 패키지

이름 패턴
- 공식: 'spring-boot-start-*'

특정 버전을 별도로 사용하고 싶을 때
```
ext['tomcat.version'] = '10.1.1'
```
- 레퍼런스: https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html#appendix.dependency-versions.properties