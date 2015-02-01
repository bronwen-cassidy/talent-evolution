package com.zynap.talentstudio.web.security.area;

import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrgUnitSearchQuery;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 20-Mar-2005
 * Time: 13:20:57
 */
public abstract class BaseAreaController extends DefaultWizardFormController {

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(ControllerConstants.TITLE, MESSAGE_KEY + page);
        long userId = ZynapWebUtils.getUserId(request);

        int targetPage = getTargetPage(request, page);
        String nodeType = Node.SUBJECT_UNIT_TYPE_;
        if(targetPage == POSITIONS_VIEW_IDX) {
            nodeType = Node.POSITION_UNIT_TYPE_;
        }
        if(targetPage == SUBJECTS_VIEW_IDX || targetPage == POSITIONS_VIEW_IDX) {
            refData.put("populations", analysisService.findAll(nodeType, userId, AccessType.PUBLIC_ACCESS.toString()));
        }
        
        return refData;
    }

    /**
     * Get AreaWrapperBean.
     *
     * @param command
     * @return AreaWrapperBean
     */
    protected AreaWrapperBean getWrapper(Object command) {
        return (AreaWrapperBean) command;
    }

    /**
     * Get the target page.
     * <br> If the user is doing a search and is on either the orgunit, position, and subject pages
     * then returns the current page.
     *
     * @param request
     * @param command
     * @param errors
     * @param currentPage
     * @return int
     */
    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

        switch (currentPage) {
            case SUBJECTS_VIEW_IDX:
                if (StringUtils.hasText(request.getParameter(SEARCH_STARTED))) return currentPage;
        }

        return super.getTargetPage(request, command, errors, currentPage);
    }

    protected void onBindInternal(HttpServletRequest request, Object command, Errors errors) {
        AreaWrapperBean areaWrapperBean = getWrapper(command);

        final int currentPage = getCurrentPage(request);
        final int targetPage = getTargetPage(request, currentPage);

        switch (currentPage) {
            case CORE_VIEW_IDX:
                areaWrapperBean.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
                break;
        }

        if (targetPage == POSITIONS_VIEW_IDX && currentPage == OU_VIEW_IDX) {
            assignCascading(request, areaWrapperBean);
        }

        if (targetPage == SUBJECTS_VIEW_IDX && !isFinishRequest(request)) {
            assignExcluded(request, areaWrapperBean);
        }
    }

    private void assignExcluded(HttpServletRequest request, AreaWrapperBean areaWrapperBean) {
        if (isBack(request)) return;
        // first do any new ones
        Collection<AreaElement> posElements = areaWrapperBean.getPositions();

        List<Long> excludedPosIds = new ArrayList<Long>();
        int index = 0;
        for (AreaElement posElement : posElements) {
            boolean assigned = RequestUtils.getBooleanParameter(request, "positions[" + index + "].excluded", false);
            if (assigned) excludedPosIds.add(posElement.getNode().getId());
            index++;
        }

        Collection<AreaElement> assignedPositions = areaWrapperBean.getAssignedPositions();
        index = 0;
        for (AreaElement areaElement : assignedPositions) {
            boolean assignExcluded = excludedPosIds.contains(areaElement.getNode().getId());
            areaElement.setExcluded(RequestUtils.getBooleanParameter(request, "assignedPositions[" + index + "].excluded", assignExcluded));
            index++;
        }
    }

    private void assignCascading(HttpServletRequest request, AreaWrapperBean areaWrapperBean) {
        if (isBack(request)) return;
        // first do any new ones
        Collection<AreaElement> ouElements = areaWrapperBean.getOrganisationUnits();

        List<Long> cascadingOuIds = new ArrayList<Long>();
        int index = 0;
        for (AreaElement ouElement : ouElements) {
            boolean assigned = RequestUtils.getBooleanParameter(request, "organisationUnits[" + index + "].cascading", false);
            if (assigned) cascadingOuIds.add(ouElement.getNode().getId());
            index++;
        }

        Collection<AreaElement> assignedOrganisationUnits = areaWrapperBean.getAssignedOrganisationUnits();
        index = 0;
        for (AreaElement areaElement : assignedOrganisationUnits) {
            boolean assignedCascades = cascadingOuIds.contains(areaElement.getNode().getId());
            areaElement.setCascading(RequestUtils.getBooleanParameter(request, "assignedOrganisationUnits[" + index + "].cascading", assignedCascades));
            index++;
        }
    }


    /**
     * Callback for custom post-processing in terms of binding and validation.
     * <br> If the user is on the first page sets the active attribute of the area to false if the check box was deselected.
     * <br> If the user is on the ou view page, clears the org unit list and does the search if required.
     * <br> If the user is on the position view page, clears the org unit list and does the search if required.
     * <br> If the user is on the subject view page, clears the org unit list and does the search if required.
     *
     * @param request current HTTP request
     * @param command bound command
     * @param errors  Errors instance for additional custom validation
     * @param page    The page number
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        AreaWrapperBean areaWrapperBean = getWrapper(command);

        switch (page) {
            case CORE_VIEW_IDX:
                AreaValidator areaValidator = (AreaValidator) getValidator();
                areaValidator.validateCoreValues(areaWrapperBean, errors);
                break;
            case OU_VIEW_IDX:
                clearOrgUnits(request, areaWrapperBean);
                orgUnitSearch(request, areaWrapperBean);
                break;
            case POSITIONS_VIEW_IDX:
                clearPositions(request, areaWrapperBean);
                positionSearch(request, areaWrapperBean);
                break;
            case SUBJECTS_VIEW_IDX:
                clearSubjects(request, areaWrapperBean);
                subjectSearch(request, areaWrapperBean);
                break;
        }
    }

    /**
     * Will clear the assigned org units if the checkboxes have all been deselected.
     *
     * @param request
     * @param areaWrapperBean
     */
    private void clearOrgUnits(HttpServletRequest request, AreaWrapperBean areaWrapperBean) {
        if (!StringUtils.hasText(request.getParameter(ORG_UNIT_IDS_PARAM))) {
            areaWrapperBean.removeOrgUnits();
        }
    }

    /**
     * Will clear the assigned positions if the checkboxes have all been deselected.
     *
     * @param request
     * @param areaWrapperBean
     */
    private void clearSubjects(HttpServletRequest request, AreaWrapperBean areaWrapperBean) {
        if (!StringUtils.hasText(request.getParameter(SUBJECT_IDS_PARAM))) {
            areaWrapperBean.removeSubjects();
        }
    }

    /**
     * Will clear the assigned subjects if the checkboxes have all been deselected.
     *
     * @param request
     * @param areaWrapperBean
     */
    private void clearPositions(HttpServletRequest request, AreaWrapperBean areaWrapperBean) {
        if (!StringUtils.hasText(request.getParameter(POSITION_IDS_PARAM))) {
            areaWrapperBean.removePositions();
        }
    }

    /**
     * Searches for org units based on criteria supplied.
     * <br> Always clears previous search results.
     *
     * @param request
     * @param areaWrapperBean
     */
    private void orgUnitSearch(HttpServletRequest request, AreaWrapperBean areaWrapperBean) {

        areaWrapperBean.clearOrganisationUnits();
        final String searchParam = request.getParameter(ORG_UNIT_SEARCH);
        if (StringUtils.hasText(searchParam)) {

            OrgUnitSearchQuery query = new OrgUnitSearchQuery();

            final String orgUnitName = request.getParameter(ORG_UNIT_LABEL);
            if (StringUtils.hasText(orgUnitName)) {
                query.setLabel(orgUnitName);
            }

            final Collection organisationUnits = organisationManager.search(ZynapWebUtils.getUserId(request), query);
            for (Iterator iterator = organisationUnits.iterator(); iterator.hasNext();) {
                OrganisationUnit organisationUnit = (OrganisationUnit) iterator.next();
                areaWrapperBean.addOrganisationUnit(organisationUnit);
            }

            assignCascading(request, areaWrapperBean);
        }
    }

    /**
     * Searches for positions based on criteria supplied.
     * <br> Always clears previous search results.
     *
     * @param request
     * @param areaWrapperBean
     * @throws TalentStudioException
     */
    private void positionSearch(HttpServletRequest request, AreaWrapperBean areaWrapperBean) throws TalentStudioException {

        areaWrapperBean.clearPositions();
        final String searchParam = request.getParameter(POSITION_SEARCH);
        if (StringUtils.hasText(searchParam)) {

            PositionSearchQuery query = new PositionSearchQuery();

            final String title = request.getParameter(POSITION_TITLE);
            if (StringUtils.hasText(title)) {
                query.setTitle(title);
            }

            final String orgUnitId = request.getParameter(POSITION_ORG_UNIT_ID);
            if (StringUtils.hasText(orgUnitId)) {
                query.setOrgUnitId(new Long(orgUnitId));
            }

            final Collection positions = positionService.search(ZynapWebUtils.getUserId(request), query);
            for (Iterator iterator = positions.iterator(); iterator.hasNext();) {
                Position position = (Position) iterator.next();
                areaWrapperBean.addPosition(position);
            }

            assignExcluded(request, areaWrapperBean);
        }
    }

    /**
     * Searches for subjects based on criteria supplied.
     * <br> Always clears previous search results.
     *
     * @param request
     * @param areaWrapperBean
     * @throws TalentStudioException
     */
    private void subjectSearch(HttpServletRequest request, AreaWrapperBean areaWrapperBean) throws TalentStudioException {
        areaWrapperBean.clearSubjects();
        final String searchParam = request.getParameter(SUBJECT_SEARCH);
        if (StringUtils.hasText(searchParam)) {
            SubjectSearchQuery subjectSearchQuery = new SubjectSearchQuery();

            final String firstName = request.getParameter(SUBJECT_FIRST_NAME);
            if (StringUtils.hasText(firstName)) {
                subjectSearchQuery.setFirstName(firstName);
            }
            final String lastName = request.getParameter(SUBJECT_LAST_NAME);
            if (StringUtils.hasText(lastName)) {
                subjectSearchQuery.setSecondName(lastName);
            }

            final Collection subjects = subjectService.search(ZynapWebUtils.getUserId(request), subjectSearchQuery);
            for (Iterator iterator = subjects.iterator(); iterator.hasNext();) {
                Subject subject = (Subject) iterator.next();
                areaWrapperBean.addSubject(subject);
            }
        }
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        this.organisationManager = organisationManager;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public static final String MESSAGE_KEY = "area.wizard.page.";

    public static final String SEARCH_STARTED = "searchStarted";

    public static final String SUBJECT_FIRST_NAME = "subjFirstName";
    public static final String SUBJECT_LAST_NAME = "subjLastName";
    public static final String SUBJECT_SEARCH = "subjSearch";

    public static final String POSITION_TITLE = "posTitle";
    public static final String POSITION_ORG_UNIT_ID = "posOrgUnitId";
    public static final String POSITION_SEARCH = "posSearch";

    public static final String ORG_UNIT_SEARCH = "ouSearch";
    public static final String ORG_UNIT_LABEL = "ouLabel";

    public static final int CORE_VIEW_IDX = 0;
    public static final int OU_VIEW_IDX = 1;
    public static final int POSITIONS_VIEW_IDX = 2;
    public static final int SUBJECTS_VIEW_IDX = 3;

    private static final String ORG_UNIT_IDS_PARAM = "orgUnitIds";
    private static final String POSITION_IDS_PARAM = "positionIds";
    private static final String SUBJECT_IDS_PARAM = "subjectIds";


    protected ISecurityManager securityManager;
    private ISubjectService subjectService;
    private IPositionService positionService;
    private IOrganisationUnitService organisationManager;
    protected IAnalysisService analysisService;
    protected IPopulationEngine populationEngine;
}
