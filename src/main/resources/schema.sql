create table department
(
    id        int AUTO_INCREMENT primary key,
    name      varchar(50) unique,
    create_at date,
    update_at date,
    delete_at date
);

create table staff
(
    id         int AUTO_INCREMENT primary key,
    first_name varchar(50),
    last_name  varchar(50),
    create_at  date,
    update_at  date,
    delete_at date,
    dep_id     int,
    username   varchar(50) unique,
    password varchar(200),
    constraint dep_staff
        foreign key (dep_id)
            references staff (id)
);


