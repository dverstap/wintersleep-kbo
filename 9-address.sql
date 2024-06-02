# There is an address in Portugal, where a field value ends with a backslash:
#   grep '\\"' data/*.csv
# That is misinterpreted by MySQL, cause the fields not to match.
# All files can be fixed using:
#   sed -i 's/\\"/\\\\"/g' data/*.csv

drop table if exists address;
create table if not exists address
(
    # EntityNumber refers to either EnterpriseNumber or EstablishmentNumber
    EntityNumber     varchar(255) not null,
    TypeOfAddress    char(4)      not null,
    CountryNL        varchar(255) not null,
    CountryFR        varchar(255) not null,
    Zipcode          varchar(255) not null,
    MunicipalityNL   varchar(255) not null,
    MunicipalityFR   varchar(255) not null,
    StreetNL         varchar(255) not null,
    StreetFR         varchar(255) not null,
    HouseNumber      varchar(255) not null,
    Box              varchar(255) not null,
    ExtraAddressInfo varchar(255) not null,
    DateStrikingOff  date,
    # auto-inc id last, so we don't have to repeat the columns in the load data command:
    id               integer      not null auto_increment,
    primary key (id)
);

load data local infile 'data/address.csv'
    into table address
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
    (EntityNumber, TypeOfAddress, CountryNL, CountryFR, Zipcode, MunicipalityNL, MunicipalityFR,
     StreetNL, StreetFR, HouseNumber, Box, ExtraAddressInfo, @DateStrikingOff)
    set
        # TODO This might not be good for fulltext address matching, but building the FTI doesn't work anyway:
        CountryNL = IF(@CountryNL != '', @CountryNL, 'België'),
        CountryFR = IF(@CountryFR != '', @CountryFR, 'Belgique'),
        DateStrikingOff = STR_TO_DATE(NULLIF(@DateStrikingOff, ''), '%d-%m-%Y')
;
show warnings;

select count(*) as address_count
from address;

# alter table address
#     add DateStrikingOff date generated always as (STR_TO_DATE(OrigDateStrikingOff, '%d-%m-%Y')) stored;
# alter table establishment
#     drop column OrigStartDate;
create index idx_address_start_date on address (DateStrikingOff);

create unique index idx_address_natural_id on address (EntityNumber);

# TODO: Hitting this bug when creating fulltext index: https://bugs.mysql.com/bug.php?id=109242
# Limited fulltext experience, so not sure if it's a good idea to create separate indexes for NL/FR:
# create fulltext index ft_idx_address_address_nl
#     on address (CountryNL, Zipcode, MunicipalityNL, StreetNL, HouseNumber, Box, ExtraAddressInfo);
# show warnings;
# create fulltext index ft_idx_address_address_fr
#     on address (CountryFR, Zipcode, MunicipalityFR, StreetFR, HouseNumber, Box, ExtraAddressInfo);
# show warnings;
