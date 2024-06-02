drop table if exists enterprise;
create table if not exists enterprise
(
    EnterpriseNumber   varchar(255) not null,
    Status             varchar(255) not null,
    JuridicalSituation varchar(255) not null,
    TypeOfEnterprise   tinyint      not null,
    JuridicalForm      char(3),
    JuridicalFormCAC   varchar(255),
    OrigStartDate      varchar(255) not null,
    # auto-inc id last, so we don't have to repeat the columns in the load data command:
    # id                 integer      not null auto_increment,
    primary key (EnterpriseNumber)
);

load data local infile 'data/enterprise.csv' into table enterprise
    fields terminated by ',' optionally enclosed by '"'
    ignore 1 lines
    #(EnterpriseNumber, Status, JuridicalSituation, TypeOfEnterprise, JuridicalForm, JuridicalFormCAC, OrigStartDate)
    set
        JuridicalForm = NULLIF(@JuridicalForm, ''),
        JuridicalFormCAC = NULLIF(@JuridicalFormCAC, '')
#StartDate = STR_TO_DATE(@StartDate, '%d-%m-%Y')
;

select count(*) as enterprise_count
from enterprise;

alter table enterprise
    add StartDate date generated always as (STR_TO_DATE(OrigStartDate, '%d-%m-%Y')) stored not null;
# alter table establishment
#     drop column OrigStartDate;
create index idx_enterprise_start_date on enterprise (StartDate);

# create unique index idx_enterprise_natural_id on enterprise (EnterpriseNumber);

# select max(JuridicalForm) from enterprise;
