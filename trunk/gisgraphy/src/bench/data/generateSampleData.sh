 #!/bin/bash


export PGPASSWORD="mdppostgres";
databaseName="gisgraphy"
tableName="osm"
pgUser="postgres"
pgHost="127.0.0.1"
dataset_size="10000"

function psql_exportToCSV {
psql -U $pgUser  -d$databaseName -h$pgHost <<COPYTEST
\f '	' 
\a
\t
\o $2
$1
\q
COPYTEST
}


psql_exportToCSV "select replace(replace(astext(location), 'POINT(',''),')','') from city c where location is not null limit $dataset_size ;" "features_location.csv"
psql_exportToCSV "select featureid from city g where location is not null limit $dataset_size ;" "featureid.csv"
psql_exportToCSV "select replace(name,' ','%20') from city c where name  is not null and countrycode='FR' limit $dataset_size ;" "features_name.csv"
psql_exportToCSV "select replace(replace(astext(location), 'POINT(',''),')','') from openstreetmap o where location is not null limit $dataset_size ;" "streets_location.csv"
psql_exportToCSV "select replace(name,' ','%20')||'%20'||replace(isin,' ','%20')||','||countrycode from openstreetmap c where name is not null and isin is not null and countrycode='FR' limit $dataset_size ;" "address.csv"

