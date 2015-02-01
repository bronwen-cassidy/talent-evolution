<%@ include file="../../includes/include.jsp"%>

<zynap:infobox title="${title}" >
    <div class="infomessage"><fmt:message key="confirm.message"/></div>

    <c:if test="${usedByNode}">
        <div class="infomessage"><fmt:message key="delete.attribute.permanent"/></div>
    </c:if>
    <spring:bind path="command">
        <%@ include file="../../includes/error_messages.jsp" %>
    </spring:bind>

    <table class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="da.general.label"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.description}"/></td>
        </tr>
        <tr>
            <td class="infobutton"></td>
            <td class="infobutton">
                <input class="inlinebutton" type="button" value="<fmt:message key="cancel"/>" name="_cancel" onclick="document.forms.cncl.submit();"/>
                <zynap:form method="post" name="cnfrm">
                    <input type="hidden" name="<%=ParameterConstants.ATTR_ID%>" value="<c:out value="${command.id}"/>"/>
                    <input type="hidden" name="<%=ParameterConstants.ARTEFACT_TYPE%>" value="<c:out value="${command.artefactType}"/>"/>
                    <input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
                    <input class="inlinebutton" type="submit" value="<fmt:message key="confirm"/>" name="cnfrm"/>
                </zynap:form>
            </td>
        </tr>
    </table>
</zynap:infobox>

<c:url value="viewDA.htm" var="viewUrl"/>
<zynap:form action="${viewUrl}" name="cncl" method="get">
    <input type="hidden" name="<%=ParameterConstants.ATTR_ID%>" value="<c:out value="${command.id}"/>"/>
</zynap:form>
