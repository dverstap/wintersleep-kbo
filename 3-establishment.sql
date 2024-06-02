drop table if exists establishment;
create table if not exists establishment
(
    EstablishmentNumber varchar(255) not null,
    OrigStartDate       varchar(255) not null,
    EnterpriseNumber    varchar(255) not null,
    primary key (EstablishmentNumber)
);

load data local infile 'data/establishment.csv'
    into table establishment
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
;

select count(*) as establishment_count
from establishment;

alter table establishment
    add StartDate date generated always as (STR_TO_DATE(OrigStartDate, '%d-%m-%Y')) stored not null;
create index idx_establishment_start_date on establishment (StartDate);
# alter table establishment
#     drop column OrigStartDate;

create unique index idx_establishment_enterprise_establishment on establishment (EnterpriseNumber, EstablishmentNumber);
