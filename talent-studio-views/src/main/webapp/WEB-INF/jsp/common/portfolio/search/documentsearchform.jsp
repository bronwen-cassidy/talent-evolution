<%@ include file="../../../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants" %>
<fmt:message key="search.criteria" var="msg"/>

<zynap:infobox title="${msg}" id="criteria">
    <zynap:form name="search" method="post" encType="multipart/form-data">
        <spring:bind path="command">
            <%@include file="../../../includes/error_messages.jsp" %>
        </spring:bind>
        <input type="hidden" name="<%=com.zynap.talentstudio.web.common.ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.node.id}"/>"/>
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infoheading" colspan="2"><fmt:message key="search.parameters"/></td>
            </tr>
            <c:if test="${command.displayDataSources}">
                <tr>
                    <td class="infolabel"><fmt:message key="documentsearch.scope"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <spring:bind path="command.selectedSources">
                            <select name="<c:out value="${status.expression}"/>" onchange="javascript:setTargetAndSubmit('_target2=2','search');">
                                <c:forEach var="datasource" items="${command.dataSources}">
                                    <option value="<c:out value="${datasource.value}"/>" <c:if test="${datasource.selected}">selected</c:if>><fmt:message key="${datasource.messageKey}"/></option>
                                </c:forEach>
                            </select>
                        </spring:bind>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td class="infolabel"><fmt:message key="documentsearch.contenttype"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <input class="inlinebutton" type="button" id="setalltypes" name="CheckAll" value="<fmt:message key="setall"/>" onClick="checkAll(document.search.selectedContent)"/>
                    <input class="inlinebutton" type="button" id="clearalltypes" name="UnCheckAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.search.selectedContent)"/><br/>
                    <spring:bind path="command.selectedContent">
                        <c:forEach var="content" items="${command.contentTypes}">
                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${content.value}"/>" <c:if test="${content.selected}">checked="checked"</c:if>/>&nbsp;<c:out value="${content.messageKey}"/><br/>
                        </c:forEach>
                    </spring:bind>
                </td>
            </tr>
            <%--<tr>--%>
                <%--<td class="infolabel"><fmt:message key="summary"/>&nbsp;:&nbsp;</td>--%>
                <%--<td class="infodata">--%>
                    <%--<spring:bind path="command.summaryType">--%>
                        <%--<select name="<c:out value="${status.expression}"/>">--%>
                            <%--<option value="Concept" <c:if test="${status.value == 'Concept'}">selected</c:if>><fmt:message key="autonomy.concept"/></option>--%>
                            <%--<option value="Context" <c:if test="${status.value == 'Context'}">selected</c:if>><fmt:message key="autonomy.context"/></option>--%>
                            <%--<option value="Quick" <c:if test="${status.value == 'Quick'}">selected</c:if>><fmt:message key="autonomy.quick"/></option>--%>
                            <%--<option value="Off" <c:if test="${status.value == 'Off'}">selected</c:if>><fmt:message key="autonomy.off"/></option>--%>
                        <%--</select>--%>
                    <%--</spring:bind>--%>
                <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td class="infolabel"><fmt:message key="threshold"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.threashold">
                        <input type="text" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="max.num.results"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.maxResults">
                        <input type="text" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <%-- Check for portfolio items before including headers, buttons, etc --%>
            <%--<c:if test="${command.notEmpty}">--%>
                <!--<tr>-->
                    <%--<td class="infoheading" colspan="2"><fmt:message key="documentsearch.requirements"/></td>--%>
                <!--</tr>-->
                <!--<tr>-->
                    <%--<td class="infolabel"><fmt:message key="portfolio.items"/>&nbsp;:&nbsp;</td>--%>
                    <!--<td class="infodata">-->
                        <%--<input class="inlinebutton" type="button" id="setallitems" name="CheckAll" value="<fmt:message key="setall"/>" onClick="checkAll(selectedItems)"/>&nbsp;--%>
                        <%--<input class="inlinebutton" type="button" id="clearallitems" name="UnCheckAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(selectedItems)"/>--%>
                        <!--<br/>-->
                        <%--<spring:bind path="command.selectedItems">--%>

                                <%--<c:forEach var="pItem" items="${command.portfolioItems}">--%>
                                    <%--<input type="checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${pItem.value}"/>" <c:if test="${pItem.selected}">checked="checked"</c:if>/>&nbsp;--%>
                                    <%--<c:out value="${pItem.name}"/>--%>
                                    <!--<br/>-->
                                <%--</c:forEach>--%>
                            <%--<%@ include file="../../../includes/error_message.jsp" %>--%>
                       <%--</spring:bind>--%>

                    <!--</td>-->
                <!--</tr>-->
            <%--</c:if>--%>
            <tr>
                <td class="infolabel">
                    <%-- mark free text as mandatory if there are no portfolio items --%>
                    <fmt:message key="free.text"/>&nbsp;:&nbsp;*                    
                </td>
                <td class="infodata">
                    <spring:bind path="command.queryText">
                        <textarea id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" rows="4"
                            onkeyup="CheckFieldLength(<c:out value="${status.expression}"/>, 'charcount', 'remaining', 8000);"
                            onkeydown="CheckFieldLength(<c:out value="${status.expression}"/>, 'charcount', 'remaining', 8000);"
                            onmouseout="CheckFieldLength(<c:out value="${status.expression}"/>, 'charcount', 'remaining', 8000);"><c:out value="${status.value}"/></textarea>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td></td>
                <td class="infolabel">
                    <span id="charcount">0</span> <fmt:message key="characters"/> | <span id="remaining">8000</span> <fmt:message key="remaining"/>
                </td>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="submit" name="_target1" value="<fmt:message key="search"/>" onclick="disableButton(this); setTargetAndSubmit('_target1=1','search')"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>


