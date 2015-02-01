package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * User: jsueiras
 * Date: 07-Oct-2005
 * Time: 14:03:46
 */
public class OrganisationUnitWrapperBean extends NodeWrapperBean implements Serializable {

    public OrganisationUnitWrapperBean(OrganisationUnit organisationUnit) {
        super(organisationUnit);
        this.organisationUnit = organisationUnit;
        
        final Collection nodeAudits = this.organisationUnit.getNodeAudits();
        if(nodeAudits != null) nodeAudits.size();

        final OrganisationUnit parentOrganisationUnit = organisationUnit.getParent();
        parentId = parentOrganisationUnit == null ? null : parentOrganisationUnit.getId();
        parentLabel = parentOrganisationUnit == null ? null : parentOrganisationUnit.getLabel();
    }

    public Long getParentId() {
        return parentId;
    }

    protected void resetIdsInternal() {

    }

    public Node getNode() {
        return organisationUnit;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    public String getLabel() {
        return organisationUnit.getLabel();
    }

    public void setLabel(String label) {
        organisationUnit.setLabel(label);
    }

    public List getSubjectSecondaryAssociations() {
        return null;
    }

    public List getSubjectPrimaryAssociations() {
        return null;
    }

    public String getComments() {
        return organisationUnit.getComments();
    }

    public void setId(Long id) {
        organisationUnit.setId(id);
    }

    public void setComments(String comments) {
        organisationUnit.setComments(comments);
    }

    public OrganisationUnit getModifiedOrganisationUnit(User user) {
        
        DynamicAttributesHelper.assignAttributeValuesToNode(getWrappedDynamicAttributes(), organisationUnit);
        
        if (!OrganisationUnit.ROOT_ORG_UNIT_ID.equals(getId())) {
            organisationUnit.setParent(new OrganisationUnit(parentId, parentLabel));
        }
        setNodeAudit(user);
        return organisationUnit;
    }

    public boolean isDefault() {
        return organisationUnit.isDefault();
    }

    OrganisationUnit organisationUnit;
    private Long parentId;
    private String parentLabel;
}
