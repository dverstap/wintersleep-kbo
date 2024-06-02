# Adding this table to be to define foreign key constraints:
drop table if exists kbo_entity;
create table if not exists kbo_entity
(
    # EntityNumber refers to either EnterpriseNumber or EstablishmentNumber or a branch id
    EntityNumber varchar(255) not null,
    primary key (EntityNumber)
);

load data local infile 'data/enterprise.csv'
    into table kbo_entity
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
    (EntityNumber)
;

load data local infile 'data/establishment.csv'
    into table kbo_entity
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
    (EntityNumber)
;

load data local infile 'data/branch.csv'
    into table kbo_entity
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
    (EntityNumber)
;

select count(*) as kbo_entity_count
from kbo_entity;
