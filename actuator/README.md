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

### health
서버의 상태를 보여준다. 기본은 1줄의 status만 보이고, 상세정보를 보려면 아래 옵션을 추가한다.
```yml
endpoint:
  health:
    show-components: always
    #show-details: always**
```
- 각 항목(key)도 활성화/비활성화 선택할 수 있다.


레퍼런스
- https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health