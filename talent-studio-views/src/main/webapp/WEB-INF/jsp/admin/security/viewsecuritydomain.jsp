
<%@ include file="../../includes/include.jsp" %>

<c:set var="domain" value="${model.securityDomain}" scope="request"/>

<zynap:actionbox>
    <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" name="_edit" action="/admin/editsecuritydomain.htm">
            <input type="hidden" name="<%=ParameterConstants.DOMAIN_ID%>" value="<c:out value="${domain.id}"/>"/>
            <zynap:button cssClass="actionbutton" name="editdomain" value="Edit" type="button" onclick="javascript:document.forms._edit.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" name="_delete" action="/admin/deletesecuritydomain.htm">
            <input type="hidden" name="<%=ParameterConstants.DOMAIN_ID%>" value="<c:out value="${domain.id}"/>"/>
            <zynap:button cssClass="actionbutton" name="deletedomain" value="Delete" type="button" onclick="javascript:document.forms._delete.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:saveUrl/>

<fmt:message key="securitydomain.title" var="title" scope="request"/>
<zynap:infobox title="${title}">
    <table class="infotable" id="userdetails" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="securitydomain.label"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${domain.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="securitydomain.comments"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${domain.comments}"/></zynap:desc></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="securitydomain.active"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${domain.active}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="securitydomain.exclusive"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${domain.exclusive}"/></td>
        </tr>

	    <tr>
		    <td class="infolabel">
			    <fmt:message key="securitydomain.roles"/>&nbsp;:&nbsp;
		    </td>
		    <td class="infodata">
			    <c:forEach items="${domain.roles}" var="role">
                    <zynap:historyLink var="viewRoleUrl" url="viewrole.htm">
                        <zynap:param name="role_id" value="${role.id}"/>
                    </zynap:historyLink>

                    <a href="<c:out value="${viewRoleUrl}"/>"><c:out value="${role.label}"/></a><br/>
			    </c:forEach>
		    </td>
	    </tr>

	    <tr>
		    <td class="infolabel">
			    <fmt:message key="securitydomain.area"/>&nbsp;:&nbsp;
		    </td>
		    <td class="infodata">
                <c:set var="area" value="${domain.area}" scope="request"/>

                <zynap:historyLink var="viewAreaUrl" url="viewarea.htm">
                    <zynap:param name="areaId" value="${area.id}"/>
                </zynap:historyLink>

                <a href="<c:out value="${viewAreaUrl}"/>"><c:out value="${area.label}"/></a><br/>
		    </td>
	    </tr>
    </table>
</zynap:infobox>
