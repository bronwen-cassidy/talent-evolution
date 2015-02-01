<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ include file="../../includes/include.jsp"%>

<zynap:infobox title="${title}" id="closeWF">
    <spring:bind path="command">
        <c:if test="${empty status.errorMessages}">
            <div class="infomessage">
                <fmt:message key="questionnaire.close.confirm"/> <c:out value="${command.label}"/>.
            </div>
        </c:if>
        <%@ include file="../../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form method="post" name="_cancel">
        <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="true"/>
        <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
    </zynap:form>

    <spring:bind path="command">
        <c:if test="${empty status.errorMessages}">
            <zynap:form method="post" action="" name="_confirm">
                <input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
                <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
            </zynap:form>
        </c:if>
    </spring:bind>
</zynap:infobox>