-- C:\home\TSIntegration\talent-studio-application\db\tables\TSModel.snp
--
-- Generated for Oracle 9i on Tue Mar 29  16:39:24 2005 by Server Generator 9.0.2.96.6

PROMPT Creating  View 'DEFAULT_OUH_OU_PERMITS'
CREATE OR REPLACE VIEW DEFAULT_OUH_OU_PERMITS
 AS
 SELECT oh.root_id as root_id ,oh.id as node_id , pt.id as
permit_id, pt.action as action
from permits_roles pr , roles r, permits pt , ou_hierarchy oh
  WHERE r.is_ounit_default='T' and r.id = pr.role_id and pr.permit_id = pt.id
and pt.content ='ORGANISATIONS'
/

PROMPT Creating  View 'DEFAULT_OUH_POSITION_PERMITS'
CREATE OR REPLACE VIEW DEFAULT_OUH_POSITION_PERMITS
 AS
 SELECT oh.root_id as root_id ,p.node_id as node_id , pt.id as
permit_id, pt.action as action
from permits_roles pr , roles r, permits pt , ou_hierarchy oh, positions p
  WHERE p.org_unit_id = oh.id and
r.is_ounit_default='T' and r.id = pr.role_id and pr.permit_id = pt.id
and pt.content ='POSITIONS'
/

PROMPT Creating  View 'DEFAULT_PH_POSITION_PERMITS'
CREATE OR REPLACE VIEW DEFAULT_PH_POSITION_PERMITS
 AS
 SELECT ph.root_id as root_id ,ph.id as node_id , p.id as
permit_id, p.action as action
from permits_roles pr , roles r, permits p , positions_hierarchy ph
  WHERE r.is_position_default='T' and r.id = pr.role_id and pr.permit_id = p.id
and p.content ='POSITIONS'
/

PROMPT Creating  View 'DEFAULT_OUH_SUBJECT_PERMITS'
CREATE OR REPLACE VIEW DEFAULT_OUH_SUBJECT_PERMITS
as
select  oh.root_id as root_id ,sa.subject_id as node_id , pt.id as
permit_id , pt.action as action
from permits_roles pr , roles r, permits pt , ou_hierarchy oh, positions p,
SUBJECT_PRIMARY_ASSOCIATIONS sa
WHERE
p.org_unit_id = oh.id and
sa.position_id = p.node_id and
r.is_ounit_default='T' and r.id = pr.role_id and pr.permit_id = pt.id
and pt.content ='SUBJECTS'
/

PROMPT Creating  View 'DEFAULT_PH_SUBJECT_PERMITS'
CREATE OR REPLACE VIEW DEFAULT_PH_SUBJECT_PERMITS
 as
 SELECT ph.root_id as root_id ,sa.subject_id as node_id , p.id as
 permit_id , p.action as action
 from permits_roles pr , roles r, permits p , positions_hierarchy ph,
  SUBJECT_PRIMARY_ASSOCIATIONS sa
 where
  sa.position_id = ph.id and
 r.is_position_default='T' and r.id = pr.role_id and pr.permit_id = p.id
 and p.content ='SUBJECTS'
/

PROMPT Creating  View 'AREA_OU'
CREATE OR REPLACE VIEW AREA_OU
 AS
  SELECT ae.area_id as area_id, oh.id as node_id
  FROM area_elements ae,ou_hierarchy_inc oh
  WHERE ae.node_id=oh.root_id                     
  AND ae.is_excluded = 'F'
  AND (ae.cascading='T' OR (ae.cascading <>'T' AND oh.root_id = oh.id))
/

PROMPT Creating  View 'AREA_POSITIONS'
CREATE OR REPLACE VIEW AREA_POSITIONS
 AS
 SELECT ae.area_id as area_id, ph.id as node_id
 FROM area_elements ae, positions_hierarchy_inc ph
 WHERE ae.node_id=ph.root_id   
 AND ae.is_excluded = 'F'
 AND ( ae.cascading='T' OR (ae.cascading <> 'T' AND ae.node_id = ph.id ))
/

PROMPT Creating  View 'AREA_POSITIONS_OU'
CREATE OR REPLACE VIEW AREA_POSITIONS_OU
 AS
 SELECT aou.area_id, p.node_id as node_id
 FROM AREA_OU aou, POSITIONS p
 WHERE p.org_unit_id = aou.node_id
 AND p.node_id NOT IN (
 	SELECT node_id
 	FROM area_elements
 	WHERE area_id=aou.area_id
 	AND is_excluded = 'T' )
/

PROMPT Creating  View 'AREA_SUBJECTS_OU'
CREATE OR REPLACE VIEW AREA_SUBJECTS_OU
 AS
 SELECT ae.area_id, sa.subject_id as node_id
from AREA_POSITIONS_OU ae, SUBJECT_PRIMARY_ASSOCIATIONS sa
  WHERE ae.node_id = sa.position_id
/

PROMPT Creating  View 'SD_POSITIONS_PERMITS'
CREATE OR REPLACE VIEW SD_POSITIONS_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_positions ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='F'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'POSITIONS'
/

PROMPT Creating  View 'AREA_SUBJECTS_POS'
CREATE OR REPLACE VIEW AREA_SUBJECTS_POS
 AS
 SELECT ae.area_id, sa.subject_id as node_id
from AREA_POSITIONS ae, SUBJECT_PRIMARY_ASSOCIATIONS sa
  WHERE ae.node_id = sa.position_id
/

PROMPT Creating  View 'SD_POSITIONS_EXC_PERMITS'
CREATE OR REPLACE VIEW SD_POSITIONS_EXC_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_positions ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='T'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'POSITIONS'
/

PROMPT Creating  View 'SD_POS_OU_PERMITS'
CREATE OR REPLACE VIEW SD_POS_OU_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
 FROM area_positions_ou ae, security_domains sd, roles_security_domains  rsd, permits_roles pr, permits pt
 WHERE sd.is_exclusive='F'
 AND sd.node_id = ae.area_id
 AND sd.id = rsd.sd_id
 AND rsd.role_id =  pr.role_id
 AND pt.id = pr.permit_id
 AND pt.content = 'POSITIONS'
/

PROMPT Creating  View 'SD_OU_EXC_PERMITS'
CREATE OR REPLACE VIEW SD_OU_EXC_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_ou ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='T'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'ORGANISATIONS'
/

PROMPT Creating  View 'SD_OU_PERMITS'
CREATE OR REPLACE VIEW SD_OU_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_ou ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='F'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'ORGANISATIONS'
/

PROMPT Creating  View 'AREA_SUBJECTS'
CREATE OR REPLACE VIEW AREA_SUBJECTS
 AS
 SELECT ae.area_id as area_id, s.node_id as node_id
from area_elements ae, subjects s
  WHERE ae.node_id=s.node_id
/

PROMPT Creating  View 'SD_POS_OU_EXC_PERMITS'
CREATE OR REPLACE VIEW SD_POS_OU_EXC_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_positions_ou ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='T'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'POSITIONS'
/

PROMPT Creating  View 'SD_SUBJECTS_PERMITS'
CREATE OR REPLACE VIEW SD_SUBJECTS_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
 from area_subjects ae, security_domains sd, roles_security_domains rsd, permits_roles pr, permits pt
 WHERE sd.is_exclusive='F'  and sd.node_id = ae.area_id
 and  sd.id = rsd.sd_id
 and  rsd.role_id =  pr.role_id
 and pt.id = pr.permit_id
 and pt.content = 'SUBJECTS'
/

PROMPT Creating  View 'SD_SUBJECTS_EXC_PERMITS'
CREATE OR REPLACE VIEW SD_SUBJECTS_EXC_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_subjects ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='T'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'SUBJECTS'
/

PROMPT Creating  View 'SD_SUB_OU_PERMITS'
CREATE OR REPLACE VIEW SD_SUB_OU_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_subjects_ou ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='F'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'SUBJECTS'
/

PROMPT Creating  View 'SD_SUB_OU_EXC_PERMITS'
CREATE OR REPLACE VIEW SD_SUB_OU_EXC_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id, pt.action as action
from area_subjects_ou ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='T'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'SUBJECTS'
/


PROMPT Creating  View 'SD_SUB_POS_EXC_PERMITS'
CREATE OR REPLACE VIEW SD_SUB_POS_EXC_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id , pt.action as action
from area_subjects_pos ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='T'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'SUBJECTS'
/

PROMPT Creating  View 'SD_SUB_POS_PERMITS'
CREATE OR REPLACE VIEW SD_SUB_POS_PERMITS
 AS
 SELECT sd.id as sd_id, ae.node_id, pt.id as permit_id , pt.action as action
from area_subjects_pos ae,security_domains sd,roles_security_domains  rsd
, permits_roles pr, permits pt
  WHERE sd.is_exclusive='T'  and sd.node_id = ae.area_id
and  sd.id = rsd.sd_id
and  rsd.role_id =  pr.role_id
and pt.id = pr.permit_id
and pt.content = 'SUBJECTS'
/

CREATE OR REPLACE VIEW "ZYNAP_OU_DEFAULT_PERMITS" ("ROOT_ID",
    "NODE_ID","PERMIT_ID","ACTION") AS
   SELECT root_id , node_id , permit_id, action from default_ouh_ou_permits dp
   where not exists
     ( select 1 from  sd_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
/

CREATE OR REPLACE VIEW "ZYNAP_SD_OU_PERMITS" ("SD_ID",
    "NODE_ID","PERMIT_ID","ACTION") AS
   SELECT sd_id , node_id , permit_id, action from sd_ou_permits dp
   where not exists
     ( select 1 from  sd_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
/

CREATE OR REPLACE VIEW "ZYNAP_USER_OU_PERMITS" ("USER_ID",
    "NODE_ID","PERMIT_ID", "ACTION") AS
    select  u.id as user_id, dp.node_id, dp.permit_id, dp.action
    from users u,subjects s,subject_primary_associations sa, positions p, ZYNAP_OU_DEFAULT_PERMITS dp
    where u.id = s.user_id and s.node_id = sa.subject_id and sa.position_id = p.node_id
    and p.org_unit_id = dp.root_id
    union
    select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, zynap_sd_ou_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
    union
    select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, sd_ou_exc_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
/

CREATE OR REPLACE VIEW "ZYNAP_POSITION_DEFAULT_PERMITS" ("ROOT_ID",
   "NODE_ID","PERMIT_ID","ACTION") AS
   SELECT root_id , node_id , permit_id, action from default_ouh_position_permits dp
   where not exists
     ( select 1 from  sd_positions_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
     ( select 1 from  sd_pos_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   union all
   SELECT root_id , node_id , permit_id, action from default_ph_position_permits dp
   where not exists
     ( select 1 from  sd_positions_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
     ( select 1 from  sd_pos_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)

/

CREATE OR REPLACE VIEW "ZYNAP_SD_POSITION_PERMITS" ("SD_ID",
    "NODE_ID","PERMIT_ID","ACTION") AS
   SELECT sd_id , node_id , permit_id, action from sd_positions_permits dp
   where not exists
     ( select 1 from  sd_positions_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
     ( select 1 from  sd_pos_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   union all
   SELECT sd_id , node_id , permit_id, action from sd_pos_ou_permits dp
   where not exists
     ( select 1 from  sd_positions_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
     ( select 1 from  sd_pos_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
/

CREATE OR REPLACE VIEW "ZYNAP_USER_POSITION_PERMITS"
    ("USER_ID","NODE_ID","PERMIT_ID","ACTION") AS
select  u.id as user_id, dp.node_id, dp.permit_id, dp.action
    from users u,subjects s,subject_primary_associations sa, positions p, ZYNAP_POSITION_DEFAULT_PERMITS dp
    where u.id = s.user_id and s.node_id = sa.subject_id and (sa.position_id = p.node_id
    and (p.org_unit_id = dp.root_id or dp.root_id = p.node_id) )
    union
select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, zynap_sd_position_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
    union
select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, sd_pos_ou_exc_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
    union
select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, sd_positions_exc_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
/


CREATE OR REPLACE VIEW "ZYNAP_SUBJECT_DEFAULT_PERMITS" ("ROOT_ID",
   "NODE_ID","PERMIT_ID","ACTION") AS
   SELECT root_id , node_id , permit_id, action from default_ouh_subject_permits dp
   where not exists
     ( select 1 from  sd_sub_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
     ( select 1 from  sd_sub_pos_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
      ( select 1 from sd_subjects_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   union all
   SELECT root_id , node_id , permit_id, action from default_ph_subject_permits dp
   where not exists
     ( select 1 from  sd_sub_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
     ( select 1 from  sd_sub_pos_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
      ( select 1 from sd_subjects_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
/

CREATE OR REPLACE VIEW "ZYNAP_SD_SUBJECT_PERMITS" ("SD_ID",
    "NODE_ID","PERMIT_ID","ACTION") AS
   SELECT sd_id, node_id, permit_id, action from sd_subjects_permits dp
   where not exists
     ( select 1 from  sd_sub_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
     ( select 1 from  sd_sub_pos_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
      ( select 1 from sd_subjects_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   union all
   SELECT sd_id, node_id, permit_id, action from sd_sub_pos_permits dp
   where not exists
      ( select 1 from  sd_sub_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
      ( select 1 from  sd_sub_pos_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
      ( select 1 from sd_subjects_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   union all
   SELECT sd_id, node_id, permit_id, action from sd_sub_ou_permits dp
   where not exists
      ( select 1 from  sd_sub_ou_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
      ( select 1 from  sd_sub_pos_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
   and not exists
      ( select 1 from sd_subjects_exc_permits where node_id = dp.node_id and permit_id = dp.permit_id)
/

CREATE OR REPLACE VIEW "ZYNAP_USER_SUBJECT_PERMITS" ("USER_ID",
    "NODE_ID","PERMIT_ID","ACTION") AS
    select  u.id as user_id, dp.node_id, dp.permit_id, dp.action
    from users u,subjects s,subject_primary_associations sa, positions p, ZYNAP_SUBJECT_DEFAULT_PERMITS dp
    where u.id = s.user_id and s.node_id = sa.subject_id and (sa.position_id = p.node_id
    and (p.org_unit_id = dp.root_id or dp.root_id = p.node_id) )
    union
    select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, ZYNAP_SD_SUBJECT_PERMITS sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
    union
    select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, sd_sub_ou_exc_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
    union
    select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, sd_sub_pos_exc_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
    union
    select u.id as user_id, sdp.node_id, sdp.permit_id, sdp.action
    from users u , security_domains_users sdu, sd_subjects_exc_permits sdp
    where u.id = sdu.user_id and sdu.sd_id = sdp.sd_id
/


CREATE OR REPLACE VIEW "ZYNAP_USER_DOMAIN_PERMITS" ("USER_ID",
    "NODE_ID","PERMIT_ID","ACTION") AS
 select user_id, node_id, permit_id, action from ZYNAP_USER_POSITION_PERMITS
 union all
 select user_id, node_id, permit_id, action from ZYNAP_USER_OU_PERMITS
 union all
 select user_id, node_id, permit_id, action from ZYNAP_USER_SUBJECT_PERMITS
/
