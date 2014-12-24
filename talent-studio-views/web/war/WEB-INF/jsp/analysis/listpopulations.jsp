<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<script type="text/javascript">

     function comboChange(formName)
     {
     	var form = document.forms[formName];
     	form.search_initiated.value = 'COMBO_CHANGE';
        form.submit();
     }

</script>


<zynap:saveUrl/>

<zynap:actionbox>
<zynap:actionEntry>
    <input class="actionbutton" type="button" value="<fmt:message key="add"/>" onclick="javascript:document.forms._add.submit();"/>
</zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="analysis.population.list" var="msg"/>

<zynap:infobox title="${msg}">

<fmt:message key="analysis.population.label" var="headerlabel" />
<fmt:message key="report.description" var="headerdescription" />
<fmt:message key="report.type" var="headertype" />
<fmt:message key="analysis.scope" var="headerscope" />
<fmt:message key="analysis.population.type" var="headerclass" />


        <c:set var="populations" scope="request" value="${command.populations}"/>

        <zynap:link var="pageUrl" url="listpopulations.htm">
            <zynap:param name="<%= ParameterConstants.HAS_RESULTS%>" value="true"/>
        </zynap:link>
        <zynap:link var="viewUrl" url="viewpopulation.htm"/>

        <display:table name="${populations}" id="populations" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">

            <display:column property="label" title="${headerlabel}" href="${viewUrl}"
                paramId="<%=ParameterConstants.POPULATION_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>


            <display:column title="${headertype}" headerClass="sortable" sortable="true" class="pager">
                   <fmt:message key="analysis.population.type.simple${populations.type}"/>
            </display:column>

            <display:column property="scope" title="${headerscope}" headerClass="sortable" sortable="true"  class="pager"/>
            <display:column property="description" title="${headerdescription}" headerClass="sorted" class="pager" maxLength="25"/>
        </display:table>

</zynap:infobox>

<zynap:form method="get" name="_add" action="/analysis/addpopulation.htm"/>

