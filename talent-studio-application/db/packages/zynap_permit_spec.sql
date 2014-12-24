CREATE OR REPLACE
PACKAGE zynap_permit_sp IS

---------------------------------------------------------------------------------------------
-- Description - 
---------------------------------------------------------------------------------------------
PROCEDURE add_all_cache_permits;
PROCEDURE delete_all_cache_permits;
PROCEDURE refresh_all_cache_permits;

procedure add_cache_permits (
    user_id_ IN INTEGER,
    node_id_ IN INTEGER,
    action_ IN VARCHAR2 );

procedure add_users_permits;
procedure delete_users_permits;

PROCEDURE populate_domain_permits;

END zynap_permit_sp;
/
