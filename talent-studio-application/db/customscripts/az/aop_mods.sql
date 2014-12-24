update questions set que_group_id=1698 where id=1697;

update questions set id=QUE_MODEL_SQ.nextval where id=1702;
update questions set id=QUE_MODEL_SQ.nextval where id=1703;
update questions set id=QUE_MODEL_SQ.nextval where id=1704;
update questions set id=QUE_MODEL_SQ.nextval where id=1705;
update questions set id=QUE_MODEL_SQ.nextval where id=1706;
update questions set id=1703 where id=1697;

-- new trend question
insert into dynamic_attributes (id, label, type, artefact_type, qd_id, is_mandatory, is_active, is_searchable, is_dynamic, is_calculated, description, refers_to)
values(-4, 'Potential Trend', 'STRUCT', 'Q', 185, 'F', 'T', 'F', 'F', 'F','Potential Trend', 'PERFORMANCETREND0.69966576754536966867');

insert into questions (id, que_group_id, da_id, question_type, title, text_id)
values(1702, 1698, -4, 'RADIO', 'Potential Trend', 6000);
