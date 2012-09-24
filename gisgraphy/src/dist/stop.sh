#!/bin/bash

cd `dirname ${BASH_SOURCE[0]}`

# the ammount of memory depends on the amount of data in the fulltext engine. 
# you can decrease it if you haven't imported a lot of countries
java -DSTOP.PORT=8079 -DSTOP.KEY=stopkey -Dfile.encoding=UTF-8 -Xmx2048m -Xms512m -jar start.jar --stop
