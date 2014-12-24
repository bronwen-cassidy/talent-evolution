<%@ include file="../includes/include.jsp" %>

<input class="inlinebutton" type="button" id="btn_setAll" name="SetAll" value="<fmt:message key="setall"/>" onClick="checkAll(document.addinfo.accessRoleIds)"/>
<input class="inlinebutton" type="button" id="btn_clrAll" name="ClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.addinfo.accessRoleIds)"/>

<div class="constrained">
<c:forEach var="accessRole" items="${command.accessRoles}">

        <spring:bind path="command.accessRoleIds">            
            <div>
                <input type="checkbox" class="input_checkbox" name="accessRoleIds" value="<c:out value="${accessRole.id}"/>" <c:if test="${accessRole.assigned}">checked</c:if> <c:if test="${accessRole.administrationRole}">disabled</c:if>/>
                &nbsp;
                <c:out value="${accessRole.label}"/>
                <c:if test="${accessRole.administrationRole}">
                    <%-- NB: this hidden field needs to stay for this role as due to be disabled it does not send through the fact that it is checked --%>
                    <input type="hidden" name="accessRoleIds" value="<c:out value="${accessRole.id}"/>"/>
                </c:if>
            </div>
        </spring:bind>

</c:forEach>
</div>