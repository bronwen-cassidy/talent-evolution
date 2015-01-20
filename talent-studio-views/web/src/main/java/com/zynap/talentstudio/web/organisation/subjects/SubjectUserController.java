package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.domain.admin.User;
import com.zynap.exception.AlphaOnlyRuleException;
import com.zynap.exception.MaxLengthRuleException;
import com.zynap.exception.MinLengthRuleException;
import com.zynap.exception.UniqueRuleException;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.users.DuplicateUsernameException;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.security.admin.AddUserFormController;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic controller that handles adding and editing user details belonging to a subject.
 *
 * @author amark
 * @since December 2005
 */
public final class SubjectUserController extends ZynapDefaultFormController {

    public SubjectUserController() {
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));

        Subject subject = subjectService.findById(RequestUtils.getRequiredLongParameter(request, ParameterConstants.SUBJECT_ID_PARAM));

        User user = (User) userService.findById(ZynapWebUtils.getUserId(request));

        List<Role> roleList = new ArrayList<Role>(user.getUserRoles());

        final SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(subject, roleList);
        subjectWrapperBean.getUserWrapper().setGroups(groupService.find(Group.TYPE_HOMEPAGE));
        return subjectWrapperBean;
    }
                                                                                                      
    /**
     * Validates passwords and sets the active flag.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {

        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;

        final UserWrapperBean userWrapper = subjectWrapperBean.getUserWrapper();
        userWrapper.setActive(RequestUtils.getBooleanParameter(request, "userWrapper.active", false));
        // handle the case where all roles are unchecked
        String accessRolesIds = request.getParameter("accessRoleIds");
        if (!StringUtils.hasText(accessRolesIds)) {
            userWrapper.clearRoles();
        }
    }

    /**
     * This method is called before the form is shown and is used to populate the
     * request with information that the form may need.
     *
     * @param request
     * @return Map
     * @throws javax.servlet.ServletException
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {

        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        final boolean newUser = subjectWrapperBean.isNewUser();

        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(AddUserFormController.SET_PASSWORD, new Boolean(newUser));
        refData.put(ControllerConstants.SUPER_USER, new Boolean(ZynapWebUtils.isSuperUser(request)));

        return refData;
    }

    public final ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        Subject newSubject = subjectWrapperBean.getModifiedSubject(UserSessionFactory.getUserSession().getUser());

        // check is new user
        final boolean newUser = subjectWrapperBean.isNewUser();

        boolean success = false;

        try {
            subjectService.update(newSubject);
            success = true;
        } catch (DuplicateUsernameException e) {
            errors.rejectValue("userWrapper.loginInfo.username", "error.duplicate.username", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("userWrapper.loginInfo.username", "error.duplicate.username", e.getMessage());
        } catch (MinLengthRuleException e) {
            errors.rejectValue("userWrapper.loginInfo." + e.getOffender(), "error.minlength.rule.violated", e.getMessage());
        } catch (MaxLengthRuleException e) {
            errors.rejectValue("userWrapper.loginInfo." + e.getOffender(), "error.maxlength.rule.violated", e.getMessage());
        } catch (AlphaOnlyRuleException e) {
            errors.rejectValue("userWrapper.loginInfo." + e.getOffender(), "error.alpha.rule.violated", e.getMessage());
        } catch (UniqueRuleException ue) {
            errors.rejectValue("userWrapper.loginInfo.password", "error.uniquepwd.rule.violated", "You cannot re-use a recent previous password. Please select another.");
        }

        if (!success) {
            // Need to reset ids to ensure that persistence layer still sees them as new unsaved objects
            if (newUser) {
                subjectWrapperBean.resetUserIds();
                subjectWrapperBean.resetPasswords();
            }
            return showForm(request, errors, getFormView());
        }

        RedirectView view = new ZynapRedirectView(getSuccessView(), ParameterConstants.SUBJECT_ID_PARAM, newSubject.getId());
        return new ModelAndView(view);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private ISubjectService subjectService;
    private IUserService userService;
    private IGroupService groupService;
}
