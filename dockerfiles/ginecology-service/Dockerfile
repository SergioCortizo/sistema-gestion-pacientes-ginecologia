FROM maven:3.6.3-openjdk-11

RUN apt-get update && apt-get -y install locales
RUN sed -i '/es_ES.UTF-8/s/^# //g' /etc/locale.gen && locale-gen

ENV LANG es_ES.UTF-8  
ENV LANGUAGE es_ES:es  
ENV LC_ALL es_ES.UTF-8

ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH