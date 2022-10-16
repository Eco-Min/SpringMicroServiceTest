## kafka
./bin/windows/zookeeper-server-start.bat  ./config/zookeeper.properties
./bin/windows/kafka-server-start.bat  ./config/server.properties

./bin/windows/kafka-topics.bat --create --topic quickstart-events --bootstrap-server localhost:9092 --partitions 1

./bin/windows//kafka-topics.bat --bootstrap-server localhost:9092 --list

./bin/windows/kafka-console-producer.bat --broker-list localhost:9092 --topic quickstart-events
./bin/windows/kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning

### confluent
.\bin\windows\connect-distributed.bat .\etc\kafka\connect-distributed.properties

### postman
post 127.0.0.1:8083/connectors
```json
{
  "name" : "my-source-connect",
  "config" : {
    "connector.class" : "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url":"jdbc:mysql://localhost:3306/mydb",
    "connection.user":"root",
    "connection.password":"1234",
    "mode": "incrementing",
    "incrementing.column.name" : "id",
    "table.whitelist":"mydb.user",
    "topic.prefix" : "my_topic_",
    "tasks.max" : "1"
  }
}
```

get http://localhost:8083/connectors

### sink connect
post 127.0.0.1:8083/connectors
```json
{
  "name":"my-sink-connect",
  "config":{
    "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url":"jdbc:mysql://localhost:3306/mydb",
    "connection.user":"root",
    "connection.password":"1234",
    "auto.create":"true",
    "auto.evolve":"true",
    "delete.enabled":"false",
    "tasks.max":"1",
    "topics":"my_topic_user"
  }
}
```
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic my_topic_user
// sink connector 에 보내는거 -> user X my_topic_user O
{"schema":{"type":"struct","fields":[{"type":"int32","optional":false,"field":"id"},{"type":"string","optional":true,"field":"user_id"},{"type":"string","optional":true,"field":"pwd"},{"type":"string","optional":true,"field":"name"},{"type":"int64","optional":true,"name":"org.apache.kafka.connect.data.Timestamp","version":1,"field":"created_at"}],"optional":false,"name":"user"},"payload":{"id":4,"user_id":"producer","pwd":"producer","name":"producer_name","created_at":1663636047000}}