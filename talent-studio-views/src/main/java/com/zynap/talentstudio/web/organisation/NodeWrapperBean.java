package com.zynap.talentstudio.web.organisation;

import com.zynap.common.util.StringUtil;
import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.NodeAudit;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValuesCollection;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.util.AuditUtils;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 07-Feb-2005
 * Time: 14:57:12
 * Base class for Node wrappers.
 */
public abstract class NodeWrapperBean extends ZynapDomainObject implements Serializable {

    /**
     * Constructor.
     *
     * @param node The Node
     */
    protected NodeWrapperBean(Node node) {

        // call this method to load the node audits collection which otherwise is lazily loaded
        // - do not remove otherwise setNodeAudit() below will fail
        // will be null if the node is a new node
        final Collection<NodeAudit> nodeAudits = node.getNodeAudits();
        if (nodeAudits != null) nodeAudits.size();
    }

    public final void setWrappedDynamicAttributes(List<FormAttribute> wrappedDynamicAttributes) {
        this.wrappedDynamicAttributes = wrappedDynamicAttributes;
    }

    public final List<FormAttribute> getWrappedDynamicAttributes() {
        if (wrappedDynamicAttributes == null) wrappedDynamicAttributes = new ArrayList<>();
        return wrappedDynamicAttributes;
    }

    public final void setPortfolioItems(Collection<PortfolioItem> portfolioItems) {
        this.portfolioItems = portfolioItems;
    }

    public final Collection<PortfolioItem> getPortfolioItems() {
        if (portfolioItems == null) portfolioItems = new HashSet<PortfolioItem>();
        return portfolioItems;
    }

    public abstract Node getNode();

    public abstract String getLabel();

    public abstract void setLabel(String label);

    public abstract Long getParentId();

    /**
     * Reset ids.
     * <br/> Required to ensure that persistence layer still sees them as new unsaved objects.
     */
    public void resetIds() {

        resetDynamicAttributeValueIds();
        getNode().setId(null);

        resetIdsInternal();
    }

    protected abstract void resetIdsInternal();

    public final Long getId() {
        final Node node = getNode();
        return node != null ? node.getId() : null;
    }

    public final boolean isActive() {
        return getNode().isActive();
    }

    public final void setActive(boolean active) {
        getNode().setActive(active);
    }

    public final void setActive(String active) {
        setActive(StringUtil.convertToBoolean(active));
    }

    public final boolean isHasAccess() {
        return getNode().isHasAccess();
    }

    public final Long getDeleteImageIndex() {
        return deleteImageIndex;
    }

    public final void setDeleteImageIndex(Long deleteImageIndex) {
        this.deleteImageIndex = deleteImageIndex;
    }

    public final void setDisplayConfigItem(DisplayConfigItem configItem) {
        this.displayConfigItem = configItem;
    }

    public final DisplayConfigItem getDisplayConfigItem() {
        return displayConfigItem;
    }

    public final String getDisplayContentLabel() {
        return displayConfigItem.getReportItems() != null ? displayConfigItem.getFirstReportLabel() : null;
    }

    public final void setCoreValues(List<Column> coreAttributes) {
        this.coreValues = coreAttributes;
    }

    public final List getCoreValues() {
        return coreValues;
    }

    public abstract List getSubjectSecondaryAssociations();

    public abstract List getSubjectPrimaryAssociations();

    protected final void setNodeAudit(User user) {
        AuditUtils.setNodeAudit(getNode(), user);
    }

    protected final void assignModifiedAssociations(final Set<ArtefactAssociation> outputCollection, List<ArtefactAssociationWrapperBean> inputCollection) {
        for (Iterator iterator = inputCollection.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) iterator.next();
            outputCollection.add(artefactAssociationWrapperBean.getModifiedAssociation());
        }
    }

    /**
     * Resets dynamic attribute value ids to null.
     */
    private void resetDynamicAttributeValueIds() {
        final AttributeValuesCollection dynamicAttributeValues = getNode().getDynamicAttributeValues();
        for (Iterator iterator = dynamicAttributeValues.getValues().iterator(); iterator.hasNext();) {
            AttributeValue attributeValue = (AttributeValue) iterator.next();
            attributeValue.setId(null);
        }
    }

    public int getNumPortfolioItems() {
        return getPortfolioItems().size();
    }

    private List<FormAttribute> wrappedDynamicAttributes;
    private Collection<PortfolioItem> portfolioItems = new ArrayList<PortfolioItem>();
    private Long deleteImageIndex;
    private DisplayConfigItem displayConfigItem;
    private List<Column> coreValues;
}
