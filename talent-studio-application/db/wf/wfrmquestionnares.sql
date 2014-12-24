rem HEADER
rem   $Header: wfrmtype.sql 26.0 2000/07/05 22:06:28 kma ship $
rem NAME
rem   wfrmtype.sql - WorkFlow ReMove TYPE
rem USAGE
rem   @wfrmtype
rem DESCRIPTION
rem   DANGER *** DANGER *** DANGER *** DANGER *** DANGER *** DANGER
rem
rem   You will be prompted for the type, from a list of valid types.
rem   Purges ALL runtime data associated with a given item type.
rem
rem   DANGER *** DANGER *** DANGER *** DANGER *** DANGER *** DANGER

begin
  if ('ALL' = 'ALL') then
    update wf_notifications set
      end_date = sysdate
    where group_id in
      (select notification_id
      from wf_item_activity_statuses
      where item_type = 'QUESTION'
      union
      select notification_id
      from wf_item_activity_statuses_h
      where item_type = 'QUESTION');

    update wf_items set
      end_date = sysdate
    where item_type = 'QUESTION';

    update wf_item_activity_statuses set
      end_date = sysdate
    where item_type = 'QUESTION';

    update wf_item_activity_statuses_h set
      end_date = sysdate
    where item_type = 'QUESTION';
  end if;

  wf_purge.total('QUESTION', null, sysdate);
end;
/
commit;


