---------------------------------------
-- remove any notification left behind
---------------------------------------
DELETE from WF_NOTIFICATIONS WHERE CONTEXT LIKE('QUESTION%') AND (STATUS='CLOSED' or STATUS='OPEN');
commit;