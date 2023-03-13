# Spring Actuator 
## Actuator 기본
기본 Url
- '/actuator' 로 들어가면 등록된 동작을 확인할 수 있다

제공 항목 추가
```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
       #include: "info, health"
```
전체 Endpoints 정보
- https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints

