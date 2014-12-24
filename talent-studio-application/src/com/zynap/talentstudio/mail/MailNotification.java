/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.mail;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 27-Nov-2006 15:06:58
 */
public class MailNotification implements IMailNotification {

    /**
     * For each of the participants in the questionnaire
     * send an email message.
     */
    public void send(String url, User fromUser, IDomainObject domainObject, User... participants) {

        if (participants == null || participants.length == 0) return;

        final String label = domainObject.getLabel();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("domainObject", domainObject);
        if (StringUtils.hasText(url)) {
            model.put("url", serverUrl + url);
        }

        String messageSubject = MessageFormat.format(subject, label);
        simpleMailMessage.setSubject(messageSubject);

        if (fromUser != null) {
            model.put("from", fromUser);
        }

        for (User user : participants) {

            CoreDetail coreDetail = user.getCoreDetail();
            String emailAddress = coreDetail.getContactEmail();

            if (StringUtils.hasText(emailAddress)) {
                simpleMailMessage.setTo(user.getLabel() + "<" + emailAddress + ">");
                model.put("user", user);
                sendMessage(emailAddress, model);
            }
        }
    }

    public void send(String url, User fromUser, String password) {

        Map<String, Object> model = new HashMap<String, Object>();
        if (StringUtils.hasText(url)) {
            model.put("url", serverUrl + url);
        }

        simpleMailMessage.setSubject(subject);

        if (fromUser != null) {
            model.put("from", fromUser);
            CoreDetail coreDetail = fromUser.getCoreDetail();
            String emailAddress = coreDetail.getContactEmail();

            if (StringUtils.hasText(emailAddress)) {
                simpleMailMessage.setTo(fromUser.getLabel() + "<" + emailAddress + ">");
                model.put("user", fromUser);
                model.put("password", password);
                sendMessage(emailAddress, model);
            }
        }
    }

    private void sendMessage(String emailAddress, Map<String, Object> model) {
        String result = null;
        try {
            result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, messageTemplate, model);
        } catch (VelocityException e) {
            logger.error(e.getMessage(), e);
        }

        simpleMailMessage.setText(result);
        try {
            mailSender.send(simpleMailMessage);
            System.out.println("mail sent!!!!");
        } catch (MailException e) {
            e.printStackTrace();
            logger.error("unable to send email message to " + emailAddress + " with message " + e.getMessage(), e);
            throw e;
        }
    }

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private SimpleMailMessage simpleMailMessage;
    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;
    private String messageTemplate;
    private String subject;
    private String serverUrl;

    private static final Log logger = LogFactory.getLog(MailNotification.class);
}
