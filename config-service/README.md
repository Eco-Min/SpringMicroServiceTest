## 모듈에 에 spring cloud config 연동
- git commit 으로 config 정보를 저장 한다
- MSA config 정보들은 한곳에 모아둔다
  git-local-repo -> git init
  - config-service - application.yaml 파일에 config 정보들을 넣는다.
    - config 정보들은 주로 git으로 관리하며 위치를 지정해주어야 한다.
  - User MicroService 에 Sprint cloud config가 연동이 되게한다.
  - resources/application.yml 에 git으로 관리하는 config 정보를 넣어둔다
- user-service 에서 spring cloud config 정보를 사용하기 위해 지정한다.
### user-service 모듈
```groovy
// build.gradle
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-config
implementation 'org.springframework.cloud:spring-cloud-starter-config:3.1.3'
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap
implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.3'
// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
implementation 'org.springframework.boot:spring-boot-starter-actuator:2.7.2'
```
```yaml
# bootstrap.yml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: ecommerce
```
user-service application.yml 에서 만약 config 정보가 바뀌어도   
프로그램 재시작이 아닌 url 호출로 refresh 할 수 있다.
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: refresh, health, beans
```
- url : .../actuator/** 로 include 값이 들어가기 때문에 filter 에서   
제거 시켜 주어야 한다.
- 실제 gateway 에서 지정하는게 아닌 cloud 에서 지정받은 port 에서 진행이 된다.   
-> gateway 에서 user-service 실행이 되게 load balance 를 지정해야 한다. 

## Spring cloud gateway 에 spring cloud config 연동