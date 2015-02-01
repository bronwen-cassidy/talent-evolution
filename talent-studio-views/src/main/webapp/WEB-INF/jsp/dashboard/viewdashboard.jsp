<%@ include file="../includes/include.jsp" %>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>

    <%
        // todo needs deletion - DELETE ME -
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache, must-revalidate, max-age=0");
        response.addIntHeader("Expires", -1);
    %>

    <%@ include file="../includes/stylesheets.jsp" %>
    <script src="<c:url value="/scripts/functions.js"/>" type="text/javascript"></script>

</head>

<body>

    <h1><fmt:message key="dashboard.heading"/></h1>

    <div id="loading_span"/>

    <table id="dashctId" class="infotable" cellpadding="0" cellspacing="0">

        <c:forEach var="item" items="${dashboardItems}" varStatus="dashboardCounter">
            <c:if test="${dashboardCounter.index % 2 == 0}"><tr></c:if>
                <td class="chartarea">
                    <div class="infoarea"><c:out value="${item.reportWrapper.label}"/></div>
                    <c:set var="producer" value="${item.producer}" scope="request"/>
                    <c:set var="labelProcessor" value="${item.labelProcessor}" scope="request"/>
                    <c:set var="percentProcessor" value="${item.percentLabelProcessor}" scope="request"/>
                    
                    <%@include file="../analysis/reports/common/charts.jsp" %>
                </td>

            <c:if test="${dashboardCounter.index % 2 != 0}"></tr></c:if>
        </c:forEach>

    </table>

</body>
</html>