<%@ include file="../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="goal.tracker" var="msg" scope="request"/>

<zynap:form method="post" name="objsetfrmid" htmlId="objsetfrmid1" encType="multipart/form-data">
    <%-- user information --%>
    <input type="hidden" id="cancelId" name="" value=""/>
    <input type="hidden" id="deleteIdx" name="deleteIndex" value="-1"/>
    <input type="hidden" id="targetId" name="" value="-1"/>
    <c:set var="subject" value="${command.nodeInfo.subject}"/>

    <%-- instructions table --%>
    <fmt:message key="goals.status" var="msg1" scope="request"><fmt:param value="${subject.label}"/></fmt:message>
    <c:import url="../objectives/objectiveinstructions.jsp"/>

    <%-- individual objects adding --%>
    <table class="infotable" cellpadding="0" cellspacing="0">
        <c:import url="../objectives/objectivesform.jsp"/>
    </table>
</zynap:form>
