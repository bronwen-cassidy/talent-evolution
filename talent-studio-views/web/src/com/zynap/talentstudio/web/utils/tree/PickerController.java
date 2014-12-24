package com.zynap.talentstudio.web.utils.tree;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:51:01
 */

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class PickerController extends AbstractWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        return checkWrapper(request);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public ISubjectService getSubjectService() {
        return subjectService;
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }


    public IPositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    protected final TreeWrapperBean checkWrapper(HttpServletRequest request) throws TalentStudioException {

        String leafId = request.getParameter(POPUP_INITIAL_LEAF_PARAM);
        String popupId = request.getParameter(POPUP_ID_PARAM);

        TreeWrapperBean wrapper = (TreeWrapperBean) ZynapWebUtils.getCommand(this.getClass().getName(), request);

        // if not found or popup id has changed, create new TreeWrapper
        if (wrapper == null || !popupId.equals(wrapper.getPopupId())) {
            Map<String, Branch> branches = new HashMap<String, Branch>();
            wrapper = getTree(branches, popupId, request);
        }

        // find branch if necessary
        if (StringUtils.hasText(leafId)) {
            findBranchByLeafId(wrapper, leafId, request);
        }

        return wrapper;
    }

    protected abstract TreeWrapperBean getTree(Map<String, Branch> branches, String popupId, HttpServletRequest request) throws TalentStudioException;

    /**
     * Populate branch that leaf belongs to so that it can be highlighted.
     *
     * @param treeWrapperBean the wrapper for the tree
     * @param leafId          the id of the leaf that has been clicked
     * @param request         the servlet request
     * @throws TalentStudioException any exceptions
     */
    protected abstract void findBranchByLeafId(TreeWrapperBean treeWrapperBean, String leafId, HttpServletRequest request) throws TalentStudioException;

    /**
     * Find and set leaves for branch.
     *
     * @param branch            a tree branch
     * @param treeWrapperBean   the wrapper for the tree and search results
     * @param request           the servlet request
     * @throws TalentStudioException any exceptions
     */
    protected abstract void updateBranch(Branch branch, TreeWrapperBean treeWrapperBean, HttpServletRequest request) throws TalentStudioException;

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        TreeWrapperBean wrapper = (TreeWrapperBean) command;
        switch (getTargetPage(request,page)) {
            case 0:
                if (wrapper.getBranchId() != null) {
                    Branch branch = (Branch) wrapper.getBranches().get(wrapper.getBranchId());
                    if (branch.getLeaves() == null || branch.getLeaves().size() == 0)
                        updateBranch(branch, wrapper, request);
                }
                String leafId = request.getParameter(POPUP_INITIAL_LEAF_PARAM);
                if (StringUtils.hasText(leafId)) {
                    findBranchByLeafId(wrapper, leafId, request);
                }
                break;
            case 1:
                doSearch(wrapper, request,errors);
                break;
        }
    }

    public void doSearch(TreeWrapperBean wrapper, HttpServletRequest request, BindException errors) throws TalentStudioException {
    }

    public final String getLeafIcon() {
        return leafIcon;
    }

    public final void setLeafIcon(String leafIcon) {
        this.leafIcon = leafIcon;
    }

    private String leafIcon;

    public static final String POPUP_ID_PARAM = "popupId";
    public static final String POPUP_INITIAL_LEAF_PARAM = "initialLeaf";
    private IDynamicAttributeService dynamicAttributeService;
    private IPositionService positionService;
    private ISubjectService subjectService;
}
