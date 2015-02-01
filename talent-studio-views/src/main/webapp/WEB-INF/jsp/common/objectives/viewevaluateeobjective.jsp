<%@ include file="../../includes/include.jsp"%>

<c:set var="objMsg" value="${objective.label}"/>
<zynap:infobox id="viewObj" title="${objMsg}">

    <c:set var="subject" value="${command.nodeInfo.subject}" scope="request"/>
    <c:set var="organisationUnits" value="${command.nodeInfo.organisationUnits}"/>
    <c:set var="positions" value="${command.nodeInfo.positions}"/>
    <%@include file="../../objectives/nodeinfo.jsp"%>

</zynap:infobox>

<zynap:tab defaultTab="objective" tabParamName="activeObjTab" url="javascript">
    <fmt:message key="goals.page" var="objectiveLabel" />
    <zynap:tabName value="${objectiveLabel}" name="objective"/>

<c:if test="${approved}">
    <fmt:message key="goals.assessment" var="assessmentLabel" />
    <zynap:tabName value="Assessments" name="assessments"/>
</c:if>

<div id="objective_span" style="display:inline">
    <c:set var="objectivesMsg" value="${objective.label}" scope="request"/>
    <c:set var="objectiveIndex" value="0" scope="request"/>
    <c:import url="../../objectives/viewobjective.jsp"/>
</div>

<c:if test="${approved}">
    <div id="assessments_span" style="display:none">
        <c:set var="objectivesMsg" value="${objective.label}" scope="request"/>        
        <c:set var="assessments" value="${objective.assessments}" scope="request"/>
        <c:set var="assessment" value="${objective.assessment}" scope="request"/>
        <c:import url="viewappraisalassessments.jsp"/>
    </div>
</c:if>

</zynap:tab>