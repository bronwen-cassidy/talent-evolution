-- create a role to lock down a tab
insert into roles (id, type, description, is_active, is_system, label) values (-1, 'AR', 'Organisation Data', 'T', 'T', 'Organisation Data Role');

-- minimum info required to add a new tab
insert into display_config_items (ID, DISPLAY_CONFIG_ID, LABEL, is_active, content_type, hideable, sort_order, roles_modifiable)
VALUES (-55, -2, 'Organisation Data', 'T', 'ATTRIBUTE', 'T', 10, 'T');

insert into reports (ID, ACCESS_TYPE, REP_TYPE, LABEL, DEFAULT_POPULATION_ID, USER_ID)
values(-55, 'PUBLIC', 'S', 'Organisation Data', -2, 0);

INSERT INTO DISPLAY_ITEM_REPORTS (ID, DISPLAY_CONFIG_ITEM_ID, REPORT_ID, CONTENT_TYPE)
VALUES (-1, -55, -55, 'S');

INSERT INTO display_config_roles (DISPLAY_CONFIG_ITEM_ID, role_id)
VALUES (-55, 1);

