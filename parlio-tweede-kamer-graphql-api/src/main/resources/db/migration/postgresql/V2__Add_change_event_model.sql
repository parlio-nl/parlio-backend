alter table change_event add column if not exists model text;
alter table change_event add column if not exists ref bigint;
