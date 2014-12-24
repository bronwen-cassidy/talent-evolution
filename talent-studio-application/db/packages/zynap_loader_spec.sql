CREATE OR REPLACE PACKAGE zynap_loader_sp IS         

  type string_array is table of varchar2(255); 
  PROCEDURE create_org_unit (idp_ in integer, label_ in varchar, parent_id_ in integer,  comments_ in varchar, is_active_ in varchar);
   
  PROCEDURE create_position (idp_ in integer, title_ in varchar, ou_id_ in varchar, parent_id_ in integer, comments_ in varchar , is_active_ in varchar);

  PROCEDURE create_area (ida_ in integer, label_ in varchar,  comments_ in varchar , is_active_ in varchar);
  
  PROCEDURE menu_permits_link;
                                                                                                                                                         
  PROCEDURE create_sample_positions (idp_ in integer, countparents_ in integer, initId_ in integer , total_ in integer);

  function splitLine (line in varchar2,separator in varchar,idx out integer) return string_array ;


END zynap_loader_sp;
/
