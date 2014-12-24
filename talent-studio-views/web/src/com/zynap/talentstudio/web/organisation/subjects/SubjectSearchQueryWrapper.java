package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.talentstudio.web.organisation.NodeSearchQueryWrapper;

/**
 * Class or Interface description.
 *
 * @author jsueiras
 * @version 0.1
 * @since 11-Feb-2005 11:25:22
 */

public class SubjectSearchQueryWrapper extends NodeSearchQueryWrapper {

    public SubjectSearchQueryWrapper(Long organisationId) {
        super(organisationId, new SubjectSearchQuery());
    }

    public SubjectSearchQueryWrapper(SubjectSearchQuery searchAdaptor) {
        super(searchAdaptor);
    }

    private SubjectSearchQuery getSubjectSearchQuery() {
        return (SubjectSearchQuery) searchAdaptor;
    }

    public void setSecondName(String name) {
        getSubjectSearchQuery().setSecondName(name);
    }

    public String getSecondName() {
        return getSubjectSearchQuery().getSecondName();
    }

    public void setFirstName(String name) {
        getSubjectSearchQuery().setFirstName(name);
    }

    public String getFirstName() {
        return getSubjectSearchQuery().getFirstName();
    }

    public String getPositionTitle() {
        return getSubjectSearchQuery().getPositionTitle();
    }

    public void setPositionTitle(String queryText) {
        getSubjectSearchQuery().setPositionTitle(queryText);
    }

    public SubjectSearchQuery getModifiedSubjectSearchQuery() {
        return (SubjectSearchQuery) getNodeSearchQuery();
    }

    public void setIncludeQuestionnaire(boolean includeQuestionnaire) {
        this.includeQuestionnaire = includeQuestionnaire;
    }

    public boolean isIncludeQuestionnaire() {
        return includeQuestionnaire;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }


    private boolean includeQuestionnaire;
    private String nodeType;
}
