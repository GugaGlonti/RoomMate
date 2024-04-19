alter table room
add column version integer not null default 0;

alter table seat
add column version integer not null default 0;

alter table reservation
add column version integer not null default 0;
