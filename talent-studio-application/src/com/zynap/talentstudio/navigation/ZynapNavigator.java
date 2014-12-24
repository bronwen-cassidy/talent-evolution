package com.zynap.talentstudio.navigation;

/**
 * User: jsueiras
 * Date: 28-Apr-2005
 * Time: 17:54:30
 */
public class ZynapNavigator {

    public ZynapNavigator(Long organisationUnitId, String orgUnitLabel) {
        this();
        this.organisationUnitId = organisationUnitId;
        this.organisationUnitLabel = orgUnitLabel;
    }

    public ZynapNavigator() {
        this.notSubmit = true;
    }


    public Long getOrganisationUnitId() {
        return organisationUnitId;
    }

    public void setOrganisationUnitId(Long organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public String getOrganisationUnitLabel() {
        return organisationUnitLabel;
    }

    public void setOrganisationUnitLabel(String organisationUnitLabel) {
        this.organisationUnitLabel = organisationUnitLabel;
    }

    public void setNotSubmit(boolean notSubmit) {
        this.notSubmit = notSubmit;
    }

    public boolean isNotSubmit() {
        return notSubmit;
    }

    public boolean isHasOU() {
        return hasOU;
    }

    public void setHasOU(boolean hasOU) {
        this.hasOU = hasOU;
    }

    Long organisationUnitId;
    String  organisationUnitLabel;
    private boolean notSubmit;
    private boolean hasOU;

    final String NAVIGATOR_COMMAND ="navigator";
    final String NAVIGATOR_TREE_COMMAND ="navigatorTree";
    final String NAVIGATOR_OU_ID =NAVIGATOR_COMMAND + ".organisationUnitId";
    final String NAVIGATOR_NOT_SUBMIT =NAVIGATOR_COMMAND + ".notSubmit";
    final String NAVIGATOR_OU_LABEL = NAVIGATOR_COMMAND +".organisationUnitLabel";
    final String NAVIGATOR_HAS_OU =  NAVIGATOR_COMMAND + "hasOU";

}
