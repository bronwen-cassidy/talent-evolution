<%@ include file="../../../includes/include.jsp" %>

<zynap:evalBack>
    <zynap:actionbox id="backbox">
        <zynap:actionEntry>
            <fmt:message key="back" var="buttonLabel" />
            <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
        </zynap:actionEntry>
    </zynap:actionbox>
</zynap:evalBack>

<%-- check if you have results --%>
<c:set var="searchPerformed" value="${command.mappedResults != null}"/>
<c:set var="hasResults" value="${searchPerformed && !empty command.mappedResults}"/>

<zynap:tab defaultTab="${command.activeTab}" id="doc_search" url="javascript" tabParamName="activeTab" >

    <fmt:message key="search.criteria" var="criteriaLabel" />
    <zynap:tabName value="${criteriaLabel}" name="criteria"  />

    <%-- if search not performed do not display these labels --%>
    <c:if test="${searchPerformed}">
        <fmt:message key="search.results" var="resultsLabel" />
        <zynap:tabName value="${resultsLabel}" name="results"  />

        <%-- if results found display this tab --%>
        <c:if test="${hasResults}">
            <fmt:message key="browse.results" var="browseResultLabel" />
            <zynap:tabName value="${browseResultLabel}" name="browse" />
        </c:if>
    </c:if>

    <div id="criteria_span" style="display:<c:choose><c:when test="${command.activeTab == 'criteria'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:import url="../common/portfolio/search/documentsearchform.jsp"/>
    </div>

    <%-- if search not performed do not display these tabs --%>
    <c:if test="${searchPerformed}">
        <div id="results_span" style="display:<c:choose><c:when test="${command.activeTab == 'results'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:import url="${results}"/>
        </div>

        <%-- if results found display this tab --%>
        <c:if test="${hasResults}">
            <div id="browse_span" style="display:<c:choose><c:when test="${command.activeTab == 'browse'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
                <%@ include file="browsedocuments.jsp"%>
            </div>
        </c:if>
    </c:if>

</zynap:tab>
