package com.zynap.talentstudio.web.account;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.objectives.ObjectiveSetDto;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.BrowseSubjectController;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.SubjectBrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.SubjectDashboardWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.portfolio.MyPortfolioHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controller that displays the subject user's own details. Eg: firstname. date of birth, extended attributes etc.
 *
 * User: amark
 * Date: 12-May-2005
 * Time: 11:44:42
 */
public class PersonalDetailsController extends BrowseSubjectController {

    public Object formBackingObject(HttpServletRequest request) throws Exception {

        BrowseNodeWrapper browseNodeWrapper = recoverFormBackingObject(request);
        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        if (browseNodeWrapper == null) {

            final Long userId = userSession.getId();
            final Subject subject;
            try {
                subject = getSubjectService().findByUserId(userId);
            } catch (Exception e) {
                browseNodeWrapper = new SubjectBrowseNodeWrapper(null, null, null);
                browseNodeWrapper.setErrorKey("error.invalid.link");
                return browseNodeWrapper;
            }

            browseNodeWrapper = new SubjectBrowseNodeWrapper(null, null, null);
            browseNodeWrapper.setNodeId(subject.getId());
            browseNodeWrapper.setNodeType(Node.SUBJECT_UNIT_TYPE_);
            MyPortfolioHelper.setDisplayInfo(displayConfigService,browseNodeWrapper, request);
            updateNodeInfo(subject, browseNodeWrapper, userSession);
        }
        return browseNodeWrapper;
    }

    /**
     * Overloaded method - required as security checks for restricted personal portfolio items
     * depend on content types, not on artefact access in personal portfolios.
     * <br/> Also
     *
     * @param currentNode
     * @param wrapper
     * @param userSession
     * @throws TalentStudioException
     */
    protected void updateNodeInfo(final Node currentNode, BrowseNodeWrapper wrapper, UserSession userSession) throws TalentStudioException {

        if (currentNode != null) {

            checkNodeAssociationAccess(currentNode);

            // given the fact that this node was returned in the list of artefacts we must have access so set flag to true
            currentNode.setHasAccess(true);
            final NodeWrapperBean nodeWrapperBean = createNodeWrapper(currentNode, wrapper);
            wrapper.setNodeWrapper(nodeWrapperBean);

            final Long subjectId = currentNode.getId();
            SubjectWrapperBean subjectWrapper = (SubjectWrapperBean) wrapper.getNodeWrapper();

            if (wrapper.getContentView().isHasObjectivesView()) {
                // set the current objective set
                final ObjectiveSetDto currentObjectiveSet = objectiveService.findCurrentObjectiveSetDto(subjectId);
                subjectWrapper.setHasArchivedObjectives(objectiveService.hasArchivedObjectiveSets(subjectId));

                if (currentObjectiveSet != null) {
                    subjectWrapper.setCurrentObjectiveSet(currentObjectiveSet);
                    subjectWrapper.setCurrentObjectives(objectiveService.findCurrentObjectives(subjectId));
                    subjectWrapper.setHasApprovableAssessments(objectiveService.isHasApprovableAssessments(subjectId));
                } else {
                    subjectWrapper.setHasApprovableAssessments(false);
                }
            }
            // browsing my details is a personal view
            subjectWrapper.setPersonalView(true);

            // check for invalid objectives
            checkObjectiveStatus(wrapper);
        } else
            wrapper.setNodeWrapper(null);
    }

    protected void addDashboardView(HttpServletRequest request, BrowseNodeWrapper wrapper, Map<String, Object> model) throws TalentStudioException {
        
        final Node node = wrapper.getNode();
        if (wrapper.isHasDashboardView() && node != null) {
            Subject subject = (Subject) node;
	        final Set<SubjectDashboardWrapper> subjectDashboardItems = dashboardBuilder.buildSubjectDashboards(subject, dashboardService, populationEngine);
            if (!subjectDashboardItems.isEmpty()) {
                model.put("dashboards", subjectDashboardItems);
            }
        }
    }
}
