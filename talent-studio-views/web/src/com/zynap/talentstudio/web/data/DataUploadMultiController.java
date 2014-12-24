package com.zynap.talentstudio.web.data;

import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class DataUploadMultiController extends ZynapMultiActionController {

    public ModelAndView uploadSuccessHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("uploadsuccess");
    }
}
