<%@ include file="../includes/include.jsp" %>

<zynap:link var="baseViewUrl" url="worklistappraisals.htm"/>
<c:set var="activeTab" value="objectives"/>
<c:set var="objectives" value="${command.objectives}" scope="request"/>
<c:set var="_formSubmission" value="true"/>
<c:set var="_pageNum" value="9"/>
<c:set var="viewPageNum" value="8"/>
<c:set var="callJavascript" value="submitViewObjective"/>
<zynap:actionbox/>

<fmt:message key="objectives" var="boxtitle" scope="request"/>
<zynap:link var="baseViewUrl" url="worklistappraisals.htm"/>
<%@include file="../common/objectives/viewobjectives.jsp" %>
