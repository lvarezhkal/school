create table Person (
person_id Integer primary key,
name varchar ,
age Integer,
driverLicence boolean);

create table Car (
car_id serial primary key,

brand varchar,
model varchar,
price Integer);

create table carOwner(
id_carowner serial,
person_id Integer references person(id),
car_id Integer references car(id));