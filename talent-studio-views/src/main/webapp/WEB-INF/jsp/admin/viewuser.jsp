<%@ include file="../includes/include.jsp" %>

<c:set var="user" value="${model.artefact}"/>
<c:set var="userId" value="${model.artefact.id}"/>
<c:set var="userHeading" value="${tabContent.label}"/>
<fmt:message key="user.details" var="userHeading"/>
<c:url var="cancelUrl" value="/admin/viewuser.htm"/>
<c:set var="showCoreDetails" value="true"/>
<%@ include file="../common/users/userbuttons.jsp" %>
<c:set var="securityDomains" value="${model.userSecurityDomains}"/> 
<%@ include file="../common/users/userdetails.jsp"%>
