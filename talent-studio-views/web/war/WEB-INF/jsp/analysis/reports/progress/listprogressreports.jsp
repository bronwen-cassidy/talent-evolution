<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_add" action="/analysis/addprogressreport.htm">
            <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add"/>" onclick="document.forms._add.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">

    <fmt:message key="report.label" var="headerlabel" />
    <fmt:message key="report.description" var="headerdescription" />
    <fmt:message key="report.definition" var="headerdefinition" />
    <fmt:message key="worklist.actions" var="headeraction" />
    <fmt:message key="report.population" var="headerpopulation" />

    <zynap:link var="runUrl" url="runviewprogressreport.htm"/>

    <display:table name="${model.reports}" id="reports" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">

        <display:column property="label" title="${headerlabel}" href="${runUrl}" paramId="<%=ParameterConstants.REPORT_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column property="description" title="${headerdescription}" sortable="true" headerClass="sortable" class="pager" maxLength="25"/>
        <display:column property="questionnaireDefinition.label" title="${headerdefinition}" sortable="true" headerClass="sortable" class="pager" maxLength="25"/>
        <display:column property="defaultPopulation.label" title="${headerpopulation}" sortable="true" headerClass="sortable" class="pager" maxLength="25"/>

        <display:column title="${headeraction}" sortable="false" headerClass="sorted" class="pager">
            <c:url value="/analysis/deleteprogressreport.htm" var="deleteUrl"><c:param name="id" value="${reports.id}"/></c:url>
            <c:url value="/analysis/editprogressreport.htm" var="openUrl"><c:param name="id" value="${reports.id}"/></c:url>
            <fmt:message key="confirm.delete.report" var="deleteMsg"><fmt:param value="${reports.label}"/></fmt:message>
            <zynap:message text="${deleteMsg}" javaScriptEscape="true" var="delMsg"/>

            <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deleteMsg}"/>');"><fmt:message key="delete"/></a>&nbsp;|&nbsp;
            <a href="<c:out value="${openUrl}"/>"><fmt:message key="edit"/></a>

        </display:column>

    </display:table>
</zynap:infobox>
