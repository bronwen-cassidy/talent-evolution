CREATE OR REPLACE
PACKAGE zynap_position_sp AS

-------------------------------------------------------------------------------------
--
-------------------------------------------------------------------------------------
PROCEDURE delete_position_cascade (
   position_id_ IN INTEGER
  ,user_id_ in integer
  ,username_ in varchar2
);

PROCEDURE delete_ou_positions_cascade (
   organisation_id_ IN INTEGER
   ,user_id_ in integer
   ,username_ in varchar2
);

END zynap_position_sp;
/
