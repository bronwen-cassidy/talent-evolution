/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.homepages;

import com.zynap.common.util.UploadedFile;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Nov-2007 10:23:31
 */
public class HomePagesValidator implements Validator {

    public boolean supports(Class clazz) {
        return HomePagesFormBean.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        HomePagesFormBean homePagesFormBean = (HomePagesFormBean) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "groupLabel", "error.required.field");
        List<HomePageWrapperBean> pages = homePagesFormBean.getHomePages();

        int index = 0;

        for (HomePageWrapperBean homePageWrapperBean : pages) {

            String url = homePageWrapperBean.getUrl();
            UploadedFile uploadHomePage = homePageWrapperBean.getUploadHomePage();
            final boolean hasUrl = StringUtils.hasText(url);
            final boolean hasUpload = !uploadHomePage.isFileEmpty();            
            if (hasUrl && hasUpload) {
                errors.rejectValue("homePages[" + index + "].uploadHomePage", "error.security.domain.multiple.homepages", "Cannot add both a url and an upload as the home page");
            }
            if(hasUrl) {
                try {
                    new URL(url);
                } catch (MalformedURLException e) {
                    errors.rejectValue("homePages[" + index + "].url", "error.malformed.url", "Url must be of the format http://");
                }

                for (String s : arenaUrls) {
                    if(url.indexOf(s) != -1) {
                        errors.rejectValue("homePages[" + index + "].url", "error.recursive.url", "Placing a home page within a home page will cause a problem");
                        break;
                    }
                }
            }
            if (hasUpload) {
                final String extension = uploadHomePage.getFileExtension();
                if (!("htm".equalsIgnoreCase(extension) || "html".equalsIgnoreCase(extension) || "vm".equalsIgnoreCase(extension))) {
                    errors.rejectValue("homePages[" + index + "].uploadHomePage", "error.required.html.page", "The uploaded file must be an html or vm page");
                }
            }
            index++;
        }
    }

    private static List<String> arenaUrls = new ArrayList<String>();
    static {
        arenaUrls.add("/admin/home.htm");
        arenaUrls.add("/analysis/home.htm");
        arenaUrls.add("/talentarena/home.htm");
        arenaUrls.add("/orgbuilder/home.htm");
        arenaUrls.add("/perfman/home.htm");
        arenaUrls.add("/talentidentifier/home.htm");
        arenaUrls.add("/succession/home.htm");
    }    
}
