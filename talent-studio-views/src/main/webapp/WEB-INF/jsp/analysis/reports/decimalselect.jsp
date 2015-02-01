<tr>
    <td class="infolabel"><fmt:message key="number.decimal.places"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <spring:bind path="command.decimalPlaces">
            <select name="<c:out value="${status.expression}"/>">
                <option value="-1" <c:if test="${command.decimalPlaces == -1}">selected</c:if>><fmt:message key="decimal.default"/></option>
                <c:forEach var="index" begin="0" end="10">
                    <option value="<c:out value="${index}"/>" <c:if test="${status.value == index}">selected</c:if>><c:out value="${index}"/></option>
                </c:forEach>
            </select>
        </spring:bind>
    </td>
</tr>