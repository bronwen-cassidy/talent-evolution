<%@ include file="../../includes/include.jsp" %>

<zynap:actionbox>
  <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
  </zynap:actionEntry>
  <zynap:actionEntry>
      <zynap:form method="get" action="editDA.htm" name="edit">
            <input type="hidden" name="<%=ParameterConstants.ATTR_ID%>" value="<c:out value="${model.da.id}"/>">
            <input type="hidden" name="<%=ParameterConstants.ARTEFACT_TYPE%>" value="<c:out value="${model.da.artefactType}"/>"/>
            <zynap:button cssClass="actionbutton" name="editda" value="Edit" type="button" onclick="javascript:document.forms.edit.submit();" />
      </zynap:form>
  </zynap:actionEntry>
  <zynap:actionEntry>
      <zynap:form method="get" action="deleteDA.htm" name="_delete">
            <input type="hidden" name="<%=ParameterConstants.ATTR_ID%>" value="<c:out value="${model.da.id}"/>">
            <input type="hidden" name="<%=ParameterConstants.ARTEFACT_TYPE%>" value="<c:out value="${model.da.artefactType}"/>"/>
            <zynap:button cssClass="actionbutton" name="delda" value="Delete" type="button" onclick="javascript:document.forms._delete.submit();" />
      </zynap:form>
  </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="view.dynamicAttribute" var="msg"/>
<zynap:infobox title="${msg}">
  <table class="infotable" cellspacing="0">
      <!-- label -->
      <tr>
          <td class="infolabel"><fmt:message key="generic.label"/>&nbsp;:&nbsp;</td>
          <td class="infodata"><c:out value="${model.da.label}"/></td>
      </tr>

      <!-- description -->
      <tr>
          <td class="infolabel"><fmt:message key="artefact.description"/>&nbsp;:&nbsp;</td>
          <td class="infodata"><zynap:desc><c:out value="${model.da.description}"/></zynap:desc></td>
      </tr>
      <!-- type -->
      <tr>
          <td class="infolabel"><fmt:message key="type"/>&nbsp;:&nbsp;</td>
          <td class="infodata"><fmt:message key="${model.da.type}"/></td>
      </tr>
      <!-- active -->
      <tr>
          <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
          <td class="infodata"><fmt:message key="${model.da.active}"/></td>
      </tr>

      <c:if test="${model.da.type == 'STRUCT' || model.da.type == 'MULTISELECT'}">
          <tr>
              <td class="infolabel"><fmt:message key="da.struct.refersto"/>&nbsp;:&nbsp;</td>
              <td class="infodata"><c:out value="${model.da.refersToTypeLabel}"/></td>
          </tr>
      </c:if>

      <c:if test="${model.da.type != 'STRUCT' && model.da.type != 'CURRENCY' && model.da.type != 'MULTISELECT' && model.da.type != 'IMG' && model.da.type != 'LINK' && !model.da.calculated}">
      <!-- minsize -->
          <tr>
              <td class="infolabel"><fmt:message key="da.${model.da.type}.minsize"/>&nbsp;:&nbsp;</td>
              <c:choose>
                  <c:when test="${model.da.type == 'DATE'}">
                      <td class="infodata"><c:out value="${model.da.minDate}"/></td>
                  </c:when>
                  <c:when test="${model.da.type == 'DATETIME'}">
                      <td class="infodata"><c:out value="${model.da.minDateTimeDisplay}"/></td>
                  </c:when>
                  <c:otherwise>
                      <td class="infodata"><c:out value="${model.da.minSize}"/></td>
                  </c:otherwise>
              </c:choose>
          </tr>
          <!-- maxsize-->
          <tr>
              <td class="infolabel"><fmt:message key="da.${model.da.type}.maxsize"/>&nbsp;:&nbsp;</td>
              <c:choose>
                  <c:when test="${model.da.type == 'DATE'}">
                      <td class="infodata"><c:out value="${model.da.maxDate}"/></td>
                  </c:when>
                  <c:when test="${model.da.type == 'DATETIME'}">
                      <td class="infodata"><c:out value="${model.da.maxDateTimeDisplay}"/></td>
                  </c:when>
                  <c:otherwise>
                      <td class="infodata"><c:out value="${model.da.maxSize}"/></td>
                  </c:otherwise>
              </c:choose>
          </tr>
      </c:if>
      <c:if test="${model.da.type == 'DOUBLE'}">
          <tr>
              <td class="infolabel"><fmt:message key="da.number.decimal.palces"/>&nbsp;:&nbsp;</td>
              <td class="infodata"><c:out value="${model.da.decimalPlaces}"/></td>
          </tr>
      </c:if>
      <c:if test="${!model.da.calculated}">
          <!-- mandatory -->
          <tr>
              <td class="infolabel"><fmt:message key="mandatory.true"/>&nbsp;:&nbsp;</td>
              <td class="infodata"><fmt:message key="${model.da.mandatory}"/></td>
          </tr>
          <!-- searchable -->
          <tr>
              <td class="infolabel"><fmt:message key="searchable"/>&nbsp;:&nbsp;</td>
              <td class="infodata"><fmt:message key="${model.da.searchable}"/></td>
          </tr>
      </c:if>
      
      <c:if test="${model.da.calculated}">
          <!-- mandatory -->
          <tr>
              <td class="infolabel"><fmt:message key="calculation"/>&nbsp;:&nbsp;</td>
              <td class="infodata">
                  <c:forEach var="expression" items="${model.expressions}">
                      <c:out value="${expression.label}"/>&nbsp;<c:out value="${expression.operator}"/>&nbsp;
                  </c:forEach>
              </td>
          </tr>                    
      </c:if>
  </table>
</zynap:infobox>
