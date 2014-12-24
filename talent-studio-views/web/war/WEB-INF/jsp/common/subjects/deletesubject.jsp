<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}">
    <!-- delete confirm message-->
    <div class="infomessage"><fmt:message key="confirm.delete.subject"/></div>

    <table class="infotable" cellspacing="0">
        <!-- Title-->
        <tr>
            <td class="infoheading" colspan="2"><fmt:message key="person.to.delete"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="admin.add.user.title"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.title}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="admin.add.user.firstname"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.firstName}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="admin.add.user.lastname"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.secondName}"/></td>
        </tr>
        <tr>
            <td class="infobutton"/>
            <td class="infobutton">
                <zynap:form method="post" name="_delete">
                    <input type="submit" class="inlinebutton" name="_cancel" value="<fmt:message key="cancel"/>"/>
                    <input type="button" class="inlinebutton" name="_delete" value="<fmt:message key="confirm"/>" onclick="document.forms._delete.submit();"/>
                    <spring:bind path="command">
                        <%@include file="../../includes/error_message.jsp"%>
                    </spring:bind>
                </zynap:form>            
            </td>
        </tr>
    </table>
</zynap:infobox>

