<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="progress.report.wizard.0" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="selectappraisal" method="post">
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="select.definition"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">

                    <spring:bind path="command.questionnaireDefinitionId">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <c:forEach var="definition" items="${questionnaireDefinitions}">
                                <option value="<c:out value="${definition.id}"/>" <c:if test="${definition.id == command.questionnaireDefinitionId}">selected</c:if>><c:out value="${definition.label}"/></option>
                            </c:forEach>
                        </select>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms.cancelled.submit();"/>
                    <input class="inlinebutton" name="_target1" type="submit" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="cancelled">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>