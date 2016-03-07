<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_add" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table class="infotable" cellspacing="0">
			<tr>
                <td class="infolabel"><fmt:message key="securitydomain.assign.area"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <c:set var="currentarea" value="${command.areaId}"/>
                    <spring:bind path="command.areaId">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${currentarea == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <c:forEach var="area" items="${command.areas}">
                                <option value="<c:out value="${area.id}"/>" <c:if test="${area.id == currentarea}">selected</c:if>><c:out value="${area.label}" /></option>
                            </c:forEach>
                        </select>
                        <%@include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="user.assign.hr"/>&nbsp;:&nbsp;</td>
                <td>
                    <table class="infotable" cellspacing="0" cellpadding="0">
                        <c:forEach var="user" items="${command.selectedUsers}" varStatus="indexer">
                            <tr>
                                <td class="infolabel">
                                    ${user.label}
                                </td>
                                <td class="infodata">
                                    <form:checkbox path="command.selectedUsers[${indexer.index}].hr"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('_add', 'pgTarget', '2', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
