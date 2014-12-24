<%@ include file="../includes/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="addobj" method="get" action="addcorporateobjectives.htm">
            <input class="actionbutton" type="button" value="<fmt:message key="add"/>" name="add" onclick="document.forms.addobj.submit();"/>   
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="corporate.goals" var="msg"/>
<zynap:infobox id="objformId" title="${msg}">

    <spring:message code="delete.confirm.message" var="deletename"/>
    <spring:message code="publish.confirm.message" var="publishname" />
    <spring:message code="archive.confirm.message" var="archivename" />


    <fmt:message key="quarter.year" var="headername" />
    <fmt:message key="objective.status" var="headerstatus" />
    <fmt:message key="objective.datecreated" var="headerdatecreated" />
    <fmt:message key="objective.datepublished" var="headerdatepublished" />
    <fmt:message key="expiry.date" var="headerexpiry" />
    <fmt:message key="worklist.actions" var="headeraction" />

    <zynap:historyLink var="pageUrl" url="listcorporateobjectives.htm"/>
    <zynap:historyLink var="viewUrl" url="viewcorporateobjectives.htm"/>

    <display:table name="${command.objectiveSets}" id="objslist" sort="list" pagesize="15" class="pager" excludedParams="*" defaultsort="1" requestURI="">

        <display:column title="${headername}" property="label" headerClass="sortable" class="pager" sortable="true" sortProperty="label" href="${viewUrl}" paramId="id" paramProperty="id" comparator="org.displaytag.model.RowSorter"/>

        <display:column sortProperty="status" title="${headerstatus}" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="${objslist.status}"/>
        </display:column>
        
        <display:column property="publishedDate" title="${headerdatepublished}" sortable="true" headerClass="sortable" class="pager" comparator="org.displaytag.model.RowSorter" decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator"/>
        <display:column property="expiryDate" title="${headerexpiry}" sortable="true" headerClass="sortable" class="pager" comparator="org.displaytag.model.RowSorter" decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator"/>

        <display:column class="pager" title="${headeraction}" headerClass="sorted">

            <c:url var="deleteUrl" value="/admin/deletecorporateobjectives.htm"><c:param name="id" value="${objslist.id}"/></c:url>
            <c:url var="publishUrl" value="/admin/publishcorporateobjectives.htm"><c:param name="id" value="${objslist.id}"/></c:url>
            <c:if test="${not objslist.published && not objslist.archived}">
                <a href="<c:url value="/admin/editcorporateobjectives.htm"><c:param name="id" value="${objslist.id}"/></c:url>"><fmt:message key="edit"/></a>&nbsp;
                |&nbsp;
                <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletename}"/>');"><fmt:message key="delete"/></a>&nbsp;
                <c:if test="${command.canPublish && objslist.hasObjectives}">
                    |&nbsp;
                    <a href="javascript:confirmAction('<c:out value="${publishUrl}"/>', '<c:out value="${publishname}"/>');"><fmt:message key="publish"/></a>&nbsp;
                </c:if>
            </c:if>
            <c:if test="${objslist.published}">
                <c:url var="archiveUrl" value="/admin/archivecorporateobjectives.htm"><c:param name="id" value="${objslist.id}"/></c:url>
                <a href="javascript:confirmAction('<c:out value="${archiveUrl}"/>', '<c:out value="${archivename}"/>');"><fmt:message key="archive"/></a>&nbsp;
            </c:if>
        </display:column>
    </display:table>
</zynap:infobox>
