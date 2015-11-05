  -- View over system users 
-- I think this view wont be useful in the new model
CREATE OR REPLACE VIEW zynap_user AS
  SELECT u.id, u.user_type, c.pref_given_name, c.first_name, c.second_name, c.title,
     u.is_active,
	l.username, l.password, l.expires, l.force_pwd_change
  FROM users u, logins l, subjects s, core_details c
  WHERE u.id = l.user_id  AND l.locked='F' AND  u.id (+) =s.user_id AND u.cd_id=c.id
--WITH READ ONLY;
/

CREATE OR REPLACE VIEW zynap_user_realm AS
  SELECT u.id, u.username, u.password
  FROM  zynap_user u
  WHERE u.is_active = 'T'
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW zynap_user_realm AS
  SELECT u.id, u.username, u.password
  FROM zynap_user u
  WHERE u.is_active = 'T'
--WITH READ ONLY;
/

 -- View for subjects
CREATE OR REPLACE VIEW zynap_subject AS
SELECT u.id,  u.user_type, c.pref_given_name, c.first_name, c.second_name, c.title, u.is_active
  FROM users u, subjects s, core_details c
  WHERE u.id = s.user_id AND u.user_type IN ('SUBJECT', 'SYSTEMSUBJECT') AND s.cd_id=c.id
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW zynap_subject_da AS
  SELECT  c.da_id, c.node_id, a.label, a.type, a.max_size, a.min_size, a.is_mandatory, a.is_searchable, refers_to, c.value
  FROM dynamic_attributes a, node_das c
  WHERE a.is_active='T' AND a.artefact_type='S'
  AND a.id = c.da_id
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW zynap_position_da AS
  SELECT  c.da_id, c.node_id, a.label, a.type, a.max_size, a.min_size, a.is_mandatory, a.is_searchable, refers_to, c.value
  FROM dynamic_attributes a, node_das c
  WHERE a.is_active='T' AND a.artefact_type='P'
  AND a.id = c.da_id
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW zynap_pos_assoc_primary_info AS
  SELECT
  p.node_id,  p.title, p.org_unit_id, pa.id, 'PRIMARY' as type_id, pa.target_id as parent_position_id, pa.value_id as subtype_id
  FROM positions p, position_associations pa ,nodes n
  WHERE pa.source_id = n.id
  AND n.id = p.node_id
  AND pa.target_id = p.node_id
  AND n.is_active='T'
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW POS_SOURCE_DERIVED_ATTR_BASE
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select  source_id, value_id, count(*)
    from position_associations group by source_id,value_id
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW POS_SOURCE_DERIVED_ATTR_ZERO
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select p.node_id, lv.id, 0
    from positions p, lookup_values lv
    where (lv.type_id = 'PRIMARY' or lv.type_id = 'SECONDARY')
and not exists
 ( select 1 from POS_SOURCE_DERIVED_ATTR_BASE psb where psb.node_id = p.node_id and psb.qualifier_id = lv.id)
--WITH READ ONLY;
/

CREATE OR REPLACE VIEW POS_TARGET_DERIVED_ATTR_BASE
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select  target_id, value_id, count(*)
    from position_associations group by target_id,value_id
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW POS_TARGET_DERIVED_ATTR_ZERO
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select p.node_id, lv.id, 0
    from positions p, lookup_values lv
    where (lv.type_id = 'PRIMARY' or lv.type_id = 'SECONDARY')
and not exists
 ( select 1 from POS_TARGET_DERIVED_ATTR_BASE psb where psb.node_id = p.node_id and psb.qualifier_id = lv.id)
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW SUB_SOURCE_DERIVED_ATTR_BASE
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select  subject_id, value_id, count(*)
    from subject_associations group by subject_id,value_id
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW SUB_SOURCE_DERIVED_ATTR_ZERO
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select s.node_id, lv.id, 0
    from subjects s, lookup_values lv
    where (lv.type_id = 'POSITIONSUBJECTASSOC' or lv.type_id = 'SECONDARYSUBJECTPOSASSOC')
and not exists
 ( select 1 from SUB_SOURCE_DERIVED_ATTR_BASE ssb where ssb.node_id = s.node_id and ssb.qualifier_id = lv.id)
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW SUB_TARGET_DERIVED_ATTR_BASE
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select  position_id, value_id, count(*)
    from subject_associations group by position_id,value_id
--WITH READ ONLY;
/
  
CREATE OR REPLACE VIEW SUB_TARGET_DERIVED_ATTR_ZERO
    ("NODE_ID","QUALIFIER_ID","ASSOCIATION_NUMBER") AS
    select p.node_id, lv.id, 0
    from positions p, lookup_values lv
    where (lv.type_id = 'POSITIONSUBJECTASSOC' or lv.type_id = 'SECONDARYSUBJECTPOSASSOC')
and not exists
 ( select 1 from SUB_TARGET_DERIVED_ATTR_BASE stb where stb.node_id = p.node_id and stb.qualifier_id = lv.id)
--WITH READ ONLY;
/
  
create or replace view NODE_SOURCE_DERIVED_ATTR(node_id,qualifier_id,association_number)
as
select  * from POS_SOURCE_DERIVED_ATTR_BASE
union all
select *  from POS_SOURCE_DERIVED_ATTR_ZERO
union all
select  * from SUB_SOURCE_DERIVED_ATTR_BASE
union all
select *  from SUB_SOURCE_DERIVED_ATTR_ZERO
--WITH READ ONLY;
/
  
create or replace view NODE_TARGET_DERIVED_ATTR(node_id,qualifier_id,association_number)
as
select  * from POS_TARGET_DERIVED_ATTR_BASE
union all
select *  from POS_TARGET_DERIVED_ATTR_ZERO
union all
select  * from SUB_TARGET_DERIVED_ATTR_BASE
union all
select *  from SUB_TARGET_DERIVED_ATTR_ZERO
--WITH READ ONLY;
/
  
create or replace view SUBJECT_SECONDARY_ASSOCIATIONS as
select sa.* from subject_associations sa, lookup_values lv where
sa.value_id = lv.id and lv.type_id = 'SECONDARYSUBJECTPOSASSOC'
--WITH READ ONLY;
/
  
-- view to select all populations together with their groups as a single list we can then limit it by group_id=? or is null queries
CREATE OR REPLACE VIEW population_groups_view AS
select p.ID,p.TYPE,p.USER_ID,p.LABEL,p.SCOPE,p.DESCRIPTION,p.ACTIVE_CRITERIA, pg.group_id from populations p left outer join population_groups pg on pg.population_id=p.id
--WITH READ ONLY;
/

CREATE OR REPLACE VIEW SURVEY_LIST AS
  select sub_spa.subject_id, cd.first_name, cd.second_name, l.username as manager_name, l.user_id as manager_id, qwp.que_wf_id
  from subject_primary_associations sub_spa,
    que_wf_participants qwp, positions p,
    subject_primary_associations boss_spa,
    subjects bosses,
    subjects subordinates,
    core_details cd,
    logins l,
    lookup_values lv
  where qwp.subject_id = sub_spa.subject_id
        and sub_spa.position_id = p.node_id
        and p.parent_id = boss_spa.position_id
        and sub_spa.value_id=lv.id
        and lv.value_id='PERMANENT'
        and lv.type_id='POSITIONSUBJECTASSOC'
        and sub_spa.subject_id = subordinates.node_id
        and subordinates.cd_id = cd.id
        and boss_spa.subject_id = bosses.node_id
        and bosses.user_id = l.user_id
  --WITH READ ONLY;
/
-- view to return all answers to any questions
CREATE OR REPLACE VIEW SEARCH_TERMS_XY AS
-- todo limit questionnaire security attributes
-- all direct answers from dynamic_attributes
    select nd.id, nd.value as label, da.description, nd.node_id as artefact_id, dp.user_id, dp.permit_id,
          case
              when n.node_type = 'S' then 'subject link'
              when n.node_type = 'P' then 'position link'
              when node_type = 'O' then 'org_unit link'
          end as link_url,
          case
              when n.node_type = 'S' then
                (select first_name || ' ' || second_name from core_details cd, subjects s where s.node_id = nd.node_id and s.cd_id = cd.id)
              when n.node_type = 'P' then
                (select title from positions p where p.node_id = nd.node_id)
              when node_type = 'O' then
                (select label from organization_units o where o.node_id = nd.node_id)
          end as name,
          'T' as manager_read,
          'T' as individual_read
    from node_das nd, dynamic_attributes da, nodes n, user_node_domain_permits dp
    where da.type in ('TEXT', 'TEXTAREA', 'LINK', 'NUMBER', 'DOUBLE', 'DATE', 'DATETIME','TEXTBOX', 'INTEGER', 'NUMBER')
          and da.qd_id is null
          and nd.da_id = da.id
          and nd.node_id = dp.node_id
          and n.id = nd.node_id
          and n.node_type in ('S', 'P', 'O')
          and dp.node_id = n.id
    UNION
    -- all direct answers from dynamic_attributes of questionnaires
    select nd.id, nd.value as label, da.description, q.subject_id as artefact_id, dp.user_id, dp.permit_id, '' as link_url,
                  (select first_name || ' ' || second_name from core_details cd, subjects s where s.node_id = nd.node_id and s.cd_id = cd.id) as name
      ,
      qw.manager_read as manager_read,
      qw.INDIVIDUAL_READ as individual_read
    from node_das nd, dynamic_attributes da, nodes n, user_node_domain_permits dp, questionnaires q, que_workflows qw
    where da.type in ('TEXT', 'TEXTAREA', 'LINK', 'NUMBER', 'DOUBLE', 'DATE', 'DATETIME','TEXTBOX', 'INTEGER', 'NUMBER')
          and da.qd_id is not null
          and nd.da_id = da.id
          and nd.node_id = n.id
          and n.node_type = 'Q'
          and nd.node_id = q.node_id
          and q.subject_id = dp.node_id
          and q.qwf_id = qw.id
    UNION
    -- all select type answers from dynamic_attributes
    select nd.id, lv.short_desc as label, da.description, nd.node_id as artefact_id, dp.user_id, dp.permit_id,
                  case
                  when n.node_type = 'S' then 'subject link'
                  when n.node_type = 'P' then 'position link'
                  when node_type = 'O' then 'org_unit link'
                  end as link_url,
                  case
                  when n.node_type = 'S' then
                    (select first_name || ' ' || second_name from core_details cd, subjects s where s.node_id = nd.node_id and s.cd_id = cd.id)
                  when n.node_type = 'P' then
                    (select title from positions p where p.node_id = nd.node_id)
                  when node_type = 'O' then
                    (select label from organization_units o where o.node_id = nd.node_id)
                  end as name,
                  'T' as manager_read,
                  'T' as individual_read
    from node_das nd, dynamic_attributes da, nodes n, user_node_domain_permits dp, lookup_values lv
    where da.type in ('STRUCT', 'MULTISELECT')
          and da.qd_id is null
          and nd.value = to_char(lv.id)
          and nd.node_id = dp.node_id
          and n.node_type in ('S', 'P', 'O')
          and dp.node_id = n.id
    UNION
    -- all select type answers from dynamic_attributes
    select nd.id, lv.short_desc as label, da.description, q.subject_id as artefact_id, dp.user_id, dp.permit_id, 'questionnaire liunk_url' as link_url,
                  (select first_name || ' ' || second_name from core_details cd, subjects s where s.node_id = nd.node_id and s.cd_id = cd.id) as name
      ,
      qw.manager_read as manager_read,
      qw.INDIVIDUAL_READ as individual_read
    from node_das nd, dynamic_attributes da, nodes n, user_node_domain_permits dp, lookup_values lv, questionnaires q, que_workflows qw
    where da.type in ('STRUCT', 'MULTISELECT', 'RADIO', 'STATUS', 'CHECKBOX')
          and da.qd_id is not null
          and nd.value = to_char(lv.id)
          and nd.node_id = n.id
          and nd.node_id = q.node_id
          and n.node_type = 'Q'
          and q.qwf_id = qw.id
          and q.subject_id = dp.node_id
--WITH READ ONLY;
/
-- view to return all answers to any questions
CREATE OR REPLACE VIEW DATA_TERMS_XY AS

  select distinct (label), id from (
-- all direct answers from dynamic_attributes
      SELECT DISTINCT
        (nd.value) AS label, nd.id as id
      FROM node_das nd, dynamic_attributes da
      WHERE da.type IN ('TEXT', 'TEXTAREA', 'LINK', 'NUMBER', 'DOUBLE', 'DATE', 'DATETIME', 'TEXTBOX', 'INTEGER', 'NUMBER')
            AND da.qd_id IS NULL
            AND nd.da_id = da.id
      UNION
      -- all direct answers from dynamic_attributes of questionnaires
      SELECT DISTINCT
        (nd.value) AS label, nd.id as id
      FROM node_das nd, dynamic_attributes da, nodes n, questionnaires q
      WHERE da.type IN ('TEXT', 'TEXTAREA', 'LINK', 'NUMBER', 'DOUBLE', 'DATE', 'DATETIME', 'TEXTBOX', 'INTEGER', 'NUMBER')
            AND da.qd_id IS NOT NULL
            AND nd.da_id = da.id
            AND nd.node_id = n.id
            AND n.node_type = 'Q'
            AND nd.node_id = q.node_id
      UNION
      -- all select type answers from dynamic_attributes
      SELECT DISTINCT
        (lv.short_desc) AS label, nd.id as id
      FROM node_das nd, dynamic_attributes da, lookup_values lv
      WHERE da.type IN ('STRUCT', 'MULTISELECT')
            AND da.qd_id IS NULL
            AND nd.value = to_char(lv.id)
      UNION
      -- all select type answers from dynamic_attributes
      SELECT DISTINCT
        (lv.short_desc) AS label, nd.id as id
      FROM node_das nd, dynamic_attributes da, lookup_values lv, questionnaires q
      WHERE da.type IN ('STRUCT', 'MULTISELECT', 'RADIO', 'STATUS', 'CHECKBOX')
            AND da.qd_id IS NOT NULL
            AND nd.value = to_char(lv.id)
            AND nd.node_id = q.node_id)
--WITH READ ONLY;
/



