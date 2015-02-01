<%@ include file="../includes/include.jsp"%>
<c:set var="viewDone" value="false"/>
<zynap:actionbox>
<c:if test="${artefact.hasPublishedObjectives}">
        <c:choose>
            <c:when test="${empty objectives}">
                <zynap:actionEntry>
                    <fmt:message key="add" var="addButtonLabel"/>
                    <zynap:artefactForm name="addobj" method="get" action="addmyobjectives.htm" tabName="activeTab" buttonMessage="${addButtonLabel}" artefactId="${artefact.id}" />
                </zynap:actionEntry>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${artefact.hasApprovableObjectives}">
                        <zynap:actionEntry>
                            <fmt:message key="edit" var="editButtonLabel"/>
                            <zynap:artefactForm name="editobj" method="get" action="editmyobjectives.htm" tabName="activeTab" buttonMessage="${editButtonLabel}" artefactId="${artefact.node.id}" />
                        </zynap:actionEntry>
                    </c:when>
                    <c:otherwise>
                        <c:set var="viewDone" value="true"/>
                        <zynap:actionEntry>
                            <fmt:message key="view" var="viewButtonLabel"/>
                            <c:if test="${artefact.hasApprovableAssessments}">
                                <fmt:message key="assess" var="viewButtonLabel"/>
                            </c:if>
                            <zynap:artefactForm name="viewobj" method="get" action="viewmyobjectives.htm" tabName="activeTab" buttonMessage="${viewButtonLabel}" artefactId="${artefact.node.id}" />
                        </zynap:actionEntry>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </c:if>
    <c:if test="${artefact.hasArchivedObjectives && !viewDone}">
        <zynap:actionEntry>
            <fmt:message key="view" var="viewButtonLabel"/>
            <zynap:artefactForm name="viewobj" method="get" action="viewmyobjectives.htm" tabName="activeTab" buttonMessage="${viewButtonLabel}" artefactId="${artefact.node.id}" />
        </zynap:actionEntry>
    </c:if>
</zynap:actionbox>

<c:url var="baseViewUrl" value="viewmyobjective.htm"/>
<c:set var="activeTab" value="${tabContent.key}"/>
<c:if test="${artefact.hasPublishedObjectives}">
    <%@ include file="../common/objectives/status.jsp" %>
</c:if>
<%@include file="../common/objectives/viewobjectives.jsp" %>
