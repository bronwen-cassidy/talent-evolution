<%-- include to display columns of cross tab reports --%>
<tr>
    <td class="infodata" colspan="2">
        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infolabel">
                    <fmt:message key="attribute"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:out value="${horizontalColumn.attributeLabel}"/>
                </td>
                <td class="infolabel">
                    <fmt:message key="horizontal"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:out value="${horizontalColumn.label}"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel">
                    <fmt:message key="attribute"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:out value="${verticalColumn.attributeLabel}"/>
                </td>
                <td class="infolabel">
                    <fmt:message key="vertical"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:out value="${verticalColumn.label}"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="report.display.as"/> &nbsp;:&nbsp;</td>
    <td class="infodata">
        
        <c:choose>
            <c:when test="${command.report.displayOption==0}">
                <fmt:message key="crosstab.format.discreet"/>
            </c:when>
            <c:when test="${command.report.displayOption==1}">
                <fmt:message key="crosstab.format.percentage"/>
            </c:when>
            <c:when test="${command.report.displayOption==2}">
                <fmt:message key="crosstab.format.artefacts"/>
            </c:when>
        </c:choose>
       
    </td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="number.decimal.places"/> &nbsp;:&nbsp;</td>
    <td class="infodata">
        <c:out value="${command.report.decimalPlaces}"/>
    </td>
</tr>