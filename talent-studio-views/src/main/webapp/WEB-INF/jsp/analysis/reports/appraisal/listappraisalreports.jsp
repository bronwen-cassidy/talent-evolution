<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_add" action="/analysis/addappraisalreport.htm">
            <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add"/>" onclick="javascript:document.forms._add.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">

    <fmt:message key="report.label" var="headerlabel" />
    <fmt:message key="report.description" var="headerdescription" />
    <fmt:message key="report.status" var="headerstatus" />
    <fmt:message key="worklist.actions" var="headeractions"/>

    <c:url var="viewUrl" value="/analysis/viewappraisalreport.htm"/>
    <c:url value="${request.requestURI}" var="pageUrl"/>

    <display:table name="${model.reports}" id="reports" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">
        <display:column property="label" title="${headerlabel}" href="${viewUrl}" paramId="<%=ParameterConstants.REPORT_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column property="description" title="${headerdescription}" headerClass="sorted" class="pager" maxLength="25"/>
        <display:column sortProperty="status" title="${headerstatus}" headerClass="sortable" sortable="true" class="pager">
            <fmt:message key="${reports.status}"/>
        </display:column>
        <%-- actions --%>
        <display:column title="${headeractions}" headerClass="sorted" sortable="false" class="pager">
            <c:url value="/analysis/deleteappraisalreport.htm" var="deleteUrl"><c:param name="id" value="${reports.id}"/></c:url>
            <c:url value="/analysis/editarchiveappraisalreport.htm" var="archiveUrl"><c:param name="id" value="${reports.id}"/></c:url>
            <c:url value="/analysis/editpublishappraisalreport.htm" var="publishUrl"><c:param name="id" value="${reports.id}"/></c:url>
            <c:url value="/analysis/editopenappraisalreport.htm" var="openUrl"><c:param name="id" value="${reports.id}"/></c:url>

            <zynap:message code="confirm.delete.report" var="deletemessage"><zynap:argument value="${reports.label}"/></zynap:message>

            <c:choose>
                <c:when test="${reports.status == 'PUBLISHED'}">
                    <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletemessage}"/>');"><fmt:message key="delete"/></a>&nbsp;|&nbsp;
                    <a href="<c:out value="${archiveUrl}"/>"><fmt:message key="archive"/></a>&nbsp;|&nbsp;
                    <a href="<c:out value="${openUrl}"/>"><fmt:message key="unpublish"/></a>
                </c:when>
                <c:when test="${reports.status == 'ARCHIVED'}">
                    <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletemessage}"/>');"><fmt:message key="delete"/></a>&nbsp;|&nbsp;
                    <a href="<c:out value="${openUrl}"/>"><fmt:message key="reopen"/></a>
                </c:when>
                <c:otherwise>
                    <%-- status is new --%>
                    <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletemessage}"/>');"><fmt:message key="delete"/></a>&nbsp;|&nbsp;
                    <a href="<c:out value="${publishUrl}"/>"><fmt:message key="publish"/></a>
                </c:otherwise>
            </c:choose>
        </display:column>

    </display:table>

</zynap:infobox>