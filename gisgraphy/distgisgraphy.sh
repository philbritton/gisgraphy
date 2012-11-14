#!/bin/bash

cd `dirname ${BASH_SOURCE[0]}`
curDir=`pwd`

if [[ -e ../addressParser ]] 
then
	cd ../addressParser/
	mvn clean install -Dmaven.test.skip
	cp ./target/*.jar $curDir/data/libs/
	cd $curDir
else
	 exit "address parser is not present"
fi

if [[ -e ../addressParser-http ]] 
then
	cd ../addressParser-http/
	mvn clean install -Dmaven.test.skip
	cp ./target/*.jar $curDir/data/libs/
	cd $curDir
else
	 exit "address parser http connector is not present"
fi

if [[ -e ../extra/link.sh ]]
then
	cd ../extra/
	./link.sh
	cd $curDir
else
	exit "extra is not present" 
fi

if [[ -e ../ws-billing ]] 
then
	cd ../ws-billing
	mvn clean install -Dmaven.test.skip
	cp ./target/*.jar $curDir/data/libs/
	cd $curDir
else
	 exit "address parser is not present"
fi

mvn clean compile javadoc:javadoc war:war hibernate3:hbm2ddl assembly:assembly -Dmaven.test.skip  -Dresetdb=false -Pgisgraphy

rm $curDir/data/libs/*
cd ../extra/
./delete.sh
