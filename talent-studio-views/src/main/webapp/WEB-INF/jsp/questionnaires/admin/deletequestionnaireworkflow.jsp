<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ include file="../../includes/include.jsp"%>

<zynap:infobox title="${title}" id="deleteWF">
    <spring:bind path="command">
        <c:if test="${empty status.errorMessages}">
            <div class="infomessage">
                <fmt:message key="questionnaire.delete.confirm"/> <c:out value="${command.label}"/>.
                <c:if test="${command.status == 'PUBLISHED'}">
                    <br/><fmt:message key="questionnaire.published.warning"/>
                </c:if>
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
                <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
            </zynap:form>
        </c:if>
    </spring:bind>
</zynap:infobox>
