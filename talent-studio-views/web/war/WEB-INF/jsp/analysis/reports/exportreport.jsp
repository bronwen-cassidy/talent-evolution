<%@ page import="ReportConstants"%>
<%@ include file="../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">
    <zynap:form method="post" name="export">
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="export.delimiter"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.delimiter">
                    <td class="infodata">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="<%= ReportConstants.COMMA %>" <c:if test="${status.value == null || status.value == ','}">selected</c:if>><fmt:message key="comma"/></option>
                            <option value="<%= ReportConstants.COLON %>" <c:if test="${status.value == ':'}">selected</c:if>><fmt:message key="colon"/></option>
                            <option value="<%= ReportConstants.SEMI_COLON %>" <c:if test="${status.value == ';'}">selected</c:if>><fmt:message key="semi-colon"/></option>
                            <option value="<%= ReportConstants.TAB %>" <c:if test="${status.value == 'tab'}">selected</c:if>><fmt:message key="tab"/></option>
                        </select>
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" name="export" type="submit" value="<fmt:message key="export"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>
