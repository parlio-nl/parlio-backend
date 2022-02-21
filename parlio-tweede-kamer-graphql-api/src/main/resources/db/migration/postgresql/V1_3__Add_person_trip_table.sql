create table person_trip
(
    person_trip_id bigserial primary key                not null,
    person_id      bigint references person (person_id) not null,
    odata_id       uuid                                 not null,
    purpose        text,
    destination    text,
    paid_by        text,
    start_date     date,
    end_date       date
)
