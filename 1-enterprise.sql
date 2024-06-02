drop table if exists enterprise;
create table if not exists enterprise
(
    # This is one enterprise where many fields are missing
    EnterpriseNumber   varchar(255) not null,
    Status             varchar(255) not null,
    JuridicalSituation varchar(255),
    TypeOfEnterprise   tinyint,
    JuridicalForm      char(3),
    JuridicalFormCAC   varchar(255),
    StartDate          date,
    # auto-inc id last, so we don't have to repeat the columns in the load data command:
    # id                 integer      not null auto_increment,
    primary key (EnterpriseNumber)
);

load data local infile 'data/enterprise.csv' into table enterprise
    fields terminated by ',' optionally enclosed by '"'
    ignore 1 lines
    (EnterpriseNumber, Status, JuridicalSituation, @TypeOfEnterprise, @JuridicalForm, @JuridicalFormCAC, @StartDate)
    set
        TypeOfEnterprise = NULLIF(@TypeOfEnterprise, ''),
        JuridicalForm = NULLIF(@JuridicalForm, ''),
        JuridicalFormCAC = NULLIF(@JuridicalFormCAC, ''),
        StartDate = STR_TO_DATE(NULLIF(@StartDate, ''), '%d-%m-%Y')
;
show warnings;

select count(*) as enterprise_count
from enterprise;

# alter table enterprise
#     add StartDate date generated always as (STR_TO_DATE(OrigStartDate, '%d-%m-%Y')) stored not null;
# alter table establishment
#     drop column OrigStartDate;
create index idx_enterprise_start_date on enterprise (StartDate);

# create unique index idx_enterprise_natural_id on enterprise (EnterpriseNumber);

# select max(JuridicalForm) from enterprise;
