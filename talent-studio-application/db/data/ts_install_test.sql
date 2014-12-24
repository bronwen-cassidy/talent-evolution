EXEC zynap_loader_sp.create_org_unit(-3, 'Test Org Unit',  0,  'Please edit this default org unit', 'T');


EXEC zynap_loader_sp.create_org_unit(-4, 'Test Org Unit', -0,  'Please edit this default org unit', 'T');


EXEC zynap_loader_sp.create_org_unit(-5, 'Test Org Unit', -3,  'Please edit this default org unit', 'T');


EXEC zynap_loader_sp.create_org_unit(-6, 'Test Org Unit', -4,  'Please edit this default org unit', 'T');


EXEC zynap_loader_sp.create_org_unit(-7, 'Test Org Unit', -6,  'Please edit this default org unit', 'T');



-- Create positions
EXEC zynap_loader_sp.create_position (-8, 'Child1',0, 1, 'This is the default position - please edit this', 'T');


EXEC zynap_loader_sp.create_position (-9, 'Child2',-3, 1, 'This is the default position - please edit this', 'T');


EXEC zynap_loader_sp.create_position (-10, 'GrandChild11',-3, -8 , 'This is the default position - please edit this', 'T');


EXEC zynap_loader_sp.create_position (-11, 'GrandChild12',-4, -8, 'This is the default position - please edit this', 'T');


EXEC zynap_loader_sp.create_position (-12, 'GrandChild21',-4, -9 , 'This is the default position - please edit this', 'T');


EXEC zynap_loader_sp.create_position (-13, 'GreatChild111',-5, -10, 'This is the default position - please edit this', 'T');


-- EXEC zynap_loader_sp.create_sample_positions (-8, 6, -14, 100);

--EXEC zynap_loader_sp.create_sample_positions (-15, 100, -200, 1000);
--
