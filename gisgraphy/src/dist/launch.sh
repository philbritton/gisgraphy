#!/bin/bash

cd `dirname ${BASH_SOURCE[0]}`
LOG_FILE="./logs/gisgraphy.log"

# the ammount of memory depends on the amount of data in the fulltext engine. 
# you can decrease it if you haven't imported a lot of countries
touch $LOG_FILE
echo "gisgraphy starting ..."
echo "Logs are outptut to $LOG_FILE"
echo "Use the stop script (in the same directory) to shutdown"
java -DSTOP.PORT=8079 -DSTOP.KEY=stopkey -Dfile.encoding=UTF-8 -Xmx2048m -Xms512m -jar start.jar 2> $LOG_FILE &
