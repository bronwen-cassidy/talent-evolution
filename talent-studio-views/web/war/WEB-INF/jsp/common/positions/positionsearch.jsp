<%@ include file="../../includes/include.jsp" %>

<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:tab defaultTab="${command.activeSearchTab}" tabParamName="activeSearchTab" url="javascript">

    <fmt:message key="search.position" var="searchTabLabel" />
    <zynap:tabName value="${searchTabLabel}" name="search"/>

    <c:if test="${command.currentNodes != null}">
        <fmt:message key="searchresults.position" var="searchResultsLabel" />
        <zynap:tabName value="${searchResultsLabel}" name="results"/>
    </c:if>

    <c:if test="${command.nodeWrapper != null}">
        <fmt:message key="browse.results" var="searchDetailsLabel" />
        <zynap:tabName value="${searchDetailsLabel}" name="browse"/>
    </c:if>

    <div id="search_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'search'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:set var="filter" scope="request" value="${command.filter}"/>
        <%@ include file="positionsearchform.jsp"%>
    </div>

    <c:if test="${command.currentNodes != null}">
        <div id="results_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'results'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
           <c:set var="positions" scope="request" value="${command.currentNodes}"/>
           <c:set var="active" scope="request" value="${command.filter.active}"/>
            <%@ include file="positionsearchresults.jsp" %>
        </div>
    </c:if>

    <c:if test="${command.nodeWrapper != null}">
        <div id="browse_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'browse'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:import url="positions/browsepositions.jsp"/>
        </div>
    </c:if>
    

</zynap:tab>
