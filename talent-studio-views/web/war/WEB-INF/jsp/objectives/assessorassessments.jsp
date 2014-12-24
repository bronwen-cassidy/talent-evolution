<%@ page import="com.zynap.talentstudio.objectives.ObjectiveConstants" %>
<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>


<fmt:message key="assessments" var="msg"/>
<zynap:infobox title="${msg}">

    <zynap:historyLink var="pageUrl" url="worklistassessments.htm"/>
    <zynap:historyLink var="editUrl" url="worklisteditassessment.htm"/>


    <fmt:message key="generic.name" var="headername" />
    <fmt:message key="objective.goal" var="objectivename"/>
    <fmt:message key="generic.description" var="headerdescription"/>    

    <display:table name="${command.assessments}" id="assessmentBean" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">
        <display:column property="subject.label" title="${headername}" sortable="true" headerClass="sortable" class="pager" group="0"/>
        <display:column property="objective.label" title="${objectivename}" href="${editUrl}" paramId="<%=ObjectiveConstants.OBJECTIVE_ID%>" paramProperty="objective.id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column property="objective.description" title="${headerdescription}" sortable="true" headerClass="sortable" class="pager"/>
    </display:table>

</zynap:infobox>