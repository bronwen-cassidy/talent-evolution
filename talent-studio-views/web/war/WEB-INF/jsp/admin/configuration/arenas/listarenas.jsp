<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<fmt:message key="generic.arena" var="headerarena" />
<fmt:message key="generic.label" var="headerlabel" />
<fmt:message key="arena.timeouts.minutes" var="headertimeout" />
<fmt:message key="sort.order" var="headerorder" />
<fmt:message key="generic.active" var="headeractive" />

<zynap:infobox title="${title}">
    <display:table name="${model.arenas}" id="arena" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" excludedParams="*" defaultsort="1">

        <display:column title="${headerarena}" sortable="true" headerClass="sortable" class="pager">
            <c:url var="editUrl" value="editarena.htm"><c:param name="arena_p" value="${arena.arenaId}"/></c:url>
			<c:set var="arenaId"><zynap:id><c:out value="${arena.label}"/></zynap:id></c:set>
            <a href="<c:out value="${editUrl}"/>" id="edit_<c:out value="${arenaId}"/>"><c:out value="${arena.label}"/></a>
        </display:column>

        <%--<display:column property="label" title="${headerlabel}" sortable="true" headerClass="sortable" class="pager"/>--%>
        <display:column property="sessionTimeout" title="${headertimeout}" sortable="true" headerClass="sortable" class="pager" />
        <display:column property="sortOrder" title="${headerorder}" sortable="true" headerClass="sortable" class="pager" />

        <display:column title="${headeractive}" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="${arena.active}"/>
        </display:column>
        
    </display:table>

</zynap:infobox>
