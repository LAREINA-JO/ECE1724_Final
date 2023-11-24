FROM wxwmatt/hadoop-release:2.1.0-hadoop3.3.1-java8

ENV HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
ADD entrypoint.sh /entrypoint.sh

RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
