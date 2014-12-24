<%@ include file="../../includes/include.jsp"%>

<c:if test="${numAppraisalReports > 0}">
    <c:set var="archiverepurl" value="runviewarchivedreport.htm" scope="request"/>
    <zynap:actionbox id="${tabContent.key}_aarep">
        <zynap:actionEntry>
            <fmt:message key="archived.reports" var="ablButtonLabel"/>
            <zynap:artefactForm name="${tabContent.key}_apprepfrm" method="get" action="${archiverepurl}" tabName="activeTab" buttonMessage="${ablButtonLabel}" buttonId="btn_${tabContent.key}" artefactId="${artefact.id}" >
                <input type="hidden" name="dc_item" value="<c:out value="${tabContent.id}"/>"/>
            </zynap:artefactForm>
        </zynap:actionEntry>
    </zynap:actionbox>
</c:if>

<fmt:message var="reportMsg" key="appraisal.summary.reports"/>
<c:if test="${empty appraisalReports}">
    <zynap:infobox title="${reportMsg}" id="rpres">
        <div class="noinfo">
            <fmt:message key="no.reports"/>
        </div>
    </zynap:infobox>
</c:if>

<c:forEach var="entry" items="${appraisalReports}" varStatus="repIndexer">
    
    <c:set var="report" value="${entry.key}"/>
    <fmt:message var="localMsg" key="appraisal.summary.report"><fmt:param value="${report.label}"/></fmt:message>
    <zynap:infobox title="${localMsg}" id="apprep">
        <table class="infotable" cellspacing="0" cellpadding="0">
            <c:set var="rows" value="${entry.value}"/>
            <c:forEach var="row" items="${rows}" varStatus="rowIndexer">
                <c:set var="styleClass" value="odd"/>
                <c:if test="${rowIndexer.index % 2 == 0}"><c:set var="styleClass" value="even"/></c:if>
                <c:choose>
                    <c:when test="${rowIndexer.index == 0}">
                        <tr class="<c:out value="${styleClass}"/>">
                            <c:forEach var="cell" items="${row.cells}">
                                <th><c:out value="${cell.displayValue}"/></th>
                            </c:forEach>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr class="<c:out value="${styleClass}"/>">
                            <c:forEach var="cell" items="${row.cells}">
                                <td class="infolabel"><c:out value="${cell.displayValue}"/></td>    
                            </c:forEach>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </table>
    </zynap:infobox>
</c:forEach>