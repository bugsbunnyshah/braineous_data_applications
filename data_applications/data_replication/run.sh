cp ./pom.xml.clean ./pom.xml
mvn clean -U

cp ./pom.xml.package ./pom.xml
mvn package
java -jar target/data_replication-1.0-jar-with-dependencies.jar