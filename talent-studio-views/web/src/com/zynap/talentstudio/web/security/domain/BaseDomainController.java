package com.zynap.talentstudio.web.security.domain;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.beans.UploadedFilePropertyEditor;
import com.zynap.util.ArrayUtils;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 16-Mar-2005
 * Time: 18:37:22
 * Abstract base class for add and edit security domain controllers.
 */
public abstract class BaseDomainController extends DefaultWizardFormController {

    public ISecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public IRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }    

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(UploadedFile.class, new UploadedFilePropertyEditor());
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(ControllerConstants.TITLE, MESSAGE_KEY + page);
        return refData;
    }

    protected void setAreas(final SecurityDomainWrapperBean securityDomainWrapperBean) {
        securityDomainWrapperBean.setAreas(getSecurityManager().getAreas());
    }

    protected void setUsers(final SecurityDomainWrapperBean securityDomainWrapperBean) {
        securityDomainWrapperBean.setUsers(getUserService().getAppUsers());
    }

    protected void setRoles(final SecurityDomainWrapperBean securityDomainWrapperBean) {
        securityDomainWrapperBean.setRoles(getRoleManager().getActiveResourceRoles());
    }

    /**
     * Get SecurityDomainWrapperBean.
     *
     * @param command
     * @return SecurityDomainWrapperBean
     */
    protected SecurityDomainWrapperBean getWrapper(Object command) {
        return (SecurityDomainWrapperBean) command;
    }

    /**
     * Bind certain fields even when we have a back request.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
        SecurityDomainWrapperBean securityDomainWrapperBean = getWrapper(command);

        switch (getCurrentPage(request)) {
            case CORE_VIEW_IDX:
                securityDomainWrapperBean.setActive(RequestUtils.getBooleanParameter(request, "active", false));
                securityDomainWrapperBean.setExclusive(RequestUtils.getBooleanParameter(request, "exclusive", false));
                break;
            case ROLES_VIEW_IDX:
                long[] roleIds = RequestUtils.getLongParameters(request, "roleIds");
                if (roleIds.length == 0) {
                    // clear the selected parameters
                    securityDomainWrapperBean.setRoleIds(ArrayUtils.convert(roleIds));
                }
                break;
            case USERS_VIEW_IDX:
                long[] userIds = RequestUtils.getLongParameters(request, "userIds");
                if (userIds.length == 0) {
                    // clear the selected users
                    securityDomainWrapperBean.setUserIds(ArrayUtils.convert(userIds));
                }
                break;
        }
    }

    /**
     * Validate if not a back request only.
     *
     * @param request
     * @param command
     * @param errors
     * @param page
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        SecurityDomainWrapperBean securityDomainWrapperBean = (SecurityDomainWrapperBean) command;
        SecurityDomainValidator securityDomainValidator = (SecurityDomainValidator) getValidator();
        switch (page) {
            case CORE_VIEW_IDX:
                securityDomainValidator.validateCoreValues(securityDomainWrapperBean, errors);
                securityDomainValidator.validateSecurityDomainLabel(securityDomainWrapperBean, errors, getSecurityManager().getAllDomains());
                break;
            case AREA_VIEW_IDX:
                securityDomainValidator.validateArea(securityDomainWrapperBean, errors);
                break;            
        }
    }

    public static final String MESSAGE_KEY = "securitydomain.wizard.page.";

    public static final int CORE_VIEW_IDX = 0;
    public static final int ROLES_VIEW_IDX = 1;
    public static final int USERS_VIEW_IDX = 2;
    public static final int AREA_VIEW_IDX = 3;

    private ISecurityManager securityManager;
    private IRoleManager roleManager;
    private IUserService userService;
    public static final String DELETE_IMAGE_INDEX = "deleteImageIndex";
}
