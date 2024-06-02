drop table if exists code;
create table if not exists code
(
    Category    varchar(255) not null,
    Code        varchar(255) not null,
    Language    char(2)      not null,
    Description varchar(255) not null,
    # auto-inc id last, so we don't have to repeat the columns in the load data command:
    id          integer      not null auto_increment,
    primary key (id)
);

load data local infile 'data/code.csv'
    into table code
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
;

select count(*) as code_count
from code;

# Code is the first column, because it has the highest selectivity.
# Language is last because it has the lowest selectivity.
create unique index idx_code_natural_id on code (Code, Category, Language);
