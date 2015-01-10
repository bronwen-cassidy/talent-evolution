package com.zynap.talentstudio.integration.adapter;

import com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by bronwen.
 * Date: 22/03/12
 * Time: 10:33
 */
public class TaapiMappingUtils {

    public static void mapSubjectAttributes(Subject s, Collection<DynamicAttribute> attrs, RespondentInfoType respondent) {

        assignAttributeValue(s, attrs, respondent.getACT_C(), "actC");
        assignAttributeValue(s, attrs, respondent.getACT_E(), "actE");
        assignAttributeValue(s, attrs, respondent.getACT_O(), "actO");
        assignAttributeValue(s, attrs, respondent.getACT_R(), "actR");
        assignAttributeValue(s, attrs, respondent.getMATCH_AMB(), "matchAMB");
        assignAttributeValue(s, attrs, respondent.getMATCH_CORE(), "matchCORE");
        assignAttributeValue(s, attrs, respondent.getMATCH_TOTAL(), "matchTOTAL");
        assignAttributeValue(s, attrs, respondent.getMOD_C(), "modC");
        assignAttributeValue(s, attrs, respondent.getMOD_E(), "modE");
        assignAttributeValue(s, attrs, respondent.getMOD_O(), "modO");
        assignAttributeValue(s, attrs, respondent.getMOD_R(),"modR");
        assignAttributeValue(s, attrs, respondent.getRANK_ALT(),"rankALT");
        assignAttributeValue(s, attrs, respondent.getRANK_AUT(),"rankAUT");
        assignAttributeValue(s, attrs, respondent.getRANK_CRE(),"rankCRE");
        assignAttributeValue(s, attrs, respondent.getRANK_ECO(),"rankECO");
        assignAttributeValue(s, attrs, respondent.getRANK_IND(),"rankIND");
        assignAttributeValue(s, attrs, respondent.getRANK_POL(),"rankPOL");
        assignAttributeValue(s, attrs, respondent.getRANK_THE(),"rankTHE");
        assignAttributeValue(s, attrs, respondent.getSCORE_ALT(),"scoreALT");
        assignAttributeValue(s, attrs, respondent.getSCORE_AUT(),"scoreAUT");
        assignAttributeValue(s, attrs, respondent.getSCORE_CRE(),"scoreCRE");
        assignAttributeValue(s, attrs, respondent.getSCORE_ECO(),"scoreECO");
        assignAttributeValue(s, attrs, respondent.getSCORE_IND(),"scoreIND");
        assignAttributeValue(s, attrs, respondent.getSCORE_POL(),"scorePOL");
        assignAttributeValue(s, attrs, respondent.getSCORE_THE(),"scoreTHE");
    }

    public static void removeTaapiAttributes(Subject s,Collection<DynamicAttribute> attrs) {
        for (int i = 0; i < externalAttributeLabels.length; i++) {
            String externalAttributeLabel = externalAttributeLabels[i];
            s.removeAttributeValue(AttributeValue.create(findAttr(attrs, externalAttributeLabel)));
        }
    }

    public static void assignAttributeValue(Subject s, Collection<DynamicAttribute> attrs, Object value, String refLabel) {
        DynamicAttribute attr = findAttr(attrs, refLabel);

        if(attr != null && value != null) {
            //clearPreviousAnswer(s, attr);
            AttributeValue attributeValue = AttributeValue.create(value.toString(), attr);
            NodeExtendedAttribute nodeExtendedAttribute = attributeValue.getNodeExtendedAttribute(0);
            nodeExtendedAttribute.setDateAdded(new Date());
            //nodeExtendedAttribute.setAddedBy();
            s.addAttributeValue(attributeValue);
        }
    }

    public static void assignAttributeValue(Subject s, Collection<DynamicAttribute> attrs, Object[] values, String[] refLabels) {
        for (int i = 0; i < refLabels.length; i++) {
            String refLabel = refLabels[i];
            Object value = values.length >= i ? values[i] : null;
            assignAttributeValue(s, attrs, value, refLabel);
        }
    }

    private static void clearPreviousAnswer(Subject subject, final DynamicAttribute attr) {
        NodeExtendedAttribute answer = (NodeExtendedAttribute) CollectionUtils.find(subject.getExtendedAttributes(), new Predicate() {
            public boolean evaluate(Object o) {
                return ((NodeExtendedAttribute) o).getDynamicAttribute().equals(attr);
            }
        });
        if(answer != null) subject.removeNodeExtendedAttribute(answer);
    }

    public static List<DynamicAttribute> findAttrs(final String[] values, Collection<DynamicAttribute> attributes) {
        List<DynamicAttribute> result = new ArrayList<DynamicAttribute>();

        for (int i = 0; i < values.length; i++) {
            final String value = values[i];
            DynamicAttribute e = (DynamicAttribute) CollectionUtils.find(attributes, new Predicate() {
                public boolean evaluate(Object o) {
                    DynamicAttribute attr = (DynamicAttribute) o;
                    return value.equals(attr.getExternalRefLabel());
                }
            });
            
            result.add(e);
        }
        return result;
    }

    public static NodeExtendedAttribute getValue(final String value, Set<NodeExtendedAttribute> attributes) {
        return (NodeExtendedAttribute) CollectionUtils.find(attributes, new Predicate() {
            public boolean evaluate(Object o) {
                NodeExtendedAttribute attr = (NodeExtendedAttribute) o;
                return value.equals(attr.getDynamicAttribute().getExternalRefLabel());
            }
        });
    }

    public static DynamicAttribute findAttr(Collection<DynamicAttribute> attributes, final String value) {
        return (DynamicAttribute) CollectionUtils.find(attributes, new Predicate() {
            public boolean evaluate(Object o) {
                DynamicAttribute attr = (DynamicAttribute) o;
                return value.equals(attr.getExternalRefLabel());
            }
        });
    }

    public static final String[] externalAttributeLabels = {"actC","actE","actO","actR","matchAMB","matchCORE","matchTOTAL","modC","modE","modO","modR","rankALT","rankAUT", "rankCRE",
            "rankECO","rankIND","rankPOL","rankTHE","scoreALT","scoreAUT","scoreCRE","scoreECO","scoreIND","scorePOL","scoreTHE"};
}
