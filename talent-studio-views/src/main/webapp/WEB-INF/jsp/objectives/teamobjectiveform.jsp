<%@ include file="../includes/include.jsp" %>

<fmt:message key="goal.tracker" var="msg" scope="request"/>

<zynap:form method="post" name="objsetfrmid" htmlId="objsetfrmid1" encType="multipart/form-data">
    <%-- user information --%>
    <zynap:infobox id="memObj" title="${msg}">

        <input type="hidden" id="cancelId" name="" value=""/>
        <input type="hidden" id="deleteIdx" name="deleteIndex" value="-1"/>
        <input type="hidden" id="targetId" name="" value="-1"/>

        <c:set var="subject" value="${command.nodeInfo.subject}"/>
        <c:set var="organisationUnits" value="${command.nodeInfo.organisationUnits}"/>
        <c:set var="positions" value="${command.nodeInfo.positions}"/>

        <%@include file="nodeinfo.jsp"%>
    </zynap:infobox>

    <%-- instructions table --%>
    <fmt:message key="goals.status" var="msg1" scope="request"/>
    <c:import url="objectiveinstructions.jsp"/>

    <table class="infotable" cellpadding="0" cellspacing="0">
        <%-- individual objectives adding --%>
        <c:import url="objectivesform.jsp"/>
    </table>
</zynap:form>
