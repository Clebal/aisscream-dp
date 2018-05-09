drop database if exists `Acme-Newspaper`;
create database `Acme-Newspaper`;

grant select, insert, update, delete
on `Acme-Newspaper`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter,
create temporary tables, lock tables, create view, create routine,
alter routine, execute, trigger, show view
on `Acme-Newspaper`.* to 'acme-manager'@'%';