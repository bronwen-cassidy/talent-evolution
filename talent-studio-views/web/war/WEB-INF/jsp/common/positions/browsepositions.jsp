<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:set var="artefact" value="${command.nodeWrapper}" scope="request"/>
    <c:if test="${artefact != null}">
        <c:if test="${allowDelete && !artefact.default}">
            <zynap:actionbox id="actions_position">
                <%-- Ensure that delete button is only present in selected arenas and never present for default position which cannot be deleted --%>
                <zynap:actionEntry>
                    <fmt:message key="delete" var="deleteButtonLabel"/>
                    <zynap:artefactForm name="_delete" method="get" action="deleteposition.htm" tabName="activeTab"
                                        buttonMessage="${deleteButtonLabel}" buttonId="btn_deletepos" artefactId="${artefact.id}">
                        <%-- include parameter that indicates if the position has just been added --%>
                        <input type="hidden" name="<%=ControllerConstants.NEW_NODE%>" value="<c:out value="${command.newNode}"/>"/>
                    </zynap:artefactForm>
                </zynap:actionEntry>
            </zynap:actionbox>
        </c:if>
    </c:if>
<%@ include file="positioninfo.jsp" %>
