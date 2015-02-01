<%@ include file="../../includes/include.jsp"%>

    <%-- set item url and portfolio items - used in viewportfolioitems.jsp --%>
    <c:set var="itemUrl" value="viewpositionportfolioitem.htm" scope="request"/>
    <c:set var="portfolioItems" value="${artefact.portfolioItems}" scope="request"/>
    <c:set var="additemaction" value="editpositionaddportfolioitem.htm" scope="request"/>
    <c:set var="searchitemsaction" value="subjectdocumentsearch.htm" scope="request"/>

    <c:import url="../portfolio/items/viewportfolioitems.jsp"/>


