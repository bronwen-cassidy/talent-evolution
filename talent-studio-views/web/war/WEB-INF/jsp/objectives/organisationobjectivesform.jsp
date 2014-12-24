<%@ include file="../includes/include.jsp"%>

<fmt:message key="organisation.goals" var="msg"/>

<zynap:form method="post" name="objsetfrmid" encType="multipart/form-data">
    <zynap:infobox id="objformId" title="${msg}">

        <input type="hidden" id="cancelId" name="" value=""/>
        <input type="hidden" id="deleteIdx" name="deleteIndex" value="-1"/>
        <input type="hidden" id="targetId" name="" value="-1"/>
        <spring:bind path="command.approveObjectives">
            <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>"/>
        </spring:bind>

        <c:set var="organisationUnits" value="${command.nodeInfo.organisationUnits}"/>
        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="organisation.unit.label"/></td>
                <td class="infodata">
                    <c:forEach var="organisationUnit" items="${organisationUnits}">
                        <c:out value="${organisationUnit.label}"/>
                    </c:forEach>
                </td>
            </tr>
        </table>
    </zynap:infobox>
    <fmt:message key="clear" var="clearBtnLabel"/>
    <table class="infotable">
        <tr>
            <td class="infolabel"><fmt:message key="objective.set.label"/>&nbsp;:&nbsp;*</td>
            <spring:bind path="command.objectiveSet.label">
                <td class="infodata">
                    <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <%@include file="../includes/error_message.jsp"%>
                </td>
            </spring:bind>
        </tr>
        <tr>
            <td class="infobox" colspan="2">
                <c:forEach var="objective" items="${command.objectives}" varStatus="objectiveIndexer">
                    <c:set var="objectiveIndex" value="${objectiveIndexer.index}" scope="request"/>
                    <fmt:message key="organisation.objective.goal" var="objectivesMsg">
                        <fmt:param value="${objectiveIndex + 1}"/>
                    </fmt:message>

                    <zynap:infobox id="objectivesBoxId" title="${objectivesMsg}">

                        <spring:bind path="command.objectives[${objectiveIndex}].selectedParentId">
                            <input type="hidden" id="bcpartnerval<c:out value="${objectiveIndex}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        </spring:bind>

                        <table class="infotable" border="0" id="objectiveTbl">
                            <tr>
                                <td class="infolabel"><fmt:message key="select.goal"/>&nbsp;:&nbsp;*</td>
                                <td class="actionButton">
                                    <input type="button" class="popupControl" value="<c:out value="${clearBtnLabel}"/>" onclick="clearObjectiveTextFields('bcpartnerval<c:out value="${objectiveIndex}"/>', 'objdetailstbl<c:out value="${objectiveIndex}"/>', 'btnBlogX<c:out value="${objectiveIndex}"/>', '<c:out value="${objectiveIndex}"/>');"/>
                                </td>
                                <td class="actionButton">
                                    <img class="popupControl" src="../images/popupClose.gif" alt="delete" onclick="removeObjective('<c:out value="${objectiveIndex}"/>');"/>
                                </td>
                            </tr>
                        </table>

                        <table class="infotable" id="objdetailstbl<c:out value="${objectiveIndex}"/>" cellspacing="0" cellpadding="0">
                            <!--parent select -->
                            <tr>
                                <td class="infolabel">&nbsp;</td>
                                <td class="infodata">
                                    <spring:bind path="command.objectives[${objectiveIndex}].parentDesc">
                                        <div class="blogContent" id="btnBlogX<c:out value="${objectiveIndex}"/>" onclick="popupShow('<zynap:message code="click.select.objective" javaScriptEscape="true"/>', this, 'objectiveSelect<c:out value="${objectiveIndex}"/>', 'bcpartnerval<c:out value="${objectiveIndex}"/>');">
                                            <c:out value="${status.value}"/>
                                        </div>
                                        <input type="hidden" id="btnBlogX<c:out value="${objectiveIndex}"/>a" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                        <%@include file="../includes/error_message.jsp"%>
                                    </spring:bind>
                                    <%@ include file="parentobjectiveselect.jsp" %>
                                </td>
                            </tr>

                            <!-- The objective label and description -->
                            <tr>
                                <td class="infolabel"><fmt:message key="title"/>&nbsp;:&nbsp;*</td>
                                <spring:bind path="command.objectives[${objectiveIndex}].label">
                                    <td class="infodata">
                                        <input id="inTextVal<c:out value="${objectiveIndex}"/>" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                        <%@include file="../includes/error_message.jsp"%>
                                    </td>
                                </spring:bind>
                            </tr>
                            
                            <tr>
                                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;*</td>
                                <spring:bind path="command.objectives[${objectiveIndex}].description">
                                    <td class="infodata">
                                        <textarea id="inTextAreaVal<c:out value="${objectiveIndex}"/>" rows="4" cols="35" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                                        <%@include file="../includes/error_message.jsp"%>
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
                <input class="inlinebutton" type="submit" id="finished2" name="_finish" value="<fmt:message key="approve"/>" onclick="setValue('approveObjectives', true);" title="<fmt:message key="helptext.approve"/>"/>
            </td>
        </tr>
    </table>

</zynap:form>