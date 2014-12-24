<%@ include file="../../includes/include.jsp"%>

<zynap:infobox title="${title}" >
    <zynap:form method="post" action="/admin/viewlookupvalue.htm" name="_cancel">
        <input type="hidden" name="<%=ParameterConstants.LOOKUP_ID%>" value="<c:out value="${model.id}"/>"/>
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infodata">
                    <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
                </td>
                <td class="infodata">
                    <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" action="${model.url}" name="_confirm">
    <input type="hidden" name="<%=ParameterConstants.LOOKUP_ID%>" value="<c:out value="${model.id}"/>"/>
    <input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
</zynap:form>

