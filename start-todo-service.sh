nohup java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9800 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -jar todo-service-service/build/libs/ToDoService-Service-1.0-SNAPSHOT.jar --spring.config.name=todo-application.properties --spring.config.location=file:configs/todo-application.properties --logging.config=/Users/orezchykov/projects/Medium-test-poc/configs/todo-service-logback.xml > todo-service-nohup.log &