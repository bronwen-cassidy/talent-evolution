CREATE OR REPLACE PACKAGE BODY zynap_loader_sp IS



 PROCEDURE create_org_unit (idp_ in integer, label_ in varchar, parent_id_ in integer, comments_ in varchar, is_active_ in varchar)
is

 id_ integer;
begin
if idp_ is null then
       select node_sq.nextval into id_ from dual;
else
    id_ := idp_;

end if;



  insert into nodes (id, node_type, comments , is_active)
  values (id_,'O', comments_, is_active_);

--Create a default org unit - required because org units cannot be orphans so system requires at least one
INSERT INTO organization_units (node_id,  label, parent_id)
VALUES (id_, label_, parent_id_);

  INSERT INTO node_audits(node_id, created, created_by) VALUES (id_,sysdate,0);

end      create_org_unit ;

PROCEDURE create_area (ida_ in integer, label_ in varchar,  comments_ in varchar , is_active_ in varchar)

is

 id_ integer;
begin
if ida_ is null then
       select node_sq.nextval into id_ from dual;
else
    id_ := ida_;

end if;



  insert into nodes (id, node_type, comments , is_active)
  values (id_,'A', comments_, is_active_);

--Create a default org unit - required because org units cannot be orphans so system requires at least one
INSERT INTO  areas(node_id,  label )
VALUES (id_, label_);

end      create_area ;



 PROCEDURE create_position ( idp_ in integer,title_ in varchar, ou_id_ in varchar,parent_id_ integer ,comments_ in varchar , is_active_ in varchar)
is
   id_ integer;
   lookvalue_id_ integer;
begin
if idp_ is null then
       select node_sq.nextval into id_ from dual;
else
    id_ := idp_;

end if;



  insert into nodes (id, node_type, comments , is_active)
  values (id_,'P', comments_, is_active_);


--Create a default position - required because positions cannot be orphans so system requires at least one
INSERT INTO positions (node_id,  title, org_unit_id, parent_id)
VALUES (id_, title_, ou_id_,parent_id_);

if parent_id_ is not null then
  select id into lookvalue_id_ from lookup_values where type_id = 'PRIMARY'  AND VALUE_ID ='DIRECT';

  insert into position_associations VALUES(assoc_sq.nextval,id_,parent_id_,lookvalue_id_);
end if;

  INSERT INTO node_audits(node_id, created, created_by) VALUES (id_,sysdate,0);


end      create_position ;

PROCEDURE menu_permits_link

IS
 CURSOR cur IS SELECT * FROM permits where type ='AP';
 url_  varchar2(400);

BEGIN
    FOR rec_ IN cur LOOP
    url_ := rec_.url;
    url_ := replace(url_,'*','%');
   	update menu_items set permit_id = rec_.id where url like url_;
   end loop;
  exception when others  then
   null;

end menu_permits_link;


PROCEDURE create_sample_positions (idp_ in integer, countparents_ in integer, initId_ in integer, total_ in integer)
is
   parentId_ integer;
   currentId_ integer;
   count_ integer;

begin

   parentId_ := idp_ -1;
   currentId_ := initId_;
   FOR count IN 1..total_
      LOOP
		parentId_ := parentId_ - 1;
		if (parentId_ < idp_ - countparents_ + 1) then
		 parentId_ := idp_;
		end if;
		currentId_ := currentId_ - 1;
		create_position( currentId_  , 'title' || currentId_ , 0 ,parentId_  , '' , 'T');
   END LOOP;

END  create_sample_positions;



  function splitLine (line in varchar2,separator in varchar,idx out integer) return string_array  is
   prefix varchar2(255);
   sufix varchar2(4000);
   results string_array;
   pos integer;
   len integer;

   begin
    sufix := line;
    idx := 0;
    results:= string_array();
    loop
       idx:=idx+1;
       pos := instr(sufix,separator);
       len := length(sufix);
       if pos <= 0 then
            results.extend;
            results(idx):=sufix;
       		exit;
       end if;
       prefix:= substr(sufix,1,pos-1);
       sufix:= substr(sufix,pos+1);
       results.extend;
       results(idx) := prefix;

    end loop;

    return results;
  end splitLine;

END zynap_loader_sp;
/
