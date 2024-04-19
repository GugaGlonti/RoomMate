create table room
(
    id          serial primary key,
    name        varchar(255) not null
);

create table seat
(
    id          serial primary key,
    room        int references room(id),

    name        varchar(255) not null,
    equipment   varchar(255) not null
);