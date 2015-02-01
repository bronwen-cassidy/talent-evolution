<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_save" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table id="allDetails" class="infotable">
            <tr valign="top">
                <td>
                    <c:if test="${command.ouShow}">
                        <!-- the orgunit table settings section -->
                        <fieldset>
                            <legend><fmt:message key="organisation.unit"/></legend>
                            <table id="orgUnit" class="infotable">
                                <tr>
                                    <td class="infolabel"><fmt:message key="background.colour"/>&nbsp;:&nbsp;</td>
                                    <spring:bind path="command.ouColor">
                                        <td class="infodata">
                                            <span style="white-space:nowrap"><input id="fldX4" style="color:<c:out value="${command.ouFgColor}"/>; background-color:<c:out value="${command.ouBgColor}"/>;" value="Org Unit Name" name="ignoreMe" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'fldX4', 'fldX5');" type="button"/></span>
                                            <input id="fldX5" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.ouColor}"/>"/>
                                        </td>
                                    </spring:bind>
                                </tr>
                            </table>
                        </fieldset>
                    </c:if>
                </td>
                <td>
                    <c:if test="${command.positionPrimaryAssociationShow || command.positionSecondaryAssociationShow}">
                        <!-- the position associations settings section -->
                        <fieldset>
                            <legend><fmt:message key="position.association.lines"/></legend>
                            <table id="positionLinks" class="infotable">
                                <c:if test="${command.positionPrimaryAssociationShow}">
                                    <tr>
                                        <td class="infoheading" colspan="2"><fmt:message key="primary"/></td>
                                    </tr>
                                    <tr>
                                        <spring:bind path="command.positionPrimaryAssociationColor">
                                            <td class="infolabel"><fmt:message key="colour"/>&nbsp;:&nbsp;</td>
                                            <td class="infodata">
                                                <span style="white-space:nowrap"><input id="fldPA4" style="color:white; background-color:<c:out value="${command.positionPrimaryAssociationColor}"/>;" value="Color" name="ignore1" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'fldPA4', 'fldPA5');" type="button"/></span>
                                                <input id="fldPA5" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.positionPrimaryAssociationColor}"/>"/>
                                            </td>
                                        </spring:bind>
                                    </tr>
                                    <tr>
                                        <td class="infolabel"><fmt:message key="line.width"/>&nbsp;:&nbsp;</td>
                                        <td class="infodata">
                                            <spring:bind path="command.positionPrimaryAssociationLine">
                                                <div style="white-space:nowrap">
                                                    <input type="radio" class="input_radio" name="positionPrimaryAssociationLine" value="2px" <c:if test="${command.positionPrimaryAssociationLine == '2px'}">checked</c:if>/>
                                                    &nbsp;
                                                    <img src="<c:url value="/images/report/blackpixel.gif"/>" height="2" width="15"/>
                                                </div>
                                                <div style="white-space:nowrap">
                                                    <input type="radio" class="input_radio" name="positionPrimaryAssociationLine" value="3px" <c:if test="${command.positionPrimaryAssociationLine == '3px'}">checked</c:if>/>
                                                    &nbsp;
                                                    <img src="<c:url value="/images/report/blackpixel.gif"/>" height="3" width="15"/>
                                                </div>
                                                <div style="white-space:nowrap">
                                                    <input type="radio" class="input_radio" name="positionPrimaryAssociationLine" value="4px" <c:if test="${command.positionPrimaryAssociationLine == '4px'}">checked</c:if>/>
                                                    &nbsp;
                                                    <img src="<c:url value="/images/report/blackpixel.gif"/>" height="4" width="15"/>
                                                </div>
                                            </spring:bind>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${command.positionSecondaryAssociationShow}">
                                    <tr>
                                        <td class="infoheading" colspan="2"><fmt:message key="secondary"/></td>
                                    </tr>
                                    <tr>
                                        <spring:bind path="command.positionSecondaryAssociationColor">
                                            <td class="infolabel"><fmt:message key="colour"/> &nbsp;:&nbsp;</td>
                                            <td class="infodata">
                                                <span style="white-space:nowrap"><input id="fldSA4" style="color:white; background-color:<c:out value="${command.positionSecondaryAssociationColor}"/>;" value="Color" name="positionSecondaryColor" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'fldSA4', 'fldSA5');" type="button"/></span>
                                                <input id="fldSA5" type="hidden" name="positionSecondaryColor" value="<c:out value="${command.positionSecondaryAssociationColor}"/>"/>
                                            </td>
                                        </spring:bind>
                                    </tr>
                                    <tr>
                                        <td class="infolabel"><fmt:message key="line.width"/>&nbsp;:&nbsp;</td>
                                        <td class="infodata">
                                            <spring:bind path="command.positionSecondaryAssociationLine">
                                                <div style="white-space:nowrap">
                                                    <input type="radio" class="input_radio" name="positionSecondaryLine" value="1px" <c:if test="${command.positionSecondaryAssociationLine == '1px'}">checked</c:if>/>
                                                    &nbsp;
                                                    <img src="<c:url value="/images/report/blackpixel.gif"/>" height="1" width="15"/>
                                                </div>
                                                <div style="white-space:nowrap">
                                                    <input type="radio" class="input_text" name="positionSecondaryAssociationLine" value="2px" <c:if test="${command.positionSecondaryAssociationLine == '2px'}">checked</c:if>/>
                                                    &nbsp;
                                                    <img src="<c:url value="/images/report/blackpixel.gif"/>" height="2" width="15"/>
                                                </div>
                                                <div style="white-space:nowrap">
                                                    <input type="radio" class="input_text" name="positionSecondaryAssociationLine" value="3px" <c:if test="${command.positionSecondaryAssociationLine == '3px'}">checked</c:if>/>
                                                    &nbsp;
                                                    <img src="<c:url value="/images/report/blackpixel.gif"/>" height="3" width="15"/>
                                                </div>
                                            </spring:bind>
                                        </td>
                                    </tr>
                                </c:if>
                            </table>
                        </fieldset>
                    </c:if>
                </td>
            </tr>
            <tr valign="top">
                <td>
                    <fieldset>
                        <legend><fmt:message key="positions"/></legend>
                        <table id="position" class="infotable">
                            <tr>
                                <td class="infoheading" colspan="2">
                                <c:choose>
                                    <c:when test="${command.selectedPositionSelectionAttribute == 'default' || command.selectedPositionSelectionAttribute == null}">
                                        <fmt:message key="chart.none"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${command.selectedPositionSelectionAttributeLabel}"/>
                                    </c:otherwise>
                                </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td class="infolabel">
                                    <fmt:message key="colour.attribute.values"/>&nbsp;:&nbsp;
                                </td>
                                <td class="infodata">
                                    <c:forEach var="val" items="${command.positionAttributeViews}" varStatus="counter">
                                        <div>
                                            <spring:bind path="command.positionAttributeViews[${counter.index}].color">
                                                <c:set var="attrId"><zynap:id><c:out value="${val.attributeName}"/></zynap:id></c:set>
                                                <span style="white-space:nowrap"><input id="posAttr<c:out value="${counter.index}"/>" style="color:<c:out value="${val.fgColor}"/>; background-color:<c:out value="${val.bgColor}"/>;" value="<c:out value="${val.displayValue}"/>" name="<c:out value="${status.expression}"/>" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'posAttr<c:out value="${counter.index}"/>', '<c:out value="${attrId}"/>');" type="button"/></span>
                                                <input id="<c:out value="${attrId}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${val.color}"/>"/>
                                            </spring:bind>
                                        </div>
                                    </c:forEach>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
                <!-- subject display options -->
                <td>
                    <c:if test="${command.subjectPrimaryAssociationsShow || command.subjectSecondaryAssociationsShow}">
                    <fieldset>
                        <legend><fmt:message key="subjects"/></legend>
                        <table id="subjects" class="infotable" cellspacing="0">
                            <c:if test="${command.subjectPrimaryAssociationsShow}">
                                <tr>
                                    <td class="infoheading" colspan="2">
                                        <fmt:message key="primary.background.colour"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="infolabel"><fmt:message key="primary"/>&nbsp;:&nbsp;</td>
                                    <td class="infodata">
                                        <!-- primary subject association values-->
                                        <c:forEach var="spVal" items="${command.subjectPrimaryAttributeViews}" varStatus="indexer" >
                                            <spring:bind path="command.subjectPrimaryAttributeViews[${indexer.index}].color">
                                                <div>
                                                    <c:set var="attrId"><zynap:id><c:out value="${spVal.attributeName}"/></zynap:id></c:set>
                                                    <span style="white-space:nowrap"><input id="fldSPA<c:out value="${indexer.index}"/>" style="color:<c:out value="${spVal.fgColor}"/>; background-color:<c:out value="${spVal.bgColor}"/>;" value="<c:out value="${spVal.displayValue}"/>" name="<c:out value="${status.expression}"/>" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'fldSPA<c:out value="${indexer.index}"/>', '<c:out value="${attrId}"/>');" type="button"/></span>
                                                    <input id="<c:out value="${attrId}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${spVal.color}"/>"/>
                                                </div>
                                            </spring:bind>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${command.subjectSecondaryAssociationsShow}">
                                <tr>
                                    <td class="infoheading" colspan="2">
                                        <fmt:message key="secondary.background.colour"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="infolabel"><fmt:message key="secondary"/>&nbsp;:&nbsp;</td>
                                    <td class="infodata">
                                        <!-- secondary subject association values-->
                                        <c:forEach var="ssVal" items="${command.subjectSecondaryAttributeViews}" varStatus="pointer" >
                                            <spring:bind path="command.subjectSecondaryAttributeViews[${pointer.index}].color">
                                                <div>
                                                    <c:set var="attrId"><zynap:id><c:out value="${ssVal.attributeName}"/></zynap:id></c:set>
                                                    <span style="white-space:nowrap"><input id="fldSSA<c:out value="${pointer.index}"/>" style="color:<c:out value="${ssVal.fgColor}"/>; background-color:<c:out value="${ssVal.bgColor}"/>;" value="<c:out value="${ssVal.displayValue}"/>" name="<c:out value="${status.expression}"/>" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'fldSSA<c:out value="${pointer.index}"/>', '<c:out value="${attrId}"/>');" type="button"/></span>
                                                    <input id="<c:out value="${attrId}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${ssVal.color}"/>"/>
                                                </div>
                                            </spring:bind>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:if>
                        </table>
                    </fieldset>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="infobutton">
                    <input class="inlinebutton" type="submit" name="_cancel" value="<fmt:message key="cancel"/>"/>
                    <input class="inlinebutton" type="button" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('_save', 'pgTarget', '1', 'backId');"/>
                    <input class="inlinebutton" name="_finish" type="submit" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:window elementId="colourPicker">
    <c:import url="/statics/colourPicker.html"/>
</zynap:window>
