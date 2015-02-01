<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}">
    <zynap:form name="addinfo" method="post">
        <table class="infotable" cellspacing="0">
            <%@ include file="userform.jsp"%>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
