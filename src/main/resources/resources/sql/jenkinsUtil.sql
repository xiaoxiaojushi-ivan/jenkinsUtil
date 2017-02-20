create database jenkinsUtil character set utf8;
grant all privileges on jenkinsUtil.* to jenkins@'%' identified by 'jenkins';
flush privileges;
