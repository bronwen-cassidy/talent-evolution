<zynap:actionbox id="${tabContent.key}_aa">
    <zynap:actionEntry>
        <fmt:message key="edit" var="editButtonLabel"/>
        <zynap:artefactForm name="${tabContent.key}_assfrm" method="get" action="${editassocurl}" tabName="activeTab" buttonMessage="${editButtonLabel}" buttonId="btn_${tabContent.key}" artefactId="${artefact.id}" >
            <input type="hidden" name="dc_item" value="<c:out value="${tabContent.id}"/>"/>
        </zynap:artefactForm>
    </zynap:actionEntry>
</zynap:actionbox>
