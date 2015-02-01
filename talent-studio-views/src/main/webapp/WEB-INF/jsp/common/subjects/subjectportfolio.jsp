<%@ include file="../../includes/include.jsp"%>

<%-- set item url and portfolio items - used in viewportfolioitems.jsp --%>
<c:set var="itemUrl" value="viewsubjectportfolioitem.htm" scope="request"/>
<c:set var="portfolioItems" value="${artefact.portfolioItems}" scope="request"/>
<c:set var="additemaction" value="editsubjectaddportfolioitem.htm" scope="request"/>
<c:set var="searchitemsaction" value="positiondocumentsearch.htm" scope="request"/>

<c:import url="../portfolio/items/viewportfolioitems.jsp"/>

<br/>

<c:url var="questionnaireUrl" value="browseviewsubjectquestionnaire.htm" scope="request"/>
<c:set var="editUrl" value="browseeditsubjectquestionnaire.htm" scope="request"/>
<c:set var="appraisals" value="${artefact.appraisals}" scope="request"/>
<c:set var="infoforms" value="${artefact.groupedInfoForms}" scope="request"/>
<%-- TS-2253 'myPortfolio' variable is used to identify whether the user is viewing their own portfolio or somebody elses and to check permissions accordingling --%>
<c:set var="myPortfolio" value="false" scope="request"/>
<c:import url="viewquestionnaires.jsp"/>