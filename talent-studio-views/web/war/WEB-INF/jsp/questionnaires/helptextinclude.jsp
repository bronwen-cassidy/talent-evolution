<%@ include file="../includes/include.jsp" %>

<zynap:message code="display.help.text" var="popupTitle" javaScriptEscape="true"/>
<zynap:message code="question.no.helptext.provided" var="nohelptext" javaScriptEscape="true"/>
<img class="clickable" src="<c:url value="/images/helpme.gif"/>"  alt="<c:out value="${popupTitle}"/>" onclick="showHelpText('<c:out value="${question.id}"/>', '<c:out value="${nohelptext}"/>', <c:out value="${editable}"/>); showHelpTextPopup('<c:out value="${popupTitle}"/>', this);"/>
