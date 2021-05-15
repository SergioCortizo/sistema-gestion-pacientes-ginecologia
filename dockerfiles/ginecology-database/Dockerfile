FROM mysql:8.0.23

RUN apt-get update && apt-get -y install locales
RUN sed -i '/es_ES.UTF-8/s/^# //g' /etc/locale.gen && locale-gen

ENV LANG es_ES.UTF-8  
ENV LANGUAGE es_ES:es  
ENV LC_ALL es_ES.UTF-8   