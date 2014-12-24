<zynap:window elementId="colourPicker">
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="use.colours"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <%-- determine if we are using colours in display or not --%>
                    <c:set var="useSelectedColours" value="${selectedColumnWrapper.colorDisplayable}"/>
                    <fmt:message key="generic.yes"/>&nbsp;<input type="radio" class="input_radio" <c:if test="${useSelectedColours}">checked</c:if>  name="useSelectedColours" value="true"/>
                    <fmt:message key="generic.no"/>&nbsp;<input type="radio" class="input_radio" <c:if test="${!useSelectedColours}">checked</c:if> name="useSelectedColours" value="false"/>
                </td>
            </tr>

            <c:forEach var="columnDisplayImage" items="${selectedColumnWrapper.columnDisplayImages}" varStatus="colourcount" >
                <tr>
                    <c:set var="displayImageLabel" value="${columnDisplayImage.lookupValue.label}"/>

                    <%-- determine background colour for select based on current value if any --%>
                    <c:set var="bgColor" value="ffffff"/>
                    <c:if test="${columnDisplayImage.displayImage != null}">
                        <c:set var="bgColor" value="${columnDisplayImage.displayImage}"/>
                    </c:if>
                    <td class="infodata"><c:out value="${displayImageLabel}"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <select style="background-color:#<c:out value="${bgColor}"/>" id="<c:out value="${colourcount.index}"/>" name="selectedColours" onChange="javascript:setBackgroundColour(this)">
                            <%@include file="colorselect.jsp" %>
                        </select>
                    </td>
                </tr>
            </c:forEach>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:popupHide()"/>
                    <input class="inlinebutton" type="button" name="_savecolour" value="<fmt:message key="save"/>" onclick="javascript:saveColours()"/>
                </td>
            </tr>
        </table>
</zynap:window>

<%-- selected colour is set by the refdata in BaseReportsWizardController --%>
<script  type="text/javascript">
    popUpShowColourPicker('<zynap:message code="select.colours" javaScriptEscape="true"/> - <c:out value="${selectedColumnWrapper.label}"/>', <c:out value="${selectedColumnIndex}"/>, 'colourPicker');
</script>
