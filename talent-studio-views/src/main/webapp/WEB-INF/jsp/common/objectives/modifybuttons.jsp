<%@ page import="static com.zynap.talentstudio.web.common.ParameterConstants.*" %>
<c:set var="viewDone" value="false"/>
<zynap:actionbox>
    <c:if test="${artefact.hasPublishedObjectives}">
        <c:choose>
            <c:when test="${empty objectives}">
                <zynap:actionEntry>
                    <fmt:message key="add" var="addButtonLabel"/>
                    <zynap:artefactForm name="addobj" method="get" action="worklistaddobjectives.htm" tabName="activeTab" buttonMessage="${addButtonLabel}" artefactId="${artefact.node.id}" />
                </zynap:actionEntry>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${artefact.hasApprovableObjectives}">
                        <zynap:actionEntry>
                            <fmt:message key="edit" var="editButtonLabel"/>
                            <zynap:artefactForm name="editobj" method="get" action="worklisteditobjectives.htm" tabName="activeTab" buttonMessage="${editButtonLabel}" artefactId="${artefact.node.id}" />
                        </zynap:actionEntry>
                    </c:when>
                    <c:otherwise>
                        <c:set var="viewDone" value="true"/>
                        <zynap:actionEntry>
                            <fmt:message key="view" var="viewButtonLabel"/>
                            <c:if test="${artefact.hasApprovableAssessments}">
                                <fmt:message key="assess" var="viewButtonLabel"/>
                            </c:if>
                            <zynap:artefactForm name="viewobj" method="get" action="worklistviewobjectives.htm" tabName="activeTab" buttonMessage="${viewButtonLabel}" artefactId="${artefact.node.id}" />
                        </zynap:actionEntry>
                        <!-- assign assessors link -->
                        <c:if test="${artefact.approved && not artefact.complete}">
                            <zynap:actionEntry>
                                <zynap:message code="assign.assessors" var="assessorTitle"/>
                                <input type="button" class="actionbutton" id="assessorBtnId" value="<c:out value="${assessorTitle}"/>" onclick="popupShow('<c:out value="${assessorTitle}"/>', this, 'assessFormPopupElem');"/>
                            </zynap:actionEntry>
                        </c:if>                        
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </c:if>
    <c:if test="${artefact.hasArchivedObjectives && !viewDone}">
        <zynap:actionEntry>
            <fmt:message key="view" var="viewButtonLabel"/>
            <zynap:artefactForm name="viewobj" method="get" action="worklistviewobjectives.htm" tabName="activeTab" buttonMessage="${viewButtonLabel}" artefactId="${artefact.node.id}" />
        </zynap:actionEntry>   
    </c:if>
    <c:if test="${artefact.statusApproved || artefact.complete}">
        <zynap:actionEntry>
            <fmt:message key="archive" var="archiveButtonLabel"/>
            <zynap:message code="archive.confirm.message" var="archivename" />
            <c:url var="archiveUrl" value="/talentarena/worklistarchiveobjectives.htm"/>

            <form method="get" action="<c:out value='${archiveUrl}'/>" name="archiveObjs">
                <input type="hidden" name="<%=NODE_ID_PARAM%>" value="<c:out value='${artefact.id}'/>"/>
                <input type="hidden" name="<%=DISABLE_COMMAND_DELETION%>" value="<%=SAVE_COMMAND%>"/>
                <input type="hidden" name="_parameter_save_command_.activeSearchTab" value="browse"/>
                <input type="hidden" name="_parameter_save_command_.activeTab" value="<c:out value="${activeTab}"/>"/>
                <input type="hidden" name="id" value="<c:out value='${artefact.currentObjectiveSet.id}'/>"/>
                <input type="button" class="actionbutton" id="archBtnId" value="<c:out value="${archiveButtonLabel}"/>" onclick="confirmActionAndPost('archiveObjs', '<c:out value="${archivename}"/>');"/>
            </form>
        </zynap:actionEntry>
    </c:if>
    <c:if test="${artefact.statusApproved}">
        <zynap:actionEntry>
            <fmt:message key="reopen" var="reopenButtonLabel"/>
            <zynap:message code="reopen.confirm.message" var="reopenname"/>
            <c:url var="reopenUrl" value="/talentarena/worklistreopenobjectives.htm"/>

            <form method="get" action="<c:out value='${reopenUrl}'/>" name="reopenObjs">
                <input type="hidden" name="<%=NODE_ID_PARAM%>" value="<c:out value='${artefact.id}'/>"/>
                <input type="hidden" name="<%=DISABLE_COMMAND_DELETION%>" value="<%=SAVE_COMMAND%>"/>
                <input type="hidden" name="_parameter_save_command_.activeSearchTab" value="browse"/>
                <input type="hidden" name="_parameter_save_command_.activeTab" value="<c:out value="${activeTab}"/>"/>
                <input type="hidden" name="id" value="<c:out value='${artefact.currentObjectiveSet.id}'/>"/>
                <input type="button" class="actionbutton" id="reopBtnId" value="<c:out value="${reopenButtonLabel}"/>" onclick="confirmActionAndPost('reopenObjs', '<c:out value="${reopenname}"/>');"/>
            </form>
        </zynap:actionEntry>
    </c:if>
</zynap:actionbox>