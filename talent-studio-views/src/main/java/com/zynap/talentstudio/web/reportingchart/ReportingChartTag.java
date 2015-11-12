/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart;

import com.zynap.common.util.FileUtils;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;

import com.zynap.talentstudio.web.tag.ZynapTagSupport;
import com.zynap.talentstudio.web.utils.HtmlUtils;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Tag library that generates the basic reporting structure view - currently used by the orgbuilder
 * and talent identifier arenas.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ReportingChartTag extends ZynapTagSupport {

    public String getSubjectUrl() {
        return subjectUrl;
    }

    public void setSubjectUrl(String subjectUrl) throws JspException {
        this.subjectUrl = ExpressionEvaluationUtils.evaluateString("subjectUrl", subjectUrl, pageContext);
    }

    public String getOrgUrl() {
        return orgUrl;
    }

    public void setOrgUrl(String orgUrl) throws JspException {
        this.orgUrl = ExpressionEvaluationUtils.evaluateString("orgUrl", orgUrl, pageContext);
    }

    public String getPositionUrl() {
        return positionUrl;
    }

    public void setPositionUrl(String positionUrl) throws JspException {
        this.positionUrl = ExpressionEvaluationUtils.evaluateString("positionUrl", positionUrl, pageContext);
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(target.toString())) {
            this.target = (Position) ExpressionEvaluationUtils.evaluate(target.toString(), target.toString(), Object.class, pageContext);
        } else {
            this.target = (Position) target;
        }
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) throws JspException {
        this.viewUrl = ExpressionEvaluationUtils.evaluateString("viewUrl", viewUrl, pageContext);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) throws JspException {
        this.imageUrl = ExpressionEvaluationUtils.evaluateString("imageUrl", imageUrl, pageContext);
    }

    /**
     * Clear up resources and reset variables to default state.
     */
    public void release() {
        subjectUrl = null;
        orgUrl = null;
        positionUrl = null;
        viewUrl = null;
        target = null;
        imageUrl = null;
    }

    protected int doInternalStartTag() throws Exception {
        StringBuilder html = new StringBuilder();

        ReportingProperties properties = getReportingProperties();
        // builds the browser table
        html.append(properties.getReportOpenTable());
        boolean buildParent = canDisplayParent(target);
        // builds the parent row
        if (buildParent) {
            if (canDisplayParent(target.getParent())) {
                String currentViewUrl = buildLinkedPageURL(target.getParent().getId(), target.getParent());
                html.append(replaceTokens(properties.getParentLink(), currentViewUrl));
            }
            html.append(getSinglePositionRow(target.getParent()));
            // add the line
            html.append(properties.getSingleRowLineHtml());
        }

        // build the target positions row it opens the row
        html.append(getSinglePositionRow(target));
        Collection children = getPrimaryAssociatedPositions(target);
        if (!children.isEmpty()) {
            // add the single line
            html.append(properties.getSingleRowLineHtml());
            // build the child position arrows
            html.append(formatChildLines(children));
            // buid, the child cells
            html.append(getChildrenCells(children));
        }

        // close the table
        html.append(properties.getReportCloseTable());
        out.write(html.toString());

        return EVAL_PAGE;
    }

    protected boolean canDisplayParent(Position target) {
        if(target.getParent() == null) return false;

        boolean multiTenant = ZynapWebUtils.isMultiTenant((HttpServletRequest) pageContext.getRequest());
        if (multiTenant && target.getOrganisationUnit().isCompanyRoot()) {
            return false;
        }
        return true;
    }

    /**
     * Get the active positions that are primarily associated with the position.
     *
     * @param position
     * @return Collection of Positions
     */
    protected Collection getPrimaryAssociatedPositions(Position position) {
        Collection targetAssociations = position.getTargetAssociations();
        return getPrimarySourceArtefacts(targetAssociations);
    }

    /**
     * Get the active subjects that are primarily associated with the position.
     *
     * @param position
     * @return Collection of Subjects
     */
    protected Collection getPrimaryAssociatedSubjects(Position position) {
        Collection subjectAssociations = position.getSubjectAssociations();
        return getPrimarySourceArtefacts(subjectAssociations);
    }

    /**
     * Get the source artefacts that are in primary associations and are active.
     *
     * @param associations
     * @return Collection of Node objects
     */
    private Collection getPrimarySourceArtefacts(Collection associations) {
        Collection<Node> children = new ArrayList<Node>();
        for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
            ArtefactAssociation artefactAssociation = (ArtefactAssociation) iterator.next();
            if (artefactAssociation.isPrimary()) {
                final Node node = artefactAssociation.getSource();
                if (node.isActive()) children.add(node);
            }
        }

        return children;
    }

    private String buildLinkedPageURL(Long positionId, Position target) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ParameterConstants.ARTEFACT_ID, positionId);
        parameters.put(ParameterConstants.ORG_UNIT_ID_PARAM, target.getOrganisationUnit().getId());
        parameters.put(ParameterConstants.TITLE, target.getOrganisationUnit().getLabel());
        return ZynapWebUtils.buildURL(viewUrl, parameters);
    }

    private String formatChildLines(Collection childPositions) {
        StringBuilder stringBuffer = new StringBuilder();

        if (childPositions.size() == 1) {
            stringBuffer.append(getReportingProperties().getSingleChildImageRow().trim());
            return stringBuffer.toString();
        }
        StringBuilder borderBuf = new StringBuilder();
        int numCells = ((childPositions.size() * 2) - 2);
        String colspan = Integer.toString(numCells);
        for (int i = 0; i < childPositions.size(); i++) {
            borderBuf.append(getReportingProperties().getChildBorderLine().trim());
        }
        Object[] tokens = {colspan.trim(), borderBuf.toString().trim()};
        stringBuffer.append(replaceTokens(getReportingProperties().getChildRowLines().trim(), tokens));
        return stringBuffer.toString();
    }

    private String getChildrenCells(Collection childPositions) {
        StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append(getReportingProperties().getChildStartRow());

        for (Iterator iterator = childPositions.iterator(); iterator.hasNext();) {
            Position childPosition = (Position) iterator.next();
            String orgLinkHtml = getOrgUnitLink(childPosition);
            String positionLinkHtml = getPositionLink(childPosition);
            String subjects = getSubjectLinks(childPosition);

            String children = getChildLinks(childPosition);

            Object[] tokens = new Object[]{orgLinkHtml, positionLinkHtml, subjects, children};
            stringBuffer.append(replaceTokens(getReportingProperties().getSingleChildCell(), tokens));
        }

        stringBuffer.append(getReportingProperties().getCloseChildRow());
        return stringBuffer.toString();
    }

    private String getChildLinks(Position childPosition) {
        StringBuilder stringBuffer = new StringBuilder();

        if (!childPosition.getChildren().isEmpty()) {
            String childPosLink = buildLinkedPageURL(childPosition.getId(), childPosition);
            stringBuffer.append(replaceTokens(getReportingProperties().getNextLevelChildLink(), childPosLink));
        }
        return stringBuffer.toString();
    }

    private String getSinglePositionRow(Position position) {
        StringBuilder html = new StringBuilder();
        String orgLinkHtml = getOrgUnitLink(position);
        String positionLinkHtml = getPositionLink(position);
        String subjects = getSubjectLinks(position);
        Object[] tokens;
        tokens = new Object[]{orgLinkHtml, positionLinkHtml, subjects};
        html.append(replaceTokens(getReportingProperties().getSingleReportRow(), tokens));

        return html.toString();
    }

    /**
     * Get the active subjects that are primarily associated with the position and build HTML containing links for each one.
     *
     * @param position
     * @return Collection of Subjects
     */
    private String getSubjectLinks(Position position) {

        final Collection subjects = getPrimaryAssociatedSubjects(position);

        StringBuilder stringBuffer = new StringBuilder();
        for (Iterator iterator = subjects.iterator(); iterator.hasNext();) {
            Subject subject = (Subject) iterator.next();
            stringBuffer.append(getSubjectPictureHtml(subject));
            stringBuffer.append(getSubjectLink(subject));
        }

        return stringBuffer.toString();
    }

    private String getSubjectPictureHtml(Subject subject) {
        String picUrl = subject.isHasPicture() ? buildNodeUrl(ParameterConstants.SUBJECT_ID_PARAM, subject, imageUrl) : defaultImageUrl;
        return replaceTokens(getReportingProperties().getSubjectPictureHtml(), picUrl);
    }

    /**
     * Active subjects get a link, otherwise just returns the name of the subject.
     *
     * @param subject
     * @return
     */
    private String getSubjectLink(Subject subject) {

        String link = "";
        if (subject.isActive()) {
            final String subjectName = HtmlUtils.htmlEscape(subject.getCoreDetail().getName());


            if (subject.isHasAccess()) {
                
                String subjectLink = buildNodeUrl(ParameterConstants.SUBJECT_ID_PARAM, subject, subjectUrl);
                link = replaceTokens(getReportingProperties().getLinkHtml(), subjectLink, subjectName);
                
            } else {
                link = subjectName;
            }

            link = link.concat("<br/>");
        }

        return link;
    }

    /**
     * Active orgunits get a link, otherwise just returns the name of the orgunit.
     *
     * @param position
     * @return
     */
    private String getOrgUnitLink(Position position) {

        String link = "";
        OrganisationUnit organisationUnit = position.getOrganisationUnit();
        if (organisationUnit.isActive()) {
            final String label = HtmlUtils.htmlEscape(organisationUnit.getLabel());
            if (organisationUnit.isHasAccess()) {

                String orgLink = buildNodeUrl(ParameterConstants.ORG_UNIT_ID_PARAM, organisationUnit, orgUrl);
                link = replaceTokens(getReportingProperties().getLinkHtml(), orgLink, label);
            } else {
                link = label;
            }
        }

        return link;
    }

    /**
     * Active positions get a link, otherwise just returns the name of the position.
     *
     * @param position
     * @return
     */
    private String getPositionLink(Position position) {

        String link = "";
        if (position.isActive()) {
            final String label = HtmlUtils.htmlEscape(position.getLabel());

            if (position.isHasAccess()) {

                String posLink = buildNodeUrl(ParameterConstants.NODE_ID_PARAM, position, positionUrl);
                link = replaceTokens(getReportingProperties().getLinkHtml(), posLink, label);
            } else {
                link = label;
            }
        }

        return link;
    }

    String buildNodeUrl(String idParam, Node node, String url) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(idParam, node.getId());
        return ZynapWebUtils.buildURL(url, parameters);
    }

    private ReportingProperties getReportingProperties() {
        if (reportingProperties == null) reportingProperties = new ReportingProperties();
        return reportingProperties;
    }

    /**
     * Inner class that loads properties file containing the HTML used to display the reporting structure chart.
     */
    class ReportingProperties {

        public ReportingProperties() {
            _properties = FileUtils.loadPropertiesFile(DEFAULT_FILENAME);
        }

        public String getReportOpenTable() {
            return getProperty(OPEN_REPORT_TABLE);
        }

        public String getReportCloseTable() {
            return getProperty(CLOSE_REPORT_TABLE);
        }

        public String getSingleReportRow() {
            return getProperty(SINGLE_REPORT_ROW);
        }

        public String getLinkHtml() {
            return getProperty(LINK);
        }

        public String getSubjectPictureHtml() {
            return getProperty(SUBJECT_PICT_HTML);
        }

        public String getSingleRowLineHtml() {
            return getProperty(SINGLE_ROW_CENTERED_LINE);
        }

        public String getChildRowLines() {
            return getProperty(CHILD_ROW_LINES);
        }

        public String getChildBorderLine() {
            return getProperty(CHILD_BORDER_LINES);
        }

        public String getSingleChildCell() {
            return getProperty(SINGLE_CHILD_CELL);
        }

        public String getChildStartRow() {
            return getProperty(CHILD_START_ROW_HTML);
        }

        public String getCloseChildRow() {
            return getProperty(CLOSE_CHILD_ROW);
        }

        public String getSingleChildImageRow() {
            return getProperty(SINGLE_CHILD_IMAGE_ROW);
        }

        public String getParentLink() {
            return getProperty(PARENT_LINK);
        }

        public String getNextLevelChildLink() {
            return getProperty(NEXT_LEVEL_CHILD_LINK);
        }

        public String getNextLevelChildLinkOpenRow() {
            return getProperty(NEXT_LEVEL_CHILD_LINK_OPEN_ROW);
        }

        public String getNextLevelChildLinkCloseRow() {
            return getProperty(NEXT_LEVEL_CHILD_LINK_CLOSE_ROW);
        }

        private String getProperty(String key) {
            return _properties.getProperty(key);
        }

        private static final String OPEN_REPORT_TABLE = "report.browser.open.table.html";
        private static final String CLOSE_REPORT_TABLE = "report.browser.close.table.html";
        private static final String SINGLE_REPORT_ROW = "single.position.report.structure";

        private static final String NEXT_LEVEL_CHILD_LINK = "position.children.next.level.link";
        private static final String NEXT_LEVEL_CHILD_LINK_OPEN_ROW = "position.children.next.level.link.start.row";
        private static final String NEXT_LEVEL_CHILD_LINK_CLOSE_ROW = "position.children.next.level.link.end.row";

        private static final String PARENT_LINK = "parent.position.parent.link";

        private static final String SINGLE_ROW_CENTERED_LINE = "report.browser.image.center.row.html";
        private static final String CHILD_ROW_LINES = "report.child.open.row";
        private static final String CHILD_BORDER_LINES = "report.children.border.row";
        private static final String LINK = "report.browser.link";
        private static final String SUBJECT_PICT_HTML = "report.browser.subject.picture";
        private static final String SINGLE_CHILD_IMAGE_ROW = "report.browser.center.row.one.child";

        private static final String CHILD_START_ROW_HTML = "report.browser.open.child.row";
        private static final String SINGLE_CHILD_CELL = "report.browser.reporting.positions";
        private static final String CLOSE_CHILD_ROW = "report.browser.close.row";

        private Properties _properties;
        private static final String DEFAULT_FILENAME = "reporting.properties";
    }

    protected String subjectUrl;
    protected String orgUrl;
    protected String positionUrl;
    protected String viewUrl;
    protected Position target;
    protected String imageUrl;
    protected final String defaultImageUrl = "../images/report/prefpeople.gif";
    private ReportingProperties reportingProperties;
}