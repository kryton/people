# docker kill some-mysql
# docker rm some-mysql
docker kill multi
docker rm multi
# docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=mysecretpassword -d -p3308:3306 mysql
# note.. I'm running with Maria DB now.
# -- you'll need to edit this line to put in the correct volume mounts.. and link the DB up.
docker run -h offline --link some-mysql:DB -v c:/logs:/logs -v c:/pic/cache:/pic/cache -v c:/pic/source:/pic/source -v c:/pic/user-upload:/pic/user-upload -v c:/pic/xfer:/pic/xfer -d  -p 9001:9000 -p 9443:9443 --name=multi kryton/zilbo_people
