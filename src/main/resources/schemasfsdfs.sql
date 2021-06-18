drop table if exists positions;
drop table if exists employees;
drop table if exists persons;

create table persons
(
    id        INT PRIMARY KEY,
    name      varchar(20),
    lastName  varchar(20),
    address   varchar(20),
    cellphone varchar(20),
    cityName  varchar(20)
);

create table positions
(
    id   INT PRIMARY KEY,
    name varchar(20)
);

create table employees
(
    id          INT PRIMARY KEY,
    person_id   int not null,
    position_id int not null,
    salary      double,

    constraint fk_person foreign key (person_id) references persons (id),
    constraint fk_position foreign key (position_id) references positions (id)
);



