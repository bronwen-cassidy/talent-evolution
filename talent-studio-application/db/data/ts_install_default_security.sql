EXEC zynap_loader_sp.create_area(-1, 'Default Area', 'Please edit this area', 'T' );
insert into area_elements(ID, AREA_ID, NODE_ID, CASCADING) VALUES(assoc_sq.nextval,-1,0,'T');
insert into area_elements(ID, AREA_ID, NODE_ID, CASCADING) VALUES(assoc_sq.nextval,-1,1,'T');

insert into security_domains VALUES(-1,-1,'F','Default Security Domain','This domain contains all','T');
insert into roles_security_domains VALUES(-1,2);
insert into security_domains_users VALUES(-1, zynap_app_sp.get_admin_user());
insert into security_domains_users VALUES(-1, zynap_app_sp.get_system_user());
--webservice user
insert into security_domains_users VALUES(-1, 2);


EXEC zynap_loader_sp.create_area(-2, 'People without position area', 'People without position area', 'T' );
insert into security_domains VALUES(-2,-2,'F','People without position domain','This domain contains people without position','T');
insert into roles_security_domains  Select '-2' ,r.id from roles r where r.label ='Full People Control';
insert into security_domains_users VALUES(-2, zynap_app_sp.get_admin_user());
insert into security_domains_users VALUES(-2, zynap_app_sp.get_system_user());

