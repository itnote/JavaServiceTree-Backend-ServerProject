FROM 313devgrp/jstree-service-framework-backend-app:2018.07.03
MAINTAINER 313DEVGRP <313@313.co.kr>

RUN rm -rf /usr/local/tomcat/webapps/rivalwar
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/local/tomcat/webapps/rivalwar.war