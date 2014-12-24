<%@ include file="../../includes/include.jsp"%>

<c:set var="hasValue" value="${question.value != null && question.value != ''}"/>

<c:choose>
    <c:when test="${hasValue}">
       <c:choose>
          
            <c:when test="${question.type == 'STRUCT' || question.type == 'SELECT'  || question.type == 'RADIO'}">
                <c:forEach var="vals" items="${question.activeLookupValues}">
                    <c:if test="${question.value == vals.id}">
                        <c:if test="${!vals.blank}"><c:out value="${vals.label}"/></c:if>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:when test="${question.type == 'LINK'}">
                <a id="link_<c:out value="${question.id}"/>" href="<c:out value="${question.value}"/>" target="_blank"><c:out value="${question.value}"/></a>
            </c:when>
            <c:when test="${question.type == 'TEXTAREA' || question.type == 'TEXTBOX'}">
                <zynap:desc><c:out value="${question.displayValue}"/></zynap:desc>
            </c:when>
            <c:when test="${question.type == 'COMMENTS'}">
                <c:forEach var="commentBean" items="${question.blogComments}">
                    <p>
                        <i>[<c:out value="${commentBean.displayDate}"/>]&nbsp;<c:out value="${commentBean.addedBy.label}"/>&nbsp;<fmt:message key="generic.wrote"/></i>:
                        <br/>
                        <c:out value="${commentBean.comment}"/>
                    </p>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:out value="${question.displayValue}"/>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${question.mandatory}">
                <span class="error"><fmt:message key="dynamicattribute.missing.value"/></span>
            </c:when>
            <c:otherwise>
                <c:if test="${question.type == 'IMG' || question.type == 'IMAGE'}">
                    <fmt:message key="no.image.available"/>
                </c:if>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
