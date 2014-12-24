<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form method="post" name="add">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table cellspacing="0" class="infotable">
            <!-- Role Name ID -->
            <tr>
                <td class="infolabel"><fmt:message key="role.name"/>&nbsp;:&nbsp;*</td>
                    <spring:bind path="command.label">
                        <td class="infodata" tabindex="0">
                            <input type="text" maxlength="200" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                            <%@ include file="../../../includes/error_message.jsp" %>
                        </td>
                    </spring:bind>

            </tr>
            <!-- Active -->
            <tr>
                <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.active">
                    <td class="infodata" tabindex="1">
                        <input type="checkbox" class="input_checkbox" name="active" <c:if test="${command.active}">checked</c:if>/>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <!-- Description -->
            <tr>
                <td class="infolabel"><fmt:message key="role.description"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.description">
                    <td class="infodata" tabindex="2">
                        <textarea name="description" rows="4"><c:out value="${status.value}"/></textarea>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <c:if test="${adding}"><input class="inlinebutton" type="button" name="back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('add', 'pgTarget', '<c:out value="${previousPage}"/>', 'backId');"/></c:if>
                    <input class="inlinebutton" type="submit" name="_target<c:out value="${nextPage}"/>" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
