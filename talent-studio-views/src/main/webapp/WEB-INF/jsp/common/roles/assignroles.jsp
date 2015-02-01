<%@ include file="../../includes/include.jsp" %>

<input class="inlinebutton" type="button" id="btn_setAll" name="SetAll" value="<fmt:message key="setall"/>" onClick="checkAll(document.addinfo.accessRoleIds)"/>
<input class="inlinebutton" type="button" id="btn_clrAll" name="ClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.addinfo.accessRoleIds)"/>

<c:set var="notSuperUser" value="${!superUser}"/>

<c:forEach var="accessRole" items="${command.accessRoles}">

    <c:set var="skip" value="${accessRole.homeRole}"/>

    <c:if test="${!skip}">

        <c:set var="shouldHideAdminRole" value="${notSuperUser && accessRole.adminRole}"/>
        
        <spring:bind path="command.accessRoleIds">
            <c:choose>
                <%-- the person logged in is not an administrator therefore connat manipulate and isAdmin = true role --%>
                <c:when test="${shouldHideAdminRole}">
                    <c:if test="${accessRole.assigned}">
                        <%-- in order not to lose any hidden assigned roles we need to pass through the id to the request --%>
                        <input type="hidden" name="accessRoleIds" value="<c:out value="${accessRole.id}"/>"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div>                        
                        <input type="checkbox" class="input_checkbox" name="accessRoleIds" value="<c:out value="${accessRole.id}"/>" <c:if test="${accessRole.assigned}">checked</c:if>/>
                        &nbsp;
                        <c:out value="${accessRole.label}"/>
                    </div>    
                </c:otherwise>
            </c:choose>
        </spring:bind>        
    </c:if>

</c:forEach>
