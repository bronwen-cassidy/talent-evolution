package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.web.analysis.GroupableBean;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Collection;


/**
 * Wrapper for populations.
 */
public class PopulationWrapperBean extends GroupableBean {

    public PopulationWrapperBean() {
        scopeChangeable = true;
    }

    public PopulationWrapperBean(Population population) {
        this();
        this.population = population;
    }

    public PopulationWrapperBean(Population population, AnalysisAttributeCollection analysisAttributeCollection) {

        this(population);
        this.attributeCollection = analysisAttributeCollection;
        buildWrappers(population);
        // get the dynamic attributes for the criteria and set them on the CriteriaWrapperBeans
        initCriteriaWrappers(analysisAttributeCollection);
    }

    public void buildWrappers(Population population) {
        populationCriterias = new ArrayList<CriteriaWrapperBean>();
        List<PopulationCriteria> criterias = population.getPopulationCriterias();
        PopulationCriteria currentCriteria;
        if (criterias != null && criterias.size() > 0) {
            CriteriaWrapperBean previousWrapC = new CriteriaWrapperBean(criterias.get(0));
            parseLeft(criterias.get(0), previousWrapC);
            addPopulationCriteria(previousWrapC);
            for (int i = 1; i < criterias.size() - 1; i++) {
                currentCriteria = criterias.get(i);
                parseRight(currentCriteria, previousWrapC);
                // The logical operator is saved in the inner next previousCriteria..
                CriteriaWrapperBean wc = new CriteriaWrapperBean(currentCriteria);
                parseLeft(currentCriteria, wc);
                previousWrapC = wc;
                addPopulationCriteria(wc);
            }
            currentCriteria = criterias.get(criterias.size() - 1);
            parseRight(currentCriteria, previousWrapC);
            // If the last criteria is just a closing bracket ..
            // we dont need to create the last wrapper for this criteria
            if (!currentCriteria.getType().equals(IPopulationEngine.BRCKT_TYPE_) && criterias.size() > 1) {
                CriteriaWrapperBean wc = new CriteriaWrapperBean(currentCriteria);
                parseLeft(currentCriteria, wc);
                addPopulationCriteria(wc);
            }
        }
    }

    public void removeInvalidCriterias() {
        if (populationCriterias != null) {
            List toRemove = new ArrayList();
            for (Iterator iterator = populationCriterias.iterator(); iterator.hasNext();) {
                CriteriaWrapperBean criteriaWrapperBean = (CriteriaWrapperBean) iterator.next();
                if (criteriaWrapperBean.isInvalid()) toRemove.add(criteriaWrapperBean);
            }

            populationCriterias.removeAll(toRemove);
        }
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public Collection<Group> getAssignedGroups() {
        return population.getGroups();
    }

    private void parseLeft(PopulationCriteria criteria, CriteriaWrapperBean wrapper) {
        if (criteria.getOperator() != null) {
            if (criteria.getOperator().indexOf(IPopulationEngine.LEFT_BRCKT_) >= 0)
                wrapper.setLeftBracket(IPopulationEngine.LEFT_BRCKT_);
            else
                wrapper.setLeftBracket("");
            wrapper.setInverse(criteria.getOperator().indexOf(IPopulationEngine.NOT) >= 0);
        } else {
            wrapper.setLeftBracket("");
            wrapper.setInverse(false);
        }
    }

    private void parseRight(PopulationCriteria criteria, CriteriaWrapperBean wrapper) {

        if (criteria.getType().equals(IPopulationEngine.BRCKT_TYPE_))
            wrapper.setRightBracket(IPopulationEngine.RIGHT_BRCKT_);
        else {
            if (criteria.getOperator() != null) {
                if (criteria.getOperator().indexOf(IPopulationEngine.RIGHT_BRCKT_) >= 0)
                    wrapper.setRightBracket(IPopulationEngine.RIGHT_BRCKT_);
                else
                    wrapper.setRightBracket("");
                int begin = criteria.getOperator().indexOf(IPopulationEngine.RIGHT_BRCKT_);
                int end = criteria.getOperator().indexOf(IPopulationEngine.LEFT_BRCKT_);
                if (end == -1) end = criteria.getOperator().indexOf(IPopulationEngine.NOT);
                end = (end >= 0) ? end : criteria.getOperator().length();
                wrapper.setOperator(" " + criteria.getOperator().substring(begin + 1, end).trim() + " ");
            }
        }
    }

    public List getPopulationCriterias() {
        return this.populationCriterias;
    }

    public void setPopulationCriterias(List<CriteriaWrapperBean> populationCriterias) {
        this.populationCriterias = populationCriterias;
    }

    public void addPopulationCriteria(CriteriaWrapperBean criteria) {
        if (populationCriterias == null) populationCriterias = new ArrayList<CriteriaWrapperBean>();
        populationCriterias.add(criteria);
    }

    public Population getPopulation() {
        return population;
    }

    public Population getModifiedPopulation() {
        if (populationCriterias != null && populationCriterias.size() > 0) {
            CriteriaWrapperBean currentCriteria = populationCriterias.get(0);
            currentCriteria.getCriteria().setOperator(currentCriteria.getLeftBracket() + currentCriteria.getNot());
            currentCriteria.updateRefValue();

            for (int i = 1; i < populationCriterias.size(); i++) {
                // The logical operator is saved in the inner next previousCriteria..
                CriteriaWrapperBean previousCriteria = populationCriterias.get(i - 1);
                currentCriteria = populationCriterias.get(i);
                PopulationCriteria pc = currentCriteria.getCriteria();
                pc.setOperator(previousCriteria.getRightBracket() + previousCriteria.getOperator() + currentCriteria.getLeftBracket() + currentCriteria.getNot());
                currentCriteria.updateRefValue();
            }
            if (currentCriteria.getRightBracket() != null && currentCriteria.getRightBracket().trim().length() > 0) {
                PopulationCriteria pc = new PopulationCriteria(IPopulationEngine.BRCKT_TYPE_);
                populationCriterias.add(new CriteriaWrapperBean(pc));
            }
        }
        updatePopulationCriteria();
        assignNewGroups(population);
        return population;
    }

    private void updatePopulationCriteria() {
        if (population.getPopulationCriterias() != null) population.getPopulationCriterias().clear();
        for (int i = 0; i < populationCriterias.size(); i++) {
            population.addPopulationCriteria((populationCriterias.get(i)).getCriteria());
        }
    }

    protected void assignNewGroups(Population population) {
        Set<Group> newGroups = new LinkedHashSet<Group>();
        for (SelectionNode selectionNode : groups) {
            if (selectionNode.isSelected()) {
                newGroups.add((Group) selectionNode.getValue());
            }
        }
        population.assignNewGroups(newGroups);
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public void setScopeChangeable(boolean scopeChangeable) {
        this.scopeChangeable = scopeChangeable;
    }

    public boolean isScopeChangeable() {
        return scopeChangeable;
    }

    public AnalysisAttributeCollection getAttributeCollection() {
        return attributeCollection;
    }

    public void setAttributeCollection(AnalysisAttributeCollection attributeCollection) {
        this.attributeCollection = attributeCollection;
    }

    /**
     * Index is the index of the attribute being modified in the list of population criteria in the command object.
     *
     * @return index
     */
    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getType() {
        return population != null ? population.getType() : null;
    }

    public RunReportWrapperBean getResultsetPreview() {
        return resultsetPreview;
    }

    public void setResultsetPreview(RunReportWrapperBean resultsetPreview) {
        this.resultsetPreview = resultsetPreview;
    }

    /**
     * Check that the criteria at the specified index has the correct AttributeWrapperBean (the AttributeWrapperBean contains the dynamic attribute definition.).
     *
     * @param index The position in the list of population criteria
     */
    public void checkCriteriaAttribute(Long index) {

        final CriteriaWrapperBean criteria = populationCriterias.get(index.intValue());
        if (criteria != null && criteria.isAttributeSet()) {
            attributeCollection.setAttributeDefinition(criteria);
            criteria.setNodeLabel(null);
        }
    }

    /**
     * Loop through CriteriaWrapperBean objects in populationCriterias Collection
     * and make sure they all have the correct AttributeWrapperBean (the AttributeWrapperBean contains the dynamic attribute definition.)
     */
    public void checkCriteriaAttributes() {

        for (Iterator it = populationCriterias.iterator(); it.hasNext();) {
            CriteriaWrapperBean criteria = (CriteriaWrapperBean) it.next();
            if (criteria.hasAttributeName()) {
                if (criteria.hasIncorrectAttributeDefinition()) {
                    attributeCollection.setAttributeDefinition(criteria);
                    final AttributeWrapperBean attributeWrapperBean = criteria.getAttributeDefinition();
                    if (attributeWrapperBean != null) {
                        criteria.setRefValue(null);
                    }
                } else
                    criteria.updateRefValue();
            } else {
                criteria.setAttribute(null);
                criteria.setAttributeDefinition(null);
                criteria.setRefValue(null);
            }
        }
    }

    /**
     * Loop through CriteriaWrapperBean objects in populationCriterias Collection
     * and make sure they all have the correct AttributeWrapperBean (the AttributeWrapperBean contains the dynamic attribute definition.)
     *
     * @param analysisAttributeCollection
     */
    private void initCriteriaWrappers(AnalysisAttributeCollection analysisAttributeCollection) {
        for (Iterator iterator = populationCriterias.iterator(); iterator.hasNext();) {
            final CriteriaWrapperBean criteria = (CriteriaWrapperBean) iterator.next();

            analysisAttributeCollection.setAttributeDefinition(criteria);
            final AttributeWrapperBean attributeWrapperBean = criteria.getAttributeDefinition();
            if (!criteria.isInvalid()) {
                final AttributeValue attributeValue = attributeWrapperBean.getAttributeValue();
                attributeValue.setValue(criteria.getRefValue());
                attributeWrapperBean.setAttributeValue(attributeValue);
            }
        }
    }

    public boolean hasOrganisationUnitCriteria() {
        if (populationCriterias == null) return false;
        for (Iterator iterator = populationCriterias.iterator(); iterator.hasNext();) {
            CriteriaWrapperBean criteriaWrapperBean = (CriteriaWrapperBean) iterator.next();
            if (criteriaWrapperBean.isOrganisationUnit()) return true;
        }
        return false;
    }

    public boolean isOrganisationPickerList() {
        return hasOrganisationUnitCriteria();
    }

    public boolean isSubjectPickerList() {
        return hasSubjectCriteria();
    }

    public boolean isPositionPickerList() {
        return hasPositionCriteria();
    }

    public boolean hasPositionCriteria() {
        if (populationCriterias == null) return false;
        for (Iterator iterator = populationCriterias.iterator(); iterator.hasNext();) {
            CriteriaWrapperBean criteriaWrapperBean = (CriteriaWrapperBean) iterator.next();
            if (criteriaWrapperBean.isPosition()) return true;
        }
        return false;
    }

    public boolean hasSubjectCriteria() {
        if (populationCriterias == null) return false;
        for (Iterator iterator = populationCriterias.iterator(); iterator.hasNext();) {
            CriteriaWrapperBean criteriaWrapperBean = (CriteriaWrapperBean) iterator.next();
            if (criteriaWrapperBean.isSubject()) return true;
        }
        return false;
    }

    /**
     * Method used in the jsp's to see if the edit and delete buttons should be removed.
     * This is required only if the populations are the 'All Positions' and 'All People' populations.
     *
     * @return true if the population is the all positions or all people populations and the populaiton is not null.
     */
    public boolean isAllPopulation() {
        return population != null && (IPopulationEngine.ALL_POSITIONS_POPULATION_ID.equals(population.getId()) || IPopulationEngine.ALL_PEOPLE_POPULATION_ID.equals(population.getId()));
    }

    public int getPageStart() {
        return resultsetPreview != null ? resultsetPreview.getPageStart() : 0;
    }

    public void setPageStart(int pageStart) {
        if (resultsetPreview != null) {
            resultsetPreview.setPageStart(pageStart);
        }
    }

    private Population population;

    private AnalysisAttributeCollection attributeCollection;

    private int target;

    private Long index;

    private List<CriteriaWrapperBean> populationCriterias;

    private boolean scopeChangeable;

    private RunReportWrapperBean resultsetPreview;
}
