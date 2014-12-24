delete from lookup_values where id=4875; -- Ascension Island
delete from lookup_values where id=4892; -- Bouvet Island
delete from lookup_values where id=4894; -- British Indian Ocean Territory
delete from lookup_values where id=4910; -- Cocos (Keeling) Islands
delete from lookup_values where id=4917; -- Cote d'Ivoire
delete from lookup_values where id=4926; -- East Timor
delete from lookup_values where id=4941; -- French Southern Territories
delete from lookup_values where id=4954; -- Guernsey
delete from lookup_values where id=4968; -- Isle of Man
delete from lookup_values where id=4973; -- Jersey
delete from lookup_values where id=5061; -- South Georgia & the South Sandwich Islands
delete from lookup_values where id=5066; -- Svalbard & Jan Mayen Islands
delete from lookup_values where id=5090; -- US Minor Outlying Islands	US Minor Outlying Islands
delete from lookup_values where id=5099; -- 5099	Western Sahara	Western Sahara
delete from lookup_values where id=5100; -- 5100	Western Samoa	Western Samoa

-- add in extras
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'AZORES', 'Azores', 'Azores', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'BALEARICISLANDS', 'Balearic Islands', 'Balearic Islands', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'CANARYISLANDS', 'Canary Islands', 'Canary Islands', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'CHATHAMISLANDS', 'Chatham Islands', 'Chatham Islands', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'DIEGOGARCIA', 'Diego Garcia', 'Diego Garcia', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'HAWAII', 'Hawaii', 'Hawaii', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'IVORYCOAST', 'Ivory Coast', 'Ivory Coast', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'PALESTINE', 'Palestine', 'Palestine', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'RODRIGUEZISLAND', 'Rodriguez Island', 'Rodriguez Island', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'SAMOA', 'Samoa', 'Samoa', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'TRISTANDACUNHA', 'Tristan da Cunha', 'Tristan da Cunha', 0, FALSE);
EXEC zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'WAKEISLAND', 'Wake Island', 'Wake Island', 0, FALSE);

-- update the labels


-- update the sort orders in alphabetic order
DECLARE
    cursor node_cur is SELECT * FROM LOOKUP_VALUES WHERE type_id ='COUNTRY0.04539890841005611628950' order by upper(short_desc);

    p_sort_order number;
    n_row lookup_values%rowtype;

    BEGIN
    	p_sort_order := 0;

        OPEN node_cur;
            LOOP
                FETCH node_cur into n_row;
                EXIT WHEN node_cur%notfound;
                p_sort_order := p_sort_order + 1;
                UPDATE LOOKUP_VALUES SET SORT_ORDER = p_sort_order where ID = n_row.id;
            END LOOP;
        CLOSE node_cur;
    END;
/

-- adding in the other to the countries list
DECLARE
 	p_sort_order number;

 	BEGIN
 		select max(sort_order + 1) into p_sort_order from lookup_values where type_id='COUNTRY0.04539890841005611628950';
 		zynap_lookup_sp.install_values('COUNTRY0.04539890841005611628950', 'OTHER.1222', 'Other', 'Other', p_sort_order, FALSE);
 	END;
/

update dynamic_attributes set refers_to='COUNTRY0.04539890841005611628950' where id=28959;
update dynamic_attributes set refers_to='COUNTRY0.04539890841005611628950' where id=28960;
update dynamic_attributes set type='TEXT' where id=28977;
update dynamic_attributes set refers_to = null where id=28977;

-- drop other types

select id, value_id, short_desc, sort_order from lookup_values where type_id='COUNTRY0.04539890841005611628950' order by upper(short_desc);
