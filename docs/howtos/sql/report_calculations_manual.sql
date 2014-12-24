--insert into calculations(ID, TYPE, FORMAT) values (-1, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-2, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-3, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-4, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-5, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-6, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-7, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-8, 'MIXED', 0);
--insert into calculations(ID, TYPE, FORMAT) values (-9, 'MIXED', 0);

--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, que_wf_id, attribute, right_bracket, format, position, operator)
--values(-14, -5, null, 482, 26886, null, 0, 0, '-');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, que_wf_id, attribute, right_bracket, format, position, operator)
--values(-15, -5, null, 482, 26882, null, 0, 1, null);
--update report_columns set calculation_id=-5 where id=516;
--
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, que_wf_id, attribute, right_bracket, format, position, operator)
--values(-16, -6, null, 482, 26894, null, 0, 0, '-');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, que_wf_id, attribute, right_bracket, format, position, operator)
--values(-17, -6, null, 482, 26890, null, 0, 1, null);
--update report_columns set calculation_id=-6 where id=517;
--
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, que_wf_id, attribute, right_bracket, format, position, operator)
--values(-18, -7, null, 482, 26903, null, 0, 0, '-');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, que_wf_id, attribute, right_bracket, format, position, operator)
--values(-19, -7, null, 482, 26898, null, 0, 1, null);
--update report_columns set calculation_id=-7 where id=518;

insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, attribute, right_bracket, format, position, operator)
values(-23, -9, '(', 185486, null, 0, 0, '+');
insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, attribute, right_bracket, format, position, operator)
values(-24, -9, null, 17394 , ')', 0, 1, null);
update report_columns set calculation_id=-9 where id=5496;


--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-3, -2, null, -1, null, 0, 0, '+');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-4, -2, null, -1, null, 0, 1, null);
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-5, -2, null, 1, null, 0, 2, null);
--update report_columns set calculation_id=-2 where id=4423;

--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-6, -3, '(', 179, null, 0, 0, '+');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-7, -3, null, 180, ')', 0, 1, '/');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-8, -3, null, -1, null, 0, 1, null);
--update report_columns set calculation_id=-3 where id=371;

--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-8, -4, null, 4, null, 0, 0, '+');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-9, -4, null, 6, null, 0, 1, null);
--update report_columns set calculation_id=-4 where id=206;
--
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-10, -5, '(', 4, null, 0, 0, '+');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-11, -5, null, 6, ')', 0, 1, '-');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-12, -5, null, 44, null, 0, 2, null);
--update report_columns set calculation_id=-5 where id=208;
--
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-13, -6, '(', 46, null, 0, 0, '+');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-14, -6, null, 47, null, 0, 1, '+');
--insert into CALCULATION_EXPRESSIONS (id, calculation_id, left_bracket, metric, right_bracket, format, position, operator)
--values(-15, -6, null, 49, ')', 0, 2, null);
--update report_columns set calculation_id=-6 where id=209;

commit;

select r.label as rep_label, r.rep_type, rc.* from report_columns rc, reports r
where is_formula = 'T'
and rc.rep_id=r.id
and rep_type='STANDARD'
order by rep_id, position;


-- we need 6 calculations

--select * from que_workflows where id=475;

--create a calculation_id for each
--select * from calculations;

--select * from metrics where id in (1, 4, 6, 161, 44, 46, 47, 49);
select * from node_das where da_id in (34937, 34939);
