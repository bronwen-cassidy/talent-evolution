<%@ include file="../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_edit" action="editmyaccount.htm">
            <input class="actionbutton" type="submit" name="_edit" value="<fmt:message key="edit"/>"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" name="_resetpwd" action="resetmypassword.htm">
            <input type="hidden" name="<%=ParameterConstants.USER_ID%>" value="<c:out value="${model.userId}"/>"/>
            <input class="actionbutton" type="button" name="_resetpwd" value="Change Password"
                   onclick="document.forms._resetpwd.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">
    <table class="infotable" cellspacing="0">
        <c:set var="loginInfo" value="${model.artefact.loginInfo}"/>
        <%@ include file="../common/users/viewlogininfo.jsp" %>
        <c:set var="coreDetail" value="${model.artefact.coreDetail}"/>
        <%@ include file="../common/users/viewcoredetails.jsp" %>
    </table>
</zynap:infobox>
