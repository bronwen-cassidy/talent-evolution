CREATE OR REPLACE
PACKAGE BODY zynap_permit_sp IS

-------------------------------------------------------------------
-- permit add for all users
--------------------------------------------------------------------
PROCEDURE add_all_cache_permits

IS
BEGIN

     insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
     select USER_ID,NODE_ID,PERMIT_ID
        from ZYNAP_USER_DOMAIN_PERMITS dp
        where not exists (
      	    select /*+ NL_SJ */ 1
      	 	from USER_NODE_DOMAIN_PERMITS cp
      	 	where cp.user_id=dp.user_id
      	 	and cp.permit_id=dp.permit_id
      	 	and cp.node_id = dp.node_id);

END add_all_cache_permits;

-------------------------------------------------------------------
-- permit delete for all users
--------------------------------------------------------------------
PROCEDURE delete_all_cache_permits

IS
BEGIN
      delete from user_node_domain_permits where rowid in (
        (select rowid
        from USER_NODE_DOMAIN_PERMITS dp
        where not exists (
      	    select /*+ NL_SJ */ 1
      	 	from ZYNAP_USER_DOMAIN_PERMITS cp
      	 	where cp.user_id=dp.user_id
      	 	and cp.permit_id=dp.permit_id
      	 	and cp.node_id = dp.node_id))
        );

END delete_all_cache_permits;

-------------------------------------------------------------------------
-- adds new permits for the logged in user only
-------------------------------------------------------------------------
procedure add_cache_permits (
    user_id_ IN INTEGER,
    node_id_ IN INTEGER,
    action_ IN VARCHAR2 )

    IS
    BEGIN
     insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
     select USER_ID,NODE_ID,PERMIT_ID
        from ZYNAP_USER_DOMAIN_PERMITS dp
        where user_id = user_id_
        and node_id = node_id_
        and not exists (
      	    select 1
      	 	from USER_NODE_DOMAIN_PERMITS cp
      	 	where cp.user_id=user_id_
      	 	and cp.permit_id=dp.permit_id
      	 	and cp.node_id = node_id_);

end add_cache_permits;

---------------------------------------------------------------------
-- adds permits for all logged in users
---------------------------------------------------------------------
procedure add_users_permits
    IS
    BEGIN

    insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
    select USER_ID,NODE_ID,PERMIT_ID
        from ZYNAP_USER_DOMAIN_PERMITS dp
        where exists (
        	select 1 from sessions_opened )
        and not exists (
      	    select /*+ NL_SJ */ 1
      	 	from USER_NODE_DOMAIN_PERMITS cp
      	 	where cp.user_id=dp.user_id
      	 	and cp.permit_id=dp.permit_id
      	 	and cp.node_id = dp.node_id);

end add_users_permits;

---------------------------------------------------------------------------------
-- deletes old permits for all logged in users for nodes that have been deleted
---------------------------------------------------------------------------------
procedure delete_users_permits
    IS
    BEGIN

    delete from user_node_domain_permits where rowid in (
        (select rowid
        from USER_NODE_DOMAIN_PERMITS dp
        where exists (
        	select 1 from sessions_opened )
        and not exists (
      	    select 1
      	 	from ZYNAP_USER_DOMAIN_PERMITS cp
      	 	where cp.user_id=dp.user_id
      	 	and cp.permit_id=dp.permit_id
      	 	and cp.node_id = dp.node_id))
        );

end delete_users_permits;

---------------------------------------------------------------------
-- add delta domain permits for all users,
-- delete delta domain permits for all users
---------------------------------------------------------------------
PROCEDURE refresh_all_cache_permits

    IS
    BEGIN

    add_all_cache_permits;
    delete_all_cache_permits;

end refresh_all_cache_permits;

-------------------------------------------------------------------------------
-- populates the user_node_domain_permits table with all permits for all people
-- note only the administrator role gets the secure permit? Domain Permit
-- administration has been removed, if it ever returns we will need to mod these
-- procedures to take into consideration the action on a permit
--------------------------------------------------------------------------------
PROCEDURE populate_domain_permits
IS
BEGIN

    -- make sure the table is empty
    delete from USER_NODE_DOMAIN_PERMITS;
    insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
    select USER_ID,NODE_ID,PERMIT_ID from ZYNAP_USER_DOMAIN_PERMITS;

END populate_domain_permits;

END zynap_permit_sp;
/
