/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.positions;


import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationUtils;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.utils.beans.UploadedFilePropertyEditor;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;

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
public abstract class BasePositionWizardController extends DefaultWizardFormController {

    public IPositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public IDisplayConfigService getDisplayConfigService() {
        return displayConfigService;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    protected BasePositionWizardController() {
        super();
        setCommandName(ControllerConstants.COMMAND_NAME);
        setAllowDirtyBack(true);
        setBindOnNewForm(false);
    }

    /**
     * Register custom binders for image extended attributes.
     *
     * @param request
     * @param binder
     * @throws Exception
     */
    protected final void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(UploadedFile.class, new UploadedFilePropertyEditor());
    }

    protected final int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

        // if adding or deleting an association stay on current page
        final boolean associationModificationRequest = ArtefactAssociationUtils.isAssociationModificationRequest(request);
        if (associationModificationRequest) {
            setPageSessionAttribute(request, currentPage);
            return currentPage;
        }

        // if deleting an image extended attribute stay on current page
        PositionWrapperBean positionWrapperBean = (PositionWrapperBean) command;
        if (DynamicAttributesHelper.isClearAttributeValueRequest(request, positionWrapperBean)) {
            setPageSessionAttribute(request, currentPage);
            return currentPage;
        }

        return super.getTargetPage(request, command, errors, currentPage);
    }

    /**
     * Clears image extended attributes if required.
     * <br/> Removes and adds associations if required.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected final void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {

        final PositionWrapperBean positionWrapperBean = (PositionWrapperBean) command;

        int page = getCurrentPage(request);
        switch (page) {
            case CORE_DETAILS_INDEX:
                DynamicAttributesHelper.clearAttributeValue(positionWrapperBean);
                break;
            case ASSOCIATION_INDEX:
                removeSecondaryAssociations(request, positionWrapperBean);
                addSecondaryAssociation(request, positionWrapperBean);
                break;
        }
    }

    protected final void removeSecondaryAssociations(HttpServletRequest request, PositionWrapperBean wrapper) {
        ArtefactAssociationUtils.removeAssociations(request, wrapper.getSecondaryAssociations());
    }

    protected final void addSecondaryAssociation(HttpServletRequest request, PositionWrapperBean wrapper) {
        if (ArtefactAssociationUtils.isAddAssociationRequest(request)) {
            wrapper.addNewSecondaryAssociation();
        }
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
    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) {

        PositionValidator positionValidator = (PositionValidator) getValidator();
        PositionWrapperBean positionWrapper = (PositionWrapperBean) command;
        switch (page) {
            case CORE_DETAILS_INDEX:
                positionValidator.validateRequiredValues(positionWrapper, errors);
                positionValidator.validateDynamicAttributes(positionWrapper, errors);
                break;
            case ASSOCIATION_INDEX:
                // only validate if not adding or deleting an association
                if (!ArtefactAssociationUtils.isAssociationModificationRequest(request)) positionValidator.validateAssociations(positionWrapper, errors);
                break;
        }
    }

    /**
     * Loads lookup values for association types.
     * <br/> Also sets page title.
     *
     * @param request
     * @param command
     * @param errors
     * @param page
     * @return
     * @throws Exception
     */
    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        PositionWrapperBean wrapper = (PositionWrapperBean) command;

        Map<String, Object> refData = new HashMap<String, Object>();

        switch (page) {
            case CORE_DETAILS_INDEX:
                break;
            case ASSOCIATION_INDEX:

                //LookupType values for primary associations
                List associationValues = getLookupManager().findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC);
                refData.put("primary", associationValues);

                // lookup values for secondary associations
                List secondaryValues = getLookupManager().findActiveLookupValues(ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC);
                refData.put("secondary", secondaryValues);
                break;
        }

        // display different title if this is the default position
        String messageKey = "position.wizard.page.";
        if (wrapper.isDefault()) {
            messageKey += "default.";
        }

        refData.put(ControllerConstants.TITLE, messageKey + page);
        return refData;
    }

    protected IPositionService positionService;
    private ILookupManager lookupManager;
    private IDynamicAttributeService dynamicAttributeService;
    private IDisplayConfigService displayConfigService;

    protected static final int CORE_DETAILS_INDEX = 0;
    protected static final int ASSOCIATION_INDEX = 1;
}
