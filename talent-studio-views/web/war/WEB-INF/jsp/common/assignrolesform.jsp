<%@ include file="../includes/include.jsp" %>

<input class="inlinebutton" type="button" id="btn_setAll" name="SetAll" value="<fmt:message key="setall"/>" onClick="checkAll(document.addinfo.roleIds)"/>
<input class="inlinebutton" type="button" id="btn_clrAll" name="ClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.addinfo.roleIds)"/>
<div class="constrained">
	<c:forEach var="role" items="${command.roles}">
        <spring:bind path="command.roleIds">
            <div>
                <input type="checkbox" class="input_checkbox" name="roleIds" value="<c:out value="${role.value.id}"/>" <c:if test="${role.selected}">checked</c:if>/>
                &nbsp;
                <c:out value="${role.value.label}"/>
            </div>
        </spring:bind>
	</c:forEach>
</div>