<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>

<fmt:message key="report.label" var="headerlabel" />
<fmt:message key="report.description" var="headerdescription" />
<fmt:message key="report.type" var="headertype" />
<fmt:message key="analysis.scope" var="headerscope" />
<fmt:message key="preferred.population" var="headerprefpopulation" />


<display:table name="${command.reports}" id="reports" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">
    <display:column property="label" title="${headerlabel}" href="${runUrl}"
        paramId="<%=ParameterConstants.REPORT_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>

    <display:column property="populationLabel" title="${headerprefpopulation}" headerClass="sortable" sortable="true"  class="pager"/>

    <display:column title="${headertype}" headerClass="sortable" sortable="true" class="pager">
        <fmt:message key="analysis.population.type.simple${reports.populationType}"/>
    </display:column>

    <display:column property="accessType" title="${headerscope}" headerClass="sortable" sortable="true"  class="pager"/>
    <display:column property="description" title="${headerdescription}" headerClass="sorted" class="pager" maxLength="25"/>
</display:table>