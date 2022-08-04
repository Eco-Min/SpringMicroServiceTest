## 모듈에 에 spring cloud config 연동
- git commit 으로 config 정보를 저장 한다
- MSA config 정보들은 한곳에 모아둔다
  git-local-repo -> git init
  - config-service - application.yaml 파일에 config 정보들을 넣는다.
    - config 정보들은 주로 git으로 관리하며 위치를 지정해주어야 한다.
  - User MicroService 에 Sprint cloud config가 연동이 되게한다.
  - resources/application.yml 에 git으로 관리하는 config 정보를 넣어둔다
- user-service 에서 spring cloud config 정보를 사용하기 위해 지정한다.
### config 모듈
```groovy
// build.gradle
implementation 'org.springframework.cloud:spring-cloud-starter-config:3.1.3'
implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.3'
implementation 'org.springframework.boot:spring-boot-starter-actuator:2.7.2'
```
```yaml
server:
  port: 8888

spring:
  application:
    name: config-service
  cloud:
    config:
      server:
        git:
          uri: file:///D:\\codes\\spring\\SpringMSAConfig\\git-local-repo
```
config 가 http://localhost:8888/ecommerce/default 이 부분을 통하여 web에서 확인 가능 
### user-service 모듈
```groovy
// build.gradle
implementation 'org.springframework.cloud:spring-cloud-starter-config:3.1.3'
implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.3'
implementation 'org.springframework.boot:spring-boot-starter-actuator:2.7.2'
```
2.4.0 이전 버전에는 config 에서 bootstrap 이 있었다고 합니다.   
이제는 없으므로 따로 빌드 시켜야 합니다.
actuator 는 config 정보들을 url로 쉽게 접근 및 수정하기위해 사용합니다.
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
user-service 모듈에서 하였던거와 갔습니다. 각각의 implements 들을 적용한 후    
application.yml, bootstrap.yml을 지정해주면 됩니다.   
후 application.yml -> route 설정을 해줘야 합니다.(user-service actuator 실행을 위해)
```yaml
....
  - id: user-service
    uri: lb://USER-SERVICE
    predicates:
      - Path=/user-service/actuator/**
      - Method=GET, POST
    filters:
      - RemoveRequestHeader=Cookie
      - RewritePath=/user-service/(?<segment>.*), /$\{segment}
....
```
## profiles 로 설정
여러 yaml 들을 관리 할때 profile 로 관리하는 경우가 있습니다.   
config 또한 같습니다.
ecommerce-dev, ecommerce-prod 같이 여러 profile로 관리하려 합니다.    
git-local-repo - ecommerce-dev, ecommerce-prod 만들어 줍니다.
```yaml
# ecommerce-dev
token:
  expiration_time: 86400000 #1day expiration token
  secret: DEVQ4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H

gateway:
  ip: xxx.xxx.xxx.xxx
```
```yaml
# ecommerce-prod
token:
  expiration_time: 86400000 #1day expiration token
  secret: PRODQ4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H

gateway:
  ip: xxx.xxx.xxx.xxx
```
두개를 등록 한 후 config 모듈을 재 실행 해보면   
http://localhost:8888/ecommerce-dev/default   
http://localhost:8888/ecommerce-prod/default   
위 두 url 을 통해 token.secret 값이 다른걸 확인 할 수 있습니다.
```yaml
# api-gateway, user-service 모듈
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: ecommerce
  profiles:
    active: dev
```
bootstrap.yml 파일에 profiles 로 실행을 시켜주면 dev 에 있는 값들이 실행이 됩니다.   
실제 api-gateway 에 prod, user-service 에 dev 로 따로 적용을 하면   
api-gateway 로 접속 (.../user-service/**) 시 token 정보가 맞지 않는 401 을띄우게 됩니다.

## github 에 config 를 저장한다면
config 모듈에 github repository 값을 넣어주면 됩니다.
```yaml
server:
  port: 8888

spring:
  application:
    name: config-service
  cloud:
    config:
      server:
        git:
          uri: githb 주소 로 이어지는것도 가능 합니다. github repository 값을 넣어 가능 합니다.
```