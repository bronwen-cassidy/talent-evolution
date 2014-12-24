/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.util.FormatterFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The form bean for an objective definition.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Mar-2007 17:41:13
 */
public class CorporateObjectivesFormBean implements Serializable {

    public CorporateObjectivesFormBean() {
    }

    public CorporateObjectivesFormBean(List<ObjectiveWrapperBean> objectives, ObjectiveSet objectiveSet) {
        this.objectiveSet = objectiveSet;
        this.objectives = objectives;
    }

    public Collection<ObjectiveSet> getObjectiveSets() {
        return objectiveSets;
    }

    public void setObjectiveSets(Collection<ObjectiveSet> objectiveSets) {
        this.objectiveSets = objectiveSets;
    }

    public void setObjectiveSet(ObjectiveSet objectiveSet) {
        this.objectiveSet = objectiveSet;
    }

    public ObjectiveSet getObjectiveSet() {
        return objectiveSet;
    }

    /**
     * Method to call on completed addition, modification of a corporate objective set.
     *
     * @return objectiveSet.
     */
    public ObjectiveSet getModifiedObjectiveSet() {
        
        objectiveSet.getObjectives().clear();
        for (ObjectiveWrapperBean wrapperBean : objectives) {
            Objective objective = wrapperBean.getModifiedObjective();
            objectiveSet.addObjective(objective);
        }
        return objectiveSet;
    }

    public List<ObjectiveWrapperBean> getObjectives() {
        return objectives;
    }

    public void addObjective(ObjectiveWrapperBean wrapperBean) {
        objectives.add(wrapperBean);
    }

    public String getExpiryDate() {
        return objectiveSet != null ? FormatterFactory.getDateFormatter().formatDateAsString(objectiveSet.getExpiryDate()) : null;
    }

    public void deleteObjective(int index) {
        objectives.remove(index);
    }

    public boolean isCanPublish() {
        
        for (ObjectiveSet set : objectiveSets) {
            if(set.isPublished()) return false;
        }
        return true;
    }

    private Collection<ObjectiveSet> objectiveSets = new ArrayList<ObjectiveSet>();
    private ObjectiveSet objectiveSet;
    private List<ObjectiveWrapperBean> objectives = new ArrayList<ObjectiveWrapperBean>();
}
