<%@ include file="../includes/include.jsp" %>

<%-- set item url and portfolio items - used in viewportfolioitems.jsp --%>
<c:set var="itemUrl" value="viewmyportfolioitem.htm" scope="request"/>
<c:set var="progressReports" value="${artefact.progressReports}" scope="request"/>

<c:url var="questionnaireUrl" value="viewmyquestionnaire.htm" scope="request"/>
<zynap:artefactLink var="viewQuestionnaireUrl" url="${questionnaireUrl}" tabName="activeTab" activeTab="${progressActiveTab}"/>

<%@include file="../common/subjects/viewprogressreports.jsp"%>