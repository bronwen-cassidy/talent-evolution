<%@ include file="../includes/include.jsp"%>

<fmt:message key="delete.population" var="msg"/>
<zynap:infobox title="${msg}">

    <div class="infomessage">
        <fmt:message key="confirm.delete.message"/>&nbsp;<c:out value="${command.label}"/>?
    </div>

    <spring:bind path="command">
        <%@ include file="../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form method="post" name="_cancel">
        <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
    </zynap:form>

    <%@ include file="../includes/check_for_errors.jsp" %>

    <zynap:form method="post" name="confirm">
        <c:if test="${hasErrors == false}">
            <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="confirm" onclick="javascript:document.forms.confirm.submit();"/>
        </c:if>
    </zynap:form>

</zynap:infobox>
