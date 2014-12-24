/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.DisplayItemReport;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationUtils;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Nov-2005 10:09:44
 */
public class EditPositionAssociationsController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));
        //get the id from the request and get the subject
        Position position = (Position) positionService.findById(RequestUtils.getRequiredLongParameter(request, ParameterConstants.SUBJECT_ID_PARAM));
        PositionWrapperBean positionWrapperBean = new PositionWrapperBean(position);
        // load the ids of the decendants so we can test clients do not create a recursive tree
        final List<Long> decendantIds = positionService.findDescendentIds(position.getId());
        positionWrapperBean.setDecendantIds(decendantIds);

        Long displayConfigItemId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.DISPLAY_ITEM_ID_PARAM);
        DisplayConfigItem configItem = displayConfigService.findConfigItemById(displayConfigItemId);

        positionWrapperBean.setDisplayConfigItem(configItem);
        return positionWrapperBean;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        DisplayConfigItem displayConfigItem = ((PositionWrapperBean) command).getDisplayConfigItem();
        List reportItems = displayConfigItem.getReportItems();
        for (int i = 0; i < reportItems.size(); i++) {
            DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(i);
            if (itemReport.isPositionPrimaryTargetAssociation()) {
                List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC);
                refData.put("primary", lookupValues);
            } else if(itemReport.isPositionSecondarySourceAssociation()) {
                List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC);
                refData.put("secondary", lookupValues);
            } else if (itemReport.isSubjectPositionPrimaryAssociation()) {
                List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);
                refData.put("primarySubjectTypes", lookupValues);
            } else if (itemReport.isSubjectPositionSecondaryAssociation()) {
                List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC);
                refData.put("secondarySubjectTypes", lookupValues);
            }
        }
        return refData;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        int targetPage = getTargetPage(request, page);
        PositionWrapperBean positionWrapperBean = (PositionWrapperBean) command;
        if (isFinishRequest(request)) {
            PositionValidator validator = (PositionValidator) getValidator();
            validator.validateAssociations(positionWrapperBean, errors);
            validator.validateSubjectPrimaryAssociations(positionWrapperBean, errors);
            validator.validateSubjectSecondaryAssociations(positionWrapperBean, errors);
            validator.validateNonRecursiveReportingAssociation(positionWrapperBean,errors);
        } else {
            switch (targetPage) {
                // cannot add or delete a positions primary associations hence these are not mapped
                case ADD_SECONDARY_ASSOC:
                    positionWrapperBean.addNewSecondaryAssociation();
                    break;
                case DELETE_SECONDARY_ASSOC:
                    ArtefactAssociationUtils.removeAssociations(request, positionWrapperBean.getSecondaryAssociations());
                    break;
                case ADD_SUBJECT_PRIMARY_ASSOC:
                    positionWrapperBean.addNewSubjectPrimaryAssociation();
                    break;
                case ADD_SUBJECT_SECONDARY_ASSOC:
                    positionWrapperBean.addNewSubjectSecondaryAssociation();
                    break;
                case DELETE_SUBJECT_PRIMARY_ASSOC:
                    ArtefactAssociationUtils.removeAssociations(request, positionWrapperBean.getSubjectPrimaryAssociations());
                    break;
                case DELETE_SUBJECT_SECONDARY_ASSOC:
                    ArtefactAssociationUtils.removeAssociations(request, positionWrapperBean.getSubjectSecondaryAssociations());
                    break;
            }
        }
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        PositionWrapperBean positionWrapperBean = (PositionWrapperBean) command;
        Position position = positionWrapperBean.getModifiedPosition(UserSessionFactory.getUserSession().getUser());
        positionService.update(position);
        positionService.updateCurrentHoldersInfo(position.getId());

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.SUBJECT_ID_PARAM, position.getId());
        return new ModelAndView(view);
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    private IPositionService positionService;
    private ILookupManager lookupManager;
    private IDisplayConfigService displayConfigService;

    private static final int ADD_SECONDARY_ASSOC = 1;
    private static final int DELETE_SECONDARY_ASSOC = 2;
    private static final int ADD_SUBJECT_PRIMARY_ASSOC = 3;
    private static final int ADD_SUBJECT_SECONDARY_ASSOC = 4;
    private static final int DELETE_SUBJECT_PRIMARY_ASSOC = 5;
    private static final int DELETE_SUBJECT_SECONDARY_ASSOC = 6;
}

