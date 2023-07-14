mvn clean
mvn package
rm -rf tmp
mkdir tmp
cd tmp
mkdir Attempt10
cd ..
cp target/Attempt10.war tmp/Attempt10/
cd tmp/Attempt10
jar -xvf Attempt10.war
rm Attempt10.war
cp -r WEB-INF/classes/META-INF/resources/* .
rm -rf WEB-INF/classes/META-INF/resources
jar -cvf Attempt10.war *
cp Attempt10.war ../../ROOT.war
rm -rf tmp
cd ../../
docker build -t app .