package com.zynap.talentstudio.web.data;

import org.directwebremoting.util.JavascriptUtil;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 *
 */
public class DataUploadController extends SimpleFormController {

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new FileUploadBean();
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        // cast the bean
        FileUploadBean bean = (FileUploadBean) command;

        //let 's see if there' s content there
        MultipartFile file = bean.getFile();
        if (file == null) {
            errors.reject("error.no.file.chosen");
            return showForm(request, response, errors);
        } else {

            UserSession userSession = ZynapWebUtils.getUserSession(request);
            String dirName = userSession.getUserPrincipal().getOrgansitionName();
            String rootPath = System.getProperty("catalina.home");
            dirName = dirName.replaceAll("[^a-zA-Z0-9]", "");

            File dir = new File(rootPath + File.separator + "tomcat" + File.separator + "tmpFiles" + File.separator + dirName);
            if (!dir.exists()) dir.mkdirs();
            String originalFileName = file.getOriginalFilename();
            String ending = originalFileName.substring(originalFileName.lastIndexOf("."));
            String prefix = originalFileName.substring(0, originalFileName.lastIndexOf(".") -1);

            Long userOrgUnitRootId = userSession.getUserOrgUnitRootId();
            if(userOrgUnitRootId == null) userOrgUnitRootId = 0L;

            String name = prefix + "_" + userSession.getId() + "_" + userOrgUnitRootId + "_" + System.currentTimeMillis();
            // Create the file on server
            name = name.replaceAll("[^a-zA-Z0-9_]", "");
            File serverFile = new File(dir.getAbsolutePath() + File.separator + name + ending);
            file.transferTo(serverFile);

        }

        // well, let's do nothing with the bean for now and return
        return new ModelAndView(new RedirectView(getSuccessView()));
    }
}
