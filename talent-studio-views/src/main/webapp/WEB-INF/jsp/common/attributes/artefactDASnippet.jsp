<%@ page import="com.zynap.talentstudio.web.common.ControllerConstants"%>
<input id="deleteImage" type="hidden" name="<%=ControllerConstants.DELETE_IMAGE_INDEX%>"/>

<c:forEach var="attr" items="${command.wrappedDynamicAttributes}" varStatus="countVar" >
    <c:set var="index" value="${countVar.index}"/>
    <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}]"/>
    <c:set var="fieldId" scope="request"><zynap:id><c:out value="${attr.label}"/>_x<c:out value="${countVar.index}"/></zynap:id></c:set>
    <tr>
        <td class="infolabel"><c:out value="${attr.label}"/>&nbsp;:&nbsp;<c:if test="${attr.mandatory && !modeSearch}">*</c:if></td>
        <%@ include file="attributesnippet.jsp"%>
    </tr>
</c:forEach>
