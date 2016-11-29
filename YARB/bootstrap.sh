#!/usr/bin/env bash

apt-get update
apt-get upgrade

apt-get install --yes git \
    postgresql-9.3 \
    vim

#
# Ubuntu Equip 
#  Java 8 Equip
# Licence: MIT
# see http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html
# http://stackoverflow.com/questions/13018626/add-apt-repository-not-found
sudo echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_base.sh && bash equip_base.sh

sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get update -y
sudo apt-get install oracle-java8-installer -y
sudo apt-get install ant -y

sudo -u postgres psql -c "CREATE USER yarb WITH PASSWORD 'yarb4156';"
sudo -u postgres psql -c "DROP DATABASE stylease;"
sudo -u postgres psql -c "CREATE DATABASE stylease;"
sudo -u postgres psql -c "GRANT CONNECT ON DATABASE stylease TO yarb;"
sudo -u postgres psql -d stylease -f /vagrant/stylease.sql
sudo -u postgres psql -c "GRANT ALL ON DATABASE stylease TO yarb;"

for tbl in $(sudo -u postgres psql -qAt -c "select tablename from pg_tables where schemaname = 'public';" stylease) ; do
    sudo -u postgres psql -d stylease -c "ALTER TABLE ${tbl} OWNER TO yarb"
done

sudo cp /vagrant/psql_config/*.conf /etc/postgresql/9.3/main/
sudo service postgresql restart
