<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form method="post" name="permissions">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infoheading">
                    <fmt:message key="assign.permissions"/> <c:out value="${command.label}"/>
                </td>
            </tr>
            <tr>
                <td class="infodata">
                    <input class="inlinebutton" type="button" name="CheckAll" value="<fmt:message key="setall"/>" onClick="checkAll(document.permissions.selectedPermitIds)"/>
                    <input class="inlinebutton" type="button" name="UnCheckAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.permissions.selectedPermitIds)"/>
                    <spring:bind path="command.selectedPermitIds">
                        <zynap:permission label="Component Name" permissions="${command.allPermits}" bindName="selectedPermitIds" editable="true"/>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" value="<fmt:message key="cancel"/>" name="_cancel" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" value="<fmt:message key="wizard.back"/>" name="_back" onclick="javascript:handleWizardBack('permissions', 'pgTarget', '<c:out value="${previousPage}"/>', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>