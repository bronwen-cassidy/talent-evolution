<%@ include file="../../../includes/include.jsp"%>
<%@ page import="com.zynap.talentstudio.web.common.ControllerConstants"%>
<%-- include to display results of tabular reports --%>

<c:if test="${resultset.page.numRecords <= 25}"><span class="pagebanner"><fmt:message key="found.items"><fmt:param value="${resultset.page.numRecords}"/></fmt:message></span></c:if>
<c:if test="${resultset.page.numRecords > 25}">
    <span class="pagebanner" id="pg_count">
        <fmt:message key="found.some.items">
            <fmt:param value="${resultset.page.numRecords}"/>
            <fmt:param value="${resultset.page.start + 1}"/>
            <fmt:param value="${resultset.page.next}"/>
        </fmt:message>
    </span>
    <!--display clickable pages to submit to the server binding in the selected start index -->
    <form name="rerunreport" method="post">
        <input type="hidden" name="_target<c:out value="${targetPage}"/>" value="<c:out value="${targetPage}"/>"/>
        <input id="pgStartId" type="hidden" name="pageStart" value="0"/>
        <input id="actTbId" type="hidden" name="activeTab" value="results"/>
        <span class="pagelinks">
            <c:set var="num" value="1"/>
            <c:forEach var="pg" begin="0" end="${resultset.page.numRecords - 1}" step="25">
                <c:choose>
                    <c:when test="${resultset.page.start == pg}">
                        <strong><c:out value="${num}"/></strong>&nbsp;&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:handleReportPageSubmit('pgStartId', '<c:out value="${pg}"/>', 'rerunreport');" onclick="tabLoading('results_span', 'loading');"><c:out value="${num}"/></a>&nbsp;&nbsp;
                    </c:otherwise>
                </c:choose>
                <c:set var="num" value="${num + 1}"/>
            </c:forEach>
        </span>
    </form>
</c:if>

<c:if test="${!lockDown}">
    <zynap:artefactLink var="viewSubjectUrl" url="viewsubject.htm" tabName="activeTab" activeTab="results" >
        <c:if test="${resultset.displayConfigArena != null}">
            <zynap:param name="<%=ControllerConstants.DISPLAY_CONFIG_KEY%>" value="${resultset.displayConfigArena}"/>
        </c:if>
    </zynap:artefactLink>
        
    <zynap:artefactLink var="viewPositionUrl" url="viewposition.htm" tabName="activeTab" activeTab="results">
        <c:if test="${resultset.displayConfigArena != null}">
            <zynap:param name="<%=ControllerConstants.DISPLAY_CONFIG_KEY%>" value="${resultset.displayConfigArena}"/>
        </c:if>
    </zynap:artefactLink>

    <zynap:artefactLink var="viewQuestionnaireUrl" url="viewsubjectquestionnaire.htm" tabName="activeTab" activeTab="results">
        <c:if test="${resultset.displayConfigArena != null}">
            <zynap:param name="<%=ControllerConstants.DISPLAY_CONFIG_KEY%>" value="${resultset.displayConfigArena}"/>
        </c:if>
    </zynap:artefactLink>
</c:if>
<c:if test="${lockDown}">
    <zynap:artefactLink var="viewQuestionnaireUrl" url="viewmyquestionnaire.htm" tabName="activeTab" activeTab="results">
        <c:if test="${resultset.displayConfigArena != null}">
            <zynap:param name="<%=ControllerConstants.DISPLAY_CONFIG_KEY%>" value="${resultset.displayConfigArena}"/>
        </c:if>
    </zynap:artefactLink>
</c:if>

<zynap:jasperTabularReportTag jasperPrint="${jasperPrint}" viewSubjectUrl="${viewSubjectUrl}" viewPositionUrl="${viewPositionUrl}" viewQuestionnaireUrl="${viewQuestionnaireUrl}" report="${resultset.report}" />