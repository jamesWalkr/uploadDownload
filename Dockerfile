#Base image
FROM openjdk:17-jdk

#Copying over the Jar file.
COPY target/fileUploadAndDownload-0.0.1-SNAPSHOT.jar fileUploadAndDownload.jar

#The final argument should match the name and location of the copied jar file.
CMD ["java", "-jar", "fileUploadAndDownload.jar"]