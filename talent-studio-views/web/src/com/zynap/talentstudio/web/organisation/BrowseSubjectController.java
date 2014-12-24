package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.domain.orgbuilder.ISearchConstants;
import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.AppraisalReportBuilder;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.ObjectiveSetDto;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectSearchQueryWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BrowseSubjectController extends BrowseNodeController {

    protected BrowseNodeWrapper recoverFormBackingObject(HttpServletRequest request) throws TalentStudioException {

        SubjectBrowseNodeWrapper wrapper = (SubjectBrowseNodeWrapper) HistoryHelper.recoverCommand(request, SubjectBrowseNodeWrapper.class);
        if (wrapper != null) {
            updateNodeInfo(wrapper, UserSessionFactory.getUserSession());
            return wrapper;
        }

        return null;
    }

    /**
     * Get the backing object used by this wizard.
     *
     * @param request
     * @return instance of {@link com.zynap.talentstudio.web.organisation.BrowseNodeWrapper}
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {

        BrowseNodeWrapper browseNodeWrapper = (SubjectBrowseNodeWrapper) super.formBackingObject(request);
        final int page = getInitialPage(request);

        if (browseNodeWrapper == null) {

            final UserSession userSession = ZynapWebUtils.getUserSession(request);
            if (page == BROWSE_INITIAL_PAGE) {
                browseNodeWrapper = createBrowseNodeWrapper(userSession, request);

            } else {
                final SubjectSearchQueryWrapper queryWrapper = new SubjectSearchQueryWrapper(new SubjectSearchQuery());
                queryWrapper.setIncludeQuestionnaire(includeQuestionnaire);                
                browseNodeWrapper = new SubjectBrowseNodeWrapper(queryWrapper, null, null);
                browseNodeWrapper.setSearching(true);
                browseNodeWrapper.setNodeType(Node.SUBJECT_UNIT_TYPE_);
                setDisplayInfo(browseNodeWrapper, request);
            }
        } else {
            // if we have a deleted node we need to see if we have any results left
            if (RequestUtils.getLongParameter(request, ControllerConstants.DELETED_NODE_ID) != null) {
                final List results = browseNodeWrapper.getCurrentNodes();
                if (results.isEmpty()) browseNodeWrapper.setActiveSearchTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);
            }
        }

        return browseNodeWrapper;
    }

    protected BrowseNodeWrapper createBrowseNodeWrapper(UserSession userSession, HttpServletRequest request) throws TalentStudioException {

        final Long userId = userSession.getId();
        final Long organisationId = ZynapWebUtils.getUserOrgUnitId(request);
        final SubjectSearchQueryWrapper queryWrapper = new SubjectSearchQueryWrapper(organisationId);

        // set active to true as browse only returns active subjects
        queryWrapper.setActive(ISearchConstants.ACTIVE);

        final List<SubjectDTO> results = subjectService.searchSubjects(userId, queryWrapper.getModifiedSubjectSearchQuery());
        BrowseNodeWrapper browseNodeWrapper = new SubjectBrowseNodeWrapper(queryWrapper, results, null);

        browseNodeWrapper.setNodeType(Node.SUBJECT_UNIT_TYPE_);

        setDisplayInfo(browseNodeWrapper, request);
        if (results.isEmpty()) browseNodeWrapper.setActiveSearchTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);

        else {
            Subject firstSubjectDto = results.get(0);
            Subject firstSubject = subjectService.findById(firstSubjectDto.getId());
            updateNodeInfo(firstSubject, browseNodeWrapper, userSession);
        }
        return browseNodeWrapper;
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        BrowseNodeWrapper wrapper = (BrowseNodeWrapper) command;
        Map model = super.referenceData(request, command, errors, page);

        final Long userId = ZynapWebUtils.getUserId(request);
        final Group userGroup = ZynapWebUtils.getUserGroup(request);
        model.put(ControllerConstants.POPULATIONS, getAnalysisService().findAllByGroup(IPopulationEngine.P_SUB_TYPE_, userId, userGroup));
        model.put(ControllerConstants.SUPER_USER, new Boolean(ZynapWebUtils.isSuperUser(request)));

        if (wrapper.getContentView() != null && wrapper.getContentView().isHasPersonalReportsView()) {
            model.put(ControllerConstants.APPRAISAL_REPORTS, appraisalReportBuilder.buildResults(wrapper.getNodeId()));
            model.put(ControllerConstants.NUM_APPRAISAL_REPORTS, appraisalReportBuilder.numArchivedReports(wrapper.getNodeId()));
        }

        addDashboardView(request, wrapper, model);

        // include searchable questionnaires for searching if requested.
        if (includeQuestionnaire)
            model.put(ControllerConstants.QUESTIONNAIRES, queWorkflowService.findSearchableInfoforms());

        return model;
    }

    protected void addDashboardView(HttpServletRequest request, BrowseNodeWrapper wrapper, Map model) throws TalentStudioException {
        final Group userGroup = ZynapWebUtils.getUserGroup(request);
        final Node node = wrapper.getNode();
        if (wrapper.getContentView().isHasDashboardView() && node != null) {
            Subject subject = (Subject) node;
            final Collection<Role> userRoles = ZynapWebUtils.getUserSession(request).getUserRoles();
            List<Dashboard> dashboards = dashboardService.findSubjectDashboards(userGroup, userRoles, subject);

            if (!dashboards.isEmpty()) {
                Set<SubjectDashboardWrapper> subjectDashboardItems = new LinkedHashSet<SubjectDashboardWrapper>();
                for (Dashboard dashboard : dashboards) {
                    final List<DashboardItem> dashboardItems = dashboard.getDashboardItems();
                    for (DashboardItem dashboardItem : dashboardItems) {
                        SubjectDashboardWrapper dw = new SubjectDashboardWrapper(dashboardItem.getId());
                        if (!subjectDashboardItems.contains(dw)) {
                            // build the info we need and add it if this is a chart we need the chart filler otherwsie we need the tabular filler
                            dashboardBuilder.buildDashboardItem(dw, subject, dashboardItem, populationEngine, false);
                            subjectDashboardItems.add(dw);
                        }
                    }
                }

                model.put("dashboards", new ArrayList(subjectDashboardItems));
            }
        }
    }

    protected void updateNodeInfo(Node currentNode, BrowseNodeWrapper wrapper, UserSession userSession) throws TalentStudioException {
        super.updateNodeInfo(currentNode, wrapper, userSession);

        if (currentNode != null) {

            final Long subjectId = currentNode.getId();
            SubjectWrapperBean subjectWrapper = (SubjectWrapperBean) wrapper.getNodeWrapper();

            // only load objectives is view is enabled
            if (wrapper.getContentView().isHasObjectivesView()) {

                final ObjectiveSetDto currentObjectiveSet = objectiveService.findCurrentObjectiveSetDto(subjectId);
                subjectWrapper.setHasArchivedObjectives(objectiveService.hasArchivedObjectiveSets(subjectId));
                subjectWrapper.setCurrentObjectiveSet(currentObjectiveSet);
                subjectWrapper.setAdministratorLoggedIn(userSession.isAdministrator());
                if (subjectWrapper.isAdministratorLoggedIn()) {
                    // set the current set of objectives for the administrator to find
                    if (currentObjectiveSet != null) {
                        subjectWrapper.setCurrentObjectives(objectiveService.findCurrentObjectives(subjectId));
                        checkObjectiveStatus(wrapper);
                    }
                }
            }
        }
    }

    protected final Node findNodeById(Long id) throws TalentStudioException {
        return subjectService.findById(id);
    }

    protected final void doSearch(BrowseNodeWrapper command, HttpServletRequest request) throws TalentStudioException {
        SubjectSearchQueryWrapper queryWrapper = (SubjectSearchQueryWrapper) command.getFilter();
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        Long userId = userSession.getId();

        SubjectSearchQuery subjectSearchQuery = queryWrapper.getModifiedSubjectSearchQuery();

        Long queId = queryWrapper.getQuestionnaireId();
        if (queId != null) subjectSearchQuery.setQuestionnaireId(queId);

        final List<SubjectDTO> subjects = subjectService.searchSubjects(userId, subjectSearchQuery);
        if (subjects.isEmpty()) command.setActiveSearchTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);
        command.setCurrentNodes(subjects);
    }

    protected final NodeWrapperBean createNodeWrapper(Node node, BrowseNodeWrapper wrapper) {

        Subject subject = (Subject) node;
        SubjectWrapperBean wrapperBean = new SubjectWrapperBean(subject);

        final User user = subject.getUser();
        List<SecurityDomain> securityDomains = new LinkedList<SecurityDomain>();
        if (user != null) {
            user.getSecurityDomains().size();
            securityDomains.addAll(user.getSecurityDomains());
        }

        wrapperBean.setSecurityDomains(securityDomains);

        // if the user has an objectives view set the value as to whether we have published objectives
        if (wrapper.getContentView().isHasObjectivesView()) {
            List<OrganisationUnit> primaryOrganisationUnits = subject.getPrimaryOrganisationUnits();
            for (OrganisationUnit primaryOrganisationUnit : primaryOrganisationUnits) {
                if (!primaryOrganisationUnit.getApprovedObjectives().isEmpty()) {
                    wrapperBean.setHasPublishedObjectives(true);
                    break;
                }
            }
        }
        return wrapperBean;
    }

    /**
     * Check access to artefacts linked to the current node by artefact associations.
     *
     * @param node The Node
     */
    protected final void checkNodeAssociationAccess(Node node) {
        subjectService.checkAccess((Subject) node);
    }

    public void setQueWorkflowService(IQueWorkflowService queWorkflowService) {
        this.queWorkflowService = queWorkflowService;
    }

    public void setIncludeQuestionnaire(boolean includeQuestionnaire) {
        this.includeQuestionnaire = includeQuestionnaire;
    }

    public boolean isIncludeQuestionnaire() {
        return includeQuestionnaire;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    public void setAppraisalReportBuilder(AppraisalReportBuilder appraisalReportBuilder) {
        this.appraisalReportBuilder = appraisalReportBuilder;
    }

    public void setDashboardService(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public void setDashboardBuilder(SubjectDashboardBuilder dashboardBuilder) {
        this.dashboardBuilder = dashboardBuilder;
    }

    protected void checkObjectiveStatus(BrowseNodeWrapper browseNodeWrapper) {

        if (browseNodeWrapper.getNodeWrapper() != null) {
            Subject subject = (Subject) browseNodeWrapper.getNode();
            List<OrganisationUnit> primaryOrganisationUnits = subject.getPrimaryOrganisationUnits();

            SubjectWrapperBean subjectWrapper = (SubjectWrapperBean) browseNodeWrapper.getNodeWrapper();
            ObjectiveSetDto objectiveSet = subjectWrapper.getCurrentObjectiveSet();
            if (objectiveSet != null) {
                boolean isObjectivesValid = objectiveService.isObjectivesValid(primaryOrganisationUnits, subjectWrapper.getId());
                if (!isObjectivesValid) {
                    objectiveSet.setStatus(ObjectiveConstants.STATUS_OPEN);
                }
            }
        }
    }

    protected IQuestionnaireService questionnaireService;
    private IQueWorkflowService queWorkflowService;
    protected IObjectiveService objectiveService;
    protected IDashboardService dashboardService;
    protected SubjectDashboardBuilder dashboardBuilder;
    private AppraisalReportBuilder appraisalReportBuilder;
    private boolean includeQuestionnaire = false; // default to false
    public static final String DEFAULT_QUESTIONNAIRE_GROUP = "default.questionnaire.group";
}

