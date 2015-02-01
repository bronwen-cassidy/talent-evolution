<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:set var="artefact" value="${command.nodeWrapper}" scope="request"/>
    <c:if test="${artefact != null && allowDelete}">
        <%-- Ensure that delete button and activate button is only present in selected arenas --%>
        <zynap:actionbox id="actions_subject">
            <zynap:actionEntry>
                <fmt:message key="delete" var="deleteButtonLabel"/>
                <zynap:artefactForm name="_delSubject" method="get" action="deletesubject.htm" tabName="activeTab"
                                    buttonMessage="${deleteButtonLabel}" buttonId="btn_deletesubject"
                                    artefactId="${artefact.id}">
                    <%-- include parameter that indicates if the subject has just been added --%>
                    <input type="hidden" name="<%=ControllerConstants.NEW_NODE%>" value="<c:out value="${command.newNode}"/>"/>
                </zynap:artefactForm>
            </zynap:actionEntry>

            <zynap:actionEntry>
                <c:choose>
                    <c:when test="${artefact.active}">
                        <fmt:message key="inactivate" var="inactivateButtonLabel"/>
                        <zynap:artefactForm name="${tabContent.key}_inactiv" method="get" action="editsubjectinactivate.htm" tabName="activeTab"
                                            buttonMessage="${inactivateButtonLabel}" buttonId="btn_inactivatesubject" artefactId="${artefact.id}">
                            <%-- include parameter that indicates if the subject has just been added --%>
                            <input type="hidden" name="<%=ControllerConstants.NEW_NODE%>" value="<c:out value="${command.newNode}"/>"/>
                        </zynap:artefactForm>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="activate" var="activateButtonLabel"/>
                        <zynap:artefactForm name="${tabContent.key}_activ" method="get" action="editsubjectactivate.htm" tabName="activeTab"
                                            buttonMessage="${activateButtonLabel}" buttonId="btn_activatesubject" artefactId="${artefact.id}">
                            <%-- include parameter that indicates if the subject has just been added --%>
                            <input type="hidden" name="<%=ControllerConstants.NEW_NODE%>" value="<c:out value="${command.newNode}"/>"/>
                        </zynap:artefactForm>
                    </c:otherwise>
                </c:choose>
            </zynap:actionEntry>
            
        </zynap:actionbox>
    </c:if>

<%@include file="subjectinfo.jsp" %>

<c:if test="${artefact.approved && not artefact.complete}">
    <zynap:window elementId="assessFormPopupElem" closeFunction="resetAssessors('assResultsTbls', 'selectedasscs_', 'personSelectorId');">
        <%@include file="../../objectives/objectiveassessorform.jsp" %>
    </zynap:window>
</c:if>