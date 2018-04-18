#!/usr/bin/env bash

#cd postgres

docker run --name creation-db-postgres -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres

#cd ../mysql

#docker run --name=modfy-db-mysql -p 3306:3306 -d mysql/mysql-server

#PASSWORD=`docker logs modfy_db_mysql |grep 'GENERATED ROOT PASSWORD' |awk '{print $5}'`

#echo "RUN THE COMMAND IN MYSQL CONSOLE: ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword'"
#echo "PASSWORD FOR CONNECTION: $PASSWORD"

#Docker exec -it modfy_db_mysql mysql -uroot -p

#docker run --name modify-mongo -p 27017:27017 -d mongo

docker run --name modify-redis -p 6379:6379 -d redis




