#!/bin/bash -e

cd $( (cd $(dirname $0) && echo $(pwd -P)) )
exec java \
	-Xmx100m \
	-D'com.sun.management.jmxremote.port=1123' \
	-D'com.sun.management.jmxremote.ssl=false' \
	-D'com.sun.management.jmxremote.access.file=access.txt' \
	-D'com.sun.management.jmxremote.password.file=passwd.txt' \
	-jar ../../target/jmxproxy-2.1.1-SNAPSHOT.jar server jmxproxy.yml
