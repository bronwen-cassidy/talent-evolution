<%@ include file="../includes/include.jsp"%>

    <tr>
        <td class="infobox" colspan="2">
            <c:forEach var="objective" items="${command.objectives}" varStatus="objectiveIndexer">

                <c:set var="objectiveIndex" value="${objectiveIndexer.index}" scope="request"/>
                <fmt:message key="staff.objective.goal" var="objectivesMsg">
                    <fmt:param value="${objectiveIndex + 1}"/>
                </fmt:message>

                <zynap:infobox id="objectivesBoxId" title="${objectivesMsg}">

                    <spring:bind path="command.objectives[${objectiveIndex}].selectedParentId">
                        <input type="hidden" id="bcpartnerval<c:out value="${objectiveIndex}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    </spring:bind>

                    <table class="infotable" border="0" id="objectiveTbl">
                        <tr>
                            <td class="infolabel"><fmt:message key="select.goal"/>&nbsp;:&nbsp;*</td>
                            <td class="infobutton"><img class="popupControl" src="../images/popupClose.gif" alt="delete" onclick="removeObjective('<c:out value="${objectiveIndex}"/>');" align="right"/></td>
                        </tr>
                    </table>

                    <table class="infotable" id="objdetailstbl" cellspacing="0" cellpadding="0">
                        <tr>
                            <td class="infolabel">&nbsp;</td>
                            <td class="infodata">
                                <spring:bind path="command.objectives[${objectiveIndex}].parentDesc">
                                    <div class="blogContent" id="btnBlogX<c:out value="${objectiveIndex}"/>" onclick="popupShow('<zynap:message code="click.select.objective" javaScriptEscape="true"/>', this, 'objectiveSelect<c:out value="${objectiveIndex}"/>', 'bcpartnerval<c:out value="${objectiveIndex}"/>');">
                                        <c:out value="${status.value}"/>    
                                    </div>
                                    <input type="hidden" id="btnBlogX<c:out value="${objectiveIndex}"/>a" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                    <%@include file="../includes/error_message.jsp"%>
                                    <c:if test="${objective.invalid}">
                                        <div class="error">
                                            <fmt:message key="objective.invalid"/>
                                        </div>
                                    </c:if>
                                </spring:bind>
                                <%@ include file="parentobjectiveselect.jsp" %>
                            </td>
                        </tr>
                        <!-- The objective label and description -->
                        <tr>
                            <td class="infolabel"><fmt:message key="title"/>&nbsp;:&nbsp;*</td>
                            <spring:bind path="command.objectives[${objectiveIndex}].label">
                                <td class="infodata">
                                    <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                    <%@include file="../includes/error_message.jsp"%>
                                </td>
                            </spring:bind>
                        </tr>
                        <tr>
                            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;*</td>
                            <spring:bind path="command.objectives[${objectiveIndex}].description">
                                <td class="infodata">
                                    <textarea rows="4" cols="65" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                                    <%@include file="../includes/error_message.jsp"%>
                                </td>
                            </spring:bind>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <table class="infotable" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <c:forEach var="heading" items="${objective.wrappedDynamicAttributes}" varStatus="headingIndex">
                                            <td class="infolabel"><c:out value="${heading.label}"/></td>
                                        </c:forEach>
                                    </tr>
                                    <tr>
                                        <c:forEach var="attr" items="${objective.wrappedDynamicAttributes}" varStatus="attrIndex">
                                            <c:set var="fieldId" value="attrDA${attr.id}${objectiveIndex}" scope="request"/>
                                            <c:set var="headingId" value="heading${attr.id}" scope="request"/>
                                            <c:set var="index" value="${attrIndex.index}" scope="request"/>
                                            <c:set var="prefix" value="command.objectives[${objectiveIndex}].wrappedDynamicAttributes[${index}]"/>
                                            <c:set var="numberfunc" scope="request">updateNumberField('<c:out value="${headingId}"/>', '<c:out value="${fieldId}"/>');</c:set>
                                            <%@include file="../common/attributes/attributesnippet.jsp"%>
                                        </c:forEach>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="infoheading" colspan="2"><fmt:message key="staff.manager.comments"/></td>
                        </tr>
                        <tr>
                            <td class="infolabel">&nbsp;</td>
                            <td class="infodata">
                                <spring:bind path="command.objectives[${objectiveIndex}].managerComments">
                                    <c:choose>
                                        <c:when test="${command.personnalObjectives}">
                                            <div class="blogContent" id="mngReadCommX<c:out value="${objectiveIndex}"/>">
                                                <c:out value="${status.value}"/>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <textarea rows="4" cols="65" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                                        </c:otherwise>
                                    </c:choose>
                                </spring:bind>
                            </td>
                        </tr>
                    </table>

                </zynap:infobox>
            </c:forEach>
            <input class="inlinebutton" type="button" name="addObjBtn" value="<fmt:message key="add"/>" onclick="addObjective();"/>

            <spring:bind path="command.approveObjectives">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>"/>
            </spring:bind>
            <spring:bind path="command.sendReview">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>"/>
            </spring:bind>
            <input type="hidden" id="isFinish" name="shouldFinish" value="x"/>

        </td>
    </tr>
    <tr>
        <td class="infobutton" colspan="2" align="center">
            <span><fmt:message key="send.email"/></span>
            <span>
                <spring:bind path="command.sendEmail">
                    <input type="checkbox" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" <c:if test="${status.value}">checked="true"</c:if> />
                </spring:bind>
            </span>
        </td>
    </tr>
    <tr>
        <td class="infobutton" colspan="2" align="center">

            <input class="inlinebutton" type="button" id="nextCancel" name="_cancel" value="<fmt:message key="cancel"/>" onclick="submitCancel();"/>
            <input class="inlinebutton" type="submit" id="finished" name="_finish" value="<fmt:message key="save"/>"/>

            <c:choose>
                <c:when test="${!command.personnalObjectives}">
                    <input class="inlinebutton" type="button" id="fin1" name="reqRev" value="<fmt:message key="request.review"/>"
                           onclick="setValue('sendReview', true); setNameAndSubmit('objsetfrmid', 'isFinish', '_finish');"
                           title="<fmt:message key="helptext.request.review"/>"/>

                    <input class="inlinebutton" type="button" id="fin2" name="appObj" value="<fmt:message key="approve"/>"
                           onclick="setValue('approveObjectives', true); setNameAndSubmit('objsetfrmid', 'isFinish', '_finish');"
                           title="<fmt:message key="helptext.approve"/>"/>
                </c:when>
                <c:otherwise>                    
                    <input class="inlinebutton" type="button" id="manReview" name="subManRev" value="<fmt:message key="submit.manager"/>"
                           onclick="setValue('sendReview', true); setNameAndSubmit('objsetfrmid', 'isFinish', '_finish');" />
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
