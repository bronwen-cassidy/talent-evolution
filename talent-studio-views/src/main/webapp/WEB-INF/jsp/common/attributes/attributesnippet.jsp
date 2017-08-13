
<c:set var="message">
    <c:if test="${attr.rangeMessage != ''}">
        <c:out escapeXml="false" value="<span class=\"rangeMessage\">"/><fmt:message key="${attr.rangeMessage}">
            <c:forEach var="p" items="${attr.rangeParams}">
                <fmt:param value="${p}"/>
            </c:forEach>
        </fmt:message><c:out escapeXml="false" value="</span>"/>
    </c:if>
</c:set>

    <c:if test="${attr.type == 'TEXT'}">
        <spring:bind path="${prefix}.value">
            <td class="infodata">
                <input id="<c:out value="${fieldId}"/>" type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <c:out value="${message}" escapeXml="false"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'NUMBER' || attr.type == 'DECIMAL'}">
        <spring:bind path="${prefix}.value">
            <td class="infodata">
                <input id="<c:out value="${fieldId}"/>" type="text" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <c:out value="${message}" escapeXml="false"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'LINK'}">
        <spring:bind path="${prefix}.value">
            <td class="infodata">
                <input id="<c:out value="${fieldId}"/>" type="text" class="input_link" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <c:out value="${message}" escapeXml="false"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'TEXTAREA'}">
        <spring:bind path="${prefix}.value">
            <td class="infodata">
                <textarea id="<c:out value="${fieldId}"/>" name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textArea>
                <c:out value="${message}" escapeXml="false"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'TIME'}">
        <c:set var="hourId" value="${fieldId}_hours" />
        <c:set var="minId" value="${fieldId}_mins" />
        <spring:bind path="${prefix}.time">
            <td class="infodata">
                <select id="<c:out value="${hourId}"/>" name="wrappedDynamicAttributes[<c:out value="${index}"/>].hour" onchange="javascript:toggleTime('<c:out value="${hourId}"/>', '<c:out value="${minId}"/>');">
                    <option name="" <c:if test="${attr.hour == null}">selected</c:if>>
                        <c:out value=""/>
                    </option>
                    <c:forEach var="maxhour" begin="0" end="23">
                        <option value="<fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty attr.hour && attr.hour == maxhour}">selected</c:if>>
                            <fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>
                        </option>
                    </c:forEach>
                </select>&nbsp;:&nbsp;<select id="<c:out value="${minId}"/>" name="wrappedDynamicAttributes[<c:out value="${index}"/>].minute" onchange="javascript:toggleTime('<c:out value="${minId}"/>', '<c:out value="${hourId}"/>');">
                    <option name="" <c:if test="${attr.minute == null}">selected</c:if>>
                        <c:out value=""/>
                    </option>
                    <c:forEach var="minute" begin="0" end="59">
                        <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty attr.minute && attr.minute == minute}">selected</c:if>>
                            <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                        </option>
                    </c:forEach>
                </select>
                <c:out value="${message}" escapeXml="false"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'STRUCT'}">
        <spring:bind path="${prefix}.value">
            <td class="infodata">
                <select id="<c:out value="${fieldId}"/>" name="<c:out value="${status.expression}"/>">
                    <c:choose>
                        <c:when test="${attr.mandatory && !modeSearch}">
                            <option value="" <c:if test="${attr.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="" <c:if test="${attr.value == null}">selected</c:if>></option>
                        </c:otherwise>
                    </c:choose>
                    <c:forEach var="vals" items="${attr.activeLookupValues}">
                        <option value="<c:out value="${vals.id}"/>" <c:if test="${attr.value == vals.id}">selected</c:if>><c:out value="${vals.label}"/></option>
                    </c:forEach>
                </select>
                <c:out value="${message}" escapeXml="false"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

<%-- todo if for searching use a normal select option else use the multiselect when editing an artefacts attributes --%>
<c:if test="${attr.type == 'MULTISELECT'}">
    <c:choose>
        <c:when test="${modeSearch}">
            <spring:bind path="${prefix}.value">
                <td class="infodata">
                    <select id="<c:out value="${fieldId}"/>" name="<c:out value="${status.expression}"/>">
                        <c:choose>
                            <c:when test="${attr.mandatory && !modeSearch}">
                                <option value="" <c:if test="${attr.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            </c:when>
                            <c:otherwise>
                                <option value="" <c:if test="${attr.value == null}">selected</c:if>></option>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach var="vals" items="${attr.activeLookupValues}">
                            <option value="<c:out value="${vals.id}"/>" <c:if test="${attr.value == vals.id}">selected</c:if>><c:out value="${vals.label}"/></option>
                        </c:forEach>
                    </select>
                    <c:out value="${message}" escapeXml="false"/>
                    <%@ include file="../../includes/error_message.jsp" %>
                </td>
            </spring:bind>
        </c:when>
        <c:otherwise>
            <spring:bind path="${prefix}.value">
                <td class="infodata">
                    <select id="<c:out value="${fieldId}"/>" name="<c:out value="${status.expression}"/>" multiple size="<c:out value="${attr.multiSelectSize}"/>">

                        <c:forEach var="vals" items="${attr.activeLookupValues}">
                            <c:set var="found" value="false"/>
                            <c:set var="xanado" value=""/>

                            <c:forEach var="msVal" items="${attr.multiSelectValues}">
                                <c:if test="${msVal.value == vals.id}">
                                    <c:set var="found" value="true"/>
                                    <c:set var="xanado" value="${msVal.id}"/>
                                </c:if>
                            </c:forEach>
                            <option value="<c:out value="${vals.id}"/>" <c:if test="${found}">selected</c:if>><c:out value="${vals.label}"/></option>
                        </c:forEach>
                    </select>
                    <%@ include file="../../includes/error_message.jsp" %>
                </td>
            </spring:bind>
        </c:otherwise>
    </c:choose>
</c:if>

    <c:if test="${attr.type == 'IMG'}">
        <spring:bind path="${prefix}.file">
            <td class="infodata">
                <c:if test="${attr.value != null}"><c:out value="${attr.value}"/><br/></c:if>
                <input id="<c:out value="${fieldId}"/>" type="file" class="input_file" name="<c:out value="${status.expression}"/>" value="<c:out value="${attr.value}"/>"/>
                <c:if test="${attr.value != null && attr.value != '' }">
                    <input class="inlinebutton" type="button" name="deleteImage" value="<fmt:message key="delete"/>" onclick="setHiddenAndSubmit('<c:out value="${currentFormName}"/>','deleteImage','<c:out value="${index}"/>');" />
                </c:if>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'DATE'}">
        <c:set var="dispId" value="${fieldId}_disp" />
        <c:set var="isoId" value="${fieldId}_iso" />
        <c:set var="allowClear"><c:choose><c:when test="${attr.mandatory && !modeSearch}">false</c:when><c:otherwise>true</c:otherwise></c:choose></c:set>
        <spring:bind path="${prefix}.date">
            <td class="infodata">
                <%-- DONT CHANGE THE LAYOUT BELOW. MUST NOT BE A SPACE BEFORE THE BUTTON --%>
                <span style="white-space: nowrap;"><input id="<c:out value="${dispId}"/>" class="input_date" type="text" value="<c:out value='${attr.displayValue}'/>" readonly="true"
                    /><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', '<c:out value="${dispId}"/>', '<c:out value="${isoId}"/>', null, <c:out value="${allowClear}"/>);"/>
                <input id="<c:out value="${isoId}"/>" name="<c:out value="${status.expression}"/>" type="hidden" value="<c:out value="${attr.date}"/>"/>
                <c:out value="${message}" escapeXml="false"/></span>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'DATETIME'}">
        <c:set var="dispId" value="${fieldId}_disp" />
        <c:set var="isoId" value="${fieldId}_iso" />
        <c:set var="allowClear"><c:choose><c:when test="${attr.mandatory && !modeSearch}">false</c:when><c:otherwise>true</c:otherwise></c:choose></c:set>
        <spring:bind path="${prefix}.dateTime">
            <td class="infodata">
                <%-- DONT CHANGE THE LAYOUT BELOW. MUST NOT BE A SPACE BEFORE THE BUTTON --%>
                <span style="white-space: nowrap;"><input id="<c:out value="${dispId}"/>" class="input_date" type="text" value="<c:out value='${attr.dateTimeDateDisplayValue}'/>" readonly="true"
                    /><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', '<c:out value="${dispId}"/>', '<c:out value="${isoId}"/>', null, <c:out value="${allowClear}"/>);"/></span>
                <input id="<c:out value="${isoId}"/>" name="wrappedDynamicAttributes[<c:out value="${index}"/>].date" type="hidden" value="<c:out value="${attr.date}"/>"/>
                <c:set var="hourId" value="${fieldId}_hours" />
                <c:set var="minId" value="${fieldId}_mins" />
                <select id="<c:out value="${hourId}"/>" name="wrappedDynamicAttributes[<c:out value="${index}"/>].hour" onchange="toggleTime('<c:out value="${hourId}"/>', '<c:out value="${minId}"/>');">
                    <option name="" <c:if test="${attr.hour == null}">selected</c:if>>
                        <c:out value=""/>
                    </option>
                    <c:forEach var="maxhour" begin="0" end="23">
                        <option value="<fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty attr.hour && attr.hour == maxhour}">selected</c:if>>
                            <fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>
                        </option>
                    </c:forEach>
                </select>&nbsp;:&nbsp;<select id="<c:out value="${minId}"/>" name="wrappedDynamicAttributes[<c:out value="${index}"/>].minute" onchange="javascript:toggleTime('<c:out value="${minId}"/>', '<c:out value="${hourId}"/>');">
                    <option name="" <c:if test="${attr.minute == null}">selected</c:if>>
                        <c:out value=""/>
                    </option>
                    <c:forEach var="minute" begin="0" end="59">
                        <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty attr.minute && attr.minute == minute}">selected</c:if>>
                            <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                        </option>
                    </c:forEach>
                </select>
                <c:out value="${message}" escapeXml="false"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </c:if>

    <c:if test="${attr.type == 'CURRENCY'}">
        
            <td class="infodata">
                <span class="left">
                <spring:bind path="${prefix}.value">
                    <input id="<c:out value="${fieldId}"/>" type="text" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <c:out value="${message}" escapeXml="false"/>
                    <%@ include file="../../includes/error_message.jsp" %>
                </spring:bind>
                </span>
                <span class="left">
                <spring:bind path="${prefix}.currency">
                    <select id="<c:out value="${fieldId}_sel"/>" name="<c:out value="${status.expression}"/>">
                        <c:choose>
                            <c:when test="${attr.mandatory && !modeSearch}">
                                <option value="" <c:if test="${attr.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            </c:when>
                            <c:otherwise>
                                <option value="" <c:if test="${attr.value == null}">selected</c:if>></option>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach var="vals" items="${attr.activeLookupValues}">
                            <option value="<c:out value="${vals.label}"/>" <c:if test="${attr.currency == vals.label}">selected</c:if>><c:out value="${vals.label}"/></option>
                        </c:forEach>
                    </select>
                    <%@ include file="../../includes/error_message.jsp" %>
                </spring:bind>
                </span>
                
            </td>
        
    </c:if>

