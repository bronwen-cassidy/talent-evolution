<%@ include file="../../includes/include.jsp" %>

<c:set var="area" value="${model.area}" scope="request"/>

<zynap:actionbox>
	<zynap:actionEntry>
		<fmt:message key="back" var="buttonLabel" />
		<zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
	</zynap:actionEntry>
	<zynap:actionEntry>
		<zynap:form method="get" name="_edit" action="/admin/editarea.htm">
			<input type="hidden" name="<%=ParameterConstants.AREA_ID%>" value="<c:out value="${area.id}"/>"/>
			<zynap:button cssClass="actionbutton" name="editarea" value="Edit" type="button" onclick="javascript:document.forms._edit.submit();"/>
		</zynap:form>
	</zynap:actionEntry>
	<zynap:actionEntry>
		<zynap:form method="get" name="_delete" action="/admin/deletearea.htm">
			<input type="hidden" name="<%=ParameterConstants.AREA_ID%>" value="<c:out value="${area.id}"/>"/>
			<zynap:button cssClass="actionbutton" name="deletearea" value="Delete" type="button" onclick="javascript:document.forms._delete.submit();"/>
		</zynap:form>
	</zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="area.title" var="title" scope="request"/>
<zynap:infobox title="${title}">
	<c:if test="${error}">
		<div class="error"><fmt:message key="area.error"/></div>
	</c:if>

	<table class="infotable" id="areadetails" cellspacing="0">
		<tr>
			<td class="infolabel"><fmt:message key="area.label"/>&nbsp;:&nbsp;</td>
			<td class="infodata"><c:out value="${area.label}"/></td>
		</tr>
        <tr>
            <td class="infolabel"><fmt:message key="area.comments"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${area.comments}"/></zynap:desc></td>
        </tr>
		<tr>
			<td class="infolabel"><fmt:message key="area.active"/>&nbsp;:&nbsp;</td>
			<td class="infodata"><fmt:message key="${area.active}"/><br/></td>
		</tr>

		<tr>
			<td class="infolabel">
				<fmt:message key="organisation.units"/>&nbsp;:&nbsp;
			</td>
			<td class="infodata">
				<c:set var="assignedOrganisationUnits" value="${area.assignedOrganisationUnits}"/>
				<c:if test="${!empty assignedOrganisationUnits}">
					<table class="infotable" id="orgunits" cellspacing="0">
                        <thead>
                            <tr>
                                <th class="infolabel"><fmt:message key="areaelement.label"/></th>
                                <th class="infolabel"><fmt:message key="areaelement.cascading"/></th>
                            </tr>
                        </thead>
                        <c:forEach items="${assignedOrganisationUnits}" var="areaElement">
							<tr>
								<td class="infodata"><c:out value="${areaElement.label}"/></td>
								<td class="infodata"><c:out value="${areaElement.cascading}"/></td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
			</td>
		</tr>

		<tr>
			<td class="infolabel">
				<fmt:message key="positions"/>&nbsp;:&nbsp;
			</td>
			<td class="infodata">
                <c:set var="assignedPositions" value="${area.assignedPositions}"/>
                <c:if test="${!empty assignedPositions}">
					<table class="infotable" id="poss" cellspacing="0">
                        <thead>
                            <tr>
                                <th class="infolabel"><fmt:message key="areaelement.label"/></th>
                                <th class="infolabel"><fmt:message key="area.positions.excluded"/></th>
                            </tr>
                         </thead>
                        <c:forEach items="${assignedPositions}" var="areaElement">
                            <c:if test="${!areaElement.fromPopulation}">
                                <tr>
                                    <td class="infodata"><c:out value="${areaElement.label}"/></td>
                                    <td class="infodata"><fmt:message key="${areaElement.excluded}"/></td>
                                </tr>
                            </c:if>
						</c:forEach>
					</table>
				</c:if>
			</td>
		</tr>

		<tr>
			<td class="infolabel">
				<fmt:message key="subjects"/>&nbsp;:&nbsp;
			</td>
			<td class="infodata">
                <c:set var="assignedSubjects" value="${area.assignedSubjects}"/>
				<c:forEach items="${assignedSubjects}" var="areaElement">
                    <c:if test="${!areaElement.fromPopulation}">
					    <div><c:out value="${areaElement.label}"/></div>
                    </c:if>
				</c:forEach>
			</td>
		</tr>
        <tr>
            <td class="infolabel">
                <fmt:message key="subject.population"/>&nbsp;:&nbsp;
            </td>
            <td class="infodata">
                <c:choose>
                    <c:when test="${model.subjectPopulation != null}"><c:out value="${model.subjectPopulation.label}"/></c:when>
                    <c:otherwise><fmt:message key="none"/></c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
			<td class="infolabel">
				<fmt:message key="position.population"/>&nbsp;:&nbsp;
			</td>
			<td class="infodata">
                <c:choose>
                    <c:when test="${model.positionPopulation != null}"><c:out value="${model.positionPopulation.label}"/></c:when>
                    <c:otherwise><fmt:message key="none"/></c:otherwise>
                </c:choose>
			</td>
		</tr>
	</table>
</zynap:infobox>
