<%@ include file="includes/include.jsp" %>

<zynap:form method="post" action="forgotpassword.htm">

    <zynap:infobox title="${title}" id="nonav">

        <spring:bind path="command">
            <%@ include file="includes/error_messages.jsp" %>
        </spring:bind>

        <table class="infotable" cellspacing="0">
             <tr>
                 <td></td>
                 <td class="infolabel">
                   <fmt:message key="forgot.password.info"/>

                </td>

            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="username"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.username">
                    <td class="infodata">
                            <input type="text" maxlength="200" class="input_text" tabindex="1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
                        <%@ include file="includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <tr>
                 <td class="actionbox">
                    <input id="loginsubmit" class="inlinebutton" type="submit" value="<fmt:message key="forgot.password.reset"/>"/>
                </td>
                <td class="actionbox">
                    <input class="inlinebutton" name="gohome" type="button" value="<fmt:message key="goto.login"/>"
                           onclick="document.forms.gohomeForm.submit();"/>
                </td>

            </tr>
        </table>
    </zynap:infobox>
</zynap:form>
<zynap:actionEntry>
    <form action="home.htm" method="get" name="gohomeForm">
    </form>
</zynap:actionEntry>