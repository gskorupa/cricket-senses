FROM java:8
# remember to declare appropriate name-version of the jar

COPY dist/sensesservice-1.0.2.jar /usr/cricket/
COPY src/java/cricket.json /usr/cricket/config/
#COPY script.js /usr/cricket/config/
COPY www/* /usr/cricket/www/
WORKDIR /usr/cricket
RUN mkdir /usr/cricket/data
VOLUME /usr/cricket/data
VOLUME /usr/cricket/www
VOLUME /usr/cricket/config
# remember to declare appropriate name-version of the jar
# remember to declare appropriate name of the service to run (-s option parameter)
CMD ["java", "-jar", "./sensesservice-1.0.2.jar", "-r", "-s", "SensesStore"]