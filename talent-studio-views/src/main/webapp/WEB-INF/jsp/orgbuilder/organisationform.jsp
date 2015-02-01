<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@include file="../includes/orgunitpicker.jsp"%>
<c:set var="hasOuTree" value="${outree != null && !empty outree}"/>

<zynap:infobox title="${title}">

    <zynap:form name="_add" method="post" encType="multipart/form-data">
        <input type="hidden" name="ignore"/>    
        <table class="infotable" cellspacing="0">
            <!-- label -->
            <tr>
                <td class="infolabel">
                    <fmt:message key="generic.name"/>&nbsp;:&nbsp;*
                </td>
                <spring:bind path="command.label">
                    <td class="infodata">                        
                        <input type="text" maxlength="100" class="input_text" name="label" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <!-- Parent organisation -->
            <c:choose>
                <%-- If not the default org unit and the org unit tree has elements display the ou picker --%>
                <c:when test="${!command.default && hasOuTree}">
                    <tr>
                        <td class="infolabel"><fmt:message key="select.parent.organisation"/>&nbsp;:&nbsp;*</td>
                        <spring:bind path="command.parentId">
                            <td class="infodata">
                                <zynap:message code="select.parent.organisation" var="ouMsg" javaScriptEscape="true"/>
                                <fmt:message key="please.select" var="parentMsg"/>
                                <c:set var="parentLabel" value="${command.parentLabel}"/>
                                <c:if test="${command.parentLabel == null}"><c:set value="${parentMsg}" var="parentLabel"/></c:if>

                                <span style="white-space: nowrap;"><input id="oufld4" type="text" class="input_text" value="<c:out value="${parentLabel}"/>" name="parentLabel" readonly="true" /><input type="button" id="pick_ou" class="partnerbutton" <c:if test="${!hasOuTree}">disabled</c:if> value="..." onclick="popupShowTree('<c:out value="${ouMsg}"/>', this, 'ouPicker', 'oufld4', 'oufld5');"/></span>
                                <input id="oufld5" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.parentId}"/>"/>
                                <%@include file="../includes/error_message.jsp" %>
                            </td>
                        </spring:bind>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td class="infolabel"><fmt:message key="parent.organisation"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <c:choose>
                                <c:when test="${command.default}">
                                    <fmt:message key="is.top.organisation"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${command.parentLabel}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>         
            <!-- Comments -->
            <tr>
                <td class="infolabel"><fmt:message key="generic.comments"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.comments">
                    <td class="infodata">
                        <textarea name="comments" rows="4"><c:out value="${status.value}"/></textarea>
                        <%@include file="../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <c:if test="${command.wrappedDynamicAttributes != null}">
                <c:set var="currentFormName" value="_next"/>
                <%@ include file="../common/attributes/artefactDASnippet.jsp" %>
            </c:if>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="submit" name="_cancel" value="<fmt:message key="cancel"/>"/>
                    <input class="inlinebutton" type="submit" name="_add" value="<fmt:message key="save"/>" />
                </td>
            </tr>
        </table>
    </zynap:form>

    <zynap:popup id="calendarPopup">
        <%@ include file="../includes/calendar.jsp" %>
    </zynap:popup>
    
</zynap:infobox>