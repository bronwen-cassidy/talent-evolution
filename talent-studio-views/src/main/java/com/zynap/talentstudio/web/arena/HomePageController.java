/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.arena;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.web.dashboard.MyDashboardWrapper;
import com.zynap.talentstudio.web.organisation.DisplayContentWrapper;
import com.zynap.talentstudio.web.organisation.SubjectDashboardBuilder;
import com.zynap.talentstudio.web.organisation.SubjectDashboardWrapper;
import com.zynap.talentstudio.web.portfolio.MyPortfolioHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 05-Feb-2004
 * Time: 15:02:32
 */
public class HomePageController implements Controller {

	@Autowired
	public HomePageController(IArenaManager arenaManager, IDynamicAttributeService dynamicAttrService, SubjectDashboardBuilder dashboardBuilder,
	                          IDashboardService dashboardService, IPopulationEngine populationEngine, IDisplayConfigService displayConfigService,
	                          ISubjectService subjectService, IQueWorkflowService queWorkflowService, IQuestionnaireService questionnaireService) {
		this.arenaManager = arenaManager;
		this.dynamicAttrService = dynamicAttrService;
		this.dashboardBuilder = dashboardBuilder;
		this.dashboardService = dashboardService;
		this.populationEngine = populationEngine;
		this.displayConfigService = displayConfigService;
		this.subjectService = subjectService;
		this.queWorkflowService = queWorkflowService;
		this.questionnaireService = questionnaireService;
	}

	//@RequestMapping(value={"", "/", "welcome"}) 
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// default time out 20 mins
		int sessionTimeout = 20;

		UserSession userSession = ZynapWebUtils.getUserSession(request);
		final String currentArenaId = userSession.getCurrentArenaId();
		Long userId = userSession.getUser().getId();
		// get current details
		String arenaHomePage = "";
		try {
			Arena arena = arenaManager.getArena(currentArenaId);
			if (arena != null) {
				sessionTimeout = arena.getSessionTimeout();
				String arenaPath = arena.getUrl();
				arenaHomePage = arenaPath.substring(0, arenaPath.lastIndexOf('/'));
			}
		} catch (TalentStudioException e) {
			logger.error(e.getMessage(), e);
		}

		// set session timeout on HttpSession
		HttpSession session = request.getSession();

		// convert to secs
		session.setMaxInactiveInterval(sessionTimeout * 60);

		HomePage homePage = ZynapWebUtils.getHomePage(request);
		Map<String, Object> model = new HashMap<>();
		Long subjectId = userSession.getSubjectId();

		buildModel(arenaHomePage, homePage, model, subjectId, request);
		model.put("userId", userId);
		// redirect to view - consists of arena id + _home eg: adminmodule_home
		return new ModelAndView(currentArenaId.toLowerCase() + "_home", model);
	}

	void buildModel(String arenaHomePage, HomePage homePage, Map<String, Object> model, Long subjectId, HttpServletRequest request) {
		if (homePage != null) {
			if (homePage.isVelocityTemplate()) {
				model.put("velocityContent", evaluateVelocityContent(homePage.getContent(), subjectId));
			} else if (StringUtils.hasText(homePage.getUrl()) && homePage.getUrl().contains("$")) {
				homePage.setUrl(evaluateVelocityContent(homePage.getUrl(), subjectId));
			} else if (StringUtils.hasText(homePage.getTabView())) {

				try {
					Subject subject = subjectService.findNodeById(subjectId);
					Set<SubjectDashboardWrapper> subjectDashboardItems = dashboardBuilder.buildSubjectDashboards(subject, dashboardService,
							populationEngine, queWorkflowService, questionnaireService, dynamicAttrService);
					if (!subjectDashboardItems.isEmpty()) {
						DisplayContentWrapper displayConfigView = MyPortfolioHelper.getPersonalExecSummaryViews(displayConfigService, request, Node.SUBJECT_UNIT_TYPE_);

						MyDashboardWrapper wrapper = new MyDashboardWrapper();
						wrapper.setDashboards(subjectDashboardItems);
						wrapper.setContentView(displayConfigView);

						model.put("dashboards", subjectDashboardItems);
						model.put("displayConfigView", displayConfigView);
						model.put("dashboard", wrapper);
					}
				} catch (TalentStudioException e) {
					e.printStackTrace();
				}

			}
			model.put("homePage", homePage);
			model.put("arenaContext", arenaHomePage);
			
		}
	}

	String evaluateVelocityContent(String content, Long subjectId) {
		String data = content;
		if (StringUtils.hasLength(content)) {

			Map<String, String> attributes = dynamicAttrService.getAllSubjectAttributes(subjectId);
			VelocityContext context = new VelocityContext(attributes);
			StringWriter writer = new StringWriter();

			Velocity.evaluate(context, writer, "homePage", content);

			data = writer.toString();
		}
		return data;
	}

	private final SubjectDashboardBuilder dashboardBuilder;
	private final IDashboardService dashboardService;
	private final IPopulationEngine populationEngine;
	private final IDisplayConfigService displayConfigService;
	private final ISubjectService subjectService;
	private final IQueWorkflowService queWorkflowService;
	private final IQuestionnaireService questionnaireService;

	private IDynamicAttributeService dynamicAttrService;
	protected final Log logger = LogFactory.getLog(getClass());
	private IArenaManager arenaManager;
}
