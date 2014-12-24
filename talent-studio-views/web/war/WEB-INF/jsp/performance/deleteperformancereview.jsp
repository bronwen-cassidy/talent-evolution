<%@ page import="com.zynap.talentstudio.web.perfomance.PerformanceReviewMultiController"%>
<%@ include file="../includes/include.jsp"%>

<zynap:infobox title="${title}" >

    <spring:bind path="command">
        <c:if test="${empty status.errorMessages}">
            <div class="infomessage">
                <fmt:message key="appraisal.delete.confirm"/>&nbsp;<c:out value="${command.label}"/>.
            </div>
        </c:if>
        <%@ include file="../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form method="post" name="_cancel">
        <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
    </zynap:form>

    <%@ include file="../includes/check_for_errors.jsp" %>

    <zynap:form method="post" action="" name="_confirm">
        <c:if test="${hasErrors == false}">
            <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="confirm" onclick="javascript:document.forms._confirm.submit();"/>
        </c:if>
    </zynap:form>
</zynap:infobox>