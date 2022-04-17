## service2 띄우는 방법
다른 project를 만들어서 service를 띄워도 되지만 run configuration으로 가능   
- run configuration 새로 추가
  - VM options   
  -Dserver.port=9002 (server port 지정)   
  이렇게 되면 Eureka server 홈페이지에 Availability Zones, status 가   
  2개가 뒤는걸 확인할 수 있다.


- Terminal 에서 새로 build 하는 법
  - intellij -> Terminal 사용 할 예정 (다른 Terminal 가능) 

### port 번호를 0 으로
port 번호를 0 으로 하면 port random 값이 생성되어 실행이 된다    
-> 하지만 server_eureka 에서는 port를 0으로 인식해 몇개가 띄어졌는지 확인이 불가능하다.   
```yaml
eureka:
  instance:
    instance-id: ${}
```