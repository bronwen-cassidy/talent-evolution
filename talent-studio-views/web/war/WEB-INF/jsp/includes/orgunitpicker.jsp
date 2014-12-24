<c:if test="${outree != null && !empty outree}">
    <zynap:window elementId="ouPicker">
        <zynap:serverTree trees="${outree}" branchIcon="ClosedFolder.gif" emptyBranchIcon="ou.gif" leafIcon="ou.gif" branchSelectable="true"/>
    </zynap:window>
</c:if>