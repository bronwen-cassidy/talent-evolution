-------------------------------------------------------------------------------------
-- 10/09/08
-- insert script to create my team views for the nhs (Vanessa's group of people)
-- it creates select group of people by collecting all people in particular home_page groups
-- the insert then adds these people together with a dynamic_attribute id of 417413
-------------------------------------------------------------------------------------

DECLARE
    CURSOR corp_obj_cur IS select u.id from users u, groups g
            where u.GROUP_ID = g.id
            and (g.label = 'Graduate Area Manager' or g.label='Graduate Local Manager');
    l_row USERS.ID%TYPE;

    BEGIN
        OPEN corp_obj_cur;
            LOOP
                FETCH corp_obj_cur INTO l_row;
                EXIT WHEN corp_obj_cur%notfound;
                    INSERT INTO my_team_views (id, user_id, da_id)
                    VALUES (CONFIG_SQ.nextval, l_row, 417413);
            END LOOP;
        CLOSE corp_obj_cur;
    END;
/
commit;
