<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:set var="artefact" value="${command.nodeWrapper}" scope="request"/>

<c:choose>
    <c:when test="${allowDelete}">
        <%-- Ensure that delete button is only present in selected arenas --%>
        <zynap:actionbox id="actions_subject">
            <zynap:actionEntry>
                <fmt:message key="back" var="buttonLabel" />
                <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
            </zynap:actionEntry>

            <zynap:actionEntry>
                 <fmt:message key="delete" var="deleteButtonLabel"/>
                 <zynap:artefactForm name="_delete" method="get" action="deletesubject.htm" tabName="activeTab" buttonMessage="${deleteButtonLabel}" buttonId="btn_deletesubject" artefactId="${artefact.id}">
                     <%-- include parameter that indicates if the subject has just been added --%>
                     <input type="hidden" name="<%=ControllerConstants.NEW_NODE%>" value="<c:out value="${command.newNode}"/>"/>
                 </zynap:artefactForm>
            </zynap:actionEntry>
        </zynap:actionbox>
    </c:when>

    <c:otherwise>
        <zynap:evalBack>
            <zynap:actionbox id="actions_subject">
                <zynap:actionEntry>
                    <fmt:message key="back" var="buttonLabel" />
                    <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
                </zynap:actionEntry>
            </zynap:actionbox>
        </zynap:evalBack>
    </c:otherwise>
</c:choose>

<c:import url="../common/subjects/subjectinfo.jsp"/>
