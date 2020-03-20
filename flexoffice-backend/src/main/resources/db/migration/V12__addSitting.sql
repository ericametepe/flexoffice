create table if not exists sitting
(
  id varchar(255),
  start_date timestamp,
  end_date timestamp,
  user_ip varchar(255),
  desk_id varchar(255)
    constraint fk5xd390ls9oou8y50r8tohnqcqTTTT
    references desk
)
;

alter table sitting owner to dockeruser
;