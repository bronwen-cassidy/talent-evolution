<%@ page import="com.zynap.talentstudio.security.roles.Role"%>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form method="post" name="_selecttype">
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel">
                    <fmt:message key="role.type"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <spring:bind path="command.type">
                    <select name="<c:out value="${status.expression}"/>">
                        <option name="resource" value="<%=Role.RESOURCE_TYPE%>" <c:if test="${command.type=='RESOURCE'}">selected</c:if>><fmt:message key="role.resource"/></option>
                        <option name="access" value="<%=Role.ACCESS_TYPE%>" <c:if test="${command.type=='ACCESS'}">selected</c:if>><fmt:message key="role.access"/></option>
                    </select>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
			        <input type="button" value="<fmt:message key="cancel"/>" name="_cancel" class="inlinebutton" onclick="javascript:document.forms._cancel.submit();"/>
                    <input type="submit" value="<fmt:message key="wizard.next"/>" name="_target1" class="inlinebutton"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="get" action="/admin/listrole.htm" name="_cancel"></zynap:form>