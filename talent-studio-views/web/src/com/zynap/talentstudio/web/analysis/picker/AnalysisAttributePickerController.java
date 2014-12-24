package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.tree.PickerController;
import com.zynap.talentstudio.web.utils.tree.Branch;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 31-Jan-2006
 * Time: 11:47:42
 */
public final class AnalysisAttributePickerController extends AbstractWizardFormController {

    public void setLeafIcon(String leafIcon) {
        this.leafIcon = leafIcon;
    }

    public void setBuilder(PopulationCriteriaBuilder builder) {
        this.builder = builder;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        AnalysisAttributeTreeWrapperBean wrapper = (AnalysisAttributeTreeWrapperBean) ZynapWebUtils.getCommand(this.getClass().getName(), request);

        if (wrapper != null) {
            updateWrapperState(request, wrapper);
        } else {
            wrapper = createWrapper(request);
        }

        builder.setViewType(wrapper.getViewType());
        return wrapper;
    }

    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        AnalysisAttributeTreeWrapperBean wrapper = (AnalysisAttributeTreeWrapperBean) command;
        updateWrapperState(request, wrapper);
    }

    private AnalysisAttributeTreeWrapperBean createWrapper(HttpServletRequest request) throws TalentStudioException {

        // get parameters from request
        final String viewType = request.getParameter(VIEW_TYPE_PARAM);
        final String artefactType = request.getParameter(POPULATION_TYPE_PARAM);
        final boolean orgunitBranch = request.getParameter(ORGUNIT_TYPE_PARAM) != null;
        String popupId = request.getParameter(PickerController.POPUP_ID_PARAM);

        List<Branch> tree = getTree(artefactType);

        final AnalysisAttributeTreeWrapperBean wrapperBean = new AnalysisAttributeTreeWrapperBean(tree, popupId, leafIcon, artefactType, viewType);

        // if there is a selected leaf update it if necessary
        String leafId = request.getParameter(PickerController.POPUP_INITIAL_LEAF_PARAM);
        if (StringUtils.hasText(leafId)) {
            findBranchByLeafId(wrapperBean, leafId, orgunitBranch);
        }

        return wrapperBean;
    }

    private List<Branch> getTree(final String artefactType) {
        List<AnalysisAttributeBranch> tree = builder.getTree(artefactType);

        // IMPORTANT: when you get the tree this method clones the branches
        // so that the leaves do not get added to the "master copy" in the PopulationCriteriaBuilder instance

        // create a new list for the cloned branches
        List<Branch> temp = new ArrayList<Branch>(tree.size());

        for (Iterator iterator = tree.iterator(); iterator.hasNext();) {
            final AnalysisAttributeBranch attributeBranch = (AnalysisAttributeBranch) iterator.next();
            // use a clone
            temp.add((Branch) attributeBranch.clone());
        }
        return temp;
    }

    private void updateWrapperState(HttpServletRequest request, AnalysisAttributeTreeWrapperBean wrapper) throws TalentStudioException {

        // get parameters from request
        final String viewType = request.getParameter(VIEW_TYPE_PARAM);
        final String artefactType = request.getParameter(POPULATION_TYPE_PARAM);
        final boolean orgunitBranch = request.getParameter(ORGUNIT_TYPE_PARAM) != null;
        String popupId = request.getParameter(PickerController.POPUP_ID_PARAM);

        // check if either artefact type or popup id has changed - if so get the tree again and clear the branch id
        if (artefactType != null && !artefactType.equals(wrapper.getType()) || popupId != null && !popupId.equals(wrapper.getPopupId())) {
            List<Branch> tree = getTree(artefactType);
            wrapper.setTree(tree);
            wrapper.setBranchId(null);
        }

        // if view type has changed clear the leaves and clear the branch id
        if (viewType != null && !viewType.equals(wrapper.getViewType())) {
            List<Branch> tree = getTree(artefactType);
            wrapper.setTree(tree);
            wrapper.setBranchId(null);
        }

        // store new view type and artefact type and popup id
        wrapper.setViewType(viewType);
        wrapper.setType(artefactType);
        wrapper.setPopupId(popupId);

        // if there is a selected branch update it if necessary
        final String branchId = wrapper.getBranchId();
        if (StringUtils.hasText(branchId)) {
            AnalysisAttributeBranch branch = builder.findBranch(wrapper.getTree(), branchId);

            if (branch != null && isNotAppraisalBranch(branch, branchId) && (branch.getLeaves() == null || branch.getLeaves().isEmpty())) {
                updateBranch(branch, wrapper);
            }
        }

        // if there is a selected leaf update it if necessary
        String leafId = request.getParameter(PickerController.POPUP_INITIAL_LEAF_PARAM);
        if (StringUtils.hasText(leafId)) {
            findBranchByLeafId(wrapper, leafId, orgunitBranch);
        }
    }

    private boolean isNotAppraisalBranch(AnalysisAttributeBranch branch, String branchId) {
        final String[] parameters = StringUtils.delimitedListToStringArray(branchId, AnalysisAttributeHelper.QUESTION_CRITERIA_DELIMITER);
        return !(branch.isQuestionnaireWorkflow() && parameters.length == 3 && isAppraisalPrefix(branchId));
    }

    private boolean isAppraisalPrefix(String branchId) {
        return branchId.startsWith(PopulationCriteriaBuilder.APPRAISALS_BRANCH_ID + QuestionnaireTreeBuilder.APPRAISAL_WORKFLOW_PREFIX);
    }

    private void findBranchByLeafId(AnalysisAttributeTreeWrapperBean treeWrapperBean, String leafId, boolean orgunitBranch) throws TalentStudioException {

        AnalysisAttributeBranch branch;
        if (orgunitBranch) {
            branch = builder.findOrgBranchByLeafId(treeWrapperBean.getTree(), leafId);
        } else {
            branch = builder.findBranchByLeafId(treeWrapperBean.getTree(), leafId, treeWrapperBean.getViewType());
        }
        if (branch != null) builder.updateBranch(branch, treeWrapperBean.getViewType());
    }

    private void updateBranch(AnalysisAttributeBranch branch, AnalysisAttributeTreeWrapperBean treeWrapperBean) throws TalentStudioException {
        builder.updateBranch(branch, treeWrapperBean.getViewType());
    }

    private String leafIcon;
    private PopulationCriteriaBuilder builder;

    private static final String POPULATION_TYPE_PARAM = "populationType";
    private static final String ORGUNIT_TYPE_PARAM = "organisationType";
    private static final String VIEW_TYPE_PARAM = "viewType";
}
