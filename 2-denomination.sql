drop table if exists denomination;
create table if not exists denomination
(
    EntityNumber       varchar(255) not null,
    Language           tinyint      not null,
    TypeOfDenomination tinyint      not null,
    Denomination       varchar(255) not null,
    # auto-inc id last, so we don't have to repeat the columns in the load data command:
    id                 integer      not null auto_increment,
    primary key (id)
);

load data local infile 'data/denomination.csv'
    into table denomination
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
    (EntityNumber, Language, TypeOfDenomination, Denomination)
;
show warnings;

select count(*) as denomination_count
from denomination;

# This combination is not actually unique:
# create unique index idx_denomination_natural_id on denomination (EntityNumber, Language, TypeOfDenomination);

create index idx_denomination_entity_number on denomination (EntityNumber);

# create fulltext index ft_idx_denomination_denomination on denomination (Denomination);
# show warnings;

create index idx_denomination_denomination on denomination (Denomination);
