package com.zynap.talentstudio.web.reportingchart;

import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.organisation.positions.Position;

import java.io.Serializable;
import java.util.List;

/**
 * User: amark
 * Date: 09-Dec-2005
 * Time: 14:18:15
 */
public class ReportChartWrapper implements Serializable {

    public ReportChartWrapper(String displayConfigKey, DomainObjectPreferenceCollection preferenceCollection) {
        this.displayConfigKey = displayConfigKey;
        this.preferenceCollection = preferenceCollection;
    }

    public String getDisplayConfigKey() {
        return displayConfigKey;
    }

    public DomainObjectPreferenceCollection getPreferenceCollection() {
        return preferenceCollection;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setSubjectPositions(List<Position> subjectPositions) {
        this.subjectPositions = subjectPositions;
    }

    public List<Position> getSubjectPositions() {
        return subjectPositions;
    }

    private boolean secure;

    private Position position;

    private final String displayConfigKey;

    private final DomainObjectPreferenceCollection preferenceCollection;

    private List<Position> subjectPositions;
}
