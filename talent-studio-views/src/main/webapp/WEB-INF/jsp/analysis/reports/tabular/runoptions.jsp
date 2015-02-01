<%-- include for common run options for tabular reports --%>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController,
                 com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean"%>

    <%--  legend  hidden fields --%>
    <input id="popupIdPrefix" type="hidden" name="prefix" value="_agr"/>
    <input type="hidden" id="colx1" name="<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>" value="-1"/>
    <input type="hidden" id="pgTarget" name="pgTarget" value=""/>
    <input id="actTbElId" type="hidden" name="activeTab" value="results"/>
    <input id="recountid" type="hidden" name="recount" value="true"/>

    <%@ include file="../../reportpopulationselect.jsp"%>
    <c:if test="${command.hasGroups}">
        <tr>
            <td class="infolabel"><fmt:message key="report.group.order.by"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <table class="infotable" cellpadding="0" cellspacing="0">
                    <c:forEach var="groupAttr" items="${command.groups}" varStatus="grpCounter">
                        <tr>
                            <td class="infolabel"><c:out value="${groupAttr.label}"/>&nbsp;&nbsp;:</td>
                            <td class="infodata">
                                <spring:bind path="command.groups[${grpCounter.index}].sortOrder">
                                    <select id="groupSortAttrId" name="<c:out value="${status.expression}"/>">
                                        <option value="<%= RunReportWrapperBean.ASCENDING %>" <c:if test="${groupAttr.sortOrder == 1}">selected</c:if>><fmt:message key="asc"/></option>
                                        <option value="<%= RunReportWrapperBean.DESCENDING %>" <c:if test="${groupAttr.sortOrder == -1}">selected</c:if>><fmt:message key="desc"/></option>
                                    </select>
                                </spring:bind>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </c:if>
    <tr>
        <td class="infolabel"><fmt:message key="report.order.by"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <spring:bind path="command.orderBy">
                <select id="ob2" name="<c:out value="${status.expression}"/>">                    
                    <c:forEach var="col" items="${command.orderableColumns}">
                        <option value="<c:out value="${col.attribute}"/>" <c:if test="${command.orderBy == col.attribute}">selected</c:if>><c:out value="${col.label}"/></option>
                    </c:forEach>
                </select>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="report.sort.order"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <spring:bind path="command.sortOrder">
                <select id="so2" name="<c:out value="${status.expression}"/>">
                    <option value="<%= RunReportWrapperBean.ASCENDING %>" <c:if test="${command.sortOrder == 1}">selected</c:if>><fmt:message key="asc"/></option>
                    <option value="<%= RunReportWrapperBean.DESCENDING %>" <c:if test="${command.sortOrder == -1}">selected</c:if>><fmt:message key="desc"/></option>
                </select>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <td class="infobutton">&nbsp;</td>
        <td class="infobutton">
            <input type="hidden" name="_redisplay" value="_redisplay"/>
            <%-- Lets change to the run tab here and put a new div there.--%>
            <input class="inlinebutton" type="submit" name="_target0" value="<fmt:message key="run"/>" onclick="tabLoading('runoptions_span', 'loading');"/>
            <input class="inlinebutton" type="button" name="export" value="<fmt:message key="csv.export"/>" onclick="populateCsvFields();"/>
            <input class="inlinebutton" type="button" name="exportpdf" value="<fmt:message key="report.pdf.export"/>" onclick="populateReportPdfFields();"/>
        </td>
    </tr>
