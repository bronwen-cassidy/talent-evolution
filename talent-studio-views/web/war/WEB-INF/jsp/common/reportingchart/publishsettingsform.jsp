<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="reports" method="post">

        <table id="chartDetails" class="infotable">

            <%-- Enter name --%>
            <tr>
                <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.viewName">
                    <td class="infodata">
                        <input type="text" maxlength="100" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.description">
                    <td class="infodata">
                        <textarea name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textarea>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="apply.security"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.secure">
                    <td class="infodata">
                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.secure}">checked</c:if>/>
                    </td>
                </spring:bind>
            </tr>

            <%-- Include page to publish to arenas --%>
            <%@ include file="../../analysis/reports/common/publishform.jsp"%>

            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="submit" name="_cancel" value="<fmt:message key="cancel"/>"/>
                    <input class="inlinebutton" name="_target1" type="submit" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>

    </zynap:form>
</zynap:infobox>
