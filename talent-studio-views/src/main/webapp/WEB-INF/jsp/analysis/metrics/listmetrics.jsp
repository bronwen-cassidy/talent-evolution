<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add"/>"
               onclick="javascript:document.forms._add.submit();"/>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="metric.label" var="headerlabel"/>
<fmt:message key="report.description" var="headerdescription"/>
<fmt:message key="report.type" var="headertype"/>
<fmt:message key="analysis.scope" var="headerscope"/>
<fmt:message key="scalar.operator" var="headeroperator"/>

<zynap:infobox title="${title}">

    <zynap:link var="pageUrl" url="listmetrics.htm"/>
    <zynap:link var="viewUrl" url="viewmetric.htm"/>

    <display:table name="${model.reports}" id="metrics" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">

        <display:column property="label" title="${headerlabel}" href="${viewUrl}"
                        paramId="<%=ParameterConstants.METRIC_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>

        <display:column title="${headeroperator}" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="scalar.operator.${metrics.operator}"/>
        </display:column>

        <display:column title="${headertype}" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="${metrics.artefactType}"/>
        </display:column>


        <display:column property="accessType" title="${headerscope}" headerClass="sortable" sortable="true" class="pager"/>
        <display:column property="description" title="${headerdescription}" headerClass="sorted" class="pager" maxLength="25"/>
    </display:table>

</zynap:infobox>

<zynap:form method="get" name="_add" action="/analysis/addmetric.htm"/>
