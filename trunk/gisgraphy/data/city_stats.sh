 #!/bin/bash

pgPassword="mdp"
export PGPASSWORD="$pgPassword";
databaseName="gisgraphy"
tableName="city"
pgUser="postgres"
#pgHost="127.0.0.1"
pgHost="88.191.82.6"
countrycode=$1
currentdir=`pwd`
#don't forget ending slash
exportDir="./data/"

if [[ -d $1 ]]
	then
		echo "countrycode is mandatory.usage:  countrycode admlevel"
		exit
	fi

if [[ -d $2 ]]
	then
		echo "admlevel is mandatory. usage:  countrycode admlevel"
		exit
	fi


function psql_exportToCSV {
     psql -d $databaseName -h $pgHost -U $pgUser  -c "COPY ($1) TO '$currentdir/$2' WITH CSV DELIMITER AS E'\t' " 
}

function psql_runsqlCommand {
     psql -d $databaseName -h $pgHost -U $pgUser  -c "$1" 
}


#for i in DE CH AU NL TR SG HK SA IR AT BE BR PT CA RU UA PL SE NO DK TN CN
#do
echo generating $1
psql_runsqlCommand "select count(*) as total_GEONAMES_city from city where countrycode='$1' and source='GEONAMES';"
psql_runsqlCommand "select count(*) as total_city from city where countrycode='$1';"
psql_runsqlCommand "select count(*) as total_adm from adm where adm$2code is not null and countrycode='$1';"
psql_runsqlCommand "select (select count(countrycode) as count from city where population!=0 and countrycode='$1' group by countrycode) as a_la_population, (select count(countrycode) as count from city where adm$2code is not  null  and countrycode='$1' group by countrycode) as a_le_code, (select count(countrycode) as count from city where adm$2code is not  null and population!=0 and countrycode='$1' group by countrycode) as a_les_deux,(select count(countrycode) as count from city where (adm$2code is not  null or population!=0) and countrycode='$1' group by countrycode) as a_l_un_ou_l_autre"
echo "as a_seulement_la_population";
psql_runsqlCommand "select name,population,adm$2code from city where population!=0 and countrycode='$1' and adm$2code is null limit 10 "
echo "as a_seulement_le code"
psql_runsqlCommand "select name,population,adm$2code from city where population=0 and countrycode='$1' and adm$2code is not null limit 10"
echo "n a ni l'un ni l autre"
psql_runsqlCommand "select name,population,adm$2code from city where population=0 and countrycode='$1' and adm$2code is null limit 20;"
psql_runsqlCommand "select name as a_pas_le_code ,population,adm$2code from city where adm$2code is null and countrycode='$1' limit 10;"

#done

