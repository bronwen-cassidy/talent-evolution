<%@ include file="../../../includes/include.jsp" %>

<c:set var="styleClassName" value="verticalOptions" scope="request"/>
<c:if test="${showHorizontal}">
    <c:set var="styleClassName" value="horizontalOptions" scope="request"/>
</c:if>


<c:if test="${question.type == 'TEXT' || question.type == 'INTEGER' || question.type == 'POSITIVEINTEGER'}">
    <input type="text" name="ignore" value="" <c:out value="${titleAttr}" escapeXml="false"/> <c:if test="${question.length != 0}">size="<c:out value="${question.length}"/>"</c:if> readonly="true"/>
</c:if>

<c:if test="${question.type == 'TEXTAREA' || question.type == 'TEXTBOX'}">
    <textarea <c:if test="${question.length != 0}">style="display:block; width:<c:out value="${question.length}"/>em;"</c:if> name="ignore" rows="4" <c:if test="${question.length != 0}">cols="<c:out value="${question.length}"/>"</c:if> <c:out value="${titleAttr}" escapeXml="false"/> readonly="true"></textArea>
</c:if>

<c:if test="${question.type == 'DATE'}">
    <span style="white-space: nowrap;"><input class="input_date" name="ignore" type="text" readonly="true" <c:out value="${titleAttr}" escapeXml="false"/>
            /><input type="button" class="partnerbutton" value="..." disabled="true"/></span>
</c:if>

<c:if test="${question.type == 'SELECT' || question.type == 'STRUCT'}">
    <select name="ig" <c:out value="${titleAttr}" escapeXml="false"/>>
        <c:if test="${!question.hasBlank}">
            <c:choose>
                <c:when test="${question.attributeDefinition.mandatory}"><option value=""><fmt:message key="please.select"/></option></c:when>
                <c:otherwise><option value=""></option></c:otherwise>
            </c:choose>
        </c:if>
        <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">
            <option value="<c:out value="${vals.id}"/>">
                <c:if test="${!vals.blank}"><c:out value="${vals.label}"/></c:if>
            </option>
        </c:forEach>
    </select>
</c:if>

<c:if test="${question.type == 'IMG' || question.type == 'IMAGE'}">
    <input type="file" class="input_file" name="ignore" <c:out value="${titleAttr}" escapeXml="false"/> disabled="true"/>
</c:if>

<c:if test="${question.type == 'RADIO'}">
    <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">
        <span class="<c:out value="${styleClassName}"/>"><input <c:out value="${titleAttr}" escapeXml="false"/>
                type="radio" class="input_radio"
                name="ignore" readonly="true" disabled="true"
                ><c:out value="${vals.label}"/></span>
    </c:forEach>
</c:if>

<c:if test="${question.type == 'CHECKBOX'}">
    <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">
        <span class="<c:out value="${styleClassName}"/>"><input
                type="checkbox"
                class="input_checkbox"
                name="ignore"
                value="<c:out value="${vals.id}"/>"
                <c:out value="${titleAttr}" escapeXml="false"/>
                disabled="true"/><c:out value="${vals.label}"/></span>
    </c:forEach>
</c:if>

<c:if test="${question.type == 'MULTISELECT'}">
    <select name="ig" <c:out value="${titleAttr}" escapeXml="false"/> multiple size="<c:out value="${question.rowCount}"/>">
        <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">
            <option value="<c:out value="${vals.id}"/>"><c:out value="${vals.label}"/></option>
        </c:forEach>
    </select>
</c:if>

<c:if test="${question.type == 'ORGANISATION' || question.type == 'SUBJECT' || question.type == 'POSITION'}">
    <span style="white-space: nowrap;"><input type="text" class="input_text"
            name="ignore"
            readonly="true"/><input type="button"
            class="partnerbutton"
            value="..."
            <c:out value="${titleAttr}" escapeXml="false"/> disabled="true"/>
    </span>
</c:if>

<c:if test="${question.type == 'COMMENTS'}">
    <div class="blogContent" id="blogX<c:out value="${index}"/>"><p>&nbsp;</p></div>
    <input type="button" id="btnBlogX<c:out value="${index}"/>" class="partnerbutton" value="..." disabled="true"/>
</c:if>
