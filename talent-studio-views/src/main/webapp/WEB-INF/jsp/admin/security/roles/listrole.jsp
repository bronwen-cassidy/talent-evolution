<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<%-- Issue TS-1112 : temporary disable Add/Edit roles

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_add" method="get" action="/admin/selectroletype.htm">
            <input type="submit" name="_add" value="<fmt:message key="add"/>" class="actionbutton"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

--%>

<fmt:message key="generic.name" var="headerlabel" />
<fmt:message key="generic.description" var="headerdescription" />
<fmt:message key="role.type" var="headerroletype" />

<zynap:infobox title="${title}">
    <c:url var="url" value="listrole.htm"/>
    <zynap:historyLink var="pageUrl" url="viewrole.htm"/>

    <display:table name="${model.roles}" id="pg_table" sort="list" pagesize="25" requestURI="${url}" class="pager" defaultsort="1">
        <display:column property="label" title="${headerlabel}" href="${pageUrl}" paramId="<%=ParameterConstants.ROLE_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column property="description" title="${headerdescription}" headerClass="sorted" class="pager"/>
        <display:column title="${headerroletype}" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="role.${pg_table.roleType}"/>
        </display:column>
    </display:table>
</zynap:infobox>
