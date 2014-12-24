package com.zynap.talentstudio.web.portfolio;

/**
 * User: amark
 * Date: 15-Jun-2005
 * Time: 14:29:02
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.ContentType;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.security.SecurityAttribute;

import org.springframework.web.servlet.ModelAndView;


import java.util.Map;

public class TestPortfolioMultiController extends ZynapMockControllerTest {


    protected void setUp() throws Exception {
        super.setUp();

        portfolioMultiController = (PortfolioMultiController) applicationContext.getBean("portfolioMultiController");
        portfolioService = portfolioMultiController.getPortfolioService();

        setUserSession(new UserSession(getAdminUserPrincipal(), getArenaMenuHandler()), mockRequest);
    }

    public void testViewPositionPortfolioItemHandler() throws Exception {

        PortfolioItem item = null;
        try {
            final ContentType contentType = (ContentType) portfolioService.getContentTypes(Node.POSITION_UNIT_TYPE_).iterator().next();
            final String contentSubType = contentType.getContentSubTypes()[0];

            item = new PortfolioItem();
            PortfolioItemFile file = new PortfolioItemFile();
            file.setBlobValue("test 123".getBytes());

            item.setNode(DEFAULT_POSITION);
            item.setContentType(contentType);
            item.setContentSubType(contentSubType);

            SecurityAttribute secAttr = new SecurityAttribute();
            secAttr.setPublicRead(false);
            item.setSecurityAttribute(secAttr);

            PortfolioItemHelper.enforceSecurityLogic(item, true);
            PortfolioItemWrapper portItemWrapper = new PortfolioItemWrapper(item);
            portItemWrapper.setMyPortfolio(false);
            portItemWrapper.setUserSession(ZynapWebUtils.getUserSession(mockRequest));

            portfolioService.create(item, file);

            assertNotNull(item.getId());
            assertTrue(item.isPrivate());

            mockRequest.addParameter(ParameterConstants.ITEM_ID, item.getId().toString());
            final ModelAndView modelAndView = portfolioMultiController.viewPositionPortfolioItemHandler(mockRequest, mockResponse);
            assertEquals("viewportfolioitem", modelAndView.getViewName());

            final Map model = getModel(modelAndView);

            assertEquals(portItemWrapper, model.get(ControllerConstants.PORTFOLIO_ITEM));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (item != null) portfolioService.delete(item.getId());
        }
    }

    PortfolioMultiController portfolioMultiController;
    private IPortfolioService portfolioService;
}