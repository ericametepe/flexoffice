
create table if not exists spatial_ref_sys
(
  srid integer not null
    constraint spatial_ref_sys_pkey
    primary key
    constraint spatial_ref_sys_srid_check
    check ((srid > 0) AND (srid <= 998999)),
  auth_name varchar(256),
  auth_srid integer,
  srtext varchar(2048),
  proj4text varchar(2048)
)
;

alter table spatial_ref_sys owner to postgres;

create table if not exists site
(
  site_id varchar(255) not null
    constraint site_pkey
    primary key,
  city varchar(255),
  country varchar(255),
  postal_code varchar(255),
  street varchar(255),
  street_number varchar(255),
  lat double precision,
  lng double precision,
  geocode geometry,
  name varchar(255),
  coordinates geometry
)
;

alter table site owner to postgres
;

create table if not exists floor
(
  floor_id varchar(255) not null
    constraint floor_pkey
    primary key,
  name varchar(255),
  site_id varchar(255)
    constraint fk1fiac5m4gdvxup0u4pdbr0yl9
    references site
)
;

alter table floor owner to postgres
;

create table if not exists desk
(
  desk_id varchar(255) not null
    constraint desk_pkey
    primary key,
  desknumber varchar(255),
  fav_count bigint,
  state varchar(255),
  use_start_date timestamp,
  floor_id varchar(255)
    constraint fkr09dqgm7tik0nhvmstdd5o134
    references floor
)
;

alter table desk owner to postgres;


insert into site(site_id, city, country, postal_code, street, street_number, coordinates, name)
values('1','Paris','France','75015','62, rue de Lille 75343 Paris Cedex 07 France','76','POINT(2.5559 49.0083)','La Tour') ;

insert into site(site_id, city, country, postal_code, street, street_number, coordinates, name)
values('2','Paris','France','75015','85, rue du Cherche-Midi 75006 Paris France','76','POINT(2.5559 49.0083)','La Tour 2') ;

insert into site(site_id, city, country, postal_code, street, street_number, coordinates, name)
values('3','Paris','France','75015','Rue de Rivoli, 75001 Paris','76','POINT(2.5559 49.0083)','La Tour 3') ;

insert into floor(floor_id, name, site_id) VALUES ('1','F1','1');
insert into floor(floor_id, name, site_id) VALUES ('2','F2','1');
insert into floor(floor_id, name, site_id) VALUES ('3','F3','1');


insert into floor(floor_id, name, site_id) VALUES ('4','F1','2');
insert into floor(floor_id, name, site_id) VALUES ('5','F2','2');
insert into floor(floor_id, name, site_id) VALUES ('6','F3','2');


insert into floor(floor_id, name, site_id) VALUES ('7','F1','3');
insert into floor(floor_id, name, site_id) VALUES ('8','F2','3');
insert into floor(floor_id, name, site_id) VALUES ('9','F3','3');




insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
          values ('4','D111111',null,'FREE',null,'1');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('5','D111112',null,'FREE',null,'1');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('6','D111113',null,'FREE',null,'1');


insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('7','D1111',null,'FREE',null,'2');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('8','D1111122',null,'FREE',null,'2');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('9','D1111133',null,'FREE',null,'2');


insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('17','D1111',null,'FREE',null,'3');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('18','D1111122',null,'FREE',null,'3');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('20','D1111133',null,'FREE',null,'3');


