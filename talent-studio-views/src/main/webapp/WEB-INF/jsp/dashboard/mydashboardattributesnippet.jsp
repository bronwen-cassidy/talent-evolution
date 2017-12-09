<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<span class="column col-3">
    <div class="infomessage"><fmt:message key="please.select.x.axis.attribute"/></div>
    <select name="attribute-x-select" id="x-attrs">
        <option value="-1"><fmt:message key="please.select"/></option>
        <c:forEach var="attrs" items="${attributes}">
            <option value="<c:out value="${attr.id}"/>"><c:out value="${attr.label}"/></option>
        </c:forEach>                       
     </select>
</span>
<span class="column col-3">
    <div class="infomessage"><fmt:message key="please.select.y.axis.attribute"/></div>	
    <select name="attribute-y-select" id="y-attrs">
        <option value="-1"><fmt:message key="please.select"/></option>
        <c:forEach var="attrs" items="${attributes}">
            <option value="<c:out value="${attr.id}"/>"><c:out value="${attr.label}"/></option>
        </c:forEach>
     </select>
</span>