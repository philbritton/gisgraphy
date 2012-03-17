#!/bin/bash

cd ../universalserialiser-commons
mvn clean eclipse:clean eclipse:eclipse
cd ../universalserialiser
mvn clean eclipse:clean eclipse:eclipse
cd ../gisgraphy-commons
mvn clean eclipse:clean eclipse:eclipse
cd ../gisgraphy-utils
mvn clean eclipse:clean eclipse:eclipse
cd ../gisgraphy
mvn clean eclipse:clean eclipse:eclipse
