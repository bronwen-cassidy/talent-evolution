package com.zynap.talentstudio.arenas;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.security.ISecureResource;
import com.zynap.talentstudio.security.permits.IPermit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 04-Feb-2004
 * Time: 17:36:02
 */
public class MenuItem extends ZynapDomainObject implements ISecureResource, Comparable<MenuItem> {

    /**
     * full constructor.
     */
    public MenuItem(Long id, String label, int sortOrder, String url, String description, String userType, IPermit permit, Report report, MenuSection menuSection) {
        this.id = id;
        this.label = label;
        this.sortOrder = sortOrder;
        this.url = url;
        this.description = description;
        this.userType = userType;
        this.permit = permit;
        this.report = report;
        this.menuSection = menuSection;
    }

    /**
     * default constructor.
     */
    public MenuItem() {
    }

    /**
     * minimal constructor.
     */
    public MenuItem(Long id, String label, int sortOrder, String url, IPermit permit, Report report, MenuSection menuSection) {
        this.id = id;
        this.label = label;
        this.sortOrder = sortOrder;
        this.url = url;
        this.permit = permit;
        this.report = report;
        this.menuSection = menuSection;
    }

    /**
     * Convenient constructor for building report menu items.
     *
     * @param label
     * @param menuSection
     * @param baseUrl
     */
    public MenuItem(String label, MenuSection menuSection, String baseUrl) {
        this.label = label;
        this.menuSection = menuSection;
        this.url = baseUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEscapedLabel() {
        return StringEscapeUtils.escapeJavaScript(label);
    }

    public MenuSection getMenuSection() {
        return this.menuSection;
    }

    public void setMenuSection(MenuSection menuSection) {
        this.menuSection = menuSection;
    }

    public IPermit getPermit() {
        return this.permit;
    }

    public void setPermit(IPermit permit) {
        this.permit = permit;
        if (permit != null) permit.addSecureResource(this);
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public int compareTo(MenuItem other) {
        // report menu items are sorted by label the rest by sort order
        if(getReport() != null) {
            return this.label.compareTo(other.label);
        }
        return new Integer(this.sortOrder).compareTo(new Integer(other.sortOrder));
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("menuSection", getMenuSection())
                .append("label", getLabel())
                .append("sortOrder", getSortOrder())
                .append("url", getUrl())
                .append("description", getDescription())
                .append("userType", getUserType())
                .toString();
    }

    public String getKey() {

        StringBuffer stringBuffer = new StringBuffer();

        if (menuSection != null) {
            MenuSectionPK primaryKey = menuSection.getPrimaryKey();
            stringBuffer.append(primaryKey.getArenaId()).append(".");
            stringBuffer.append(primaryKey.getId()).append(".");
        }

        if (label != null)
            stringBuffer.append(label);

        return StringUtils.replace(stringBuffer.toString(), " ", "_");
    }

    /**
     * Is this a report menu item.
     *
     * @return true or false
     */
    public boolean isReportMenuItem() {
        return this.report != null;
    }

    /**
     * Is this a reporting chart menu item.
     *
     * @return true or false
     */
    public boolean isReportingChartMenuItem() {
        return this.preference != null;
    }

    public boolean isQuestionnireMenuItem() {
        return "/talentarena/worklistquestionnaires.htm".equals(url);
    }

    public boolean isAppraisalMenuItem() {
        return "/talentarena/worklistappraisals.htm".equals(url);
    }

    public boolean isAssessmentMenuItem() {
        return "/talentarena/worklistassessments.htm".equals(url);
    }

    public boolean isInboxMenuItem() {
        return "/talentarena/worklistmessages.htm".equals(url);
    }

    public Arena getArena() {
        return menuSection.getArena();
    }

    public Long getReportId() {
        return isReportMenuItem() ? report.getId() : null;
    }

    public Long getPreferenceId() {
        return isReportingChartMenuItem() ? preference.getId() : null;
    }

    private String description;
    private String url;
    private int sortOrder;
    private String userType;
    private MenuSection menuSection;
    private IPermit permit;
    private Report report;

    private Preference preference;
}
