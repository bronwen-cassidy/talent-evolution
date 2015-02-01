<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="addHomePages" action="/admin/addhomepages.htm">
            <input class="actionbutton" id="add" name="addHPages" type="button" value="<fmt:message key="add"/>" onclick="javascript:document.forms.addHomePages.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<c:url value="/admin/listhomepages.htm" var="pageUrl"/>
<c:url value="/admin/viewhomepages.htm" var="viewUrl"/>

<zynap:infobox title="${title}">

    <fmt:message key="generic.label" var="headerlabel"/>
    <fmt:message key="worklist.actions" var="headeractions"/>


    <display:table name="${groups}" htmlId="grpTblId" id="group" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" excludedParams="*" defaultsort="1">
        <display:column property="label" title="${headerlabel}" href="${viewUrl}" paramId="groupId" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column title="${headeractions}"  headerClass="sorted" class="pager">
            <c:url value="/admin/edithomepages.htm" var="editUrl">
                <c:param name="groupId" value="${group.id}"/>
            </c:url>
            <a href="<c:out value="${editUrl}"/>"><fmt:message key="edit"/></a>
            &nbsp; | &nbsp;
            <c:url value="/admin/deletehomepages.htm" var="deleteUrl">
                <c:param name="groupId" value="${group.id}"/>
            </c:url>
            <fmt:message var="delmsg" key="confirm.delete.group"><fmt:param value="${group.label}"/></fmt:message>
            <zynap:message text="${delmsg}" javaScriptEscape="true" var="deletemessage"/>
            <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletemessage}"/>');"><fmt:message key="delete"/></a>
        </display:column>
    </display:table>

</zynap:infobox>