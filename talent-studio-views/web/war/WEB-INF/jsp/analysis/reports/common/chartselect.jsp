<tr>
    <td class="infolabel"><fmt:message key="select.chart.view"/>&nbsp:&nbsp;</td>
    <td class="infodata">
        <input type="hidden" name="activeTab" value="chart"/>
        <input type="hidden" id="barChartTarget" name="_target4" value="4"/>
        <spring:bind path="command.chartType">
            <select name="chartType">
                <c:forEach var="type" items="${chartTypes}">
                    <option value="<c:out value="${type}"/>" <c:if test="${command.chartType == type}">selected</c:if>><fmt:message key="${type}"/></option>
                </c:forEach>
            </select>
        </spring:bind>
    </td>
</tr>
<tr>
    <td class="infobutton"></td>
    <td class="infobutton">
        <input class="inlinebutton" type="button" name="chtOpBut" id="chtOpBut" value="<fmt:message key="bar.chart"/>" onclick="javascript:document.forms.genBarCt.submit();"/>
    </td>
</tr>