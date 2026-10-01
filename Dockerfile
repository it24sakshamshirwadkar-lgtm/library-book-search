FROM tomcat:9.0-jdk11
COPY target/library-book-search.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
