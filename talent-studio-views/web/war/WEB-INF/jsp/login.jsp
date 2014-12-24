<%@ include file="includes/include.jsp" %>

<zynap:form method="post" name="_login">

    <div class="policyheader"><fmt:message key="policy.header"/></div>
    <br/>

    <div class="policystatement">
        <c:import url="/help/policy.html"/>
    </div>
    <br/>

    <fmt:message key="login.accept.message" var="loginMsg" scope="request"/>
    <zynap:infobox title="${loginMsg}" id="nonav">

        <spring:bind path="command">
            <%@ include file="includes/error_messages.jsp" %>
        </spring:bind>
        <c:set var="locked" value="${command.locked}"/>

        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="username"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.username">
                    <td class="infodata">
                        <input id="usrFldId" type="text" maxlength="200" class="input_text" tabindex="1" name="<c:out value="${status.expression}"/>"
                               value="<c:out value="${status.value}"/>" <c:if test="${locked}">disabled</c:if>/>

                        <div class="policy">
                            <fmt:message key="username.info"/>
                        </div>
                        <%@ include file="includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="password"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.password">
                    <td class="infodata">
                        <input id="pswdFldId" type="password" maxlength="50" class="input_password" name="<c:out value="${status.expression}"/>"
                               value="<c:out value="${status.value}"/>" <c:if test="${locked}">disabled</c:if>/>
                        <%@ include file="includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input id="loginsubmit" class="inlinebutton" type="submit" value="<fmt:message key="login"/>"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="actionbox" align="center">
                    <input class="actionbutton" name="resetPass" type="button" value="<fmt:message key="forgot.password"/>"
                           onclick="document.forms.forgotPasswd.submit();"/>
                </td>
            </tr>
        </table>
    </zynap:infobox>
</zynap:form>
<zynap:actionEntry>
    <form action="forgotpassword.htm" method="get" name="forgotPasswd"/>
</zynap:actionEntry>