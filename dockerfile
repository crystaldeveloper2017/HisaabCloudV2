FROM tomcat:jre17

# Some code that copies the war into the webapps folder
COPY customizedpos_staging.war /usr/local/tomcat/webapps/

# run the tomcat server
CMD ["catalina.sh", "run"]

EXPOSE 8080