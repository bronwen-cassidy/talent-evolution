<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:historyLink var="pageUrl" url="${request.requestURI}"/>
<zynap:historyLink var="viewUrl" url="viewquestionnairedefinition.htm"/>

<zynap:saveUrl url="${pageUrl}"/>

<zynap:actionbox id="qnairDefActions">
    <zynap:actionEntry>
        <zynap:form method="get" name="addQue" action="/admin/addquestionnairedefinition.htm">
            <input class="actionbutton" type="submit" name="addQueBtn" value="<fmt:message key="add"/>"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="generic.name" var="headername" />
<fmt:message key="questionnaire.title" var="headertitle" />
<fmt:message key="generic.description" var="headerdescription" />

<zynap:infobox title="${title}" id="listDef">

    <display:table name="${model.questionnaireDefinitions}" id="quedef" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1" excludedParams="*">

        <display:column property="label" title="${headername}" href="${viewUrl}"
            paramId="<%=ParameterConstants.QUESTIONNAIRE_DEF_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>

        <display:column property="title" title="${headertitle}" headerClass="sorted" class="pager"/>
        <display:column property="description" title="${headerdescription}" headerClass="sorted" class="pager"/>

    </display:table>

</zynap:infobox>