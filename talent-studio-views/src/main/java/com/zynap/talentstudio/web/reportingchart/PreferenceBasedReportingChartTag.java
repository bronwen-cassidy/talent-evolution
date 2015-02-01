/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart;

import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.ArtefactAssociationHelper;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.preferences.format.FormattingAttribute;
import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.AttributeView;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.preferences.AttributePreferenceAccessor;
import com.zynap.talentstudio.web.reportingchart.settings.ViewSettingsWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.util.format.MessageTemplateFormatter;
import com.zynap.util.resource.PropertiesManager;
import com.zynap.web.utils.HtmlUtils;
import com.zynap.domain.admin.User;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tag library that generates the reporting structure view using the user's display preferences
 * - currently used by the succession builder arena only.
 *
 * @author amark
 * @version 1.0
 */
public class PreferenceBasedReportingChartTag extends ReportingChartTag {

    private static final PropertiesManager PROPERTIES_MANAGER = PropertiesManager.getInstance(PreferenceBasedReportingChartTag.class);

    public void setPreferences(Object preferences) throws JspException {
        if (preferences == null) return;
        if (ExpressionEvaluationUtils.isExpressionLanguage(preferences.toString())) {
            this.preferences = (DomainObjectPreferenceCollection) ExpressionEvaluationUtils.evaluate(preferences.toString(), preferences.toString(), Object.class, pageContext);
        } else {
            this.preferences = (DomainObjectPreferenceCollection) preferences;
        }
    }

    /**
     * Clear up resources and reset variables to default state.
     */
    public void release() {
        super.release();
        preferences = null;
        secure = false;
        sequence = 0;
    }

    protected int doInternalStartTag() throws Exception {

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(getProperty("report.structure.start"));
        boolean owner = false;

        // only do this check if we are displaying the secure view
        if (secure) {
            List<Subject> subjects = target.getCurrentHolders();
            for (Subject subject : subjects) {
                final User user = subject.getUser();
                if (user != null) {
                    if (user.getId().equals(ZynapWebUtils.getUserId((HttpServletRequest) pageContext.getRequest()))) {
                        owner = true;
                    }
                }
            }
        }

        boolean displayParent = (!secure || !owner);
        final Position parent = target.getParent();
        if (parent != null) displayParent = displayParent || parent.isHasAccess();
        displayParent = canDisplayParent(target) && displayParent;

        displayPosition(stringBuffer, target, displayParent);
        displayPrimaryAssociations(stringBuffer, target);

        stringBuffer.append(getProperty("report.structure.end"));

        out.write(stringBuffer.toString());

        return EVAL_PAGE;
    }

    public void setSecure(String secure) throws JspException {
        this.secure = ExpressionEvaluationUtils.evaluateBoolean("secure", secure, pageContext);
    }

    private void displayPosition(StringBuilder stringBuffer, Position position, boolean displayParent) throws Exception {

        // only ever display active positions
        if (position.isActive()) {
            Position parent = position.getParent();

            if (displayParent && parent != null) {
                // check that primary associations are to be displayed as parent-child association are always primary
                AttributePreference attributePreference = AttributePreferenceAccessor.get(preferences, position, ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY);
                if (attributePreference.isDisplayable()) {

                    stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.parent.start"));

                    final Position grandParent = parent.getParent();
                    if (grandParent != null) {                        
                        boolean displayUpNavigation = !secure || grandParent.isHasAccess();
                        if (displayUpNavigation) {
                            String parentLink = getProperty("com.zynap.talentstudio.organisation.positions.Position.parent.link");
                            stringBuffer.append(replacePlaceHolders(parentLink, buildURL(viewUrl, parent, ParameterConstants.ARTEFACT_ID)));
                        }
                    }

                    displayPosition(stringBuffer, parent, false);

                    String link = getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociation.line");
                    FormattingInfo formattingInfo = AttributePreferenceAccessor.get(preferences, parent, ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY).getFormattingInfo();
                    stringBuffer.append(replacePlaceHolders(link, formattingInfo.getFormattingAttributes()));

                    stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.parent.end"));
                }
            }

            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.start"));

            displayOrganisationUnit(position.getOrganisationUnit(), stringBuffer);

            // include link to view position if user has access
            if (position.isHasAccess()) {
                stringBuffer.append(applyPreferences(preferences, position, ViewSettingsWrapperBean.TITLE_KEY, buildURL(positionUrl, position, ParameterConstants.NODE_ID_PARAM)));
                applyExtendedAttributePreferences(position, stringBuffer);
            } else {
                stringBuffer.append(applyNoAccessPreferences(preferences, position, ViewSettingsWrapperBean.TITLE_KEY, new HashMap<String, Object>()));
            }

            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.inner.end"));

            displayPrimarySubjectAssociations(position, stringBuffer);
            displaySecondarySubjectAssociations(position, stringBuffer);

            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.end"));
        }
    }

    private void displayOrganisationUnit(OrganisationUnit organisationUnit, StringBuilder stringBuffer) {
        // show active org units only
        if (organisationUnit.isActive()) {
            // include link to view org unit if user has access
            if (organisationUnit.isHasAccess()) {
                stringBuffer.append(applyPreferences(preferences, organisationUnit, ViewSettingsWrapperBean.LABEL_KEY, buildURL(orgUrl, organisationUnit, ParameterConstants.ORG_UNIT_ID_PARAM)));
            } else {
                stringBuffer.append(applyNoAccessPreferences(preferences, organisationUnit, ViewSettingsWrapperBean.LABEL_KEY, new HashMap<String, Object>()));
            }
        }
    }

    private void displayPrimarySubjectAssociations(Position position, StringBuilder stringBuffer) {

        // check which primary position-to-subject associations are selected to be displayed
        final Collection<ArtefactAssociation> primaryAssociations = ArtefactAssociationHelper.getPrimaryAssociations(position.getSubjectAssociations());
        // sort the associations by qualifier sort order
        List<ArtefactAssociation> associations = sortAssociations(primaryAssociations);

        Map subjectPrimaryAssociations = applySubjectAssociationPreferences(associations, true);

        // now iterate through the list of primary position-to-subject associations
        if (!subjectPrimaryAssociations.isEmpty()) {
            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primarySubjects.start"));
            displaySubjectAssociations(subjectPrimaryAssociations, stringBuffer);
            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primarySubjects.end"));
        }
    }

    private void displaySecondarySubjectAssociations(Position position, StringBuilder stringBuffer) {

        // check which subject position-to-subject associations are selected to be displayed
        final Collection<ArtefactAssociation> secondaryAssociations = ArtefactAssociationHelper.getSecondaryAssociations(position.getSubjectAssociations());

        List<ArtefactAssociation> associations = sortAssociations(secondaryAssociations);

        Map subjectSecondaryAssociations = applySubjectAssociationPreferences(associations, false);

        // now iterate through the list of secondary position-to-subject associations
        if (!subjectSecondaryAssociations.isEmpty()) {
            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.secondarySubjects.start"));
            displaySubjectAssociations(subjectSecondaryAssociations, stringBuffer);
            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.secondarySubjects.end"));
        }
    }

    private List<ArtefactAssociation> sortAssociations(Collection<ArtefactAssociation> artefactAssociations) {

        List<ArtefactAssociation> associations = new ArrayList<ArtefactAssociation>(artefactAssociations);
        Collections.sort(associations, new Comparator<ArtefactAssociation>() {
            public int compare(ArtefactAssociation o1, ArtefactAssociation o2) {
                return new Integer(o1.getQualifier().getSortOrder()).compareTo(new Integer(o2.getQualifier().getSortOrder()));
            }
        });
        return associations;
    }

    private Map applySubjectAssociationPreferences(Collection associations, boolean primary) {

        Map<SubjectAssociation, AttributeView> selectedAssociations = new LinkedHashMap<SubjectAssociation, AttributeView>();
        Iterator it = associations.iterator();
        while (it.hasNext()) {
            SubjectAssociation association = (SubjectAssociation) it.next();

            AttributeView attributeView = AttributePreferenceAccessor.getAttributePreference(preferences, association,
                    ViewSettingsWrapperBean.SUBJECT_ASSOCIATION_TYPE_KEY, Boolean.toString(primary));
            // include subject only if has attribute preference and subject is active
            if (attributeView != null && association.getSource().isActive()) {
                selectedAssociations.put(association, attributeView);
            }
        }
        return selectedAssociations;
    }

    private void displaySubjectAssociations(Map subjectAssociations, StringBuilder stringBuffer) {
        for (Iterator iterator = subjectAssociations.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            final Subject subject = (Subject) ((SubjectAssociation) entry.getKey()).getSource();
            String picUrl = subject.isHasPicture() ? buildNodeUrl(ParameterConstants.SUBJECT_ID_PARAM, subject, imageUrl) : defaultImageUrl;

            final AttributeView attributeView = (AttributeView) entry.getValue();
            FormattingInfo formattingInfo = attributeView.getFormattingInfo();

            final Map<String, FormattingAttribute> content = formattingInfo.getFormattingAttributes();
            FormattingAttribute picAttr = new FormattingAttribute("pictureUrl", picUrl);
            content.put("pictureUrl", picAttr);

            stringBuffer.append(replacePlaceHolders(getProperty("com.zynap.talentstudio.organisation.subjects.SubjectAssociation.start"), content));

            displaySubject(subject, stringBuffer);

            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.subjects.SubjectAssociation.end"));
        }
    }

    private void displaySubject(Subject subject, StringBuilder stringBuffer) {

        final boolean hasAccess = subject.isHasAccess();
        if (hasAccess) {
            Map url = buildURL(subjectUrl, subject, ParameterConstants.SUBJECT_ID_PARAM);
            stringBuffer.append(replacePlaceHolders(getProperty("com.zynap.talentstudio.organisation.subjects.Subject.start"), url));
        }

        Map<String, String> content = new HashMap<String, String>();
        content.put(ViewSettingsWrapperBean.LABEL_KEY, HtmlUtils.htmlEscape(subject.getLabel()));
        stringBuffer.append(replacePlaceHolders(getProperty("com.zynap.talentstudio.organisation.subjects.Subject.name"), content));

        if (hasAccess) {
            stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.subjects.Subject.end"));
            applyExtendedAttributePreferences(subject, stringBuffer);
        }
    }

    private void displayPrimaryAssociations(StringBuilder stringBuffer, Position position) throws Exception {

        AttributePreference attributePreference = AttributePreferenceAccessor.get(preferences, position, ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY);
        if (attributePreference.isDisplayable()) {
            Collection primaryAssociations = getPrimaryAssociatedPositions(position);
            if (!primaryAssociations.isEmpty()) {
                Map formattingAttributes = attributePreference.getFormattingInfo().getFormattingAttributes();
                int numberOfPrimaryAssociations = primaryAssociations.size();

                // draw line connecting target to its primary associations
                String link = getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociation.line");
                stringBuffer.append(replacePlaceHolders(link, formattingAttributes));

                // nested table for primary associations
                stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.start"));

                if (numberOfPrimaryAssociations > 1) {
                    // first row contains horizontal lines - formatted based on preferences
                    stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.connectors.start"));

                    for (int i = 0; i < numberOfPrimaryAssociations; i++) {
                        stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.connectors.table.start"));
                        String format;
                        if (i == 0) {
                            format = getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.first");
                        } else if (i == (numberOfPrimaryAssociations - 1)) {
                            format = getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.last");
                        } else {
                            format = getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.default");
                        }

                        stringBuffer.append(replacePlaceHolders(format, formattingAttributes));
                        stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.connectors.table.end"));
                    }

                    // close first row
                    stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.connectors.end"));
                }

                // second row contains a cell for each primary association
                stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.connectors.start"));

                for (Iterator iterator = primaryAssociations.iterator(); iterator.hasNext();) {
                    Position associatedPosition = (Position) iterator.next();

                    // open table to put position inside
                    stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.item.start"));
                    displayPosition(stringBuffer, associatedPosition, false);

                    // display link to children
                    if (!associatedPosition.getChildren().isEmpty()) {
                        String childLink = getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.child.link");
                        stringBuffer.append(replacePlaceHolders(childLink, buildURL(viewUrl, associatedPosition, ParameterConstants.ARTEFACT_ID)));
                    }

                    // close position table
                    stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.item.end"));
                }

                // close second row
                stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.connectors.end"));

                // close table and outer row
                stringBuffer.append(getProperty("com.zynap.talentstudio.organisation.positions.Position.primaryAssociations.end"));
            }
        }
    }

    /**
     * Apply extended attribute preferences.
     * <br> for each attribute value on the Node, check to see if it should be displayed.
     *
     * @param node         The Node
     * @param stringBuffer
     */
    private void applyExtendedAttributePreferences(Node node, StringBuilder stringBuffer) {

        String property = getProperty(node.getClass().getName() + "." + DYNAMIC_ATTR_FIELD_NAME);
        Collection attributes = node.getDynamicAttributeValues().getValues();
        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {

            // get attribute preference from list for each dynamic attribute based on name and check that it is displayable
            AttributeValue attributeValue = (AttributeValue) iterator.next();
            final AttributePreference attributePreference = AttributePreferenceAccessor.get(preferences, node, attributeValue.getDynamicAttribute().getExternalRefLabel());
            if (attributePreference != null && attributePreference.isDisplayable()) {
                Map<String, String> content = new HashMap<String, String>();
                content.put(ID_KEY, "axxb" + ++sequence + "_" + attributeValue.getDynamicAttribute().getId());
                content.put(TITLE_KEY, attributeValue.getDynamicAttribute().getDescription());
                content.put(ViewSettingsWrapperBean.LABEL_KEY, HtmlUtils.htmlEscape(attributeValue.getDynamicAttribute().getLabel()));
                content.put(VALUE_KEY, HtmlUtils.htmlEscape(attributeValue.getDisplayValue()));
                stringBuffer.append(replacePlaceHolders(property, content));
            }
        }
    }

    private Map<String, Object> buildURL(String url, Node domainObject, String idParameter) {

        String completeUrl = ZynapWebUtils.buildURL(url, idParameter, domainObject.getId());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(URL_KEY, completeUrl);

        return map;
    }

    private String applyPreferences(DomainObjectPreferenceCollection domainObjectPreferenceCollection, Node node, String attributeName, Map<String, Object> content) {

        String format = getProperty(getDomainObjectAttributeName(node, attributeName));
        return getValue(domainObjectPreferenceCollection, node, attributeName, content, format);
    }

    private String applyNoAccessPreferences(DomainObjectPreferenceCollection domainObjectPreferenceCollection, Node node, String attributeName, Map<String, Object> content) {

        String format = getProperty(getNoAccessDomainObjectAttributeName(node, attributeName));
        return getValue(domainObjectPreferenceCollection, node, attributeName, content, format);
    }

    private String getValue(DomainObjectPreferenceCollection domainObjectPreferenceCollection, Node node, String attributeName, Map<String, Object> content, String format) {
        AttributeView attributeView = AttributePreferenceAccessor.getAttributePreference(domainObjectPreferenceCollection, node, attributeName);
        if (attributeView != null) {
            content.put(VALUE_KEY, HtmlUtils.htmlEscape(attributeView.getExpectedValue()));

            FormattingInfo formattingInfo = attributeView.getFormattingInfo();
            if (formattingInfo != null) {
                content.putAll(formattingInfo.getFormattingAttributes());
            }

            return replacePlaceHolders(format, content);
        }

        return "";
    }

    private String getDomainObjectAttributeName(Node node, String attributeName) {
        return node.getClass().getName() + "." + attributeName;
    }

    private String getNoAccessDomainObjectAttributeName(Node node, String attributeName) {
        return getDomainObjectAttributeName(node, attributeName) + NO_LINK;
    }

    private String getProperty(String propertyName) {
        return PROPERTIES_MANAGER.getString(propertyName);
    }

    private String replacePlaceHolders(String format, Map content) {
        MessageTemplateFormatter formatter = new MessageTemplateFormatter(format);
        return formatter.format(content);
    }

    private DomainObjectPreferenceCollection preferences;
    private int sequence = 0;

    private static final String DYNAMIC_ATTR_FIELD_NAME = "dynamicAttributeValue";
    private static final String NO_LINK = ".noLink";

    private static final String URL_KEY = "url";
    private static final String VALUE_KEY = "value";
    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";

    private boolean secure;
}