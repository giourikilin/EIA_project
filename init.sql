CREATE DATABASE IF NOT EXISTS Users;
-- INSERT INTO mysql.user (Host, User, authentication_string) VALUES ('%', 'root', password('root'));
-- GRANT ALL ON *.* TO 'root'@'%' WITH GRANT OPTION;
UPDATE mysql.user 
SET host = '%'
WHERE user = 'root';
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root';
flush privileges;