<%@ page import="com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController"%>
<%@ include file="../includes/include.jsp" %>

<c:if test="${!command.editing}">
    <zynap:actionbox id="dc_rep">
        <zynap:actionEntry>
            <zynap:form method="post" action="" name="editDCI">
                <input type="hidden" name="_target3" value="3"/>
                <input type="button" class="actionbutton" id="editDCI3" value="<fmt:message key="edit"/>" onclick="document.forms.editDCI.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
    </zynap:actionbox>
</c:if>

<zynap:infobox id="details" title="${command.displayConfigItem.label}">

<c:if test="${!command.editing}">

    <table class="infotable" id="viewdetails">
        <tr>
            <td class="infolabel"><fmt:message key="current.defined.label"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.displayConfigItem.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="visible"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${command.displayConfigItem.active}"/></td>
        </tr>

        <tr>
            <td class="infoheading"><fmt:message key="access.role"/></td>
            <td class="infoheading"><fmt:message key="groups"/></td>
        </tr>

        <c:set var="userRoles" value="${command.displayConfigItemRoles}"/>
        <c:set var="rolesEmpty" value="${empty userRoles}"/>
        <c:set var="groups" value="${command.displayConfigItemGroups}"/>
        <c:set var="groupsEmpty" value="${empty groups}"/>
        <tr>
            <td class="infodata"><%@include file="../common/viewroles.jsp"%></td>
            <td class="infodata"><%@include file="../common/viewgroups.jsp"%></td>
        </tr>

        <c:if test="${!command.reportConfigItem}">
            <tr>
                <td class="infolabel"><fmt:message key="display.defined.attributes"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><fmt:message key="predefined.area.content"/></td>
            </tr>
        </c:if>
        <c:if test="${command.reportConfigItem}">

            <c:forEach var="report" items="${command.reports}" varStatus="repIndexer">
                <tr>
                    <td class="infoheading" colspan="2"><c:out value="${report.description}"/></td>
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="current.section.label"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><c:out value="${report.label}"/></td>
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="display.defined.attributes"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <table class="infotable" id="viewattrs<c:out value="${repIndexer.index}"/>">
                            <tr>
                                <th class="infolabel"><fmt:message key="display.attribute.value"/></th>
                                <th class="infolabel"><fmt:message key="display.attribute.label"/></th>
                            </tr>

                            <c:forEach var="rcolumn" items="${report.columns}">
                                <c:choose>
                                    <c:when test="${rcolumn.invalid}">
                                        <tr>
                                            <td class="infodata"><span class="error"><fmt:message key="invalid.criteria"/></span></td>
                                            <td class="infodata">
                                                <c:out value="${rcolumn.label}"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td class="infolabel">
                                                <c:out value="${rcolumn.attributeLabel}"/>
                                            </td>
                                            <td class="infodata">
                                                <c:out value="${rcolumn.label}"/>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </table>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
</c:if>

<c:if test="${command.editing}">

    <spring:bind path="command">
        <%@include file="../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form method="post" name="addinfo" action="">

        <table class="infotable" id="viewdetails" cellpadding="0">
            <tr>
                <spring:bind path="command.displayConfigItem.label">
                    <td class="infolabel"><fmt:message key="current.defined.label"/>&nbsp;:&nbsp;*</td>
                    <td class="infodata">
                        <input type="text" id="ifconfig" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../includes/error_messages.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <c:if test="${command.displayConfigItem.hideable}">
                <tr>
                    <td class="infolabel"><fmt:message key="visible"/>&nbsp;:&nbsp;</td>
                    <spring:bind path="command.displayConfigItem.active">
                        <td class="infodata">
                            <input class="input_checkbox" type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.displayConfigItem.active}">checked</c:if>/>
                            <%@ include file="../includes/error_messages.jsp" %>
                        </td>
                    </spring:bind>
                </tr>
            </c:if>

            <c:if test="${command.displayConfigItem.rolesModifiable}">
                <tr>
                    <td class="infoheading"><fmt:message key="access.role"/></td>
                    <td class="infoheading"><fmt:message key="groups"/></td>
                </tr>
                <tr>
                    <td class="infodata">                        
                        <c:import url="../displayconfig/assignrolesform.jsp"/>
                    </td>
                    <td class="infodata">
                        <c:import url="../common/assigngroupsform.jsp"/>
                    </td>
                </tr>
            </c:if>
            <%-- handle the reports may be more than one at this point --%>
            <c:if test="${command.reportConfigItem}">

                <c:forEach var="rep" items="${command.reports}" varStatus="repIndexer" >
                    <tr>
                        <td class="infoheading" colspan="2"><c:out value="${rep.description}"/></td>
                    </tr>
                    <tr>
                        <spring:bind path="command.reports[${repIndexer.index}].label">
                            <td class="infolabel"><fmt:message key="current.section.label"/>&nbsp;:&nbsp;*</td>
                            <td class="infodata">
                                <input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                <%@ include file="../includes/error_messages.jsp" %>
                            </td>
                        </spring:bind>
                    </tr>
                    <tr>
                        <td class="infolabel">
                            <fmt:message key="display.defined.attributes"/>&nbsp;:&nbsp;
                        </td>
                        <td class="infodata">

                            <table class="infotable" id="viewattributes">
                                <tr>
                                    <th class="infolabel"><fmt:message key="display.attribute.value"/></th>
                                    <th class="infolabel"><fmt:message key="display.attribute.label"/></th>
                                    <th class="infolabel">&nbsp;</th>
                                </tr>

                                <c:forEach var="col" items="${rep.columns}" varStatus="indexer">
                                    <tr>

                                        <td class="infodata">
                                            <spring:bind path="command.reports[${repIndexer.index}].columns[${indexer.index}].attribute">
                                                <c:set var="fieldId"><zynap:id><c:out value="${status.expression}"/></zynap:id></c:set>
                                                <c:set var="btnAction">showColumnTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'columnTree', '<c:out value="${fieldId}_label"/>', '<c:out value="${fieldId}"/>', '<c:out value="reports${repIndexer.index}.columns${indexer.index}.label"/>')</c:set>

                                                <%-- determine the correct label --%>
                                                <fmt:message key="please.select" var="label"/>
                                                <c:if test="${col.attributeSet}">
                                                    <c:set var="label" value="${col.attributeDefinition.label}"/>
                                                </c:if>

                                                <span style="white-space: nowrap;"><input id="<c:out value="${fieldId}"/>_label" type="text" class="input_text"
                                                       value="<c:out value="${label}"/>"
                                                       name="<c:out value="${status.expression}_label"/>"
                                                       readonly="true"/><input type="button" class="partnerbutton" value="..." id="navOUPopup" onclick="<c:out value="${btnAction}"/>"/></span>

                                                <input id="<c:out value="${fieldId}"/>" type="hidden" name="<c:out value="${status.expression}"/>"
                                                       value="<c:out value="${status.value}"/>"/>
                                                <%@ include file="../includes/error_messages.jsp" %>
                                            </spring:bind>
                                        </td>

                                        <td class="infodata">
                                            <spring:bind path="command.reports[${repIndexer.index}].columns[${indexer.index}].label">
                                                <c:set var="fieldId"><zynap:id><c:out value="${status.expression}"/></zynap:id></c:set>
                                                <input type="text" maxlength="240" class="input_text" id="<c:out value="${fieldId}"/>"
                                                       name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                                <%@ include file="../includes/error_messages.jsp" %>
                                            </spring:bind>
                                        </td>

                                        <td class="infodata">
                                            <input type="button" name="delColIdx<c:out value="${indexer.index}"/><c:out value="${repIndexer.index}"/>" class="inlinebutton" value="<fmt:message key="delete"/>" onclick="configItemReportColumn('addinfo', 'deleteIndex', '<c:out value="${indexer.index}"/>', 'targetIndex' ,'5', '<c:out value="${repIndexer.index}"/>');"/>
                                        </td>
                                        
                                    </tr>
                                </c:forEach>

                                <tr>
                                    <td class="infodata" colspan="3">
                                        <input class="inlinebutton" type="button" name="addCol" value="<fmt:message key="add"/>" onclick="configItemReportColumn('addinfo', 'addIndex', 'yes', 'targetIndex', '4', '<c:out value="${repIndexer.index}"/>');"/>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>

            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton" colspan="2">
                    <input class="inlinebutton" type="button" name="cancelRes" value="<fmt:message key="cancel"/>" onclick="document.forms.cancelRes.submit();"/>
                    <input class="inlinebutton" type="submit" name="_target6" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>

        <input id="addIndex" type="hidden" name="<%= BaseReportsWizardController.ADD_PREFIX %>" value="-1"/>
        <input id="deleteIndex" type="hidden" name="<%= BaseReportsWizardController.DELETE_PREFIX %>" value="-1"/>
        <input id="targetIndex" type="hidden" name="" value=""/>
        <input id="reportIndex" type="hidden" name="repIndex" value="-1"/>
    </zynap:form>

</c:if>

</zynap:infobox>

<zynap:form method="post" name="cancelRes" action="">
    <input type="hidden" name="_target7" value="7"/>
</zynap:form>














