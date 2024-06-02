select d1_0.id,
       e1_0.EntityNumber,
       case
           when e1_2.EntityNumber is not null then 1
           when e1_1.EnterpriseNumber is not null then 2
           when e1_3.EstablishmentNumber is not null then 3
           when e1_0.EntityNumber is not null then 0 end,
       e1_2.EnterpriseNumber,
       e1_2.StartDate,
       e1_1.JuridicalForm,
       e1_1.JuridicalFormCAC,
       e1_1.JuridicalSituation,
       e1_1.StartDate,
       e1_1.Status,
       e1_1.TypeOfEnterprise,
       e1_3.EnterpriseNumber,
       e1_3.StartDate,
       d1_0.Language,
       d1_0.Denomination,
       d1_0.TypeOfDenomination
from denomination d1_0
         join (
    kbo_entity e1_0
        left join enterprise e1_1 on e1_0.EntityNumber = e1_1.EnterpriseNumber
        left join branch e1_2 on e1_0.EntityNumber = e1_2.EntityNumber
        left join establishment e1_3 on e1_0.EntityNumber = e1_3.EstablishmentNumber)
              on e1_0.EntityNumber = d1_0.EntityNumber
         join enterprise e2_0 on e1_1.EnterpriseNumber = e2_0.EnterpriseNumber
where d1_0.Denomination = 'VDAB'
;


select d1_0.id,
       e1_0.EntityNumber,
       case
           when e1_1.EntityNumber is not null then 1
           when e1_2.EnterpriseNumber is not null then 2
           when e1_3.EstablishmentNumber is not null then 3
           when e1_0.EntityNumber is not null then 0 end,
       e1_1.EnterpriseNumber,
       e1_1.StartDate,
       e1_2.JuridicalForm,
       e1_2.JuridicalFormCAC,
       e1_2.JuridicalSituation,
       e1_2.StartDate,
       e1_2.Status,
       e1_2.TypeOfEnterprise,
       e1_3.EnterpriseNumber,
       e1_3.StartDate,
       d1_0.Language,
       d1_0.Denomination,
       d1_0.TypeOfDenomination
from denomination d1_0
         join (
    kbo_entity e1_0
        left join branch e1_1 on e1_0.EntityNumber = e1_1.EntityNumber
        left join enterprise e1_2 on e1_0.EntityNumber = e1_2.EnterpriseNumber
        left join establishment e1_3 on e1_0.EntityNumber = e1_3.EstablishmentNumber)
              on e1_0.EntityNumber = d1_0.EntityNumber
where d1_0.Denomination = 'VDAB'
  and case
          when e1_1.EntityNumber is not null then 1
          when e1_2.EnterpriseNumber is not null then 2
          when e1_3.EstablishmentNumber is not null then 3
          when e1_0.EntityNumber is not null then 0 end = 2


select d1_0.id,
       e1_0.EntityNumber,
       case
           when e1_1.EntityNumber is not null then 1
           when e1_2.EnterpriseNumber is not null then 2
           when e1_3.EstablishmentNumber is not null then 3
           when e1_0.EntityNumber is not null then 0 end,
       e1_1.EnterpriseNumber,
       e1_1.StartDate,
       e1_2.JuridicalForm,
       e1_2.JuridicalFormCAC,
       e1_2.JuridicalSituation,
       e1_2.StartDate,
       e1_2.Status,
       e1_2.TypeOfEnterprise,
       e1_3.EnterpriseNumber,
       e1_3.StartDate,
       d1_0.Language,
       d1_0.Denomination,
       d1_0.TypeOfDenomination
from denomination d1_0
         join (kbo_entity e1_0
             left join branch e1_1 on e1_0.EntityNumber = e1_1.EntityNumber
             left join enterprise e1_2 on e1_0.EntityNumber = e1_2.EnterpriseNumber
             left join establishment e1_3 on e1_0.EntityNumber = e1_3.EstablishmentNumber)
             on e1_0.EntityNumber = d1_0.EntityNumber
where d1_0.Denomination = 'VDAB'
;

select *
from denomination d inner join enterprise e on d.EntityNumber = e.EnterpriseNumber
where d.Denomination = 'VDAB'