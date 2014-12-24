<%@ include file="../../includes/include.jsp"%>

<zynap:actionbox>
    <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message var="reportMsg" key="appraisal.summary.reports"/>
<zynap:infobox title="${reportMsg}" id="rpres">

<c:if test="${empty command.reports}">
    <div class="infomessage">
        <fmt:message key="no.reports"/>
    </div>
</c:if>

<c:if test="${not empty command.reports}">
    <table class="infotable" cellpadding="0" cellspacing="0">
        <tr>
            <td class="infodata">
                <span><fmt:message key="select.appraisal.report"/>&nbsp;:&nbsp;</span>
                <span>
                <zynap:form method="post" name="archapprep">
                    <select name="id" onchange="document.forms['archapprep'].submit();">
                        <c:forEach var="rep" items="${command.reports}">
                            <option value="<c:out value="${rep.id}"/>" <c:if test="${command.selectedReportId == rep.id}">selected</c:if>><c:out value="${rep.label}"/></option>
                        </c:forEach>
                    </select>
                </zynap:form>
                </span>
            </td>
        </tr>
    </table>
</c:if>

<c:if test="${empty command.results}">
    <div class="infomessage">
        <fmt:message key="no.appraisal.answers"/>
    </div>
</c:if>
<br/><br/>
<c:forEach var="entry" items="${command.results}" varStatus="repIndexer">
    
    <c:set var="report" value="${entry.key}"/>
    <fmt:message var="localMsg" key="appraisal.summary.report"><fmt:param value="${report.label}"/></fmt:message>

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
</c:forEach>

</zynap:infobox>