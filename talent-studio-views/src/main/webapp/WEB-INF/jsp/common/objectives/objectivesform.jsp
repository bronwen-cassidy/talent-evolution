<%@ include file="../../includes/include.jsp"%>    
<table class="infotable">
    <tr>
        <td class="infobox" colspan="2">
            <c:forEach var="objective" items="${command.objectives}" varStatus="objectiveIndexer">
                <c:set var="objectiveIndex" value="${objectiveIndexer.index}" scope="request"/>
                <fmt:message key="corporate.objective.goal" var="objectivesMsg">
                    <fmt:param value="${objectiveIndex + 1}"/>
                </fmt:message>

                <zynap:infobox id="objectivesBoxId" title="${objectivesMsg}">
                    <table class="infotable" id="objectiveTbl">
                        <!-- The objective label and description -->
                        <tr>
                            <td class="infolabel"><fmt:message key="title"/>&nbsp;:&nbsp;*</td>
                            <spring:bind path="command.objectives[${objectiveIndex}].label">
                                <td class="infodata">
                                    <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                    <%@include file="../../includes/error_message.jsp"%>
                                </td>
                            </spring:bind>
                            <td class="infobutton"><img class="popupControl" src="../images/popupClose.gif" alt="delete" onclick="removeObjective('<c:out value="${objectiveIndex}"/>');" align="right"/></td>
                        </tr>
                        <tr>
                            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;*</td>
                            <spring:bind path="command.objectives[${objectiveIndex}].description">
                                <td class="infodata">
                                    <textarea rows="4" cols="35" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                                    <%@include file="../../includes/error_message.jsp"%>
                                </td>
                            </spring:bind>
                        </tr>
                    </table>                    
                </zynap:infobox>
            </c:forEach>
            <input class="inlinebutton" type="button" name="addObjBtn" value="<fmt:message key="add"/>" onclick="addObjective();"/>
        </td>
    </tr>
    <tr>
        <td class="infobutton" colspan="2" align="center">
            <input class="inlinebutton" type="button" id="nextCancel" name="_cancel" value="<fmt:message key="cancel"/>" onclick="submitCancel();"/>
            <input class="inlinebutton" type="submit" id="finished" name="_finish" value="<fmt:message key="save"/>"/>
        </td>
    </tr>
</table>