<zynap:popup id="calendarPopup">
    <%@ include file="../includes/calendar.jsp" %>
</zynap:popup>

<c:if test="${command.organisationPickerList}">
    <c:if test="${orgUnitTree != null && !empty orgUnitTree}">
        <zynap:window elementId="orgTree">
            <zynap:serverTree trees="${orgUnitTree}" branchIcon="ClosedFolder.gif" emptyBranchIcon="ou.gif" leafIcon="ou.gif" branchSelectable="true"/>
        </zynap:window>
    </c:if>
</c:if>

<c:if test="${command.positionPickerList}">
    <zynap:window elementId="positionTree" src="../picker/positionandholderpicker.htm"/>
</c:if>

<c:if test="${command.subjectPickerList}">
    <zynap:window elementId="subjectTree" src="../picker/subjectpicker.htm"/>
</c:if>

<zynap:window elementId="helpText"/>
