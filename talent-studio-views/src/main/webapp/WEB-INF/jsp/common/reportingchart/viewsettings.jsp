<%@ page import="com.zynap.talentstudio.web.reportingchart.settings.ChartSettingsMultiController" %>
<%@ include file="../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_back" method="post" action="listchartsettings.htm">
            <input class="actionbutton" type="button" value="<fmt:message key="back"/>" onclick="document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="_run" method="get" action="reportingchart.htm">
            <input type="hidden" name="<%=ChartSettingsMultiController.PREFERENCE_ID%>" value="<c:out value="${model.bean.id}"/>"/>
            <input class="actionbutton" type="button" name="_run" value="<fmt:message key="run"/>" onclick="document.forms._run.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="_edit" method="get" action="editchartsettings.htm">
            <input type="hidden" name="<%=ChartSettingsMultiController.PREFERENCE_ID%>" value="<c:out value="${model.bean.id}"/>"/>
            <input class="actionbutton" type="button" value="<fmt:message key="edit"/>" onclick="document.forms._edit.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="_delete" method="get" action="deletechartsettings.htm">
            <input type="hidden" name="<%=ChartSettingsMultiController.PREFERENCE_ID%>" value="<c:out value="${model.bean.id}"/>"/>
            <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" onclick="document.forms._delete.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="_reset" method="get" action="resetchartsettings.htm">
            <input type="hidden" name="<%=ChartSettingsMultiController.PREFERENCE_ID%>" value="<c:out value="${model.bean.id}"/>"/>
            <input class="actionbutton" type="button" value="<fmt:message key="chart.restore.default"/>" onclick="document.forms._reset.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:saveUrl/>

<zynap:infobox title="${title}">
<table id="allDetails" width="100%">
<tr>
    <td colspan="2">
        <fieldset>
            <legend><fmt:message key="chart.details"/></legend>
            <table id="chartDetail" class="infotable">
                <tr>
                    <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><c:out value="${model.bean.viewName}"/></td>
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><zynap:desc><c:out value="${model.bean.description}"/></zynap:desc></td>
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="apply.security"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><fmt:message key="${model.bean.secure}"/></td>
                </tr>
                <tr>
                    <td class="infolabel">
                        <fmt:message key="assigned.arenas"/>&nbsp;:&nbsp;
                    </td>
                    <td class="infodata">
                        <c:forEach var="assignedArena" items="${model.bean.assignedArenas}">
                            <div><c:out value="${assignedArena.label}"/></div>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td class="infolabel">
                        <fmt:message key="assigned.groups"/>&nbsp;:&nbsp;
                    </td>
                    <td class="infodata">
                        <c:forEach var="assignedGroup" items="${model.bean.assignedGroups}">
                            <div><c:out value="${assignedGroup.label}"/></div>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </fieldset>
    </td>
</tr>

<tr valign="top">
    <td>
        <!-- the orgunit table settings section -->
        <fieldset>
            <legend><fmt:message key="organisation.unit"/></legend>
            <table id="orgUnit" class="infotable">
                <tr>
                    <td class="infolabel"><fmt:message key="organisation.unit.label"/>:</td>
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${model.bean.ouShow}">
                                <div class="OrgChartColoured" style="color:<c:out value="${model.bean.ouFgColor}"/>; background-color:<c:out value="${model.bean.ouBgColor}"/>;">
                                    <fmt:message key="org.unit.name"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="not.displayed"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </fieldset>
    </td>
    <td>
        <!-- the position associations settings section -->
        <fieldset>
            <legend><fmt:message key="position.association.lines"/></legend>
            <table id="positionLinks" class="infotable" cellspacing="0">
                <tr>
                    <td class="infolabel"><fmt:message key="primary"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${model.bean.positionPrimaryAssociationShow}">
                                <hr style="border:none;height:<c:out value="${model.bean.positionPrimaryAssociationLine}"/>;background-color:<c:out value="${model.bean.positionPrimaryAssociationColor}"/>;color:<c:out value="${model.bean.positionPrimaryAssociationColor}"/>"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="not.displayed"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                    <%-- commented out as reporting structure does not support position to position secondary associations
                                        <tr>
                                            <td class="infolabel">Secondary&nbsp;:&nbsp;</td>
                                            <td class="infodata">
                                                <c:choose>
                                                    <c:when test="${model.bean.positionSecondaryAssociationShow}">
                                                        <hr style="border:none;height:<c:out value="${model.bean.positionSecondaryAssociationLine}"/>;background-color:<c:out value="${model.bean.positionSecondaryAssociationColor}"/>;color:<c:out value="${model.bean.positionSecondaryAssociationColor}"/>"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        Not displayed.
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                    --%>
            </table>
        </fieldset>
    </td>
</tr>
<tr valign="top">
<td>
    <fieldset>
        <legend><fmt:message key="positions"/></legend>
        <table id="position" class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel">
                    <fmt:message key="background.colour.attribute"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:choose>
                        <c:when test="${model.bean.selectedPositionSelectionAttribute == 'none' || model.bean.selectedPositionSelectionAttribute == 'title'}">
                            <fmt:message key="chart.none"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${model.bean.selectedPositionSelectionAttributeLabel}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td class="infolabel">
                    <fmt:message key="background.colour"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:forEach var="val" items="${model.bean.positionSelectedValues}">
                        <div class="OrgChartColoured"
                             style="color:<c:out value="${val.fgColor}"/>; background-color:<c:out value="${val.bgColor}"/>;">
                            <c:choose>
                                <c:when test="${empty val.expectedValue || val.expectedValue == 'fixed color' || val.expectedValue == 'default'}">
                                    <fmt:message key="chart.default"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${val.displayValue}"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td class="infolabel">
                    <fmt:message key="displayed.attributes"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:if test="${empty model.bean.currentDisplayPositionAttributes}"><fmt:message key="chart.none"/></c:if>
                    <c:forEach var="types2" items="${model.bean.currentDisplayPositionAttributes}">
                        <c:if test="${types2.displayable}">
                            <div><c:out value="${types2.displayValue}"/></div>
                        </c:if>
                    </c:forEach>
                </td>
            </tr>
        </table>
    </fieldset>
</td>

<!-- subject display options -->
<td>
    <fieldset>
        <legend><fmt:message key="subjects"/></legend>
        <table id="subjects" class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="primary"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <c:choose>
                        <c:when test="${model.bean.subjectPrimaryAssociationsShow}">
                            <c:forEach var="primary" items="${model.bean.subjectPrimaryAttributes}">
                                <c:if test="${primary.displayable}">
                                    <div class="OrgChartColoured"
                                         style="color:<c:out value="${primary.fgColor}"/>; background-color:<c:out value="${primary.bgColor}"/>;">
                                        <c:choose>
                                            <c:when test="${primary.default}">
                                                <fmt:message key="chart.default"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${primary.displayValue}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="not.displayed"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="secondary"/> &nbsp;:&nbsp;</td>
                <td class="infodata">
                    <c:choose>
                        <c:when test="${model.bean.subjectSecondaryAssociationsShow}">
                            <c:forEach var="sec" items="${model.bean.subjectSecondaryAttributes}">
                                <c:if test="${sec.displayable}">
                                    <div class="OrgChartColoured"
                                         style="color:<c:out value="${sec.fgColor}"/>; background-color:<c:out value="${sec.bgColor}"/>;">
                                        <c:choose>
                                            <c:when test="${sec.default}">
                                                <fmt:message key="chart.default"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${sec.displayValue}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="not.displayed"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>

            <c:if test="${model.bean.subjectPrimaryAssociationsShow || model.bean.subjectSecondaryAssociationsShow}">
                <tr>
                    <td class="infolabel">
                        <fmt:message key="displayed.attributes"/>&nbsp;:&nbsp;
                    </td>
                    <td class="infodata">
                        <c:if test="${empty model.bean.currentDisplaySubjectAttributes}"><fmt:message key="chart.none"/></c:if>
                        <c:forEach var="pref" items="${model.bean.currentDisplaySubjectAttributes}">
                            <c:if test="${pref.attributeName != 'coreDetail.firstName' && pref.attributeName != 'coreDetail.secondName'}">
                                <div>
                                    <c:out value="${pref.displayValue}"/>
                                </div>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:if>
        </table>
    </fieldset>
  </td>
 </tr>
 </table>
</zynap:infobox>
