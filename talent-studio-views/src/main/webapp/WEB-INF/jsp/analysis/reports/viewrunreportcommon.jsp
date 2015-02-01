<tr>
    <td class="infolabel"><fmt:message key="report.report"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <select name="reportId">
            <c:forEach var="rep" items="${reports}">
                <option value="<c:out value="${rep.id}"/>" <c:if test="${command.reportId == rep.id}">selected</c:if>>
                    <c:out value="${rep.label}"/>
                </option>
            </c:forEach>
        </select>
    </td>
</tr>
