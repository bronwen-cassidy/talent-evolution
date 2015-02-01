<zynap:actionbox>        

    <c:if test="${artefact.complete || artefact.hasArchivedObjectives || artefact.shouldDisplayAdminView}">
        <%--there is a current set of objectives and they are completed --%>
        <zynap:actionEntry>
            <fmt:message key="view" var="viewButtonLabel"/>
            <zynap:artefactForm name="viewobj" method="get" action="viewsubjectobjectives.htm" tabName="activeTab" buttonMessage="${viewButtonLabel}" artefactId="${artefact.node.id}" />
        </zynap:actionEntry>
    </c:if>

</zynap:actionbox>