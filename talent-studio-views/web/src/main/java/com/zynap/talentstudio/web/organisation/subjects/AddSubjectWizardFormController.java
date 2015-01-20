/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectPicture;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationUtils;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.beans.UploadedFilePropertyEditor;
import com.zynap.talentstudio.web.utils.controller.ViewConfig;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class AddSubjectWizardFormController extends DefaultWizardFormController {

    public AddSubjectWizardFormController() {

    }

    public ViewConfig getCancelViewConfig() {
        return cancelViewConfig;
    }

    public void setCancelViewConfig(ViewConfig cancelViewConfig) {
        this.cancelViewConfig = cancelViewConfig;
    }

    /**
     * Form backing object consists of an empty subject object except for the default dynamic attributePreferences.
     *
     * @param request
     * @return Object    a Subject
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Subject subject = new Subject();
        SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(subject);

        // set addable attributes and defined extended attributes on the wrapper
        DisplayConfig displayConfig = displayConfigService.find(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.ADD_TYPE);
        DisplayConfigItem configItem = displayConfig.getFirstDisplayConfigItem();
        DynamicAttributesHelper.assignDisplayConfigAttributes(subjectWrapperBean, configItem, subject, dynamicAttributeService);
        return subjectWrapperBean;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(PAGE_TITLE, TITLE_KEY + page);

        switch (page) {
            case CORE_DETAILS_IDX:
                List titles = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_TITLE);
                refData.put(ControllerConstants.TITLES, titles);
                break;
            case ASSOCIATION_VIEW_IDX:
                List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);
                refData.put(ASSOCIATION_TYPES, lookupValues);
                break;
        }

        // set title
        refData.put(ControllerConstants.TITLE, TITLE_KEY + page);
        return refData;
    }

    protected ModelAndView processFinishInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        Subject newSubject = subjectWrapperBean.getModifiedSubject(UserSessionFactory.getUserSession().getUser());
        SubjectPicture picture = subjectWrapperBean.getModifiedSubjectPicture();

        subjectService.create(newSubject, picture);
        subjectService.updateCurrentJobInfo(newSubject.getId());

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ParameterConstants.SUBJECT_ID_PARAM, newSubject.getId());

        // this parameter indicates that the position has just been added - used later when deleting
        parameters.put(ControllerConstants.NEW_NODE, Boolean.TRUE);
        RedirectView view = new ZynapRedirectView(getSuccessView(), parameters);

        return new ModelAndView(view);
    }

    protected ModelAndView processCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        return new ModelAndView(cancelViewConfig.getRedirectView());
    }

    protected final int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

        // if adding or deleting an association stay on current page
        final boolean associationModificationRequest = ArtefactAssociationUtils.isAssociationModificationRequest(request);
        if (associationModificationRequest) {
            setPageSessionAttribute(request, currentPage);
            return currentPage;
        }

        // if deleting an image extended attribute stay on current page
        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        if (DynamicAttributesHelper.isClearAttributeValueRequest(request, subjectWrapperBean)) {
            setPageSessionAttribute(request, currentPage);
            return currentPage;
        }

        return super.getTargetPage(request, command, errors, currentPage);
    }

    protected final void removeAssociations(HttpServletRequest request, SubjectWrapperBean subjectWrapperBean) {
        ArtefactAssociationUtils.removeAssociations(request, subjectWrapperBean.getSubjectPrimaryAssociations());
    }

    protected final void addAssociation(HttpServletRequest request, SubjectWrapperBean subjectWrapperBean) {
        if (ArtefactAssociationUtils.isAddAssociationRequest(request)) {
            subjectWrapperBean.addNewPrimaryAssociation();
        }
    }

    /**
     * Register custom binders for image extended attributes, date of birth, picture.
     *
     * @param request
     * @param binder
     * @throws Exception
     */
    protected final void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(UploadedFile.class, new UploadedFilePropertyEditor());
        binder.registerCustomEditor(Date.class, AnalysisAttributeHelper.DOB_ATTR, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Validate core details and extended attributes.
     * <br/> Also validates associations.
     *
     * @param request
     * @param command
     * @param errors
     * @param page
     */
    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        SubjectValidator subjectValidator = (SubjectValidator) getValidator();
        SubjectWrapperBean subject = (SubjectWrapperBean) command;
        switch (page) {
            case CORE_DETAILS_IDX:
                subjectValidator.validateCoreValues(subject, errors);
                subjectValidator.validateRequiredValues(subject, errors);
                subjectValidator.validateDynamicAttributes(subject, errors);
                break;
            case ASSOCIATION_VIEW_IDX:
                // only validate if not adding or deleting an association
                if (!ArtefactAssociationUtils.isAssociationModificationRequest(request)) {
                    final Long userId = ZynapWebUtils.getUserId(request);
                    final boolean allowOrphans = securityManager.isDomainMember(ISecurityManager.ORPHANS_DOMAIN_ID, userId);
                    subjectValidator.validateSubjectPrimaryAssociations(subject, errors, allowOrphans);
                }
                break;
        }
    }

    /**
     * Clears image extended attributes if required.
     * <br/> Removes and adds associations if required.
     * <br/> Also sets inactive flag on the subject.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected final void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {

        int page = getCurrentPage(request);
        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        switch (page) {
            case CORE_DETAILS_IDX:
                DynamicAttributesHelper.clearAttributeValue(subjectWrapperBean);
                break;
            case ASSOCIATION_VIEW_IDX:
                removeAssociations(request, subjectWrapperBean);
                addAssociation(request, subjectWrapperBean);
                break;
        }
    }

    private ViewConfig cancelViewConfig;

    private ILookupManager lookupManager;
    protected ISecurityManager securityManager;
    ISubjectService subjectService;
    private IDisplayConfigService displayConfigService;
    private IDynamicAttributeService dynamicAttributeService;

    protected static final int CORE_DETAILS_IDX = 0;
    protected static final int ASSOCIATION_VIEW_IDX = 1;

    protected static final String PAGE_TITLE = "pagetitle";
    protected static final String TITLE_KEY = "add.subject.wizard.page.";
    protected static final String ASSOCIATION_TYPES = "associationTypes";
}
