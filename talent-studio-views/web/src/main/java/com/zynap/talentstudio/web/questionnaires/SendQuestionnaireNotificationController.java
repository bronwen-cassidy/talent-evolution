package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.messages.IMessageService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.UrlBeanPair;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: bcassidy
 * Date: 26-Jan-2015
 * Time: 10:04:11
 */
public class SendQuestionnaireNotificationController implements Controller,  WorkflowConstants {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        boolean sendToInbox = RequestUtils.getBooleanParameter(request, "sendToInbox", false);
        boolean sendEmail = RequestUtils.getBooleanParameter(request, "sendEmail", false);
        boolean managerView = RequestUtils.getBooleanParameter(request, "managerView", false);
        boolean isMyPortfolio = RequestUtils.getBooleanParameter(request, "isMyPortfolio", false);

        Long subjectId = RequestUtils.getLongParameter(request, "subjectId", null);
        Long qId = RequestUtils.getLongParameter(request, "qId", null);
        String queLabel = RequestUtils.getStringParameter(request, "qLabel", "");
        List<Long> selectedManagers = Arrays.asList(RequestUtils.getAllLongParameters(request, "selectedManagers"));

        boolean process = sendEmail || sendToInbox;
        if (process) {
            List<User> participants = new ArrayList<User>();
            // we are going to the subordinate who is the subject of the questionnaire
            if (managerView) {
                User toUser = userService.findBySubjectId(subjectId);
                if (toUser != null) participants.add(toUser);
            } else {
                // find the manager
                Subject subject = subjectService.findById(subjectId);
                participants = subject.getManagers();
            }
            if (isMyPortfolio) {
                // only check for selected managers if there are more than 1
                if (participants.size() > 1) {
                    //if there is more then one manager then do the following filter
                    //filter managers to only one rather then all -i.e the manager selected
                    Iterator<User> participant = participants.iterator();
                    while (participant.hasNext()) {
                        User user = participant.next();
                        if (!selectedManagers.contains(user.getId())) {
                            participant.remove();
                        }
                    }
                }
            }
            if (!participants.isEmpty()) {

                if (sendToInbox)
                    messageService.create(queLabel, qId, managerView, ZynapWebUtils.getUser(request), participants);
                if (sendEmail) {
                    UrlBeanPair pair;
                    if (sendToInbox) pair = mailNotifications.get(INBOX_MAIL);
                    else {
                        if (managerView) pair = mailNotifications.get(NO_INBOX_MAIL_MANAGER);
                        else pair = mailNotifications.get(NO_INBOX_MAIL_INDIVIDUAL);
                    }
                    IMailNotification mailNotification = pair.getRef();
                    String url = pair.getUrl();
                    mailNotification.send(url, ZynapWebUtils.getUser(request), new Questionnaire(qId, queLabel), participants.toArray(new User[participants.size()]));
                }
                //wrapper.setSendSuccess(true);
            }
        }
        return null;
    }

    @Autowired
    protected Map<String, UrlBeanPair> mailNotifications;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISubjectService subjectService;

    protected final String INBOX_MAIL = "INBOX";
    protected final String NO_INBOX_MAIL_MANAGER = "NO_INBOX_MANAGER";
    protected final String NO_INBOX_MAIL_INDIVIDUAL = "NO_INBOX_STAFF";

    
}