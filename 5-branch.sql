drop table if exists branch;

# "Id","StartDate","EnterpriseNumber"
create table if not exists branch
(
    EntityNumber     varchar(255) not null, # The Id column in the CVS file is really an EntityNumber
    StartDate        date         not null,
    EnterpriseNumber varchar(255) not null,
    # auto-inc id last, so we don't have to repeat the columns in the load data command:
    #id               integer      not null auto_increment,
    primary key (EntityNumber)
);

load data local infile 'data/branch.csv'
    into table branch
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
    (EntityNumber, @StartDate, EnterpriseNumber)
    set
        StartDate = STR_TO_DATE(NULLIF(@StartDate, ''), '%d-%m-%Y')
;
show warnings;

select count(*) as branch_count
from branch;

# alter table branch
#     add StartDate date generated always as (STR_TO_DATE(OrigStartDate, '%d-%m-%Y')) stored not null;
create index idx_branch_start_date on branch (StartDate);
# alter table branch
#     drop column OrigStartDate;

create index idx_branch_enterprise_number on branch (EnterpriseNumber);
