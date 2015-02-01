<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_add" action="/analysis/addchartreport.htm">
            <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add"/>" onclick="javascript:document.forms._add.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" name="_addspider" action="/analysis/addspiderchartreport.htm">
            <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add.spider.chart"/>" onclick="javascript:document.forms._addspider.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">

   <zynap:link var="pageUrl" url="listchartreports.htm"/>
   <zynap:link var="runUrl" url="runviewchartreport.htm"/>

    <fmt:message key="report.label" var="headerlabel" />
    <fmt:message key="report.description" var="headerdescription" />
    <fmt:message key="report.type" var="headertype" />
    <fmt:message key="chart.type" var="headercharttype" />
    <fmt:message key="analysis.scope" var="headerscope" />
    <fmt:message key="preferred.population" var="headerprefpopulation" />


    <display:table name="${command.reports}" id="reports" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">
        <display:column sortProperty="label" title="${headerlabel}" sortable="true" headerClass="sortable" class="pager">
            <c:set var ="runViewUrl" value="${runUrl}"/>
            <c:if test="${reports.chartType == 'SPIDER'}">
                <c:set var ="runViewUrl" value="runviewspiderchartreport.htm"/>    
            </c:if>
            <a href="<c:url value="${runViewUrl}"><c:param name="id" value="${reports.id}"/></c:url> "><c:out value="${reports.label}"/></a>
        </display:column>

        <display:column property="populationLabel" title="${headerprefpopulation}" headerClass="sortable" sortable="true"  class="pager"/>

        <display:column title="${headertype}" headerClass="sortable" sortable="true" class="pager">
            <fmt:message key="analysis.population.type.simple${reports.populationType}"/>
        </display:column>

        <display:column sortProperty="chartType" title="${headercharttype}" headerClass="sortable" sortable="true"  class="pager">
            <fmt:message key="${reports.chartType}"/>
        </display:column>
        <display:column property="accessType" title="${headerscope}" headerClass="sortable" sortable="true"  class="pager"/>
        <display:column property="description" title="${headerdescription}" headerClass="sorted" class="pager" maxLength="25"/>
    </display:table>

</zynap:infobox>
