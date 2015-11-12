package com.zynap.talentstudio.web.controller.admin;

import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserPassword;
import com.zynap.exception.AlphaOnlyRuleException;
import com.zynap.exception.InvalidPasswordException;
import com.zynap.exception.MaxLengthRuleException;
import com.zynap.exception.MinLengthRuleException;
import com.zynap.exception.UniqueRuleException;
import com.zynap.exception.UserLoginFailedException;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 22-Apr-2005
 * Time: 17:54:54
 */
public abstract class BaseResetPasswordFormController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        final String previousUrl = HistoryHelper.getBackURL(request);
        setCancelView(previousUrl);
        setSuccessView(previousUrl);
        UserPassword password = new UserPassword();
        Long userId = UserMultiController.getUserId(request);
        password.setUserId(userId);
        return password;
    }

    protected Map referenceData(HttpServletRequest request) {
        Map refdata = new HashMap();
        String cancelUrl = request.getParameter(ControllerConstants.CANCEL_URL);
        refdata.put(ControllerConstants.CANCEL_URL, cancelUrl != null ? cancelUrl : getCancelView());
        return refdata;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        UserPassword pwdObject = (UserPassword) command;

        try {
            getUserService().changePassword(pwdObject);
        } catch (UniqueRuleException ue) {
            errors.rejectValue("newPassword", "error.uniquepwd.rule.violated", "You cannot re-use a recent previous password. Please select another.");
        } catch (MinLengthRuleException me) {
            errors.rejectValue("newPassword", "error.minlength.rule.violated", me.getMessage());
        } catch (MaxLengthRuleException mle) {
            errors.rejectValue("newPassword", "error.maxlength.rule.violated", mle.getMessage());
        } catch (AlphaOnlyRuleException ae) {
            errors.rejectValue("newPassword", "error.alpha.rule.violated", "Password contains illegal characters");
        } catch (UserLoginFailedException le) {
            errors.rejectValue("username", le.getMessage(), null, "Login failed");
        } catch (InvalidPasswordException ie) {
            errors.rejectValue("oldPassword", ie.getMessage(), null, "Existing password does not match");
        }

        // If we have any errors the go back to the form that was posted
        if (errors.hasErrors()) {
            return showForm(request, response, errors);
        }

        final User user = (User) getUserService().findById(pwdObject.getUserId());
        return new ModelAndView(new UserRedirectView(getSuccessView(), user));
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        UserPassword pwdObject = (UserPassword) command;
        final User user = (User) getUserService().findById(pwdObject.getUserId());
        return new ModelAndView(new UserRedirectView(getCancelView(), user));
    }

    public IUserService getUserService() {
        return _userService;
    }

    public void setUserService(IUserService userService) {
        _userService = userService;
    }

    private IUserService _userService;
}
