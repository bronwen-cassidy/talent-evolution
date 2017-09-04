--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '


update lookup_values set value_id='DECIMAL', short_desc='Decimal',description='Decimal Number' where id=12;
insert into lookup_values(id, TYPE_ID, VALUE_ID, SHORT_DESC, DESCRIPTION,SORT_ORDER) 
values (LOOKUPVALUE_SQ.nextval, 'DATYPE', 'CURRENCY', 'Currency', 'Currency',1);

insert into lookup_types(ID, DESCRIPTION, TYPE, IS_ACTIVE, IS_SYSTEM, LABEL) values ('CURRENCY','Currency Codes','SYSTEM','T','T','Currencies');
insert into lookup_values(id, TYPE_ID, VALUE_ID, SHORT_DESC, DESCRIPTION, SORT_ORDER)
values (LOOKUPVALUE_SQ.nextval, 'CURRENCY', 'USD', 'USD', 'USD', 1);
--=============================================================================

alter table node_das add currency VARCHAR2(10 CHAR);

--======================================
@packages/zynap_node_spec.sql
@packages/zynap_node_body.sql

ALTER PACKAGE ZYNAP_NODE_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_NODE_SP COMPILE BODY;


insert into versions(version) values('5.4.8-5.4.9');

alter table QUE_WORKFLOWS add parent_label varchar2(1000 CHAR);

commit;
