<%@ page import="com.zynap.talentstudio.web.organisation.BrowseNodeWrapper"%>
<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:set var="artefact" value="${command.nodeWrapper}"/>
<c:set var="display" value="${allowDelete && !artefact.default}"/>

<c:choose>
    <c:when test="${display}">
        <%-- Ensure that delete button is only present in selected arenas and never present for default position which cannot be deleted --%>
        <zynap:actionbox id="backbox1">
            <zynap:actionEntry>
                <fmt:message key="back" var="buttonLabel" />
                <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
            </zynap:actionEntry>

            <zynap:actionEntry>
                 <fmt:message key="delete" var="deleteButtonLabel"/>
                 <zynap:artefactForm name="_delete" method="get" action="deleteposition.htm" tabName="activeTab" buttonMessage="${deleteButtonLabel}" buttonId="btn_deletepos" artefactId="${artefact.id}">
                     <%-- include parameter that indicates if the position has just been added --%>
                     <input type="hidden" name="<%=ControllerConstants.NEW_NODE%>" value="<c:out value="${command.newNode}"/>"/>
                 </zynap:artefactForm>
            </zynap:actionEntry>
        </zynap:actionbox>
    </c:when>

    <c:otherwise>
        <zynap:evalBack>
            <zynap:actionbox id="backbox2">
                <zynap:actionEntry>
                    <fmt:message key="back" var="buttonLabel" />
                    <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
                </zynap:actionEntry>
            </zynap:actionbox>
        </zynap:evalBack>
    </c:otherwise>
</c:choose>

<%@ include file="positioninfo.jsp"%>
