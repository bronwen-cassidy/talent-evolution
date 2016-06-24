package com.zynap.talentstudio.web.arena;

import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.security.homepages.HomePage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by bronwen.cassidy on 30/03/2016. man
 */
@RunWith(MockitoJUnitRunner.class)
public class HomePageControllerTest {


    @InjectMocks
    private HomePageController homePageController;

    @Mock
    private IDynamicAttributeService dynamicAttrService;

    @Test
    public void testHomePage() {

        Map<String, String> results = new HashMap<>();
        results.put("whatareyourjobresponsibilities0_22076822474514868", "my job responsibilities are");
        when(dynamicAttrService.getAllSubjectAttributes(-6L)).thenReturn(results);

        String content = "<h1> Hello velocity templates </h1>\n" +
                "<p> Testing upload using velocity templates </p>\n" +
                "<p> my value of what are you job responsibilities = $whatareyourjobresponsibilities0_22076822474514868 </p>";
        String actual = homePageController.evaluateVelocityContent(content, -6L);

        String expected = "<h1> Hello velocity templates </h1>\n" +
                "<p> Testing upload using velocity templates </p>\n" +
                "<p> my value of what are you job responsibilities = my job responsibilities are </p>";

        assertEquals(expected, actual);

    }

    @Test
    public void testHomePageWithLink() {

        Map<String, String> results = new HashMap<>();
        results.put("whatareyourjobresponsibilities0_22076822474514868", "meeeeee");
        results.put("gotcha", "testme");
        results.put("pollydied", "yesyesyes");

        when(dynamicAttrService.getAllSubjectAttributes(-6L)).thenReturn(results);

        String content = "<a href=\"www.testme.com?username=$whatareyourjobresponsibilities0_22076822474514868&password=$gotcha\">Click Me</a>";
        String actual = homePageController.evaluateVelocityContent(content, -6L);

        String expected = "<a href=\"www.testme.com?username=meeeeee&password=testme\">Click Me</a>";

        assertEquals(expected, actual);

    }

    @Test
    public void testHomePageUrl() {

        Map<String, String> results = new HashMap<>();
        results.put("username", "brendaa");
        results.put("password", "helooooo");
        results.put("pollydied", "yesyesyes");

        when(dynamicAttrService.getAllSubjectAttributes(-6L)).thenReturn(results);

        String content = "http://www.google.com?username=$username&password=$password";
        Map<String, Object> model = new HashMap<>();
        HomePage homePage = new HomePage("Home", "DemoCompany");
        homePage.setUrl(content);
        homePageController.buildModel("dontcare", homePage, model, -6L);
        String expected = "http://www.google.com?username=brendaa&password=helooooo";
        String actual = ((HomePage) model.get("homePage")).getUrl();

        assertEquals(expected, actual);

    }
}
