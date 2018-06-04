drop database if exists `Acme-Chollos-Rifas`;
create database `Acme-Chollos-Rifas`;

grant select, insert, update, delete
on `Acme-Chollos-Rifas`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter,
create temporary tables, lock tables, create view, create routine,
alter routine, execute, trigger, show view
on `Acme-Chollos-Rifas`.* to 'acme-manager'@'%';