package com.zynap.talentstudio.web.utils.displaytag;

import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;

import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * Decorator used to convert dates to correct display format.
 *
 * User: amark
 * Date: 29-Mar-2005
 * Time: 14:17:48
 */
public class MyTeamColumnDecorator extends WebApplicationAwareColumnDecorator {

    /**
     * Convert date to display format.
     * @param o
     * @return The formatted date, or an empty string if there is a problem.
     * @throws org.displaytag.exception.DecoratorException
     */
    public String decorate(Object o) throws DecoratorException {
        return o != null ? o.toString() : null;
    }


    public Object decorateInternal(XmlWebApplicationContext context, Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {

        final ServletRequest request = pageContext.getRequest();

        ISubjectService subjectService = (ISubjectService) context.getBean(DisplayTagConstants.SUBJECT_SERVICE);

        Long managerUserId = (Long) request.getAttribute(MANAGER_USER_ID);
        // get the view
        NodeExtendedAttribute answer = subjectService.getTeamViewAttribute(managerUserId, (Long)columnValue);
        final String result = ArtefactAttributeViewFormatter.formatValue(answer, "");
        return decorate(result);
    }

    private static final String MANAGER_USER_ID = "managerUserId";
}