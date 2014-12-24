<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%-- Title used for boxes --%>
<fmt:message key="confirm.merge" var="boxtitle"/>

<c:choose>
    <c:when test="${error == null}">
        <zynap:infobox title="${boxtitle}">

            <zynap:form method="post" name="_merge">
                <table class="pager" cellspacing="0">
                    <thead>
                        <tr>
                            <th class="sorted"><fmt:message key="organisation.unit.label"/></th>
                            <th class="sorted"><fmt:message key="sub.organisation.units"/></th>
                            <th class="sorted"><fmt:message key="associated.positions"/></th>
                        </tr>
                    </thead>
                    <!-- display for the two selected org units -->
                    <c:forEach var="ou" items="${command.mergedFrom}">
                        <tr>
                            <td class="pager">
                                <c:out value="${ou.label}"/>
                            </td>
                            <td class="pager">
                                <c:forEach var="childOU" items="${ou.children}">
                                    <c:out value="${childOU.label}"/><br/>
                                </c:forEach>
                            </td>
                            <td class="pager">
                                <c:forEach var="position" items="${ou.positions}">
                                    <c:out value="${position.label}"/><br/>
                                </c:forEach>
                            </td>
                            <input type="hidden" name="c_id" value="<c:out value="${ou.id}"/>"/>
                        </tr>
                    </c:forEach>
                </table>

                <br/>

                <table class="infotable" cellspacing="0">
                    <!-- parent organisation unit is not editable -->
                    <tr>
                        <td class="infolabel"><fmt:message key="parent.organisation"/>&nbsp;:&nbsp;</td>
                        <td class="infodata" colspan="2"><c:out value="${command.parentLabel}"/></td>
                    </tr>
                    <tr>
                        <td class="infolabel"><fmt:message key="new.label"/>&nbsp;:&nbsp;*</td>
                        <spring:bind path="command.label">
                            <td class="infodata" colspan="2">
                                <input type="text" maxlength="100" class="input_text" name="label" value="<c:out value="${status.value}"/>"/>
                                <%@ include file="../includes/error_message.jsp" %>
                        </td>
                        </spring:bind>
                    </tr>
                    <!-- Comments -->
                    <tr>
                        <td class="infolabel"><fmt:message key="generic.comments"/>&nbsp;:&nbsp;</td>
                        <spring:bind path="command.comments">
                            <td class="infodata">
                                <textarea name="comments" rows="4"><c:out value="${status.value}"/></textarea>
                                <%@ include file="../includes/error_message.jsp" %>
                            </td>
                        </spring:bind>
                    </tr>

                    <tr>
                        <td class="infobutton"></td>
                        <td class="infobutton">
                            <fmt:message key="cancel" var="buttonLabel" />
                            <input type="submit" class="inlinebutton" name="_cancel" value="<fmt:message key="cancel"/>"/>
                            <input type="submit" class="inlinebutton" name="_merge" value="<fmt:message key="save"/>"/>
                        </td>
                    </tr>
                </table>
            </zynap:form>
        </zynap:infobox>
    </c:when>
    <c:otherwise>

        <zynap:actionbox>
            <zynap:actionEntry>
                <fmt:message key="back" var="buttonLabel" />
                <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
            </zynap:actionEntry>
        </zynap:actionbox>

        <zynap:infobox title="${boxtitle}">
            <div class="error"><fmt:message key="${error}"/></div>
        </zynap:infobox>

    </c:otherwise>
</c:choose>
