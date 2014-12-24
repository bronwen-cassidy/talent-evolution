/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.util.FormatterFactory;

import java.util.Date;

/**
 * Class which represents the view of a blog comment. It wraps the addedBy, dateAdded
 * and comment value for a single comment.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jan-2007 11:32:31
 */
public class BlogCommentBean {

    public BlogCommentBean(NodeExtendedAttribute nodeExtendedAttribute) {
        setAddedBy(nodeExtendedAttribute.getAddedBy());
        setDateAdded(nodeExtendedAttribute.getDateAdded());
        setComment(nodeExtendedAttribute.getValue());
    }


    public BlogCommentBean() {
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDisplayDate() {
        return FormatterFactory.getDateFormatter().formatDateTimeAsString(dateAdded);
    }

    private User addedBy;
    private Date dateAdded;
    private String comment;
}
