<%@ include file="../includes/include.jsp" %>

<zynap:infobox title="${title}">
    <zynap:form name="_edit" method="post">
        <table cellspacing="0" class="infotable">

            <%-- username is read-only --%>        
            <tr>
                <td class="infolabel"><fmt:message key="admin.add.user.username"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><c:out value="${command.loginInfo.username}"/></td>
            </tr>

            <%-- core details --%>
            <%@ include file="../common/users/coredetailsform.jsp"%>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_edit" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel" action="viewmyaccount.htm"/>
