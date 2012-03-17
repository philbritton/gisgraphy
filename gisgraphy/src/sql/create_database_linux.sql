-- replace YOURUSER and YOURIP by the Database values
 
psql -U YOURUSER  -H YOURIP -c  "CREATE DATABASE gisgraphy WITH TEMPLATE = template0 ENCODING = 'UTF8';"
