/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.rules;

import com.zynap.domain.ZynapDomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Config extends ZynapDomainObject {

    public Config() {
    }

    public Config(Long id, String label, String comments) {
        super(id, label);
        this.comments = comments;
    }

    public Config(Long id, String label, String comments, boolean active, Collection<Rule> rules) {
        super(id, active, label);
        this.comments = comments;
        this.rules = rules;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Collection<Rule> getRules() {
        return rules;
    }

    public void setRules(Collection<Rule> rules) {
        this.rules = new ArrayList<Rule>(rules);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("comments", getComments())
                .append("isActive", isActive())
                .toString();
    }

    /**
     * Get rule.
     *
     * @param ruleId The rule id
     * @return The Rule or null
     */
    public Rule getRule(final Long ruleId) {
        return (Rule) CollectionUtils.find(getRules(), new Predicate() {
            public boolean evaluate(Object object) {
                Rule rule = (Rule) object;
                if (rule.getId().equals(ruleId)) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * nullable persistent field
     */
    private String comments;
    
    /**
     * persistent field
     */
    private Collection<Rule> rules;
    public static final Long PASSWORD_CONFIG_ID = new Long(-1);
    public static final Long USERNAME_CONFIG_ID = new Long(-2);
    public static final Long AUTHORISATION_CONFIG_ID = new Long(-3);
}
