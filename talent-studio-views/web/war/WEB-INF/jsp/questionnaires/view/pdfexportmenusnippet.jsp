<%@ include file="../../includes/include.jsp" %>
<c:choose>
    <c:when test="${myPortfolio == true}">
        <zynap:actionEntry>
            <form action="exportmyexecsubjectquestionnairepdf.htm" method="post" name="downloadmyexecsummarypdf" target="_blank">
                <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.LEAVE_COMMAND%>"/>
                <input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.subjectId}"/>"/>
                <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.questionnaireId}"/>"/>
                <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="<c:out value="${command.workflowId}"/>"/>
                <input class="actionbutton" name="getpdf" type="button" value="<fmt:message key="export.pdf"/>"
                       onclick="document.forms.downloadmyexecsummarypdf.submit();"/>

            </form>
        </zynap:actionEntry>
    </c:when>
    <c:otherwise>
        <zynap:actionEntry>
            <form action="exportexecsubjectquestionnairepdf.htm" method="post" name="downloadexecsummarypdf" target="_blank">
                <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.LEAVE_COMMAND%>"/>
                <input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.subjectId}"/>"/>
                <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.questionnaireId}"/>"/>
                <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="<c:out value="${command.workflowId}"/>"/>
                <input class="actionbutton" name="getpdf" type="button" value="<fmt:message key="export.pdf"/>"
                       onclick="document.forms.downloadexecsummarypdf.submit();"/>

            </form>
        </zynap:actionEntry>
    </c:otherwise>
</c:choose>