/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jan-2007 11:30:06
 */
public class BlogCommentQuestionWrapper extends QuestionAttributeWrapperBean {

    public BlogCommentQuestionWrapper(QuestionAttribute question) {
        super(question);
        blogComments = new ArrayList<BlogCommentBean>();
    }

    public BlogCommentQuestionWrapper(QuestionAttribute question, AttributeValue attributeValue, User user) {
        super(question, attributeValue);
        blogComments = new ArrayList<BlogCommentBean>();
        this.user = user;
        initializeBlogComments();
    }

    private void initializeBlogComments() {
        List<NodeExtendedAttribute> comments = attributeValue.getNodeExtendedAttributes();
        if(comments != null) {
            for (NodeExtendedAttribute nodeExtendedAttribute : comments) {
                blogComments.add(new BlogCommentBean(nodeExtendedAttribute));
            }
        }
    }

    public Long getAttributeValueId(){
        return attributeValueId;
    }

    public void setAttributeValueId(Long attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    public AttributeValue getModifiedAttributeValue() {

        if(StringUtils.hasText(lastComment)) {
            BlogCommentBean blogCommentBean = new BlogCommentBean();
            blogCommentBean.setAddedBy(user);
            blogCommentBean.setDateAdded(new Date());
            blogCommentBean.setComment(lastComment);
            addBlogComment(blogCommentBean);
        }

        final DynamicAttribute dynamicAttribute = getAttributeDefinition();
        final AttributeValue newAttributeValue = AttributeValue.create(dynamicAttribute);

        for (BlogCommentBean blogCommentBean : blogComments) {
            final String value = blogCommentBean.getComment();
            if (StringUtils.hasText(value)) {
                NodeExtendedAttribute nodeExtendedAttribute = new NodeExtendedAttribute(value, dynamicAttribute);
                nodeExtendedAttribute.setAddedBy(blogCommentBean.getAddedBy());
                nodeExtendedAttribute.setDateAdded(blogCommentBean.getDateAdded());
                newAttributeValue.addValue(nodeExtendedAttribute, false);
            }
        }

        return newAttributeValue;
    }

    public void addBlogComment(BlogCommentBean commentBean) {
        blogComments.add(commentBean);
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }

    public List getBlogComments() {
        return blogComments;
    }

    private List<BlogCommentBean> blogComments;
    private User user;
    private String lastComment;
    private Long attributeValueId;
}
