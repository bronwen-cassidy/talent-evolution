Set define off;

ALTER TABLE ROLES DROP COLUMN COMMENTS;

@packages/zynap_auth_body.sql

COMMIT;