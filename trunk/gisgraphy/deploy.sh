./distgisgraphy.sh
cd target;
mkdir release;
tar -xvf gisgraphy-1.0.tar.gz -Crelease;
mv release/* release/toupload;
scp -r -C release/toupload/webapps/ROOT gisgraphy@www.gisgraphy.com:trashbin
ssh gisgraphy@www.gisgraphy.com 'tomcatstop;rm -rf services/gisgraphy/ROOT/;mv trashbin/* ./services/gisgraphy/;tomcatstart;'