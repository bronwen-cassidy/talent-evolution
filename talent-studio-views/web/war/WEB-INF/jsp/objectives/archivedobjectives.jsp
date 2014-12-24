

<c:if test="${not empty command.archivedObjectiveSets}">
    <zynap:form method="post" name="archObjSetFrm">
        <input type="hidden" id="archObjSetTarget" name="hiddObjFrmField" value="-1"/>
        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="objectives"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <select name="objectiveSetId" onchange="submitFormWithTarget('archObjSetFrm', '4', 'archObjSetTarget')">
                        <c:forEach var="archivedSet" items="${command.archivedObjectiveSets}">
                            <option value="<c:out value="${archivedSet.id}"/>" <c:if test="${command.objectiveSet.id == archivedSet.id}">selected</c:if>><c:out value="${archivedSet.label}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
    </zynap:form>
</c:if>