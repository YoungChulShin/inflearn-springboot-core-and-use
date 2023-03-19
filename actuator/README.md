# Spring 모니터링 
## Actuator
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

### 보안
actuator 포트 변경
```yaml
management:
  server:
    port: 9092
```

base url 변경
```yaml
management:
  endpoints:
    web:
      base-path: "/manage"
```

### 톰캣
톰캣 관련 지표 활성화
```yaml
server:
  tomcat:
    mbeanregistry:
      enabled: true
```
- 위 지표를 활성화하면 톰캣 관련 더 많은 지표를 볼 수 있다
- 유용한 지표
   ```
   "tomcat.threads.busy"
   "tomcat.threads.config.max"
   "tomcat.threads.current"
   ```

### Custom metric - Counter
개념
- 시간이 지날 수록 값이 증가하는 카운터 메트릭을 등록한다

방법 1 - `Counter`를 이용해서 직접 등록하는 방법
```java
// my.order 로 메트릭이 등록된다
// tag를 이용해서 프로메테우스에서 값을 조회할 수 있다
Counter.builder("my.order")
        .tag("class", this.getClass().getName())
        .tag("method", "order")
        .description("order")
        .register(registry).increment();
```

방법 2 - `'@Counted'` 애노테이션 사용
1. 메트릭을 적용할 메서드에 애노테이션 적용
   ```java
   // my.order로 메트릭이 등록
   // 클래스, 메서드 명 등이 태그로 등록된다
   @Counted("my.order")
   public void order() { }
   ```
2. 애노테이션이 동작할 수 있도록 Aspect 등록
   ```java
   @Bean
   public CountedAspect countedAspect(MeterRegistry registry) {
     return new CountedAspect(registry);
   }
   ```

### Custom metric - Timer
개념
- 요청의 누적실행 시간, 누적 실행 횟수, 최대 실행 시간을 확인할 수 있다
- 누적실행 시간과 누적실행 횟수를 이용하면 평균 실행시간을 구할 수 있다

메트릭 정보
```
my_order_seconds_count: 누적 실행 횟수
my_order_seconds_sum: 누적 실행 시간
my_order_seconds_max: 최대 실행 시간
```

방법 1 - `Timer`를 이용해서 직접 등록하는 방법
```java
@Override
public void order() {
   // time 구현체를 생성. 
   // my.order 로 메트릭을 등록한다
   Timer timer = Timer.builder("my.order")
      .tag("class", this.getClass().getName())
      .tag("method", "order")
      .description("order")
      .register(registry);

   // 시간을 측정할 부분을 record() 메서드를 이용해서 감싸준다
   timer.record(() -> {
   log.info("주문");
   stock.decrementAndGet();
   sleep(500);
   });
}
```

방법 2- `'@Timed'` 애노테이션을 이용해서 등록하는 방법
1. 메트릭을 적용할 메서드에 애노테이션 추가
   ```java
   // my.order 타이머 메트릭이 등록된다
   @Timed("my.order")
   public void order() { {}
   ```
2. 애노테이션을 활성화하기 위해서 `TimedAspect`를 빈으로 등록해준다
   ```java
   @Bean
   public TimedAspect timedAspect(MeterRegistry registry) {
     return new TimedAspect(registry);
   }
   ```

기타
- 구간별 평균 실행 시간을 구하려면 'increase' 함수로 묶어 준다
   ```
   increase(my_order_seconds_sum[1m]) / increase(my_order_seconds_count[1m])
   ```


## 마이크로미터
개념
- 표준 측정 방식을 제공한다
- overview
   ![overview](/images/micrometer_overview.png)
- 표준 측정 방식을 이용해서 데이터를 수집하고, 모니터링 툴 구현체에 맞게 변환해준다

## 프로메테우스
개념
- 메트릭 정보를 보관하는 DB
- 공식 사이트: https://prometheus.io

설치 및 실행
- 설치 1: homebrew
   ```
   // 설치
   brew install prometheus

   // 설정 파일이 포함된 곳에서 prometheus 실행
   '/opt/homebrew/etc' 경로로 이동해서 prometheus를 실행한다
   ```
- 설치 2: 직접 다운로드해서 사용
   - homepage에서 파일을 다운받는다
   - mac의 경우 '보안 및 개인정보' 탭에서 prometheus가 실행될 수 있도록 허용한다
   - 다운받은 폴더에서 'prometheus'를 실행한다
- 접속은 `'localhost:9090'` 으로 접속한다

스프링 연동
- 마이크로미터 의존성 추가
   ```
   implementation 'io.micrometer:micrometer-registry-prometheus'
   ```
  - 이 의존성이 추가되면 'actuator' 정보에 'prometheus'가 추가된다. 
     ```
     "prometheus": {
      "href": "http://localhost:8080/actuator/prometheus",
      "templated": false
     },
     ```
- 프로메테우스 설정에서 job 추가
   - `'prometheus.yml'` 파일에 job을 추가한다
   - 'scrape_configs' 하위에 추가한다
      ```yml
      # spring job
      - job_name: "spring actuator"
        metrics_path: '/actuator/prometheus'  # 메트릭 수집 경로
        scrape_interval: 1s # 메트릭 수집 주기
        static_configs:
          - targets: ["localhost:8080"] # 서버 정보
      ```
  - '수집 주기'는 기본 값은 1분이고, 상용에서는 서버 부하를 고려해서 10m ~ 1m 정도로 설정한다. 
- 설정 파일 변경 이후에 프로메테우스 재실행
   - 'status -> configuration' 로 들어가면 수정한 설정 정보를 확인할 수 있다
   - 'status -> targets' 로 들어가면 target 정보 및 상태를 알 수 있다

레이블 일치 연산자
- `'='`: 동일한 값 선택
- `'!='`: 다른 값 선택
- `'=~'`: 정규식이 일치하는 값 선택
- `'!~'`: 정규식이 일치하지 않는 값 선택
   ```
   // uri = /log, method = GET
   {uri="/log", method="GET"}

   // GET or POST
   {method=~"GET|POST"}

   // acuator로 시작하는 것 제외
   {uri!~"/actuator.*"}
   ```

데이터 타입 - 게이지

데이터 타입 - 카운트
- 계속 증가하는 값. 계속 증가하기 때문에 인사이트를 얻기 어렵다. 
- increase, rate 등을 이용해서 변화량을 체크해준다
   ```
   // 분당 '/log' 요청이 얼마나 증가했는가
   increase(xxx_count{uri="/log}[1m])
   ```
- rate: increase와 비슷하지만 비율로 보여준다
- irate: 뭔가 급격하게 증가한 값을 알고 싶을 때 사용
   

## 그라파나
개념
- 메트릭 정보를 보여주는 대시보드

설치
- 직접 다운로드 방법
   1. homepage에서 [다운로드](https://grafana.com/grafana/download?platform=mac)
   2. 압축을 풀고, bin 폴더에서 `'grafana-server'` 실행
   3. 'localhost:3000' 접속

프로메테우스 연동
1. 'configuration' -> 'add datasource'
2. prometheus 선택
3. 프로메테우스 url 입력. (예: http://localhost:9090)
4. 'Save & test' 버튼 클릭 
5. 'configuration'을 다시 들어오면 prometheus 등록 확인

대시보드 활용
- https://grafana.com/grafana/dashboards/
- 키워드로 검색할 수 있다. 예: spring
   - https://grafana.com/grafana/dashboards/4701-jvm-micrometer/
   - https://grafana.com/grafana/dashboards/11378-justai-system-monitor/

## 전체 연동
![grafana-prometheus](/images/grafana-prometheus.png)

![prometheus](/images/prometheus.png)