<zynap:window elementId="legendPicker">
        <table class="infotable">
            <c:forEach var="columnDisplayImage" items="${selectedColumn.columnDisplayImages}" varStatus="colourcount" >
            <tr>
                <c:set var="displayImageLabel" value="${columnDisplayImage.lookupValue.label}"/>

                <td class="infodata" width="1%">
                    <img class="colorcell" src="<c:url value="/images/report/tabular/${columnDisplayImage.displayImage}.gif"/>"
							 alt="<c:out value="${displayImageLabel}"/>"/>
                </td>
                <td class="infodata" width="99%"><c:out value="${displayImageLabel}"/></td>
            </tr>
            </c:forEach>
            <tr>
                <td class="infodata" width="1%">
                    <img class="colorcell" src="<c:url value="/images/report/tabular/FFFFFF.gif"/>" alt="<fmt:message key="legend.other"/>"/>
                </td>
                <td class="infodata" width="99%"><fmt:message key="legend.other"/></td>
            </tr>
        </table>
</zynap:window>
<%-- selected colour is set by the refdata in RunReportController --%>
<script defer="defer" type="text/javascript">
    popupShowColourLegend('<zynap:message code="legend"/><zynap:message text="${selectedColumn.label}" javaScriptEscape="true"/>', '<c:out value="${prefix}"/>', '<c:out value="${selectedColumnIndex}"/>', '<c:out value="legendPicker"/>');
</script>
