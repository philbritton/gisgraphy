#!/bin/sh -e
function check_root_password {
if [ `id -u` != 0 ]; then
        read -p "this will install gisgraphy as a daemon. It will start Gisgraphy at startup. It has been tested on Ubuntu. No waranty are given. You must run this script as root. Press CTRL+C to abort and run the script as root or enter the root password : " rootpassword
fi
}

check_root_password
echo "giving rights to scripts"
chmod a+rx ./startupscript
chmod a+rx ./launch.sh
chmod a+rx ./stop.sh
echo "copying script"
sudo cp ./startupscript /etc/init.d/gisgraphy
echo "removing old startup script if necessary"
sudo update-rc.d -f gisgraphy remove
echo "adding startup script"
sudo update-rc.d gisgraphy defaults
