create table if not exists person
(
    person_id     bigint primary key,
    slug          text not null unique,
    first_name    text not null,
    family_name   text not null,
    name_initials text not null,
    date_of_birth text not null
);

create table if not exists change_event
(
    change_event_id bigint primary key,
    operation_name  text        not null,
    time            timestamptz not null
);

create table if not exists change_event_entry
(
    change_event_entry_id bigint primary key,
    change_event_id       bigint references change_event not null,
    key                   text                           not null,
    data                  jsonb                          not null
);
