-- replace YOURUSER and YOURIP by the Database values
 psql -U YOURUSER  -H YOURIP -c  "CREATE DATABASE gisgraphy WITH TEMPLATE = Template_postgis ENCODING = 'UTF8';"
