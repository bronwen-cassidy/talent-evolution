<%@ include file="../includes/include.jsp" %>

<input class="inlinebutton" type="button" id="btn_setGpAll" name="GpSetAll" value="<fmt:message key="setall"/>" onClick="checkAll(document.addinfo.groupIds)"/>
<input class="inlinebutton" type="button" id="btn_clrGpAll" name="GpClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.addinfo.groupIds)"/>
<div class="constrained">
    <c:forEach var="group" items="${command.groups}">
        <spring:bind path="command.groupIds">
            <div>
                <input type="checkbox" class="input_checkbox" name="groupIds" value="<c:out value="${group.value.id}"/>" <c:if test="${group.selected}">checked</c:if>/>
                &nbsp;
                <c:out value="${group.value.label}"/>
            </div>
        </spring:bind>
    </c:forEach>
</div>