--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		Installs the default analysis information
-- Pre-condition:	This script should never be run alone. It should ALWAYS be started from ts_inserts.sql
-- Version: 		1.0
-- Since:			18/03/2005
-- Author: 			TS4 Team
--------------------------------------------------------------------------------------------------

INSERT INTO POPULATIONS (ID, TYPE, USER_ID, LABEL, SCOPE, DESCRIPTION)
VALUES(-1, 'P', 0, 'All Positions', 'Public', 'All Positions');

INSERT INTO POPULATIONS (ID, TYPE, USER_ID, LABEL, SCOPE, DESCRIPTION)
VALUES(-2, 'S', 0, 'All People', 'Public', 'All People');


 ------------------------------------------
 --     DEFAULT COUNT METRIC             --
 ------------------------------------------

 INSERT INTO METRICS(ID,ACCESS_TYPE,DESCRIPTION,LABEL,USER_ID,OPERATOR, ATTRIBUTE)
 VALUES(-1, 'Public', 'Counts the number of occurances', 'Count', 0, 'count', 'id');

