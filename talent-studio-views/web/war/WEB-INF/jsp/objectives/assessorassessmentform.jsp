<%@ include file="../includes/include.jsp"%>

<fmt:message key="objective.assessment.for" var="msg">
    <fmt:param value="${command.subject.label}"/>
</fmt:message>

<c:url value="worklisteditassessment.htm" var="formUrl"><c:param name="objective_id" value="${command.objective.id}"/></c:url>

<zynap:infobox title="${msg}">
    <zynap:form action="${formUrl}" method="post" name="assessorfrm">
        <table class="infotable" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th class="infolabel"><fmt:message key="goal.review"/></th>
                    <th class="infolabel"><fmt:message key="your.comments"/></th>
                    <th class="infolabel"><fmt:message key="your.rating"/></th>
                </tr>
            </thead>
            <tr>
                <td class="infodata">
                    <div class="comments">
                        <zynap:desc><c:out value="${command.objective.description}"/></zynap:desc>
                    </div>
                    <input type="hidden" id="canId" name="changeMe" value="-1"/>
                </td>
                <td class="infodata">
                    <spring:bind path="command.selfComment">
                        <textarea rows="17" cols="24" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                    </spring:bind>
                </td>
                <td class="infodata">
                    <spring:bind path="command.selfRating">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${command.selfRating == null}">selected="true"</c:if>>&nbsp;</option>
                            <c:forEach var="rating" items="${command.ratingValues}">
                                <option value="<c:out value="${rating.id}"/>" <c:if test="${command.selfRating == rating.id}">selected="true"</c:if>><c:out value="${rating.label}"/></option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="actionButton"></td>
                <td class="actionButton">
                    <input type="button" name="_cancel" id="cancelBtn" value="<fmt:message key="cancel"/>" onclick="setNameAndSubmit('assessorfrm', 'canId', '_cancel');"/>&nbsp;
                    <input type="submit" name="_finish" id="finBtn" value="<fmt:message key="save"/>"/></td>
                <td class="actionButton"></td>
            </tr>
        </table>        
    </zynap:form>
</zynap:infobox>