create table reservation
(
    id          serial primary key,
    seat        int references seat(id),
    seat_key    int,

    start_date  timestamp not null,
    end_date    timestamp not null,
    username    varchar(50) not null
)