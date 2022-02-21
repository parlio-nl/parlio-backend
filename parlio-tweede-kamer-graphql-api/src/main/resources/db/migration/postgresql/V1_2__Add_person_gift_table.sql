create table person_gift
(
    person_gift_id bigserial primary key                not null,
    person_id      bigint references person (person_id) not null,
    odata_id       uuid                                 not null,
    description    text,
    date           date
)
