--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table que_workflows modify description varchar2(2500);

CREATE TABLE CHART_COLUMN_ATTRIBUTES
(
    ID NUMBER NOT NULL
    ,REPORT_COLUMN_ID NUMBER NOT NULL
    ,ATTRIBUTE VARCHAR2(1024) NOT NULL
    ,QUE_WF_ID NUMBER
    ,ROLE VARCHAR2(64)
    ,CATEGORY VARCHAR2 (1024) NOT NULL
    ,EXPECTED_VALUE INTEGER
)
/

ALTER TABLE CHART_COLUMN_ATTRIBUTES
 ADD (CONSTRAINT REP_COL_ATTR_PK PRIMARY KEY
  (ID))
/
ALTER TABLE CHART_COLUMN_ATTRIBUTES ADD (CONSTRAINT
 RCA_RC_FK FOREIGN KEY
  (REPORT_COLUMN_ID) REFERENCES REPORT_COLUMNS
  (ID))
/
ALTER TABLE CHART_COLUMN_ATTRIBUTES ADD (CONSTRAINT
 RCA_QWF_FK FOREIGN KEY
  (QUE_WF_ID) REFERENCES QUE_WORKFLOWS
  (ID))
/

ALTER TABLE HOME_PAGES ADD LOCAL_PAGE_URL VARCHAR2(512);
ALTER TABLE HOME_PAGES ADD HOST_NAME VARCHAR2(512);
alter table home_pages add is_internal_url varchar(1) default 'F';

alter table node_das modify value varchar2(4000 CHAR);

--select * from PERMITS where url = '/.*/viewmy*.*';
--select * from permits_roles where permit_id in (select id from permits where url = '/.*/viewmy*.*');
--delete from permits_roles where permit_id in (261, 266, 271, 276);
--delete from permits where id in (261, 266, 271, 276);

update permits p set p.url = replace((select pr.url from permits pr where p.id = pr.id), '/talentarena', '/.*')
where p.url like '%talentarena/%'
and p.url not in ('/talentarena/home.htm', '/talentarena/menu.htm', '/talentarena/processdescription.htm', '/talentarena/editmyaccount.htm', '/talentarena/listchartsettings.htm', '/talentarena/viewchartsettings.htm', '/talentarena/editchartsettings.htm',
'/talentarena/addchartsettings.htm', '/talentarena/resetchartsettings.htm', '/talentarena/deletechartsettings.htm', '/talentarena/resetmypassword.htm',
'/talentarena/worklist*.*', '/talentarena/run.*report.htm', '/talentarena/reportingchart.htm');




prompt ********************** please handle the local page urls for all the clients *************************** 
prompt ********************** please delete duplicate permits and run a general tidy up *************************** 

commit;
