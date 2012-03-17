#!/bin/bash

cd `dirname ${BASH_SOURCE[0]}`

java -Dfile.encoding=UTF-8 -Xmx1024m -Xms512m  -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n -jar start.jar 
