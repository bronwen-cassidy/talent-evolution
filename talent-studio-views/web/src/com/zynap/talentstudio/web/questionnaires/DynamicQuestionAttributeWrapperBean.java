/* Copyright: Copyright (c) 2004
 * Company:
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Subclass of attribute wrapper bean that handles dynamic line item questions.
 *
 * @author Angus Mark
 */
public class DynamicQuestionAttributeWrapperBean extends QuestionAttributeWrapperBean {

    /**
     * Builds collection of QuestionAttributeWrapperBean for each NodeExtendedAttribute in the AttributeValue.
     * <br/> If there are no NodeExtendedAttributes adds several up to the value of maxItems.
     *
     * @param question the question attribute
     * @param attributeValue the answer to the question
     * @param maxItems the maximum number of answers/questions in the dynamic line item
     */
    public DynamicQuestionAttributeWrapperBean(QuestionAttribute question, AttributeValue attributeValue, int maxItems) {
        super(question, attributeValue);

        questionWrappers = new ArrayList<QuestionAttributeWrapperBean>();

        // build QuestionAttributeWrapperBeans
        for (int i = 0; i <= maxItems; i++) {
            final QuestionAttributeWrapperBean questionAttributeWrapperBean = new QuestionAttributeWrapperBean(question, AttributeValue.create(attributeDefinition));
            questionAttributeWrapperBean.setDynamicPosition(i);
            questionWrappers.add(questionAttributeWrapperBean);
        }

        // if attribute value has node extended attributes set value on correct wrapper
        final List nodeExtendedAttributes = attributeValue.getNodeExtendedAttributes();
        for (Object nodeExtendedAttribute1 : nodeExtendedAttributes) {
            final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttribute1;
            final int position = nodeExtendedAttribute.getDynamicPosition();
            final QuestionAttributeWrapperBean questionAttributeWrapperBean = questionWrappers.get(position);
            questionAttributeWrapperBean.setAttributeValue(AttributeValue.create(nodeExtendedAttribute));
        }
    }

    /**
     * Build new AttributeValue.
     * <br/> Then add NodeExtendedAttributes for each QuestionAttributeWrapperBean in questionWrappers collection.
     *
     * @return AttributeValue
     */
    public AttributeValue getModifiedAttributeValue() {

        final DynamicAttribute dynamicAttribute = getAttributeDefinition();
        final AttributeValue newAttributeValue = AttributeValue.create(dynamicAttribute);
        newAttributeValue.setDisabled(attributeValue.isDisabled());

        for (int i = 0; i < questionWrappers.size(); i++) {
            QuestionAttributeWrapperBean questionAttributeWrapperBean = questionWrappers.get(i);
            final Integer dynamicPosition = questionAttributeWrapperBean.getDynamicPosition();

            final String value = questionAttributeWrapperBean.getValue();
            if (StringUtils.hasText(value)) {

                NodeExtendedAttribute nodeExtendedAttribute = new NodeExtendedAttribute(value, dynamicAttribute);
                nodeExtendedAttribute.setDynamicPosition(dynamicPosition);
                nodeExtendedAttribute.setDisabled(questionAttributeWrapperBean.isDisabled());
                newAttributeValue.addValue(nodeExtendedAttribute, false);
            }
        }

        return newAttributeValue;
    }

    /**
     * Add new QuestionAttributeWrapperBean to list.
     * <br/> Also sets dynamic position.
     */
    public void addDynamicLineItem() {
        final QuestionAttributeWrapperBean questionAttributeWrapperBean = new QuestionAttributeWrapperBean(getQuestion());
        questionAttributeWrapperBean.setDynamicPosition(questionWrappers.size());
        questionAttributeWrapperBean.setDisabled(false);
        questionWrappers.add(questionAttributeWrapperBean);        
    }

    /**
     * Delete QuestionAttributeWrapperBean from list.
     * <br/> Also decreases dynamic position of subsequent QuestionAttributeWrapperBeans.
     *
     * @param index the index of the dynamic line item to delete
     */
    public void deleteDynamicLineItem(Long index) {
        if (index != null) {
            final int pos = index.intValue();
            final int count = questionWrappers.size();
            if (pos < count) {
                questionWrappers.remove(pos);
                final List subList = questionWrappers.subList(pos, count - 1);
                for (int i = 0; i < subList.size(); i++) {
                    QuestionAttributeWrapperBean questionAttributeWrapperBean = (QuestionAttributeWrapperBean) subList.get(i);
                    final Integer currentPosition = questionAttributeWrapperBean.getDynamicPosition();
                    final int newPosition = currentPosition.intValue() - 1;
                    questionAttributeWrapperBean.setDynamicPosition(newPosition);
                }
            }
        }
    }

    /**
     * Get QuestionAttributeWrapperBeans.
     *
     * @return Collection of QuestionAttributeWrapperBeans.
     */
    public List getQuestionWrappers() {
        return questionWrappers;
    }

    private final List<QuestionAttributeWrapperBean> questionWrappers;
}
