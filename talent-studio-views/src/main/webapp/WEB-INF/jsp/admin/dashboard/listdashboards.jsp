<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="addDashboard" action="/admin/adddashboard.htm">
            <input class="actionbutton" id="add" name="addDBd" type="button" value="<fmt:message key="add.person"/>" onclick="document.forms.addDashboard.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" name="addPersonalDashboard" action="/admin/addpersonaldashboard.htm">
            <input class="actionbutton" id="add" name="addDBd" type="button" value="<fmt:message key="add.personal"/>" onclick="document.forms.addPersonalDashboard.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<c:url value="/admin/listdashboards.htm" var="pageUrl"/>
<c:url value="/admin/viewdashboard.htm" var="viewUrl"/>

<zynap:infobox title="${title}">

    <fmt:message key="generic.label" var="headerlabel"/>
    <fmt:message key="worklist.actions" var="headeractions"/>
    <fmt:message key="dashboard.type" var="headertype"/>
    <fmt:message key="population" var="headerpopulation"/>


    <display:table name="${model.dashboards}" htmlId="grpTblId" id="dashboard" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" excludedParams="*" defaultsort="1">

        <display:column property="label" title="${headerlabel}" href="${viewUrl}" paramId="dashboardId" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column title="${headertype}" sortProperty="type" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="${dashboard.type}"/>
        </display:column>
        <display:column property="population.label" title="${headerpopulation}" sortable="true" headerClass="sortable" class="pager"/>
        <%-- actions one of them needs to be a republish --%>
        <display:column title="${headeractions}"  headerClass="sorted" class="pager">
            <c:url value="/admin/editdashboard.htm" var="editUrl">
                <c:param name="dashboardId" value="${dashboard.id}"/>
            </c:url>            
            <a href="<c:out value="${editUrl}"/>"><fmt:message key="edit"/></a>
            &nbsp; | &nbsp;
            <c:url value="/admin/deletedashboard.htm" var="deleteUrl">
                <c:param name="dashboardId" value="${dashboard.id}"/>
            </c:url>
            <fmt:message key="confirm.delete.dashboard" var="delmessage"><fmt:param value="${dashboard.label}"/></fmt:message>
            <zynap:message var="deletemessage" javaScriptEscape="true" text="${delmessage}"/>
            <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletemessage}"/>');"><fmt:message key="delete"/></a>
        </display:column>
    </display:table>

</zynap:infobox>