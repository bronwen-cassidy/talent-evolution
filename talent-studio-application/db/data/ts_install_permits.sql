-- Permits for Administration arena

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'HOME', 'Access to Home Page','T', '/admin/home.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'MENUS', 'Access to Menus','T', '/admin/menu.htm', null, null, null);

-- Permits for security domain admin
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SECURITY DOMAINS' ,'Permission to Browse Security Domains', 'T','/admin/listsecuritydomain.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','SECURITY DOMAINS' ,'Permission to View Security Domains', 'T','/admin/viewsecuritydomain.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','SECURITY DOMAINS' ,'Permission to Create Security Domains', 'T','/admin/addsecuritydomain.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','SECURITY DOMAINS' ,'Permission to Edit Security Domains', 'T','/admin/editsecuritydomain.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','SECURITY DOMAINS' ,'Permission to Delete Security Domains', 'T','/admin/deletesecuritydomain.htm', null, null, null) ;

-- Home page Permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SECURITY DOMAINS' ,'Permission to Browse Home Pages', 'T','/admin/listhomepages.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','SECURITY DOMAINS' ,'Permission to Modify Home Pages', 'T','/admin/.*homepages.htm', null, null, null) ;

-- Permits for area admin
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','AREAS' ,'Permission to Browse Areas', 'T','/admin/listarea.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','AREAS' ,'Permission to View Areas', 'T','/admin/viewarea.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','AREAS' ,'Permission to Create Areas', 'T','/admin/addarea.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','AREAS' ,'Permission to Edit Areas', 'T','/admin/editarea.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','AREAS' ,'Permission to Delete Areas', 'T','/admin/deletearea.htm', null, null, null) ;

-- Permits for role admin
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ROLES' ,'Permission to Browse Roles', 'T','/admin/listrole.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','ROLES' ,'Permission to View Roles', 'T','/admin/viewrole.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','ROLES' ,'Permission to Create Roles', 'T','/admin/selectroletype.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','ROLES' ,'Permission to Edit Roles', 'T','/admin/editrole.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','ROLES' ,'Permission to Delete Roles', 'T','/admin/deleterole.htm', null, null, null) ;

-- Permits for user admin
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','USERS' ,'Permission to Browse Users', 'T','/admin/listuser.htm', null, null, null)  ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','USERS' ,'Permission to View Users', 'T','/admin/viewuser.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','USERS' ,'Permission to Create Users', 'T','/admin/adduser.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','USERS' ,'Permission to Edit Users', 'T','/admin/edituser.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','USERS' ,'Permission to Delete Users', 'T','/admin/deleteuser.htm', null, null, null);

-- Permits for extended attributes admin
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ATTRIBUTES' ,'Permission to Browse Person Attributes', 'T','/admin/listsubjectDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ATTRIBUTES' ,'Permission to Browse Position Attributes', 'T','/admin/listpositionDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ATTRIBUTES' ,'Permission to Browse Organisation Attributes', 'T','/admin/listorganisationDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ATTRIBUTES' ,'Permission to Browse Position Attributes', 'T','/admin/listpositionDAforarena.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ATTRIBUTES' ,'Permission to Browse Position Attributes', 'T','/admin/listsubjectDAforarena.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','ATTRIBUTES'   ,'Permission to View Details of Person/Position Attributes', 'T','/admin/viewDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','ATTRIBUTES' ,'Permission to Create Person/Position Attributes', 'T','/admin/addDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','ATTRIBUTES' ,'Permission to Edit Person/Position Attributes', 'T','/admin/editDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','ATTRIBUTES' ,'Permission to Make Person/Position Attributes Mandatory', 'T','/admin/mandatoryDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','ATTRIBUTES' ,'Permission to Disable/Enable Person/Position Attributes', 'T','/admin/toggleactiveDA.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','ATTRIBUTES' ,'Permission to Remove Person/Position Attributes From Arenas', 'T','/admin/removeDAfromarena.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','ATTRIBUTES' ,'Permission to Modify Person/Position Attributes Sort Order', 'T','/admin/updateDAsortorder.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','ATTRIBUTES' ,'Permission to Delete Person/Position Attributes', 'T','/admin/deleteDA.htm', null, null, null);

-- Permits for security rules settings admin
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','TEMPLATES' ,'Permission to View Settings', 'T','/admin/listsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','TEMPLATES' ,'Permission to View Syncs', 'T','/admin/listadaptors.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','TEMPLATES' ,'Permission to View Tab Settings', 'T','/admin/tabsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','TEMPLATES' ,'Permission to Edit Tab Settings', 'T','/admin/edittabsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','TEMPLATES' ,'Permission to Edit Settings', 'T','/admin/editrules.htm', null, null, null);

-- Permits for questionnaires admin
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','QUESTIONNAIRES' ,'Permission to list questionnaire definitions', 'T','/admin/listquestionnairedefinition.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','QUESTIONNAIRES' ,'Permission to list questionnaires', 'T','/admin/listqueworkflows.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','QUESTIONNAIRES' ,'Permission to add questionnaire definitions', 'T','/admin/addquestionnaire.*.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','QUESTIONNAIRES' ,'Permission to edit questionnaire definitions', 'T','/admin/editquestion.*.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','QUESTIONNAIRES' ,'Permission to view questionnaire definition details', 'T','/admin/viewquestion.*.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','QUESTIONNAIRES' ,'Permission to delete questionnaire definitions', 'T','/admin/confirmdeletequestionnaire.*.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'close','QUESTIONNAIRES' ,'Permission to close questionnaires', 'T','/admin/.*questionnaireworkflow.*.htm', null, null, null);

-- Permits for corporate objectives
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','OBJECTIVES' ,'Permission to add/edit corporate objectives', 'T','/admin/.*objectives.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','OBJECTIVES' ,'Permission to list corporate objectives', 'T','/admin/listcorporateobjectives.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','OBJECTIVES' ,'Permission to list corporate objectives', 'T','/admin/listorgunitobjectives.htm', null, null, null);

-- Permits for editing arena settings
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','TEMPLATES' ,'Permission to View Arena Configuration', 'T','/admin/listarenas.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','TEMPLATES' ,'Permission to Edit Arena Configuration', 'T','/admin/editarena.htm', null, null, null);

-- Permits for viewing and editing display config settings
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','TEMPLATES' ,'Permission to View, Edit Display Config Settings', 'T','/admin/displayconfigsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','DASHBOARDS' ,'Permission to Browse Dashboards', 'T','/admin/listdashboards.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','DASHBOARDS' ,'Permission to Modify Dashboards', 'T','/admin/.*dashboard.htm', null, null, null);

-- Permits for selection types
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','LOOKUPS' ,'Permission to Browse Selection Types', 'T','/admin/listlookuptypes.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','LOOKUPS' ,'Permission to Add Selection Types', 'T','/admin/addlookuptype.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','LOOKUPS' ,'Permission to Edit Selection Types', 'T','/admin/editlookuptype.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','LOOKUPS'   ,'Permission to View Selection Type Values', 'T','/admin/listlookupvalues.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','LOOKUPS' ,'Permission to Add Selection Type Values', 'T','/admin/addlookupvalue.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','LOOKUPS'   ,'Permission to View Selection Type Value', 'T','/admin/viewlookupvalue.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','LOOKUPS' ,'Permission to Edit Selection Type Value', 'T','/admin/editlookupvalue.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','LOOKUPS' ,'Permission to Delete Selection Type Value', 'T','/admin/deletelookupvalue.htm', null, null, null);

-- Permit to reset user passwords (ie: passwords other than one's own
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','PASSWORDS' ,'Permission to Reset People/Users Passwords', 'T','/admin/resetpassword.htm', null, null, null);

-- Org builder app permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'HOME', 'Access to Home Page','T', '/orgbuilder/home.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'MENUS', 'Access to Menus','T', '/orgbuilder/menu.htm', null, null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ORGANISATIONS' ,'Permission to Browse Organisations', 'T','/orgbuilder/listorganisations.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Browse People', 'T','/orgbuilder/listsubject.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','QUESTIONNAIRES' ,'Permission to Browse Questionnaires', 'T','/orgbuilder/listquestionnaire.htm', null, null, null) ;

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','POSITIONS' ,'Permission to Search Positions', 'T','/orgbuilder/listposition.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Browse People via Autonomy', 'T','/orgbuilder/subjectdocumentsearch.*.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','SUBJECTS' ,'Permission to Browse People', 'T','/orgbuilder/browsesubject.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','POSITIONS' ,'Permission to Browse Positions', 'T','/orgbuilder/browseposition.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','REPORTING STRUCTURE' ,'Permission to Browse Reporting Structure', 'T','/orgbuilder/reportingchart.htm', null, null, null);


-- Org builder app permits to edit subject user details and reset subject user passwords
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','USERS' ,'Permission to Edit Users', 'T','/orgbuilder/edituser.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','USERS' ,'Permission to Delete Users', 'T','/orgbuilder/deleteuser.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','PASSWORDS' ,'Permission to Reset People/Users Passwords', 'T','/orgbuilder/resetpassword.htm', null, null, null);

-- Org Builder permit to run reports
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'run','REPORTS' ,'Permission to Run Reports', 'T','/orgbuilder/run.*report.htm', null, null, null);

-- Talent identifier app permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'HOME', 'Access to Home Page','T', '/talentidentifier/home.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'MENUS', 'Access to Menus','T', '/talentidentifier/menu.htm', null, null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Search People', 'T','/talentidentifier/listsubject.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','POSITIONS' ,'Permission to Search Positions', 'T','/talentidentifier/listposition.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','SUBJECTS' ,'Permission to Browse People', 'T','/talentidentifier/browsesubject.*', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','POSITIONS' ,'Permission to Browse Positions', 'T','/talentidentifier/browseposition.*', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','REPORTING STRUCTURE' ,'Permission to Browse Reporting Structure', 'T','/talentidentifier/reportingchart.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Browse People via Autonomy', 'T','/talentidentifier/subjectdocumentsearch.*.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','DOCUMENTS' ,'Permission to Search People and Positions using their documents', 'T','/talentidentifier/documentsearch.htm', null, null, null);

-- Talent identifier permit to run reports
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'run','REPORTS' ,'Permission to Run Reports', 'T','/talentidentifier/run.*report.htm', null, null, null);

-- Succession Builder app permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'HOME', 'Access to Home Page','T', '/succession/home.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'MENUS', 'Access to Menus','T', '/succession/menu.htm', null, null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Browse People', 'T','/succession/listsubject.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','POSITIONS' ,'Permission to Search Positions', 'T','/succession/listposition.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','REPORTING STRUCTURE' ,'Permission to Browse Reporting Structure', 'T','/succession/reportingchart.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','POSITIONS' ,'Permission to Browse Positions', 'T','/succession/browseposition.*', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','SUBJECTS' ,'Permission to Browse People', 'T','/succession/browsesubject.*', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Browse People via Autonomy', 'T','/succession/subjectdocumentsearch.*.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','DOCUMENTS' ,'Permission to Search People and Positions using their documents', 'T','/succession/documentsearch.htm', null, null, null);

-- Succession Builder permit to run reports
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'run','REPORTS' ,'Permission to Run Reports', 'T','/succession/run.*report.htm', null, null, null);

-- Analyser arena app permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'HOME', 'Access to Home Page','T', '/analysis/home.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'MENUS', 'Access to Menus','T', '/analysis/menu.htm', null, null, null);

-- populations
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','POPULATIONS' ,'Permission to Browse Populations', 'T','/analysis/listpopulations.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','POPULATIONS' ,'Permission to Create Populations', 'T','/analysis/addpopulation.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','POPULATIONS' ,'Permission to Edit Populations', 'T','/analysis/editpopulation.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','POPULATIONS' ,'Permission to View Population Details', 'T','/analysis/viewpopulation.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','POPULATIONS' ,'Permission to Delete Populations', 'T','/analysis/deletepopulation.htm', null, null, null);

-- reports
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','REPORTS' ,'Permission to Browse Reports', 'T','/analysis/listreports.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','CROSSTAB REPORTS' ,'Permission to Browse Cross-Tab Reports', 'T','/analysis/listcrosstabreports.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','METRIC REPORTS' ,'Permission to Browse Metric Reports', 'T','/analysis/listmetricreports.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','APPRAISAL REPORTS' ,'Permission to Browse Appraisal Report Templates', 'T','/analysis/listappraisalreports.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','CHART REPORTS' ,'Permission to Browse Chart Reports', 'T','/analysis/listchartreports.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','PROGRESS REPORTS' ,'Permission to Browse Progress Reports', 'T','/analysis/listprogressreports.htm', null, null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','REPORTS' ,'Permission to Create Reports', 'T','/analysis/add.*report.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','REPORTS' ,'Permission to Edit Reports', 'T','/analysis/edit.*report.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','REPORTS' ,'Permission to Delete Reports', 'T','/analysis/delete.*report.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','REPORTS' ,'Permission to View Report Details', 'T','/analysis/view.*.report.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','REPORTS' ,'Permission to View and Run Reports', 'T','/analysis/runview.*report.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'run','REPORTS' ,'Permission to Run Reports', 'T','/analysis/run.*report.htm', null, null, null);

-- metrics
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','METRICS' ,'Permission to Browse Metrics', 'T','/analysis/listmetrics.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','METRICS' ,'Permission to Manage Metrics', 'T','/analysis/.*metric.htm', null, null, null);

-- portfolio document search
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Browse People via Autonomy', 'T','/analysis/subjectdocumentsearch.*.htm', null, null, null) ;
-- Permits for reporting chart settings
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','CHART SETTINGS' ,'Permission to Browse Reporting Charts', 'T','/analysis/listchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','CHART SETTINGS' ,'Permission to View Reporting Charts', 'T','/analysis/viewchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','CHART SETTINGS' ,'Permission to Edit Reporting Charts', 'T','/analysis/editchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','CHART SETTINGS' ,'Permission to Add Reporting Charts', 'T','/analysis/addchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','CHART SETTINGS' ,'Permission to Reset Reporting Charts', 'T','/analysis/resetchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','CHART SETTINGS' ,'Permission to Delete Reporting Charts', 'T','/analysis/deletechartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','REPORTING STRUCTURE' ,'Permission to Browse Reporting Structure', 'T','/analysis/reportingchart.htm', null, null, null);

-- Performance Management arena app permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'HOME', 'Access to Home Page','T', '/perfman/home.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'MENUS', 'Access to Menus','T', '/perfman/menu.htm', null, null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Search People', 'T','/perfman/listsubject.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','POSITIONS' ,'Permission to Search Positions', 'T','/perfman/listposition.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','POSITIONS' ,'Permission to Browse Positions', 'T','/perfman/browseposition.*', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','SUBJECTS' ,'Permission to Browse People', 'T','/perfman/browsesubject.*', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','REPORTING STRUCTURE' ,'Permission to Browse Reporting Structure', 'T','/perfman/reportingchart.htm', null, null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','SUBJECTS' ,'Permission to Browse People via Autonomy', 'T','/perfman/subjectdocumentsearch.*.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','DOCUMENTS' ,'Permission to Search People and Positions using their documents', 'T','/perfman/documentsearch.htm', null, null, null);

-- Appraisal permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','APPRAISALS' ,'Permission to Browse Appraisals', 'T','/perfman/listperformancereviews.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','APPRAISALS' ,'Permission to View Appraisals', 'T','/perfman/view.*review.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','APPRAISALS' ,'Permission to Create Appraisals', 'T','/perfman/add.*review.htm', null, null, null) ;
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','APPRAISALS' ,'Permission to Delete Appraisals', 'T','/perfman/delete.*review.htm', null, null, null) ;

-- Performance Management permit to run reports
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'run','REPORTS' ,'Permission to Run Reports', 'T','/perfman/run.*report.htm', null, null, null);

--Home arena app permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'HOME', 'Access to Home Page','T', '/talentarena/home.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'MENUS', 'Access to Menus','T', '/talentarena/menu.htm', null, null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'PROCESS', 'Access to Process Description','T', '/talentarena/processdescription.htm', null, null, null);

-- my details
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'DETAILS', 'Access to My Details','T', '/.*/viewmy*.*', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'DETAILS', 'Permission to Edit My Details','T', '/.*/editmydetails.htm', null, null, null);

--my account
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'ACCOUNT', 'Permission to Edit My Account','T', '/talentarena/editmyaccount.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'ACCOUNT', 'Permission to View My Account','T', '/talentarena/viewmyaccount.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'ACCOUNT', 'Permission to View My Details','T', '/talentarena/viewmydetails.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'ACCOUNT', 'Permission to View My Team','T', '/talentarena/viewmyteam.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'ACCOUNT', 'Permission to View My Portfolio','T', '/talentarena/viewmyportfolio.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'ACCOUNT', 'Permission to View My Dashboard','T', '/talentarena/viewmydashboard.htm', null, null, null);

-- chart settings
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','CHART SETTINGS' ,'Permission to Browse My Reporting Charts', 'T','/talentarena/listchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','CHART SETTINGS' ,'Permission to View My Reporting Charts', 'T','/talentarena/viewchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','CHART SETTINGS' ,'Permission to Edit My Reporting Charts', 'T','/talentarena/editchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','CHART SETTINGS' ,'Permission to Add My Reporting Charts', 'T','/talentarena/addchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','CHART SETTINGS' ,'Permission to Reset My Reporting Charts', 'T','/talentarena/resetchartsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'delete','CHART SETTINGS' ,'Permission to Delete My Reporting Charts', 'T','/talentarena/deletechartsettings.htm', null, null, null);

-- reset password
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'PASSWORDS', 'Permission to Edit My Password','T', '/talentarena/resetmypassword.htm', null, null, null);

-- todo lists
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'edit', 'TODOS', 'My todo lists','T', '/talentarena/worklist*.*', null, null, null);

-- my objectives permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'admin', 'OBJECTIVES', 'objectives','T', '/.*/.*myobjective*.*', null, null, null);

-- my portfolio document search
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'search', 'POSITIONS', 'Permission to Browse Positions via Autonomy','T', '/.*/myportfoliopositionsearch.*.htm', null, null, null);

-- my portfolio view portfolio items permits
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','CV' ,'Permission to View Curriculum Vitae', 'T','/.*/viewmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','SS' ,'Permission to View Person Sourced', 'T','/.*/viewmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','WC' ,'Permission to View Written Correspondence', 'T','/.*/viewmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','WP' ,'Permission to View Work Products', 'T','/.*/viewmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','TR' ,'Permission to View Training Records', 'T','/.*/viewmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','AR' ,'Permission to View Performance and Reviews', 'T','/.*/viewmyportfolioitem.htm', null, null, null);

-- permit to download my portfolio item
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'download','MY PORTFOLIO' ,'Permission to Download Portfolio Items', 'T','/.*/viewsubjectportfoliofile.htm', null, null, null);

-- my portfolio questionnaire permits
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', null, 'view','QUESTIONNAIRES' ,'Permission to View My Questionnaires', 'T','/.*/viewmyquestionnaire.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', null, 'edit','QUESTIONNAIRES' ,'Permission to Edit My Questionnaires', 'T','/.*/editmy.*questionnaire.htm', null, null, null);

  -- my portfolio create portfolio items permits
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','CV' ,'Permission to Create Curriculum Vitae', 'T','/.*/addmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','SS' ,'Permission to Create Person Sourced', 'T','/.*/addmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','WC' ,'Permission to Create Written Correspondence', 'T','/.*/addmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','WP' ,'Permission to Create Work Products', 'T','/.*/addmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','TR' ,'Permission to Create Training Records', 'T','/.*/addmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','AR' ,'Permission to Create Performance and Reviews', 'T','/.*/addmyportfolioitem.htm', null, null, null);

  -- my portfolio edit portfolio items permits
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'edit','CV' ,'Permission to Edit Curriculum Vitae', 'T','/.*/editmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'edit','SS' ,'Permission to Edit Person Sourced', 'T','/.*/editmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'edit','WC' ,'Permission to Edit Written Correspondence', 'T','/.*/editmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'edit','WP' ,'Permission to Edit Work Products', 'T','/.*/editmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'edit','TR' ,'Permission to Edit Training Records', 'T','/.*/editmyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'edit','AR' ,'Permission to Edit Performance and Reviews', 'T','/.*/editmyportfolioitem.htm', null, null, null);

  -- my portfolio delete portfolio items permits
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'delete','CV' ,'Permission to Delete Curriculum Vitae', 'T','/.*/deletemyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'delete','SS' ,'Permission to Delete Person Sourced', 'T','/.*/deletemyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'delete','WC' ,'Permission to Delete Written Correspondence', 'T','/.*/deletemyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'delete','WP' ,'Permission to Delete Work Products', 'T','/.*/deletemyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'delete','TR' ,'Permission to Delete Training Records', 'T','/.*/deletemyportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'delete','AR' ,'Permission to Delete Performance and Reviews', 'T','/.*/deletemyportfolioitem.htm', null, null, null);

  -- my position portfolio create portfolio items permits
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','DESC' ,'Permission to Create Position Description', 'T','/.*/addmypositionportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','CF' ,'Permission to Create Competency Framework', 'T','/.*/addmypositionportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'create','SM' ,'Permission to Create Supporting Materials', 'T','/.*/addmypositionportfolioitem.htm', null, null, null);

  -- my position portfolio view portfolio items permits
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','DESC' ,'Permission to View Position Description', 'T','/.*/viewmypositionportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','CF' ,'Permission to View Competency Framework', 'T','/.*/viewmypositionportfolioitem.htm', null, null, null);
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'c_type', 'view','SM' ,'Permission to View Supporting Materials', 'T','/.*/viewmypositionportfolioitem.htm', null, null, null);

-- permit to download my position portfolio item
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'download','MY POSITION PORTFOLIO' ,'Permission to Download Position Portfolio Items', 'T','/.*/viewpositionportfoliofile.htm', null, null, null);

-- permit to run reports
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'run','REPORTS' ,'Permission to Run Reports', 'T','/talentarena/run.*report.htm', null, null, null);

-- permit for reporting chart
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','REPORTING STRUCTURE' ,'Permission to Browse Reporting Structure', 'T','/talentarena/reportingchart.htm', null, null, null);

--Org Builder application permits to add elements
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','POSITIONS' ,'Permission to Create Positions', 'T','/orgbuilder/addposition.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','ORGANISATIONS' ,'Permission to Create Organisations', 'T','/orgbuilder/addorganisation.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'create','SUBJECTS' ,'Permission to Create People', 'T','/orgbuilder/addsubject.htm', null, null, null);

--------------------------------------------------------------------------
-- permits covering all arenas
--------------------------------------------------------------------------
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','POSITIONS' ,'Permission to Browse Positions via Autonomy', 'T','/.*positiondocumentsearch.*.htm', null, null, null) ;

-- Domain permits to access the elements in the organization
-- the urls should match any arena
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'view','POSITIONS' ,'Permission to View Positions', 'T','/.*viewposition.*', 'command.node.id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'edit','POSITIONS' ,'Permission to Edit Positions', 'T','/.*editposition.*', 'command.node.id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'delete','POSITIONS' ,'Permission to Delete Positions', 'T','/.*deleteposition.*', 'command.node.id', null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'view','ORGANISATIONS' ,'Permission to View Organisations', 'T','/.*vieworganisation.*', 'id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'edit','ORGANISATIONS' ,'Permission to Edit Organisations', 'T','/.*editorganisation.*', 'id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'delete','ORGANISATIONS' ,'Permission to Delete Organisations', 'T','/.*deleteorganisation.*', 'id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'merge','ORGANISATIONS' ,'Permission to Merge Organisations', 'T','/.*mergeorganisation.*', 'c_id', null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'view','SUBJECTS' ,'Permission to View People', 'T','/.*viewsubject.*', 'command.node.id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'delete','SUBJECTS' ,'Permission to Delete People', 'T','/.*deletesubject.*', 'command.node.id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'edit','SUBJECTS' ,'Permission to Edit People', 'T','/.*editsubject.*', 'command.node.id', null, null);

-- Domain permits to control the security on the elements in the organization
-- They should be used to filter when someone is building an area and a secure domain
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'secure','ORGANISATIONS' ,'Permission to Organisations security settings', 'T','/.*secureorganisation.*', 'command.node.id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'secure','POSITIONS' ,'Permission to Positions security settings', 'T','/.*secureposition.*', 'command.node.id', null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'DP', 'secure','SUBJECTS' ,'Permission to People security settings', 'T','/.*securesubject.*', 'command.node.id', null, null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'UPLOADS', 'Access to Data Uploads','T', '/admin/datauploads.htm', null, null, null);

exec zynap_loader_sp.menu_permits_link;