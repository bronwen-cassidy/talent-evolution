/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.exception.TalentStudioException;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;


/**
 * Class or Interface description.
 *
 * @author jsueiras
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BasePopulationWizardController extends DefaultWizardFormController {

    protected Object recoverFormBackingObject(HttpServletRequest request) throws Exception {
        return HistoryHelper.recoverCommand(request, PopulationWrapperBean.class);
    }

    static Long getPopulationId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.POPULATION_ID);
    }

    protected BasePopulationWizardController() {
        setCommandName(ControllerConstants.COMMAND_NAME);
        setAllowDirtyBack(true);
        setBindOnNewForm(false);
    }

    protected final Collection<Group> getUserGroups() throws TalentStudioException {
        return groupService.find(Group.TYPE_HOMEPAGE);
    }

    protected Collection<SelectionNode> wrapGroups(Collection<Group> groups, Population population) {
        return SelectionNodeHelper.createDomainObjectSelections(groups, population.getGroups());
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map refData = new HashMap();

        PopulationWrapperBean wrapper = (PopulationWrapperBean) command;
        switch (page) {
            case ADD_CORE_DETAILS_PAGE_INDEX:
                break;

            case NOT_ADD_CRITERIA_INDEX:

                if (!errors.hasErrors()) {
                    wrapper.checkCriteriaAttributes();
                }

                return referenceDataForCriteria(wrapper);

            case ADD_CRITERIA_INDEX:

                if (!errors.hasErrors()) {
                    final Long index = wrapper.getIndex();
                    if (index == null) {
                        // if no index means that a new criteria is being added
                        wrapper.addPopulationCriteria(new CriteriaWrapperBean(new PopulationCriteria()));
                    } else {
                        // means that a criteria has been modified
                        wrapper.checkCriteriaAttribute(index);
                    }

                    wrapper.checkCriteriaAttributes();
                }

                return referenceDataForCriteria(wrapper);

        }

        return refData;
    }

    protected final void validateBrackets(PopulationWrapperBean wrapper, Errors errors) {
        PopulationValidator populationValidator = (PopulationValidator) getValidator();
        populationValidator.validateBrackets(wrapper, errors);
    }

    protected final Map referenceDataForViewCriteria(PopulationWrapperBean wrapper) throws Exception {
        Map refData = new HashMap();

        if (wrapper.getAttributeCollection() == null) {
            wrapper.setAttributeCollection(builder.buildCollection());
        }

        return refData;
    }

    protected final Map referenceDataForCriteria(PopulationWrapperBean wrapper) throws Exception {

        Map refData = referenceDataForViewCriteria(wrapper);

        if (wrapper.hasOrganisationUnitCriteria()) {
            refData.put("orgUnitTree", TreeBuilderHelper.buildOrgUnitTree(organisationService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID)));
        }

        refData.put("operators", IPopulationEngine.availableOperators);

        return refData;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        PopulationWrapperBean wrapper = (PopulationWrapperBean) command;
        int target = getTargetPage(request, page);
        wrapper.setTarget(target);

        if (wrapper.getPopulationCriterias() != null) {
            for (int i = 0; i < wrapper.getPopulationCriterias().size(); i++) {
                CriteriaWrapperBean criteria = (CriteriaWrapperBean) wrapper.getPopulationCriterias().get(i);
                criteria.setInverse(RequestUtils.getBooleanParameter(request, "populationCriterias[" + i + "].inverse", false));
            }
        }

        if (target == NOT_ADD_CRITERIA_INDEX && wrapper.getIndex() != null) {
            wrapper.getPopulationCriterias().remove(wrapper.getIndex().intValue());
        }

        if (target == ADD_CRITERIA_INDEX || target == NOT_ADD_CRITERIA_INDEX) {
            if (request.getParameter(GROUP_BINDING_PARAM) == null) wrapper.setGroupIds(new Long[0]);
        }
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws Exception {
        RedirectView view = new ZynapRedirectView(getCancelView());
        return new ModelAndView(view);
    }

    /**
     * Empty implementation to ensure that non-wizard controller subclasses do not have provide empty implementations as well.
     *
     * @param request
     * @param response
     * @param command
     * @param errors
     * @throws Exception
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    /**
     * Set node label on criteria that refer to a node (a position, a subject or an org unit.)
     * <br/> Required because the criteria holds the node id not the node label (ie: "0" rather than "Board")
     * - this method changes that so the user sees the name of the Node being referenced.
     *
     * @param wrapper
     */
    protected final void setNodeLabelInCriteria(PopulationWrapperBean wrapper) {
        List list = wrapper.getPopulationCriterias();
        if (list != null)
            for (int i = 0; i < list.size(); i++) {
                CriteriaWrapperBean criteria = (CriteriaWrapperBean) list.get(i);
                if (criteria.isNode() || criteria.isLastUpdatedBy()) {
                    DynamicAttribute attributeDefinition = criteria.getAttributeDefinition().getAttributeDefinition();
                    criteria.setNodeLabel(dynamicAttributeService.getDomainObjectLabel(AttributeValue.create(criteria.getRefValue(), attributeDefinition)));
                }
            }
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setOrganisationService(IOrganisationUnitService organisationService) {
        this.organisationService = organisationService;
    }

    public void setBuilder(PopulationCriteriaBuilder builder) {
        this.builder = builder;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private IOrganisationUnitService organisationService;
    private IDynamicAttributeService dynamicAttributeService;
    private IGroupService groupService;
    protected IAnalysisService analysisService;
    protected PopulationCriteriaBuilder builder;

    protected static final int ADD_CORE_DETAILS_PAGE_INDEX = 0;
    protected static final int ADD_CRITERIA_INDEX = 1;
    protected static final int NOT_ADD_CRITERIA_INDEX = 2;
    private static final String GROUP_BINDING_PARAM = "groupIds";
}
