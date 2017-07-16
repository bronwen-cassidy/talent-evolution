talent-evolution
================

Build with: 
=====================================================================================================
mvn clean install -Plive -Dclient=grupobimbo -Dmulti.tenant=false
=====================================================================================================
where -P is in live (goes to the live server) dev (dev environment) home (my home env) TME (a nice shortcut for the live TME version)
-Dclient is the name of the client we are building for (must have a corresponding client directory under talent-studio-views module)
-Dmulti.tenant can be either true or false 

new in version version 5.4.8
-------------------------------
builds 100% with maven
new HR implementation into security domains
ability for managers to submit performance review to their managers or to their HR

bug fixes
--------------------------------
questionnaire form time out fixed

