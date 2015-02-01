<%@ include file="../../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="backlr" method="get" action="listappraisalreports.htm">
           <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="document.forms.backlr.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="editar" method="get" action="editappraisalreport.htm">
            <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${model.report.id}"/>"/>
            <input class="actionbutton" type="button" value="<fmt:message key="edit"/>" onclick="document.forms.editar.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">
    <table class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="metric.label"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${model.report.label}"/></td>
        </tr>        
         <tr>
            <td class="infolabel"><fmt:message key="report.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${model.report.description}"/></zynap:desc></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="scalar.operator"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${model.report.operator}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="appraisal"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${model.report.performanceReview.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="report.columns"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <table id="reportcolumninfo" class="infotable" cellspacing="0">
                    <tr>
                        <th class="sorted"><fmt:message key="report.weighting"/></th>
                        <th class="sorted"><fmt:message key="column.label"/></th>
                    </tr>

                    <c:forEach var="column" items="${model.report.columns}" varStatus="indexer">
                        <tr>
                            <td class="infodata"><c:out value="${column.weighting}"/></td>
                            <td class="infodata"><c:out value="${column.label}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </table>
</zynap:infobox>