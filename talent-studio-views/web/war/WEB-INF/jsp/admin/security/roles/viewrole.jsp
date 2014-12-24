<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:set var="roleId" value="${model.role.id}"/>

<c:url var="url" value="/admin/viewrole.htm"><c:param name="role_id" value="${roleId}"/></c:url>

<zynap:evalBack>
    <zynap:actionbox>
        <zynap:actionEntry>
            <fmt:message key="back" var="buttonLabel" />
            <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
        </zynap:actionEntry>
    </zynap:actionbox>    
</zynap:evalBack>

<%-- Issue TS-1112 : temporary disable Add/Edit roles
    <zynap:actionbox>
        <zynap:actionEntry>
            <zynap:form method="get" action="/admin/editrole.htm" name="formEditRole">
                <input type="hidden" name="<%=ParameterConstants.ROLE_ID%>" value="<c:out value="${roleId}"/>"/>
                <zynap:button cssClass="actionbutton" name="editrole" value="Edit" type="button" onclick="javascript:document.forms.formEditRole.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
        <c:if test="${!model.role.system}">
            <zynap:actionEntry>
                <zynap:form name="_delete" method="get" action="/admin/deleterole.htm">
                    <input type="hidden" name="<%=ParameterConstants.ROLE_ID%>" value="<c:out value="${roleId}"/>"/>
                    <zynap:button cssClass="actionbutton" name="deleterole" value="Delete" type="button" onclick="javascript:document.forms._delete.submit();"/>
                </zynap:form>
            </zynap:actionEntry>
        </c:if>
    </zynap:actionbox>
--%>

    <fmt:message key="role" var="roleTabLabel" />
    <zynap:infobox title="${roleTabLabel}">
        <table class="infotable" cellspacing="0">
            <%-- Role Name--%>
            <tr>
                <td class="infolabel"><fmt:message key="role.name"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><c:out value="${model.role.label}"/></td>
            </tr>
            <%-- Role Type--%>
            <tr>
                <td class="infolabel"><fmt:message key="role.type"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><fmt:message key="${model.role.roleType}"/></td>
            </tr>
            <%-- Is Active --%>
            <tr>
                <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <fmt:message key="${model.role.active}"/>                    
                </td>
            </tr>
            <%-- Description --%>
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><zynap:desc><c:out value="${model.role.description}"/></zynap:desc></td>
            </tr>
        </table>

        <%-- test if any permissions if not leave a message--%>
        <c:choose>
            <c:when test="${empty model.role.allPermits}">
                <div class="infomessage"><fmt:message key="no.permissions.for.role"/></div>
            </c:when>
            <c:otherwise>
                <div class="infomessage"><fmt:message key="access.permissions"/></div>
                <%-- Permissions --%>
                <zynap:permission label="Component Name" permissions="${model.role.allPermits}" bindName="selectedPermitIds"/>
            </c:otherwise>
        </c:choose>
    </zynap:infobox>

