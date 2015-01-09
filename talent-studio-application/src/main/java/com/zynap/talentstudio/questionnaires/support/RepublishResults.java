/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 20-Jun-2007 09:53:00
 */

public class RepublishResults {

    public void setUsersAdded(String[] usersAdded) {
        this.usersAdded = usersAdded;
    }

    public String[] getUsersRemoved() {
        return usersRemoved;
    }

    public String[] getUsersAdded() {
        return usersAdded;
    }

    public void setUsersRemoved(String[] usersRemoved) {
        this.usersRemoved = usersRemoved;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Long getQueId() {
        return queId;
    }

    public void setQueId(Long queId) {
        this.queId = queId;
    }

    private Long queId;
    private String[] usersAdded;
    private String[] usersRemoved;
    private Date completedDate;
    
}
