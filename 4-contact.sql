drop table if exists contact;
create table if not exists contact
(
    EntityNumber  varchar(255) not null,
    EntityContact char(3)      not null,
    ContactType   varchar(16)  not null,
    Value         varchar(255) not null,
    # auto-inc id last, so we don't have to repeat the columns in the load data command:
    id            integer      not null auto_increment,
    primary key (id)
);

load data local infile 'data/contact.csv'
    into table contact
    fields terminated by ','
    optionally enclosed by '"'
    ignore 1 lines
    (EntityNumber, EntityContact, ContactType, Value)
;
show warnings;

select count(*) as contact_count
from contact;

# There can be multiple phone numbers and email addresses for each entity, so this isn't possible:
# create unique index idx_contact_natural_id on contact (EntityNumber, ContactType);

create index contact_entity_number on contact (EntityNumber);

# create fulltext index ft_idx_contact_contact on contact (Value);
# show warnings;

create index idx_contact_contact on contact (Value);
