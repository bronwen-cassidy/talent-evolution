<%@ include file="../../includes/include.jsp" %>

<dl class="menuDescription">

    <c:forEach var="menuitem" items="${menuitems}" varStatus="status">

        <c:url value="${menuitem.url}" var="menuItemUrl">
            <c:param name="navigator.notSubmit" value="true"></c:param>
            <c:if test="${menuitem.reportMenuItem}">
                <c:param name="id" value="${menuitem.reportId}"/>
                <c:param name="displayConfigKey" value="${menuItem.menuSection.id}"/>
            </c:if>
            <c:if test="${menuitem.reportingChartMenuItem}">
                <c:param name="preferenceId" value="${menuitem.preferenceId}"/>
                <c:param name="displayConfigKey" value="${menuItem.menuSection.id}"/>
            </c:if>
        </c:url>

        <c:set var="miLabel" value="${menuitem.label}"/>
        <c:if test="${not menuitem.reportMenuItem && not menuitem.reportingChartMenuItem}">
            <fmt:message key="${menuitem.label}" var="miLabel"/>
        </c:if>

        <dt>
            <img src="<c:url value="/images/menu/txt.gif"/>">
            &nbsp;            
            <a href="<c:out value="${menuItemUrl}"/>">
                <c:out value="${miLabel}"/>
            </a>
        </dt>

        <dd>
            <c:choose>
                <c:when test="${menuitem.reportMenuItem}">
                    <c:out value="${menuitem.report.description}"/>
                </c:when>
                <c:when test="${menuitem.reportingChartMenuItem}">
                    <c:out value="${menuitem.preference.description}"/>                
                </c:when>
                <c:otherwise>
                    <zynap:arenaMenuItem menuItem="${menuitem}"/>
                </c:otherwise>
            </c:choose>
        </dd>

    </c:forEach>

</dl>