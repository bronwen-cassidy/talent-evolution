package com.zynap.talentstudio.web.controller.admin;

import com.zynap.domain.admin.UserSearchQuery;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.security.admin.UserSearchQueryWrapper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ControllerUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Controller that handles user search.
 *
 * User: bcassidy
 * Date: 23-Apr-2004
 */
public class ListUserController extends ZynapDefaultFormController {

    /**
     * Overriden form submission method for search.
     * <br> See {@link ControllerUtils}.isSearchFormSubmission()) for further details.
     * The problem here is that when clicking on the pager tag to resort the results represents a
     * get request this makes spring believe it has to go back to the form backing object, hence the result set is lost.
     * This hasResults parameter has been added to tell spring that this is not a new request.
     *
     * @param request
     * @return True if this is a form submission.
     */
    protected boolean isFormSubmission(HttpServletRequest request) {
        return (ControllerUtils.isSearchFormSubmission(request)) || super.isFormSubmission(request);
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("groups", groupService.find(Group.TYPE_HOMEPAGE));
        return refData;
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse httpServletResponse, Object command, Errors errors) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        UserSearchQueryWrapper queryWrapper = (UserSearchQueryWrapper) command;
        
        // do the search
        Collection users = getUserService().search(ZynapWebUtils.getUserId(request), queryWrapper.getSearchAdaptor());
        model.put(ControllerConstants.USERS, users);       

        request.setAttribute(ControllerConstants.MODEL_NAME, model);
        return showForm(request, errors, getFormView(), model);
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return new UserSearchQueryWrapper(new UserSearchQuery());
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private IUserService userService;
    private IGroupService groupService;
}
