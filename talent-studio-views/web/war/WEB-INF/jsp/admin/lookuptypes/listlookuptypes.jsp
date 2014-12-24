<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_add" method="get" action="/admin/addlookuptype.htm">
            <input type="submit" class="actionbutton" value="<fmt:message key="add"/>" name="_add"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="lookuptypes" var="msg"/>

<fmt:message key="generic.name" var="headerlabel" />
<fmt:message key="generic.description" var="headerdescription" />
<fmt:message key="generic.active" var="headeractive" />

<zynap:infobox title="${msg}">
    <c:set var="lookups" scope="request" value="${model.lookups}"/>

    <zynap:historyLink var="url" url="listlookuptypes.htm"/>
    <zynap:historyLink var="pageUrl" url="listlookupvalues.htm"/>

    <display:table name="${lookups}" id="lookup" sort="list" pagesize="25" requestURI="${url}" class="pager" defaultsort="1">
        <display:column property="label" href="${pageUrl}" paramId="<%=ParameterConstants.LOOKUP_TYPE_ID%>" paramProperty="typeId" title="${headerlabel}" sortable="true" headerClass="sortable" class="pager"/>
        <display:column property="description" title="${headerdescription}" sortable="true" headerClass="sortable" class="pager"/>
        <display:column title="${headeractive}" headerClass="sorted" class="pager">
            <fmt:message key="${lookup.active}"/>        
        </display:column>
    </display:table>
</zynap:infobox>

