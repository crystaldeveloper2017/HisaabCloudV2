FROM tomcat:9.0.76-jre17

# Some code that copies the war into the webapps folder
COPY customizedpos_staging.war /usr/local/tomcat/webapps/

# Expose the port
EXPOSE 8080