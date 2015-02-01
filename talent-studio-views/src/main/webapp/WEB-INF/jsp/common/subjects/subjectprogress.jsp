<%@ include file="../../includes/include.jsp"%>

<c:set var="progressReports" value="${artefact.progressReports}" scope="request"/>
<c:url var="questionnaireUrl" value="browseviewsubjectquestionnaire.htm" scope="request"/>
<zynap:artefactLink var="viewQuestionnaireUrl" url="${questionnaireUrl}" tabName="activeTab" activeTab="${progressActiveTab}"/>

<%@include file="viewprogressreports.jsp"%>