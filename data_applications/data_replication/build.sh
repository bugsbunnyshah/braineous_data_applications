cp ./pom.xml.clean ./pom.xml
mvn clean -U

cp ./pom.xml.package ./pom.xml
mvn package -Dquarkus.package.main-class=datarepl -DskipTests

cp target/*-runner.jar .