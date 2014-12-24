DROP TABLE WF_ITEM_ACTIVITY_STATUSES;
DROP TABLE WF_ITEM_ACTIVITY_STATUSES_H;
DROP TABLE WF_ITEM_ATTRIBUTES_TL;
DROP TABLE WF_ITEM_ATTRIBUTE_VALUES;
DROP TABLE WF_ITEM_ATTRIBUTES;
DROP TABLE WF_ITEMS;
DROP TABLE WF_ACTIVITY_ATTRIBUTES_TL;
DROP TABLE WF_ACTIVITY_ATTRIBUTES;
DROP TABLE WF_ACTIVITY_ATTR_VALUES;
DROP TABLE WF_ACTIVITY_TRANSITIONS;
DROP TABLE WF_PROCESS_ACTIVITIES;
DROP TABLE WF_ACTIVITIES_TL;
DROP TABLE WF_ACTIVITIES;
DROP TABLE WF_MESSAGE_ATTRIBUTES_TL;
DROP TABLE WF_MESSAGE_ATTRIBUTES;
DROP TABLE WF_MESSAGES_TL;
DROP TABLE WF_MESSAGES;
DROP TABLE WF_AGENTS;
DROP TABLE WF_AGENT_GROUPS;
DROP TABLE WF_EVENTDEMO_ITEMS;
DROP TABLE WF_EVENTDEMO_PO;
DROP TABLE WF_EVENT_GROUPS;
DROP TABLE WF_EVENT_SUBSCRIPTIONS;
DROP TABLE WF_EVENTS_TL;
DROP TABLE WF_EVENTS;
DROP TABLE WF_LOCAL_LANGUAGES;
DROP TABLE WF_LOCAL_USER_ROLES;
DROP TABLE WF_LOCAL_ROLES;
DROP TABLE WF_LOCAL_USERS;
DROP TABLE WF_LOOKUPS_TL;
DROP TABLE WF_LOOKUP_TYPES_TL;
DROP TABLE WF_ITEM_TYPES_TL;
DROP TABLE WF_ITEM_TYPES;
DROP TABLE WF_NOTIFICATIONS;
DROP TABLE WF_NOTIFICATION_ATTRIBUTES;
DROP TABLE WF_QUEUES;
DROP TABLE WF_REQDEMO_EMP_AUTHORITY;
DROP TABLE WF_REQDEMO_EMP_HIERARCHY;
--DROP TABLE WF_RESOURCES;
DROP TABLE WF_ROUTING_RULE_ATTRIBUTES;
DROP TABLE WF_ROUTING_RULES;
DROP TABLE WF_SURVEY_DEMO;
DROP TABLE WF_SYSTEMS;
CREATE TABLE "WF_ITEM_ATTRIBUTE_VALUES" ("ITEM_TYPE"
    VARCHAR2(8 byte) NOT NULL, "ITEM_KEY" VARCHAR2(240 byte) NOT
    NULL, "NAME" VARCHAR2(30 byte) NOT NULL, "TEXT_VALUE"
    VARCHAR2(4000 byte), "NUMBER_VALUE" NUMBER, "DATE_VALUE" DATE,
    "EVENT_VALUE" "WF_EVENT_T")
    TABLESPACE "USERS" PCTFREE 25 PCTUSED 0 INITRANS 1 MAXTRANS
    255
    STORAGE ( INITIAL 16K NEXT 0K MINEXTENTS 1 MAXEXTENTS
    2147483645 PCTINCREASE 0)
    LOGGING LOB("EVENT_VALUE"."EVENT_DATA") STORE AS ( TABLESPACE
    "USERS"
    STORAGE ( INITIAL 64K NEXT 0K MINEXTENTS 1 MAXEXTENTS
    2147483645 PCTINCREASE 0)
    ENABLE
    STORAGE IN ROW
    NOCACHE CHUNK 8192 PCTVERSION 10);
CREATE UNIQUE INDEX "WF_ITEM_ATTRIBUTE_VALUES_PK"
    ON "WF_ITEM_ATTRIBUTE_VALUES"  ("ITEM_TYPE",
    "ITEM_KEY", "NAME")
    TABLESPACE "USERS" PCTFREE 10 INITRANS 2 MAXTRANS 255
    STORAGE ( INITIAL 16K NEXT 0K MINEXTENTS 1 MAXEXTENTS
    2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1)
    LOGGING;
QUIT;
