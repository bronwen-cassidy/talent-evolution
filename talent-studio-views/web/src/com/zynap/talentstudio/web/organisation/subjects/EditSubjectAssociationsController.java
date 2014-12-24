package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.DisplayItemReport;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationUtils;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
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
 * The class represents the ability to edit the associations of a a subject
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class EditSubjectAssociationsController extends DefaultWizardFormController {

    public EditSubjectAssociationsController() {
        super();
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));
        //get the id from the request and get the subject
        Subject subject = subjectService.findById(RequestUtils.getRequiredLongParameter(request, ParameterConstants.SUBJECT_ID_PARAM));
        SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(subject);

        Long displayConfigItemId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.DISPLAY_ITEM_ID_PARAM);
        DisplayConfigItem configItem = displayConfigService.findConfigItemById(displayConfigItemId);

        subjectWrapperBean.setDisplayConfigItem(configItem);
        return subjectWrapperBean;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        DisplayConfigItem displayConfigItem = ((SubjectWrapperBean) command).getDisplayConfigItem();
        List reportItems = displayConfigItem.getReportItems();
        for (int i = 0; i < reportItems.size(); i++) {
            DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(i);
            if (itemReport.isSubjectPrimaryAssociation()) {
                List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);
                refData.put("primaryTypes", lookupValues);
            } else if (itemReport.isSubjectSecondaryAssociation()) {
                List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC);
                refData.put("secondaryTypes", lookupValues);
            }
        }
        return refData;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        
        int targetPage = getTargetPage(request, page);
        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        if (isFinishRequest(request)) {
            SubjectValidator validator = (SubjectValidator) getValidator();
            final Long userId = ZynapWebUtils.getUserId(request);
            final boolean allowOrphans = securityManager.isDomainMember(ISecurityManager.ORPHANS_DOMAIN_ID, userId);
            validator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors, allowOrphans);
            validator.validateSubjectSecondaryAssociations(subjectWrapperBean, errors);
        } else {
            switch (targetPage) {
                case ADD_PRIMARY_ASSOC:
                    subjectWrapperBean.addNewPrimaryAssociation();
                    break;
                case ADD_SECONDARY_ASSOC:
                    subjectWrapperBean.addNewSecondaryAssociation();
                    break;
                case DELETE_PRIMARY_ASSOC:
                    ArtefactAssociationUtils.removeAssociations(request, subjectWrapperBean.getSubjectPrimaryAssociations());
                    break;
                case DELETE_SECONDARY_ASSOC:
                    ArtefactAssociationUtils.removeAssociations(request, subjectWrapperBean.getSubjectSecondaryAssociations());
                    break;
            }
        }
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        Subject subject = subjectWrapperBean.getModifiedSubject(UserSessionFactory.getUserSession().getUser());
        subjectService.update(subject);
        subjectService.updateCurrentJobInfo(subject.getId());
        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.SUBJECT_ID_PARAM, subject.getId());
        return new ModelAndView(view);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    private ILookupManager lookupManager;
    private ISubjectService subjectService;
    private IDisplayConfigService displayConfigService;
    private ISecurityManager securityManager;

    private static final int ADD_PRIMARY_ASSOC = 1;
    private static final int ADD_SECONDARY_ASSOC = 2;
    private static final int DELETE_PRIMARY_ASSOC = 3;
    private static final int DELETE_SECONDARY_ASSOC = 4;
}
