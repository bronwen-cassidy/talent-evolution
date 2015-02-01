<zynap:infobox title="${userHeading}" id="useracct">
    <table class="infotable" id="u_serdetails" cellspacing="0">
        <c:set var="loginInfo" value="${user.loginInfo}"/>
        <c:set var="homePageGroup" value="${user.homePageGroup}"/>
        <%@include file="viewlogininfo.jsp" %>
        <c:set var="coreDetail" value="${user.coreDetail}"/>
        <c:if test="${showCoreDetails}">
            <%-- include core details --%>
            <%@include file="viewcoredetails.jsp" %>
        </c:if>
        <tr>
            <td class="infolabel"><fmt:message key="contact.email"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${coreDetail.contactEmail}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${user.active}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="homepage.group"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${homePageGroup.label}"/></td>
        </tr>
        <c:set var="userRoles" value="${user.userRoles}"/>
        <c:set var="rolesEmpty" value="${user.userRolesEmpty}"/>
        <%@ include file="../roles/viewroles.jsp" %>
        <%@ include file="../../admin/security/usersecuritydomains.jsp"%>


    </table>
</zynap:infobox>
