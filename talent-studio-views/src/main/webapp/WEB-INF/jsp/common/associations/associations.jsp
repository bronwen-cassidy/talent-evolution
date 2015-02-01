<zynap:infobox title="${reportItem.report.label}" id="${tbId}ain">
    <c:choose>
        <c:when test="${empty associations}">
            <div class="noinfo"><fmt:message key="no.defined.associations"/></div>
        </c:when>
        <c:otherwise>
            <table id="<c:out value="${tbId}"/>_table" class="infotable" cellspacing="0">

                <zynap:artefactLink var="viewPositionUrl" url="viewposition.htm" tabName="activeTab" activeTab="${tabContent.key}" >
                    <zynap:param name="_parameter_save_command_.activeSearchTab" value="browse"/>
                </zynap:artefactLink>

                <zynap:artefactLink var="viewSubjectUrl" url="viewsubject.htm" tabName="activeTab" activeTab="${tabContent.key}"  >
                    <zynap:param name="_parameter_save_command_.activeSearchTab" value="browse"/>
                </zynap:artefactLink>

                <zynap:associationViewTag checkNodeAccess="true" associations="${associations}" viewSubjectUrl="${viewSubjectUrl}" viewPositionUrl="${viewPositionUrl}" report="${reportItem.report}" inverse="${invert}" displayHelper="${displayHelper}" imageUrl="${imageUrl}"/>
            </table>
        </c:otherwise>
    </c:choose>
</zynap:infobox>
