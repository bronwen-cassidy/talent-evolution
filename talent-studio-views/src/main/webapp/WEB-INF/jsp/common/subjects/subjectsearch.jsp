<%@ include file="../../includes/include.jsp" %>

<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:tab defaultTab="${command.activeSearchTab}" tabParamName="activeSearchTab" url="javascript">

    <fmt:message key="search.subject" var="searchTabLabel" />
    <zynap:tabName value="${searchTabLabel}" name="search"/>

    <c:if test="${command.currentNodes != null}">
        <fmt:message key="searchresults.subject" var="searchResultsLabel" />
        <zynap:tabName value="${searchResultsLabel}" name="results"/>
    </c:if>
    <c:if test="${command.nodeWrapper != null}">
        <fmt:message key="browse.results" var="searchDetailsLabel" />
        <zynap:tabName value="${searchDetailsLabel}" name="browse"/>
    </c:if>

    <div id="search_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'search'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:set var="filter" scope="request" value="${command.filter}"/>
        <%@ include file="subjectsearchform.jsp"%>
    </div>


    <c:if test="${command.currentNodes != null}">
        <div id="results_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'results'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:set var="subjects" scope="request" value="${command.currentNodes}"/>
            <c:set var="active" scope="request" value="${command.filter.active}"/>
            <c:url var="viewListPageUrl" scope="request" value="listsubject.htm"/>
            <c:set var="activeSearchTab" scope="request" value="results"/>
            <c:import url="subjects/subjectsearchresults.jsp"/>
        </div>
    </c:if>
    <c:if test="${command.nodeWrapper != null}">
        <div id="browse_span" style="display:<c:choose><c:when test="${command.activeSearchTab == 'browse'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">            
            <c:import url="subjects/browsesubjects.jsp"/>
        </div>
    </c:if>
    

</zynap:tab>
