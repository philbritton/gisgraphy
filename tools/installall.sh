#!/bin/bash

cd ../universalserialiser-commons
mvn clean install -Dmaven.test.skip
cd ../universalserialiser
mvn clean install -Dmaven.test.skip
cd ../gisgraphy-commons
mvn clean install -Dmaven.test.skip
cd ../gisgraphy-utils
mvn clean install -Dmaven.test.skip

