<%@ include file="../../includes/include.jsp" %>

<c:choose>
    <c:when test="${command.errorKey == null}">
        <%@include file="../questionnairepopupinclude.jsp"%>

        <c:set var="questionnaireLabel" value="${command.questionnaireLabel}" scope="request"/>
        <c:set var="questionnaireGroups" value="${command.questionnaireGroups}" scope="request"/>
        <c:set var="infoForm" value="true" scope="request"/>
        <c:set var="managerAccessView" value="${command.managerView}" scope="request"/>
        <c:import url="../questionnaires/view/questionnairenav.jsp"/>
        <c:import url="../questionnaires/answer/questionnaireform.jsp"/>
    </c:when>
    <c:otherwise>
        <zynap:infobox title="${qTabLabel}" id="questionnaire">
            <c:import url="../questionnaires/view/questionnairenav.jsp"/>
            <div class="infomessage" id="no_results"><fmt:message key="${command.errorKey}"/></div>
        </zynap:infobox>
    </c:otherwise>
</c:choose>