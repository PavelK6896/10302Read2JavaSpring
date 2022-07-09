
# 10302Read2JavaSpring

The post feed application. Allows you to write posts.




JAVA_HOME=`$HOME\.jdks\corretto-17.0.3\bin`  mvn test
JAVA_HOME=`$HOME\.jdks\corretto-17.0.3\bin`  mvn clean package -P dev -D skipTests -D maven.test.skip=true -e


http://localhost:8080/api/read2/swagger-ui/index.html
http://localhost:8080/api/read2/
