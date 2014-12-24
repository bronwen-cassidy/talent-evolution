<%@ include file="../../../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <zynap:form method="post" name="rules">
        <input type="hidden" name="<%=ParameterConstants.CONFIG_ID_PARAM%>" value="<c:out value="${command.targetConfig.id}"/>"/>

        <table class="infotable" cellspacing="0">
            <c:forEach var="rule" items="${command.targetConfig.rules}" varStatus="count" >
                <tr>
                    <td class="infolabel"><c:out value="${rule.description}"/>&nbsp;:&nbsp;</td>
                    <spring:bind path="command.targetConfig.rules[${count.index}].value">
                        <td class="infodata">
                            <c:choose>
                                <c:when test="${rule.value == 'T' || rule.value == 'F'}">
                                    <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${rule.value == 'T'}"> checked</c:if> />                                    
                                </c:when>
                                <c:otherwise>
                                    <input type="text" maxlength="200" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${rule.value}"/>"/>
                                </c:otherwise>
                            </c:choose>
                            <br/><%@ include file="../../../includes/error_message.jsp" %>
                        </td>
                    </spring:bind>
                </tr>
            </c:forEach>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" name="_cancel" type="submit" value="<fmt:message key="cancel"/>"/>
                    <input class="inlinebutton" name="_save" type="submit" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>
