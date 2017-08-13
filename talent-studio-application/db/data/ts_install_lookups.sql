--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		Installs the lookup types and it's values in the system
--
-- Pre-condition:	Database must have been set up (ts_setup.sql) + System user must be installaed (ts_install_def_user.sql)
-- Version: 		1.0
-- Since:			26/05/2004
-- Author: 			Andreas Andersson
--------------------------------------------------------------------------------------------------

-- gender
EXEC zynap_lookup_sp.install_type( 'GENDER', 'SYSTEM', 'Genders for people', 'Gender');
EXEC zynap_lookup_sp.install_values( 'GENDER', 'MALE', 'Male', 'Male', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'GENDER', 'FEMALE', 'Female', 'Female', 20, FALSE);

  -- title
EXEC zynap_lookup_sp.install_type( 'TITLE', 'SYSTEM', 'Possible titles for people. i.e. Mr, Miss, Mrs etc.', 'Title');
EXEC zynap_lookup_sp.install_values( 'TITLE', 'MR', 'Mr', 'Mr', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'TITLE', 'MRS', 'Mrs', 'Mrs', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'TITLE', 'MISS', 'Miss', 'Miss', 30, FALSE);

-- CHOICE TYPE
EXEC zynap_lookup_sp.install_type( 'CHOICE', 'SYSTEM', 'yes/no values', 'Choice', FALSE);
EXEC zynap_lookup_sp.install_values( 'CHOICE', 'YES', 'Yes', 'Yes', 0, FALSE);
EXEC zynap_lookup_sp.install_values( 'CHOICE', 'NO', 'No', 'No', 1, FALSE);

-- Data types for dynamic attributes
EXEC zynap_lookup_sp.install_type( 'DATYPE', 'SYSTEM', 'Attribute Types', 'Attribute',TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'TEXT', 'Text', 'Single line text', 0, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'TEXTAREA', 'Text Block', 'Multiple line text block', 1, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'NUMBER', 'Number', 'Number', 2, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'DECIMAL', 'Decimal', 'Decimal Number', 3, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'CURRENCY', 'Currency', 'Currency', 4, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'DATE', 'Date', 'Date', 5, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'TIME', 'Time', 'Time', 6, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'DATETIME', 'Date ' || CHR(38) || ' Time', 'Date ' || CHR(38) || ' Time', 7, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'IMG', 'Image', 'Image', 8, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'STRUCT', 'Selection List', 'Drop-down selection list', 9, TRUE);
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'LINK', 'Link', 'Hyperlink', 10, TRUE);


insert into lookup_types(ID, DESCRIPTION, TYPE, IS_ACTIVE, IS_SYSTEM, LABEL) values ('CURRENCY','Currency Codes','SYSTEM','T','T','Currencies');
insert into lookup_values(id, TYPE_ID, VALUE_ID, SHORT_DESC, DESCRIPTION)
values (LOOKUPVALUE_SQ.nextval, 'CURRENCY', 'USD', 'USD', 'USD');

  -- Artefact types
EXEC zynap_lookup_sp.install_type( 'ARTEFACTTYPE', 'SYSTEM', 'Artefacts that can have attributes', 'Artefact Type', TRUE);
EXEC zynap_lookup_sp.install_values( 'ARTEFACTTYPE', 'S', 'Person', 'Person', 0, TRUE);
EXEC zynap_lookup_sp.install_values( 'ARTEFACTTYPE', 'P', 'Position', 'Position', 1, TRUE);

EXEC zynap_lookup_sp.install_type( 'CURRENCY', 'SYSTEM', 'Currency Codes', 'Currency Codes', TRUE);
EXEC zynap_lookup_sp.install_values( 'CURRENCY', 'USD', 'USD', 'USD', 0, TRUE);

--- position classification
EXEC zynap_lookup_sp.install_type( 'CLASSIFICATION', 'SYSTEM', 'Position classification for a position', 'Classification', FALSE);
--The customer should enter their own selection values for classification

--- Remuneration Lookup Type Packages
EXEC zynap_lookup_sp.install_type( 'REMUNERATION', 'SYSTEM', 'Remuneration Codes', 'Remuneration', FALSE);

--- Job Category Types
EXEC zynap_lookup_sp.install_type( 'JOBCATEGORY', 'SYSTEM', 'Position Category Codes', 'Position Category', FALSE);

--- Job Location Types
EXEC zynap_lookup_sp.install_type( 'LOCATION', 'SYSTEM', 'Position Location Codes', 'Position Location', FALSE);

--- Job Grade Types
EXEC zynap_lookup_sp.install_type( 'JOBGRADE', 'SYSTEM', 'Position Grade Codes', 'Position Grade', FALSE);

--- Position primary/secondary associations
EXEC zynap_lookup_sp.install_type( 'PRIMARY', 'SYSTEM', 'Primary position/position association types', 'Position Primary Association', FALSE);
EXEC zynap_lookup_sp.install_values( 'PRIMARY', 'DIRECT', 'Direct', 'Direct' , 10, TRUE);
EXEC zynap_lookup_sp.install_values( 'PRIMARY', 'FUNCTIONAL', 'Functional', 'Functional', 20, TRUE);

EXEC zynap_lookup_sp.install_type( 'SECONDARY', 'SYSTEM', 'Secondary position/position association types', 'Position Secondary Association', FALSE);
EXEC zynap_lookup_sp.install_values( 'SECONDARY', 'PROVISIONAL', 'Provisional', 'Provisional' , 10, TRUE);
EXEC zynap_lookup_sp.install_values( 'SECONDARY', 'TEMPORARY', 'Temporary', 'Temporary' , 20, TRUE);

  -- countries
EXEC zynap_lookup_sp.install_type( 'COUNTRY', 'SYSTEM', 'Country Codes', 'Country', FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AF', 'Afghanistan', 'Afghanistan',10, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AL', 'Albania', 'Albania',30, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'DZ', 'Algeria', 'Algeria',40, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AS', 'American Samoa', 'American Samoa',50, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AD', 'Andorra', 'Andorra',60, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AO', 'Angola', 'Angola',70, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AI', 'Anguilla', 'Anguilla',80, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AQ', 'Antarctica', 'Antarctic Australian Territory',90, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AG', 'Antigua and Barbuda', 'Antigua and Barbuda',100, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AR', 'Argentina', 'Argentina',110, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AM', 'Armenia', 'Armenia',120, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AW', 'Aruba', 'Aruba',130, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AU', 'Australia', 'Australia (incl Cocos Island)',140, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AT', 'Austria', 'Austria',150, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AZ', 'Azerbaijan', 'Azerbaijan',160, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PT1','Azores', 'Azores',165, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BS', 'Bahamas', 'Bahamas',170, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BH', 'Bahrain', 'Bahrain',180, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ES1','Balearic Islands', 'Balearic Islands',185, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BD', 'Bangladesh', 'Bangladesh',190, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BB', 'Barbados', 'Barbados',200, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BY', 'Belarus', 'Belarus (Belorussia)',210, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BE', 'Belgium', 'Belgium',220, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BZ', 'Belize', 'Belize',230, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BJ', 'Benin', 'Benin',240, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BM', 'Bermuda', 'Bermuda',250, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BT', 'Bhutan', 'Bhutan',260, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BO', 'Bolivia', 'Bolivia',270, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BA', 'Bosnia-Herzegovina', 'Bosnia and Herzegovina',280, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BW', 'Botswana', 'Botswana',290, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BR', 'Brazil', 'Brazil',310, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BN', 'Brunei', 'Brunei Darussalam',330, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BG', 'Bulgaria', 'Bulgaria',340, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BF', 'Burkina Faso', 'Burkina Faso',350, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MM1','Burma', 'Burma',355, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'BI', 'Burundi', 'Burundi',360, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KH', 'Cambodia', 'Cambodia',370, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CM', 'Cameroon', 'Cameroon',380, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CA', 'Canada', 'Canada',390, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ES2', 'Canary Islands', 'Canary Islands',395, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CV', 'Cape Verde', 'Cape Verde',400, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KY', 'Cayman Islands', 'Cayman Islands',410, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CF', 'Central African Republic', 'Central African Republic',420, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TD', 'Chad', 'Chad',430, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NZ1', 'Chatham Islands', 'Chatham Islands',435, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CL', 'Chile', 'Chile',440, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CN', 'China', 'China',450, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CX', 'Christmas Island', 'Christmas Island',460, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CO', 'Colombia', 'Colombia',480, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KM', 'Comoros', 'Comoros',490, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CG', 'Congo', 'Congo',500, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CD', 'Congo, Democratic Republic', 'Democratic Republic of the Congo',510, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CK', 'Cook Islands', 'Cook Islands',520, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CR', 'Costa Rica', 'Costa Rica',530, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'HR', 'Croatia', 'Croatia',550, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CU', 'Cuba', 'Cuba',560, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CY', 'Cyprus', 'Cyprus',570, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CZ', 'Czech Republic', 'Czech Republic',580, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'DK', 'Denmark', 'Denmark',590, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'DG', 'Diego Garcia', 'Diego Garcia',595, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'DJ', 'Djibouti', 'Djibouti',600, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'DM', 'Dominica', 'Dominica',610, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'DO', 'Dominican Republic', 'Dominican Republic',620, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'EC', 'Ecuador', 'Ecuador',630, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'EG', 'Egypt', 'Egypt',640, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SV', 'El Salvador', 'El Salvador',650, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GQ', 'Equatorial Guinea', 'Equatorial Guinea',660, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ER', 'Eritrea', 'Eritrea',670, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'EE', 'Estonia', 'Estonia',680, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ET', 'Ethiopia', 'Ethiopia',690, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'FK', 'Falkland Islands', 'Falkland Islands (Malvinas)',700, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'FO', 'Faroe Islands', 'Faroe Islands',710, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'FJ', 'Fiji', 'Fiji',720, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'FI', 'Finland', 'Finland',730, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'FR', 'France', 'France',740, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GF', 'French Guiana', 'French Guiana',750, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PF', 'French Polynesia', 'French Polynesia',760, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GA', 'Gabon', 'Gabon',780, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GM', 'Gambia', 'Gambia',790, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GE', 'Georgia', 'Georgia',800, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'DE', 'Germany', 'Germany',810, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GH', 'Ghana', 'Ghana',820, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GI', 'Gibraltar', 'Gibraltar',830, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GR', 'Greece', 'Greece',840, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GL', 'Greenland', 'Greenland',850, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GD', 'Grenada (incl Carriacou)', 'Grenada (incl Carriacou)',860, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GP', 'Guadeloupe', 'Guadeloupe',870, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GU', 'Guam', 'Guam',880, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GT', 'Guatemala', 'Guatemala',890, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GN', 'Guinea', 'Guinea',900, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GW', 'Guinea-Bissau', 'Guinea-Bissau',910, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GY', 'Guyana', 'Guyana',920, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'HT', 'Haiti', 'Haiti',930, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'US1','Hawaii', 'Hawaii',950, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'HN', 'Honduras', 'Honduras',960, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'HK', 'Hong Kong', 'Hong Kong',970, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'HU', 'Hungary', 'Hungary',980, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'IS', 'Iceland', 'Iceland',990, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'IN', 'India', 'India',1000, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ID', 'Indonesia', 'Indonesia',1010, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'IR', 'Iran', 'Islamic Republic of Iran',1020, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'IQ', 'Iraq', 'Iraq',1030, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'IE', 'Ireland', 'Republic of Ireland',1040, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'IL', 'Israel', 'Israel',1050, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'IT', 'Italy', 'Italy',1060, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CI', 'Ivory Coast', 'Ivory Coast (Cote d''Ivoire)',1065, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'JM', 'Jamaica', 'Jamaica',1070, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'JP', 'Japan', 'Japan',1080, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'JO', 'Jordan', 'Jordan',1090, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KZ', 'Kazakhstan', 'Kazakhstan',1100, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KE', 'Kenya', 'Kenya',1110, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KI', 'Kiribati', 'Kiribati',1120, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KP', 'Korea (North)', 'Democratic People''s republic of Korea',1130, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KR', 'Korea (South)', 'Republic of Korea',1140, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KW', 'Kuwait', 'Kuwait',1150, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KG', 'Kyrgyzstan', 'Kyrgyzstan (Kirghizia)',1160, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LA', 'Laos', 'Lao People''s Democratic Republic',1170, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LV', 'Latvia', 'Latvia',1180, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LB', 'Lebanon', 'Lebanon',1190, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LS', 'Lesotho', 'Lesotho',1200, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LR', 'Liberia', 'Liberia',1210, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LY', 'Libyan', 'Libyan Arab Jamahiriya',1220, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LI', 'Liechtenstein', 'Liechtenstein',1230, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LT', 'Lithuania', 'Lithuania',1240, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LU', 'Luxembourg', 'Luxembourg',1250, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MO', 'Macao', 'Macao',1260, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MK', 'Macedonia', 'Macedonia',1270, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MG', 'Madagascar', 'Madagascar',1280, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MW', 'Malawi', 'Malawi',1290, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MY', 'Malaysia', 'Malaysia',1300, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MV', 'Maldives', 'Maldives',1310, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ML', 'Mali', 'Mali',1320, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MT', 'Malta', 'Malta',1330, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MH', 'Marshall Islands', 'Marshall Islands',1340, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MQ', 'Martinique', 'Martinique',1350, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MR', 'Mauritania', 'Mauritania',1360, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MU', 'Mauritius', 'Mauritius (incl Rodriguez Islands)',1370, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'YT', 'Mayotte', 'Mayotte',1380, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MX', 'Mexico', 'Mexico',1390, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'FM', 'Micronesia', 'Federated States of Micronesia',1400, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MD', 'Moldova (Moldovia)', 'Republic of Moldova',1410, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MC', 'Monaco', 'Monaco',1420, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MN', 'Mongolia', 'Mongolia',1430, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MS', 'Montserrat', 'Montserrat',1440, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MA', 'Morocco', 'Morocco',1450, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MZ', 'Mozambique', 'Mozambique',1460, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MM', 'Myanmar', 'Myanmar',1470, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NA', 'Namibia', 'Namibia',1480, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NR', 'Nauru', 'Nauru',1490, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NP', 'Nepal', 'Nepal',1500, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NL', 'Netherlands', 'Netherlands',1510, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AN', 'Netherlands Antilles', 'Netherlands Antilles',1520, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NC', 'New Caledonia', 'New Caledonia',1530, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NZ', 'New Zealand', 'New Zealand (incl Chatham Island)',1540, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NI', 'Nicaragua', 'Nicaragua',1550, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NE', 'Niger', 'Niger',1560, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NG', 'Nigeria', 'Nigeria',1570, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NU', 'Niue', 'Niue',1580, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NF', 'Norfolk Island', 'Norfolk Island',1590, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MP', 'Northern Marianas', 'Northern Mariana Islands',1600, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'NO', 'Norway', 'Norway',1610, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'OM', 'Oman', 'Oman',1620, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PK', 'Pakistan', 'Pakistan',1630, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PW', 'Palau', 'Palau',1640, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PS', 'Palestine', 'Palestinian Authority',1650, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PA', 'Panama', 'Panama',1660, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PG', 'Papua New Guinea', 'Papua New Guinea',1670, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PY', 'Paraguay', 'Paraguay',1680, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PE', 'Peru', 'Peru',1690, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PH', 'Philippines', 'Philippines',1700, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PN', 'Pitcairn Islands', 'Pitcairn Islands',1710, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PL', 'Poland', 'Poland',1720, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PT', 'Portugal', 'Portugal (incl Azores and Madeira)',1730, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PR', 'Puerto Rico', 'Puerto Rico',1740, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'QA', 'Qatar', 'Qatar',1750, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'RE', 'Reunion', 'Reunion',1760, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'MU1','Rodriguez Island', 'Rodriguez Island',1765, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'RO', 'Romania', 'Romania',1770, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'RU', 'Russian Federation', 'Russian Federation',1780, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'RW', 'Rwanda', 'Rwanda',1790, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'WS', 'Samoa', 'Samoa',1850, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SM', 'San Marino', 'San Marino',1860, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SA', 'Saudi Arabia', 'Saudi Arabia',1880, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SN', 'Senegal', 'Senegal',1890, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CS', 'Serbia and Montenegro', 'Serbia and Montenegro',1900, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SC', 'Seychelles', 'Seychelles',1910, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SL', 'Sierra Leone', 'Sierra Leone',1920, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SG', 'Singapore', 'Singapore',1930, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SK', 'Slovakia', 'Slovakia',1940, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SI', 'Slovenia', 'Slovenia',1950, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ST', 'Sao Tome and Principe', 'Sao Tome and Principe',1955, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SB', 'Solomon Islands', 'Solomon Islands',1960, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SO', 'Somalia', 'Somalia',1970, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ZA', 'South Africa', 'South Africa',1980, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ES', 'Spain', 'Spain',2000, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LK', 'Sri Lanka', 'Sri Lanka',2010, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SH', 'St. Helena', 'Saint Helena',2012, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'KN', 'St. Kitts and Nevis', 'Saint Kitts and Nevis',2014, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'LC', 'St. Lucia', 'Saint Lucia',2016, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'PM', 'St. Pierre and Miquelon', 'Saint Pierre Miquelon',2017, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'VC', 'St. Vincent and Grenadines', 'Sainy Vincent and the Grenadines.',2018, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SD', 'Sudan', 'Sudan',2020, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SR', 'Suriname', 'Suriname',2030, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SZ', 'Swaziland', 'Swaziland',2050, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SE', 'Sweden', 'Sweden',2060, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'CH', 'Switzerland', 'Switzerland',2070, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'SY', 'Syriana', 'Syrian Arab Republic',2080, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TW', 'Taiwan', 'Taiwan',2090, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TJ', 'Tajikistan', 'Tajikistan',2100, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TZ', 'Tanzania', 'United Republic of Tanzania',2110, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TH', 'Thailand', 'Thailand',2120, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TG', 'Togo', 'Togo',2140, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TK', 'Tokelau', 'Tokelau',2150, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TO', 'Tonga', 'Tonga',2160, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TT', 'Trinidad and Tobago', 'Trinidad and Tobago',2170, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TA', 'Tristan da Cunha', 'Tristan da Cunha',2175, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TN', 'Tunisia', 'Tunisia',2180, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TR', 'Turkey', 'Turkey',2190, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TM', 'Turkmenistan', 'Turkmenistan',2200, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TC', 'Turks and Caicos Islands', 'Turks and Caicos',2210, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'TV', 'Tuvalu', 'Tuvalu',2220, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'UG', 'Uganda', 'Uganda',2230, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'UA', 'Ukraine', 'Ukraine',2240, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'AE', 'United Arab Emirates', 'United Arab Emirates',2250, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'GB', 'United Kingdom', 'United Kingdom',1, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'US', 'United States', 'United States of America',2270, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'UY', 'Uruguay', 'Uruguay',2290, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'UZ', 'Uzbekistan', 'Uzbekistan',2300, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'VU', 'Vanuatu', 'Vanuatu',2310, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'VA', 'Vatican City', 'Vatican City (Holy See)',2315, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'VE', 'Venezuela', 'Venezuela',2320, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'VN', 'Vietnam', 'Vietnam',2330, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'VG', 'Virgin Islands (UK)', 'Virgin Islands (UK)',2340, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'VI', 'Virgin Islands (US)', 'Virgin Islands (US)',2350, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'WK', 'Wake Island', 'Wake Island',2355, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'WF', 'Wallis and Futuna', 'Wallis and Futuna',2360, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'YE', 'Yemen', 'Yemen',2380, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ZR', 'Zaire', 'Zaire',2385, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ZM', 'Zambia', 'Zambia',2390, FALSE);
EXEC zynap_lookup_sp.install_values( 'COUNTRY', 'ZW', 'Zimbabwe', 'Zimbabwe',2400, FALSE);


--- language
EXEC zynap_lookup_sp.install_type( 'LANGUAGE', 'SYSTEM', 'Language Codes', 'Language', FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'EN', 'English', 'English', 2, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AA', 'Afar', 'Afar', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AB', 'Abkhazian', 'Abkhazian', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AF', 'Afrikaans', 'Afrikaans', 30, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SQ', 'Albanian', 'Albanian', 40, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AM', 'Amharic', 'Amharic', 50, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AR', 'Arabic', 'Arabic', 60, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'HY', 'Armenian', 'Armenian', 70, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AS', 'Assamese', 'Assamese', 80, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AY', 'Aymara', 'Aymara', 90, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'AZ', 'Azerbaijani', 'Azerbaijani', 100, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BA', 'Bashkir', 'Bashkir', 110, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'EU', 'Basque', 'Basque', 120, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BN', 'Bengali Bangla', 'Bengali Bangla', 130, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'DZ', 'Bhutani', 'Bhutani', 140, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BH', 'Bihari', 'Bihari', 150, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BI', 'Bislama', 'Bislama', 160, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BR', 'Breton', 'Breton', 170, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BG', 'Bulgarian', 'Bulgarian', 180, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MY', 'Burmese', 'Burmese', 190, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BE', 'Byelorussian', 'Byelorussian', 200, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KM', 'Cambodian', 'Cambodian', 210, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'CA', 'Catalan', 'Catalan', 220, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'ZH', 'Chinese', 'Chinese', 230, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'CO', 'Corsican', 'Corsican', 240, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'HR', 'Croatian', 'Croatian', 250, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'CS', 'Czech', 'Czech', 260, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'DA', 'Danish', 'Danish', 270, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'NL', 'Dutch', 'Dutch', 280, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'EO', 'Esperanto', 'Esperanto', 290, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'ET', 'Estonian', 'Estonian', 300, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'FO', 'Faeroese', 'Faeroese', 310, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'FJ', 'Fiji', 'Fiji', 320, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'FI', 'Finnish', 'Finnish', 330, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'FR', 'French', 'French', 340, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'FY', 'Frisian', 'Frisian', 350, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'GD', 'Gaelic', 'Gaelic Scots Gaelic', 360, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'GL', 'Galician', 'Galician', 370, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KA', 'Georgian', 'Georgian', 380, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'DE', 'German', 'German', 390, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'EL', 'Greek', 'Greek', 400, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KL', 'Greenlandic', 'Greenlandic', 410, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'GN', 'Guarani', 'Guarani', 420, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'GU', 'Gujarati', 'Gujarati', 430, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'HA', 'Hausa', 'Hausa', 440, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'IW', 'Hebrew', 'Hebrew', 450, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'HI', 'Hindi', 'Hindi', 460, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'HU', 'Hungarian', 'Hungarian', 470, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'IS', 'Icelandic', 'Icelandic', 480, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'IN', 'Indonesian', 'Indonesian', 490, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'IA', 'Interlingua', 'Interlingua', 500, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'IE', 'Interlingue', 'Interlingue', 510, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'IK', 'Inupiak', 'Inupiak', 520, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'GA', 'Irish', 'Irish', 530, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'IT', 'Italian', 'Italian', 540, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'JA', 'Japanese', 'Japanese', 550, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'JW', 'Javanese', 'Javanese', 560, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KN', 'Kannada', 'Kannada', 570, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KS', 'Kashmiri', 'Kashmiri', 580, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KK', 'Kazakh', 'Kazakh', 590, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'RW', 'Kinyarwanda', 'Kinyarwanda', 600, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KY', 'Kirghiz', 'Kirghiz', 610, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'RN', 'Kirundi', 'Kirundi', 620, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KO', 'Korean', 'Korean', 630, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'KU', 'Kurdish', 'Kurdish', 640, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'LO', 'Laothian', 'Laothian', 650, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'LA', 'Latin', 'Latin', 660, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'LV', 'Latvian Lettish', 'Latvian Lettish', 670, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'LN', 'Lingala', 'Lingala', 680, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'LT', 'Lithuanian', 'Lithuanian', 690, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MK', 'Macedonian', 'Macedonian', 700, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MG', 'Malagasy', 'Malagasy', 710, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MS', 'Malay', 'Malay', 720, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MT', 'Maltese', 'Maltese', 730, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'ML', 'Malayalam', 'Malayalam', 740, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MI', 'Maori', 'Maori', 750, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MR', 'Marathi', 'Marathi', 760, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MO', 'Moldavian', 'Moldavian', 770, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'MN', 'Mongolian', 'Mongolian', 780, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'NA', 'Nauru', 'Nauru', 790, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'NE', 'Nepali', 'Nepali', 800, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'NO', 'Norwegian', 'Norwegian', 810, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'OC', 'Occitan', 'Occitan', 820, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'OR', 'Oriya', 'Oriya', 830, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'OM', 'Oromo Afan', 'Oromo Afan', 840, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'PS', 'Pashto Pushto', 'Pashto Pushto', 850, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'FA', 'Persian', 'Persian', 860, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'PL', 'Polish', 'Polish', 870, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'PT', 'Portuguese', 'Portuguese', 880, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'PA', 'Punjabi', 'Punjabi', 890, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'QU', 'Quechua', 'Quechua', 900, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'RM', 'Rhaeto-Romance', 'Rhaeto-Romance', 910, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'RO', 'Romanian', 'Romanian', 920, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'RU', 'Russian', 'Russian', 930, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SM', 'Samoan', 'Samoan', 940, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SG', 'Sangro', 'Sangro', 950, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SA', 'Sanskrit', 'Sanskrit', 960, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SR', 'Serbian', 'Serbian', 970, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SH', 'Serbo-Croatian', 'Serbo-Croatian', 980, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'ST', 'Sesotho', 'Sesotho', 990, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TN', 'Setswana', 'Setswana', 1000, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SD', 'Sindhi', 'Sindhi', 1010, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SI', 'Singhalese', 'Singhalese', 1020, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SS', 'Siswati', 'Siswati', 1030, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SK', 'Slovak', 'Slovak', 1040, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SL', 'Slovenian', 'Slovenian', 1050, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SN', 'Shona', 'Shona', 1060, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SO', 'Somali', 'Somali', 1070, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'ES', 'Spanish', 'Spanish', 1080, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SU', 'Sudanese', 'Sudanese', 1090, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SW', 'Swahili', 'Swahili', 1100, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'SV', 'Swedish', 'Swedish', 1110, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TL', 'Tagalog', 'Tagalog', 1120, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TG', 'Tajik', 'Tajik', 1130, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TT', 'Tatar', 'Tatar', 1140, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TA', 'Tamil', 'Tamil', 1150, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TE', 'Tegulu', 'Tegulu', 1150, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TH', 'Thai', 'Thai', 1160, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'BO', 'Tibetan', 'Tibetan', 1170, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TI', 'Tigrinya', 'Tigrinya', 1180, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TO', 'Tonga', 'Tonga', 1190, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TS', 'Tsonga', 'Tsonga', 1200, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TR', 'Turkish', 'Turkish', 1210, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TK', 'Turkmen', 'Turkmen', 1220, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'TW', 'Twi', 'Twi', 1230, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'UK', 'Ukrainian', 'Ukrainian', 1240, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'UR', 'Urdu', 'Urdu', 1250, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'UZ', 'Uzbek', 'Uzbek', 1260, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'VI', 'Vietnamese', 'Vietnamese', 1270, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'VO', 'Volapuk', 'Volapuk', 1280, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'CY', 'Welsh', 'Welsh', 1290, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'WO', 'Wolof', 'Wolof', 1300, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'XH', 'Xhosa', 'Xhosa', 1310, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'JI', 'Yiddish', 'Yiddish', 1320, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'YO', 'Yoruba', 'Yoruba', 1330, FALSE);
EXEC zynap_lookup_sp.install_values( 'LANGUAGE', 'ZU', 'Zulu', 'Zulu', 1340, FALSE);

--Adding types for position/person association
EXEC zynap_lookup_sp.install_type( 'POSITIONSUBJECTASSOC', 'SYSTEM', 'Primary person/position association types', 'Person Primary Association', FALSE);

--Adding look up values for position/person association
EXEC zynap_lookup_sp.install_values( 'POSITIONSUBJECTASSOC', 'ACTING', 'Acting', 'Acting holder of the position', 10, TRUE);
EXEC zynap_lookup_sp.install_values( 'POSITIONSUBJECTASSOC', 'TEMPORARY', 'Temporary', 'Temporary holder of the position', 20, TRUE);
EXEC zynap_lookup_sp.install_values( 'POSITIONSUBJECTASSOC', 'SECONDEDTO', 'Seconded To', 'Seconded holder of the position', 30, TRUE);
EXEC zynap_lookup_sp.install_values( 'POSITIONSUBJECTASSOC', 'PERMANENT', 'Permanent', 'Permanent holder of the position', 40, TRUE);

-- types for person/position secondary associations
EXEC zynap_lookup_sp.install_type( 'SECONDARYSUBJECTPOSASSOC', 'SYSTEM', 'Secondary person/position association types', 'Person Secondary Association', FALSE);

--Adding look up values for secondary position/person association
EXEC zynap_lookup_sp.install_values( 'SECONDARYSUBJECTPOSASSOC', 'EMERGENCY', 'Emergency', 'Emergency successor for the position', 10, TRUE);
EXEC zynap_lookup_sp.install_values( 'SECONDARYSUBJECTPOSASSOC', 'READYNOW', 'Ready Now', 'Person is ready now for the position', 20, TRUE);
EXEC zynap_lookup_sp.install_values( 'SECONDARYSUBJECTPOSASSOC', 'SHORTTERM', 'Short Term', 'Person is in short term consideration for the position', 30, TRUE);
EXEC zynap_lookup_sp.install_values( 'SECONDARYSUBJECTPOSASSOC', 'MEDIUMTERM', 'Medium Term', 'Person is the medium term replacement for the position', 40, TRUE);

-- appraisal roles
EXEC zynap_lookup_sp.install_type( 'APPRAISAL_ROLES', 'SYSTEM', 'Appraisal Roles', 'Appraisal Roles');
EXEC zynap_lookup_sp.install_values( 'APPRAISAL_ROLES', 'SELFEVALUATOR', 'Self Evaluator', 'Self Evaluator', 10, TRUE);
EXEC zynap_lookup_sp.install_values( 'APPRAISAL_ROLES', 'INTERNALCUSTOMER', 'Internal Customer', 'Internal Customer', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'APPRAISAL_ROLES', 'PEER', 'Peer', 'Peer', 30, FALSE); 

-- objective goal
EXEC zynap_lookup_sp.install_type( 'OBJ_TYPE', 'SYSTEM', 'Objective goal types', 'Goal Type');
EXEC zynap_lookup_sp.install_values( 'OBJ_TYPE', 'PERFORMANCE', 'Performance', 'Performance', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_TYPE', 'LEARNING', 'Learning', 'Learning Goal Type', 10, FALSE);

-- priority
EXEC zynap_lookup_sp.install_type( 'OBJ_IMPORTANCE', 'SYSTEM', 'Objective priority ratings', 'Priority');
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'A', 'A', 'A', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'B', 'B', 'B', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'C', 'C', 'C', 30, FALSE);

-- Leadership Style
EXEC zynap_lookup_sp.install_type( 'OBJ_LEADERSHIP', 'SYSTEM', 'Objective Leadership Style', 'Leadership Style');
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'DIRECTING', 'Directing', 'The Leadership provides specific direction and closely monitors task accomplishment', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'COACHING', 'Coaching', 'The leader continues to direct and closely monitor task accomplishment, but also explains decisions, solicits suggestions, and supports progress.', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'SUPPORTING', 'Supporting', 'The leader facilitates and supports people''s efforts toward task accomplishment and shares responsibility for decision-making with them.', 30, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'DELEGATING', 'Delegating', 'The leader turns over responsibility for decision-making and problem-solving to people.', 40, FALSE);

-- goal rating
EXEC zynap_lookup_sp.install_type( 'OBJ_RATING', 'SYSTEM', 'Goal Rating', 'Rating');
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'NONE', 'None', 'ONone', 1, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'OUTSTANDING', 'Outstanding', 'Outstanding Performance', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'EXCEEDED', 'Exceeded', 'Exceeded Goal Requirements', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'MET_EXPECTATIONS', 'Met Expectations', 'Met Expectations', 30, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'FELL_BELOW', 'Fell Below', 'Fell Below Expectations', 40, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'UNACCEPTABLE', 'Unacceptable', 'Unacceptable', 50, FALSE);

-- succession planning reasons
EXEC zynap_lookup_sp.install_type('SUCCESSION_REASON', 'SYSTEM', 'Reasons for Succession', 'Vacancy Reasons');
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'CAREERMOVE', 'Career Move', 'Career Move', 10, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'PROMOTION', 'Promotion', 'Promotion', 20, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'RETIREMENT', 'Retirement', 'Retirement', 30, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'ASSIGNMENTEND', 'End of Assignment', 'End of Assignment', 40, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'OTHERSR', 'Other', 'Other', 50, FALSE);

-- any other values needing to be added should be added here not in the middle
EXEC zynap_lookup_sp.install_values( 'DATYPE', 'MULTISELECT', 'Multi Selection List', 'Multiple choice drop-down selection list', 10, TRUE);
