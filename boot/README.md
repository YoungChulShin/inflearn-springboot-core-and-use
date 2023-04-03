# 저장소 설명
스프링 부트를 이용한 웹서버 실행 실습

# SpringBootJar
## 실행 가능한 Jar(Executable Jar)
- 스프링부트의 Jar는 일반 Jar와는 구조가 다르다. FatJar는 아니지만 실행가능한 Jar가 생성된다
- Jar가 Jar를 포함할 수 있다
   - 라이브러리를 확인하기 쉽다
   - Jar를 가지기 때문에 클래스파일 이름이 겹치지 않는다
- 자바 표준은 아니고, 스프링부트에서 정의한 것


## 구조
- META-INF: Manifest 정보
- org/springframework/boot/loader
   - JarLauncher.class: 스프링부트 `main()` 실행 클래스
     - JarLauncher가 jar를 읽어 들이고, 이후에 `Start-Class`에 있는 main() 메서드를 실행해준다
- BOOT-INF
   - classes: 우리가 개발한 class 파일과 리소스 파일
   - lib: 외부 라이브러리
- classpath.idx: 외부 라이브러리 경로
- layers.idx: 스프링부트 구조 경로

## 실행 과정
1. `jar -jar xxx.jar` 실행
2. `MANIFEST.MF` 인식
3. `JarLauncher.main()` 실행
   - BOOT-INF/classes 인식
   - BOOT-INF/lib 인식
4. `BootApplication.main()`(=Start-Class) 실행