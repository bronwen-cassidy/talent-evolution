<zynap:actionbox id="${tabContent.key}_tt">
    <zynap:actionEntry>
        <fmt:message key="edit" var="editButtonLabel"/>
        <zynap:artefactForm name="${tabContent.key}_frm" method="get" action="${editsurl}" tabName="activeTab" buttonMessage="${editButtonLabel}" buttonId="btn_${tabContent.key}" artefactId="${artefact.id}">
            <input type="hidden" name="dc_item" value="<c:out value="${tabContent.id}"/>"/>
        </zynap:artefactForm>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:artefactLink var="viewSubjectUrl" url="viewsubject.htm" tabName="activeTab" activeTab="${tabContent.key}" />
<zynap:artefactLink var="viewPositionUrl" url="viewposition.htm" tabName="activeTab" activeTab="${tabContent.key}"/>

<zynap:infobox title="${reportItem.report.label}" id="${tabContent.key}_rdet" htmlEscape="true">
    <c:choose>
        <c:when test="${empty reportItem.report.columns}">
            <span class="noinfo"><fmt:message key="no.artefact.information"/></span>
        </c:when>
        <c:otherwise>
            <table id="<c:out value="${tabContent.key}"/>_table" class="infotable" cellspacing="0">
                <zynap:artefactViewTag cellCount="1" report="${reportItem.report}" node="${artefact.node}" checkNodeAccess="true" viewSubjectUrl="${viewSubjectUrl}" viewPositionUrl="${viewPositionUrl}" imageUrl="${imageUrl}" displayHelper="${displayHelper}" viewType="${viewType}"/>
            </table>
        </c:otherwise>
    </c:choose>
</zynap:infobox>
