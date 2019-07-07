# Use latest jboss/base-jdk:8 image as the base
FROM jboss/base-jdk:8

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 12.0.0.Final
ENV WILDFLY_SHA1 b2039cc4979c7e50a0b6ee0e5153d13d537d492f 
ENV JBOSS_HOME /opt/jboss/wildfly
ENV JBOSS_DEPLOY ${JBOSS_HOME}/standalone/deployments/

USER root

RUN yum install -y nc && yum clean all

COPY wait-for-db.sh /opt/bin/
RUN chmod +x /opt/bin/wait-for-db.sh

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME \
    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

USER jboss

# Expose the ports we're interested in
# EXPOSE 8080

# welcome
COPY /conf/komeo-welcome-content /opt/jboss/wildfly/komeo-welcome-content

# database
COPY /conf/mysql/main/module.xml /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/
COPY /conf/mysql/main/mysql-connector-java-5.1.45-bin.jar /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/

# configuration
COPY /conf/standalone.xml  /opt/jboss/wildfly/standalone/configuration/
COPY /conf/standalone.conf /opt/jboss/wildfly/bin/

# Set environment variables for database access
ENV DB_HOST db
ENV DB_PORT 3306
ARG DB_NAME
ENV DB_NAME=${DB_NAME}
ARG DB_USER
ENV DB_USER=${DB_USER}
ARG DB_PASSWORD
ENV DB_PASSWORD=${DB_PASSWORD}

ENTRYPOINT ["/opt/bin/wait-for-db.sh"]

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-Djboss.socket.binding.port-offset=-3080"]

# Deploy war files, e.g. 
COPY /user/target/user.war ${JBOSS_DEPLOY}
COPY /gitlab/target/gitlab.war ${JBOSS_DEPLOY}

