<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_add" method="get" action="/admin/addarea.htm">
            <input type="submit" name="_add" value="<fmt:message key="add"/>" class="actionbutton"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="generic.name" var="headerlabel" />
<fmt:message key="generic.active" var="headeractive" />

<zynap:infobox title="${title}">
    <c:url var="url" value="listarea.htm"/>
    <zynap:historyLink var="pageUrl" url="viewarea.htm"/>

    <display:table name="${model.areas}" id="area" sort="list" pagesize="25" requestURI="${url}" class="pager" defaultsort="1">
        <display:column property="label" title="${headerlabel}" href="${pageUrl}" paramId="<%=ParameterConstants.AREA_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column title="${headeractive}" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="${area.active}"/>
        </display:column>
    </display:table>
</zynap:infobox>
