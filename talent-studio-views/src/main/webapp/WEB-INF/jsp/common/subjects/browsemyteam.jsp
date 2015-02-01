<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:tab defaultTab="${command.activeSearchTab}" tabParamName="activeSearchTab" url="javascript">

    <c:if test="${command.currentNodes != null}">
        <fmt:message key="subject.team" var="searchResultsLabel" />
        <zynap:tabName value="${searchResultsLabel}" name="search"/>
    </c:if>
    <c:if test="${command.nodeWrapper != null}">
        <fmt:message key="browse.results" var="searchDetailsLabel" />
        <zynap:tabName value="${searchDetailsLabel}" name="browse"/>
    </c:if>

    <c:if test="${command.currentNodes != null}">
        <div id="search_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'search'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:set var="subjects" scope="request" value="${command.currentNodes}"/>
            <c:url var="viewListPageUrl" scope="request" value="viewmyteam.htm"/>
            <c:set var="activeSearchTab" scope="request" value="search"/>
            <c:set var="hasCustomColumn" value="${command.hasCustomColumn}" scope="request"/>
            <c:set var="managerUserId" value="${command.userId}" scope="request"/>
            <c:set var ="attrLabel" value="${command.attributeLabel}" scope="request"/>
            <c:import url="subjects/subjectsearchresults.jsp"/>
        </div>
    </c:if>
    <c:if test="${command.nodeWrapper != null}">
        <div id="browse_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'browse'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">            
            <c:import url="subjects/browsesubjects.jsp"/>
        </div>
    </c:if>
</zynap:tab>
