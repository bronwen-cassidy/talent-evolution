<%@ include file="../../includes/include.jsp" %>

<c:choose>
    <c:when test="${homePage != null}">
        <c:choose>
            <c:when test="${homePage.dashboard}">
                <c:set var="tabContent" value="${displayConfigView.dashboardView}" scope="request"/>
                <c:url var="questionnaireUrl" value="viewmyquestionnaire.htm" scope="request"/>
                <table class="artefact">
                    <tr>
                        <td class="artefact">
                            <c:set var="dashboardHeading" value="${tabContent.label}" scope="request"/>
                            <c:set var="dashboards" value="${dashboards}" scope="request"/>
                            <a href="dashboards/editmydashboard.htm">
                                <fmt:message key="add.series.chart"/>
                            </a>
                            <c:import url="../common/subjects/viewsubjectdashboard.jsp"/>
                        </td>
                    </tr>
                </table>
            </c:when>
            <c:when test="${homePage.hasData}">
                <c:choose>
                    <c:when test="${homePage.velocityTemplate}">
                        <c:url var="urla" value="${arenaContext}/homepage.htm"/>
                        <c:out value="${velocityContent}" escapeXml="false"/>
                    </c:when>
                    <c:otherwise>
                        <c:url var="urla" value="${arenaContext}/homepage.htm"/>
                        <c:out value="${homePage.content}" escapeXml="false"/>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${homePage.internalUrl}">
                <c:url var="urla" value="${homePage.url}"/>
                <script type="text/javascript">
                $(function() {
                    $.get('<c:out value="${urla}"/>', function(data) {
                            $('#homepgId1').html(data);
                    });
                });
                </script>
                <div id="homepgId1" class="homepage">

                </div>
            </c:when>
            <c:when test="${homePage.hasUrl}">
                <c:url var="urla" value="${homePage.url}"/>
                <div id="homepgeId" class="homepage" style="height:100%;">
                    <iframe class="serverPopup"  src="<c:out value="${urla}"/>" style="width:100%; height:100%;"></iframe>
                </div>
            </c:when>
            <c:otherwise>
                <zynap:arenaHomePageText/>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <zynap:arenaHomePageText/>
    </c:otherwise>
</c:choose>