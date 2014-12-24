<%@ include file="../../includes/include.jsp"%>

<c:if test="${empty progressReports}">
    <span class="noinfo" id="pg_count"><fmt:message key="nothing.found"/></span>   
</c:if>

<c:forEach var="reportWrapper" items="${progressReports}">

    <c:set var="filledReport" value="${reportWrapper.filledReport}"/>
    <c:set var="report" value="${reportWrapper.report}"/>    
    <c:set var="reportId" value="${report.id}"/>
    <div>
        <span>
            <a href="<c:url value="runexportpdfprogressreport.htm"><c:param name="report_id" value="${reportId}"/><c:param name="command.node.id" value="${artefact.id}"/></c:url>" target="_blank"><fmt:message key="export.pdf"/></a>&nbsp;|&nbsp; 
        </span>
        <span>
            <a href="<c:url value="runexportcsvprogressreport.htm"><c:param name="report_id" value="${reportId}"/><c:param name="command.node.id" value="${artefact.id}"/></c:url>" target="_blank"><fmt:message key="export.csv"/></a>
        </span>
    </div>
    <fieldset>
        <legend><c:out value="${report.label}"/></legend>
        <script type="text/javascript">
            $(function()
            {
                $("#abcxr<c:out value="${report.id}"/>").tablesorter({widthFixed: true, widgets: ['zebra']});
            });
        </script>
        <zynap:jasperTabularReportTag jasperPrint="${filledReport.jasperPrint}" viewSubjectUrl=""
                                      viewPositionUrl="" viewQuestionnaireUrl="${viewQuestionnaireUrl}"
                                      report="${report}"/>
    </fieldset>

</c:forEach>