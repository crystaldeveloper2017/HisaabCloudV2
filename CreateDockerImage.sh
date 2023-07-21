mvn clean
mvn package
cp target/Attempt10.war ROOT.war
docker build -t crystaldevelopers2017/hisaabcloud:latest .