



http://localhost:8080/api/read2/swagger-ui/index.html

JAVA_HOME=`$HOME\.jdks\liberica-17.0.2\bin`  mvn test
JAVA_HOME=`$HOME\.jdks\liberica-17.0.2\bin`  mvn clean package -P dev -D skipTests -D maven.test.skip=true -e

