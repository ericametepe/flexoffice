




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
  name varchar(255)
);


create table if not exists floor
(
  floor_id varchar(255) not null
    constraint floor_pkey
    primary key,
  name varchar(255),
  site_id varchar(255)
    constraint fk1fiac5m4gdvxup0u4pdbr0yl9
    references site
);


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


insert into site(site_id, city, country, postal_code, street, street_number, name,lat,lng)
values('S1','Paris','France','75015','62, rue de Lille 75343 Paris Cedex 07 France','76','La Tour',48.859650,2.326280) ;

insert into site(site_id, city, country, postal_code, street, street_number, name,lat,lng)
values('S2','Paris','France','75015','85, rue du Cherche-Midi','76','La Tour 2',48.847431,2.322710);

insert into site(site_id, city, country, postal_code, street, street_number, name, lat,lng)
values('S3','Paris','France','75008','3 Avenue du Général Eisenhower','76','La Tour 3',48.861488,2.339130) ;


insert into site(site_id, city, country, postal_code, street, street_number, name, lat,lng)
values('S4','Paris','France','75116','Avenue du Président Wilson','13','La Tour 4',48.864960,2.300530);


insert into floor(floor_id, name, site_id) VALUES ('F41','Etage F41','S4');
insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('1f114048-2f9a-11ea-978f-2e728ce88125','Desk-1f114048-2f9a-11ea-978f-2e728ce88125',null,'FREE',null,'F41');
insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('1f114638-2f9a-11ea-978f-2e728ce88125','Desk-1f114638-2f9a-11ea-978f-2e728ce88125',null,'FREE',null,'F41');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('1f1147dc-2f9a-11ea-978f-2e728ce88125','Desk-1f1147dc-2f9a-11ea-978f-2e728ce88125',null,'FREE',null,'F41');

insert into floor(floor_id, name, site_id) VALUES ('F42','Etage F42','S4');
insert into floor(floor_id, name, site_id) VALUES ('F43','Etage F43','S4');





insert into floor(floor_id, name, site_id) VALUES ('1','F1','S1');
insert into floor(floor_id, name, site_id) VALUES ('2','F2','S1');
insert into floor(floor_id, name, site_id) VALUES ('3','F3','S1');


insert into floor(floor_id, name, site_id) VALUES ('4','F1','S2');
insert into floor(floor_id, name, site_id) VALUES ('5','F2','S2');
insert into floor(floor_id, name, site_id) VALUES ('6','F3','S2');


insert into floor(floor_id, name, site_id) VALUES ('7','FS31','S3');
insert into floor(floor_id, name, site_id) VALUES ('8','FS32','S3');
insert into floor(floor_id, name, site_id) VALUES ('9','FS33','S3');




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


insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('21','D1111',null,'FREE',null,'4');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('22','D1111122',null,'FREE',null,'4');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('23','D1111133',null,'FREE',null,'4');


insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('24','D1111',null,'FREE',null,'5');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('25','D1111122',null,'FREE',null,'5');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('26','D1111133',null,'FREE',null,'5');


insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('27','D1111',null,'FREE',null,'6');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('28','D1111122',null,'FREE',null,'7');

insert into desk (desk_id, desknumber, fav_count, state, use_start_date, floor_id)
values ('29','D1111133',null,'FREE',null,'9');


