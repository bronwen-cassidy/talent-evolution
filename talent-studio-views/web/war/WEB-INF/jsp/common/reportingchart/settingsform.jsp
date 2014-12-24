<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_update" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table id="allDetails" class="infotable">

            <tr valign="top">
                <td>
                    <!-- the orgunit table settings section -->
                    <fieldset>
                        <legend><fmt:message key="organisation.unit"/></legend>
                        <table id="orgUnit" class="infotable">
                            <tr>
                                <td class="infolabel"><fmt:message key="display.organisation.unit"/>&nbsp;:&nbsp;</td>
                                <spring:bind path="command.ouShow">
                                    <td class="infodata">
                                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.ouShow}">checked</c:if>/>
                                    </td>
                                </spring:bind>
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
                                <td class="infolabel"><fmt:message key="display.primary"/>&nbsp;:&nbsp;</td>
                                <spring:bind path="command.positionPrimaryAssociationShow">
                                    <td class="infodata">
                                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.positionPrimaryAssociationShow}">checked</c:if>/>
                                    </td>
                                </spring:bind>
                            </tr>
<%-- commented out as reporting structure does not support position to position secondary associations
                            <tr>
                                <td class="infolabel">Display secondary&nbsp;:&nbsp;</td>
                                <spring:bind path="command.positionSecondaryAssociationShow">
                                    <td class="infodata">
                                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.positionSecondaryAssociationShow}">checked</c:if>/>
                                    </td>
                                </spring:bind>
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
                        <table id="position" class="infotable">
                            <tr>
                                <td class="infolabel"><fmt:message key="background.colour.attribute"/> &nbsp;:&nbsp;</td>
                                <spring:bind path="command.selectedPositionSelectionAttribute">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="-12:default:default" <c:if test="${command.selectedPositionSelectionAttribute == null || command.selectedPositionSelectionAttribute == 'default'}">selected</c:if>><fmt:message key="chart.none"/></option>
                                            <c:forEach var="types" items="${command.positionSelectionAttributes}">
                                                <option value="<c:out value="${types.key}:${types.attributeName}:${types.displayValue}"/>" <c:if test="${types.attributeName == command.selectedPositionSelectionAttribute}">selected</c:if>><c:out value="${types.displayValue}"/></option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </spring:bind>
                            </tr>
                            <tr>
                                <td class="infolabel"><fmt:message key="display.attributes"/>&nbsp;:&nbsp;</td>
                                <td class="infodata">
                                    <c:forEach var="types" items="${command.displayPositionAttributes}" varStatus="selected">
                                        <c:if test="${types.attributeName != 'primaryAssociation' && types.attributeName != 'secondaryAssociations' && types.attributeName != 'title' && types.attributeName != 'default'}">
                                            <div style="white-space:nowrap">
                                                <input type="checkbox" class="input_checkbox" name="selectedPositionAttributes" value="<c:out value="${types.attributeName}:${types.displayName}"/>" <c:if test="${types.displayable}">checked</c:if>/>
                                                &nbsp;
                                                <c:out value="${types.displayName}"/>
                                            </div>
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
                                <spring:bind path="command.subjectPrimaryAssociationsShow">
                                    <td class="infolabel">Display Primary&nbsp;:&nbsp;</td>
                                    <td class="infodata">
                                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.subjectPrimaryAssociationsShow}">checked</c:if>/>
                                    </td>
                                </spring:bind>
                            </tr>
                            <tr>
                                <td class="infolabel"><fmt:message key="primary.types.display"/> &nbsp;:&nbsp;</td>
                                <td class="infodata">
                                    <spring:bind path="command.selectedSubjectPrimaryValues">
                                        <c:forEach var="primary" items="${command.subjectPrimaryValues}">
                                            <div style="white-space:nowrap">
                                                <input type="checkbox" class="input_checkbox" name="selectedSubjectPrimaryValues" value="<c:out value="${primary.attributeName}:${primary.displayValue}"/>" <c:if test="${primary.displayable}">checked</c:if>/>
                                                &nbsp;
                                                <c:out value="${primary.displayValue}"/>
                                            </div>
                                        </c:forEach>
                                    </spring:bind>
                                </td>
                            </tr>

                            <tr>
                                <spring:bind path="command.subjectSecondaryAssociationsShow">
                                    <td class="infolabel"><fmt:message key="display.secondary"/> &nbsp;:&nbsp;</td>
                                    <td class="infodata">
                                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.subjectSecondaryAssociationsShow}">checked</c:if>/>
                                    </td>
                                </spring:bind>
                            </tr>
                            <tr>
                                <td class="infolabel"><fmt:message key="secondary.types.display"/>&nbsp;:&nbsp;</td>
                                <td class="infodata">
                                    <spring:bind path="command.selectedSubjectSecondaryValues">
                                        <c:forEach var="sec" items="${command.subjectSecondaryValues}">
                                            <div style="white-space:nowrap">
                                                <input type="checkbox" class="input_checkbox" name="selectedSubjectSecondaryValues" value="<c:out value="${sec.attributeName}:${sec.displayValue}"/>" <c:if test="${sec.displayable}">checked</c:if>/>
                                                &nbsp;
                                                <c:out value="${sec.displayValue}"/>
                                            </div>
                                        </c:forEach>
                                    </spring:bind>
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="infolabel"><fmt:message key="display.attributes"/> &nbsp;:&nbsp;</td>
                                <td class="infodata">
                                    <c:forEach var="pref" items="${command.displaySubjectAttributes}">
                                        <c:if test="${pref.attributeName != 'coreDetail.firstName' && pref.attributeName != 'coreDetail.secondName'}">
                                            <div style="white-space:nowrap">
                                                <input type="checkbox" class="input_checkbox" name="selectedSubjectAttributes" value="<c:out value="${pref.attributeName}:${pref.displayValue}"/>" <c:if test="${pref.displayable}">checked</c:if>/>
                                                &nbsp;<c:out value="${pref.displayValue}"/>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        <tr>
            <td class="infobutton" colspan="2">
                <input class="inlinebutton" type="submit" name="_cancel" value="<fmt:message key="cancel"/>"/>
                <input class="inlinebutton" type="button" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('_update', 'pgTarget', '0', 'backId');"/>
                <input class="inlinebutton" name="_target2" type="submit" value="<fmt:message key="wizard.next"/>"/>
            </td>
        </tr>
    </table>
    </zynap:form>
</zynap:infobox>