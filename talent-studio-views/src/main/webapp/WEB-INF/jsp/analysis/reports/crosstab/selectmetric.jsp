<%-- include used to select metric for cross tab reports --%>
<tr>
	<td class="infolabel"><fmt:message key="metric"/>&nbsp;:&nbsp;</td>
	<td class="infodata">
		<spring:bind path="command.metricId">
			<select name="<c:out value="${status.expression}"/>">
				<option value="-1" <c:if test="${command.metricId == null}">selected</c:if>><fmt:message key="scalar.operator.count"/></option>
				<c:forEach var="met" items="${command.metrics}">
					<option value="<c:out value="${met.id}"/>" <c:if test="${command.metricId == met.id}">selected</c:if>><c:out value="${met.label}"/></option>
				</c:forEach>
			</select>
		</spring:bind>
	</td>
</tr>