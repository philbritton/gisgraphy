#!/bin/bash

cd ../universalserialiser-commons
mvn clean install -Dmaven.test.skip
cd ../universalserialiser
mvn clean install -Dmaven.test.skip
cd ../gisgraphy-commons
mvn clean install -Dmaven.test.skip
cd ../addressParser-commons
mvn clean install -Dmaven.test.skip
if [[ -d "../addressParser-http" ]]
then
	cd ../addressParser-http
	mvn clean install -Dmaven.test.skip
fi
cd ../gisgraphy-utils
mvn clean install -Dmaven.test.skip

