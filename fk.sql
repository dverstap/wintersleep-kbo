alter table enterprise
    add constraint fk_enterprise_entity
        foreign key (EnterpriseNumber)
            references kbo_entity (EntityNumber);

alter table establishment
    add constraint fk_establishment_entity
        foreign key (EstablishmentNumber)
            references kbo_entity (EntityNumber);

alter table establishment
    add constraint fk_establishment_enterprise
        foreign key (EnterpriseNumber)
            references enterprise (EnterpriseNumber);

alter table denomination
    add constraint fk_denomination_entity
        foreign key (EntityNumber)
            references kbo_entity (EntityNumber);

alter table branch
    add constraint fk_branch_entity
        foreign key (EntityNumber)
            references kbo_entity (EntityNumber);

alter table branch
    add constraint fk_branch_enterprise
        foreign key (EnterpriseNumber)
            references enterprise (EnterpriseNumber);

alter table address
    add constraint fk_address_entity
        foreign key (EntityNumber)
            references kbo_entity (EntityNumber);

alter table contact
    add constraint fk_contact_entity
        foreign key (EntityNumber)
            references kbo_entity (EntityNumber);
