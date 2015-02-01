package com.zynap.talentstudio.web.workflow.performance;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import java.io.Serializable;
import java.util.List;

/**
 * User: amark
 * Date: 06-Dec-2005
 * Time: 14:02:11
 * Class that represents an answer to a given question - used when a manager views evaluator answers for an appraisal.
 * <br/> Each answer is provided by a different user who fills a role - hence the fields.
 * <br/> The answer is an AttributeValue.
 */
public final class PerformanceReviewAnswer implements Serializable, Comparable {

    private final User user;

    private final LookupValue role;

    private final AttributeValue attributeValue;

    public PerformanceReviewAnswer(User user, LookupValue role, AttributeValue attributeValue) {
        this.user = user;
        this.role = role;
        this.attributeValue = attributeValue;
    }

    public String getUserName() {
        return user.getLabel();
    }

    public String getRole() {
        return role.getLabel();
    }

    public String[] getDisplayValue() {

        final String nodeLabel = attributeValue.getLabel();
        final List nodeExtendedAttributes = attributeValue.getNodeExtendedAttributes();
        final String[] results = new String[nodeExtendedAttributes.size()];

        for (int i = 0; i < nodeExtendedAttributes.size(); i++) {
            final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttributes.get(i);
            results[i] = ArtefactAttributeViewFormatter.formatValue(nodeExtendedAttribute, nodeLabel);
        }

        return results;
    }

    public int compareTo(Object o) {
        PerformanceReviewAnswer other = (PerformanceReviewAnswer) o;
        return role.compareBySortOrder(other.role);
    }
}
