<%@ include file="../includes/include.jsp" %>

<%-- set item url and portfolio items - used in viewportfolioitems.jsp --%>
<c:set var="itemUrl" value="viewmyportfolioitem.htm" scope="request"/>
<c:set var="portfolioItems" value="${artefact.portfolioItems}" scope="request"/>
<c:set var="additemaction" value="addmyportfolioitem.htm" scope="request"/>
<c:set var="searchitemsaction" value="myportfoliopositionsearch.htm" scope="request"/>

<c:import url="../common/portfolio/items/viewportfolioitems.jsp"/>

<c:set var="editUrl" value="editmybrowsequestionnaire.htm" scope="request"/>
<c:url var="questionnaireUrl" value="viewmyquestionnaire.htm" scope="request"/>
<c:set var="appraisals" value="${artefact.appraisals}" scope="request"/>
<c:set var="infoforms" value="${artefact.groupedInfoForms}" scope="request"/>
<%-- TS-2253: 'myPortfolio' variable is used to identify whether the user is viewing their own portfolio or somebody elses and to check write permissions accordingling.  --%>
<c:set var="myPortfolio" value="true" scope="request"/>

<c:import url="../common/subjects/viewquestionnaires.jsp"/>