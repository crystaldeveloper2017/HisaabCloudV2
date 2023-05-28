### HisaabCloud_MAVEN
----------------------------------------------------------------------
Steps to Setup Development Environment
-----------------------------------
-----------------------------------

Step 1:
Clone the Code Base from 

https://github.com/crystaldeveloper2017/HisaabCloud_MAVEN

-----------------------------------



Step 2:
go to directory {ProjectDir}/src/main/java/com/crystal
and then clone 
https://github.com/crystaldeveloper2017/Frameworkpackage

-----------------------------------

Step 3:
go to directory {ProjectDir}\src\main\resources\META-INF\resources
https://github.com/crystaldeveloper2017/frameworkjsps

-----------------------------------

Step 4:

Create a new file.. /src/com/crystal/customizedpos/Configuration/Config.yaml

```
mysqlusername: ""
password: ""
host: "hisaabcloud.in"
port: "3306"
mySqlPath: "1"
schemaName: customizedpos_staging
projectName: customizedpos Staging
thread_sleep: 0
isAuditEnabled: "true"
copyAttachmentsToBuffer: "false"
persistentPath: "/home/ubuntu/ags_attachments/"
queryLogEnabled: "false"
sendEmail: "false"
```

Make Sure to replace the credentials in the first two lines.

Once the Above are Done 
Open this file 
{ProjectDir}\src\main\java\com\crystal\SpringBootServletJspApplication.java

and run it.. Once it runs, Try to open

http://localhost:8080

