DECLARE

CURSOR das_cur IS select nd.* from node_das nd, dynamic_attributes da, questionnaires q
where nd.node_id =q.node_id
and q.qwf_id=1
and nd.da_id=da.id
and da.label=' This Year';

das_rec node_das%rowtype;

qwf_new_val number;
subject_id_ number;
q_id_ number;
q_count number;

BEGIN

 qwf_new_val:=2;

 FOR das_rec IN das_cur LOOP

      -- load in the subject_id from questionnaires with the given node_id
      select subject_id into subject_id_ from questionnaires where node_id=das_rec.node_id;

      -- check to see if we have a questionnaire
      select count(*) into q_count from questionnaires where qwf_id=qwf_new_val and subject_id=subject_id_;

      IF q_count > 0 THEN
          -- we have a questionnaire no need to create one
        dbms_output.put_line('in the if');

      ELSE
          -- we need to create a questionnaire
        dbms_output.put_line('in the else');

      END IF;


 END LOOP;

END;
/

