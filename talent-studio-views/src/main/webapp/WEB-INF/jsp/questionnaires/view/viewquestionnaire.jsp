<%@ include file="../../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.web.workflow.WorkflowConstants" %>

<%-- this uses DWR to load the username. Since we don't want to delay loading the page we defer the loading of the username --%>
<script type="text/javascript">
    loadUserName('<c:out value="${command.subjectId}"/>', '<zynap:message code="no.user.details" javaScriptEscape="true"/>');
</script>


<fmt:message key="questionnaire.label" var="title">
    <fmt:param><c:out value="${command.questionnaireLabel}"/></fmt:param>
</fmt:message>

<c:if test="${command.performanceReview}">
    <c:set var="roleLabel" value="${command.questionnaire.role.label}"/>
    <c:if test="${command.questionnaire.manager}"><fmt:message var="roleLabel" key="worklist.appraisal.role.manager"/></c:if>
    <c:if test="${roleLabel == null}"><fmt:message var="roleLabel" key="appraisee"/></c:if>

    <fmt:message key="questionnaire.detailed.label" var="title">
        <fmt:param><c:out value="${command.questionnaireLabel}"/></fmt:param>
        <fmt:param><c:out value="${command.questionnaire.user.label}"/></fmt:param>
        <fmt:param><c:out value="${roleLabel}"/></fmt:param>
    </fmt:message>
</c:if>

<zynap:actionbox>

    <zynap:evalBack>
        <zynap:actionEntry>
            <fmt:message key="back" var="buttonLabel"/>
            <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
        </zynap:actionEntry>
    </zynap:evalBack>
  
    <c:if test="${!command.notificationBased && !command.completed && command.writePermission}">
            <zynap:actionEntry>
                <zynap:form action="${editView}" method="get" name="editquestionnaire">
                    <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.LEAVE_COMMAND%>"/>
                    <input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.subjectId}"/>"/>
                    <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.questionnaireId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="<c:out value="${command.workflowId}"/>"/>
                    <input type="hidden" name="myPortfolio" value="<c:out value="${command.myPortfolio}"/>"/>
                    <input class="actionbutton" id="editquestionnaireButton" name="edit" type="button" value="<fmt:message key="edit"/>"
                           onclick="document.forms.editquestionnaire.submit();"/>
                </zynap:form>
            </zynap:actionEntry>


    </c:if>

    <%@ include file="pdfexportmenusnippet.jsp" %>

</zynap:actionbox>


<%@ include file="viewquestionnairesnippet.jsp" %>

<zynap:window elementId="helpText"/>
