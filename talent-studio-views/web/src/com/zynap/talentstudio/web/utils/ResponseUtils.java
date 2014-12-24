/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils;

import com.zynap.common.util.FileUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ResponseUtils {

    /**
     * Writes data to the response. Sets the appropriate headers for the response, such as content-type, content length
     * and buffer size
     *
     * @param response           the response that will be written to
     * @param request            the servlet request
     * @param fileName           the name of the file when wishing to force a download of the content, should have a file ending
     * @param data               the byte[] of data to write
     * @param forceDownload      whether to set the Content-Disposition header if true fileName should not be null
     * @throws ServletException  wraps any IOException thrown
     */
    public static void writeToResponse(HttpServletResponse response, HttpServletRequest request,
                                       String fileName, byte[] data,
                                       boolean forceDownload) throws ServletException {

        String mimeType = request.getSession().getServletContext().getMimeType("." + FileUtils.getExtension(fileName));
        ServletOutputStream ouputStream = null;
        try {
            // bug workaround for internet explorer 6 sp 1 under https see: http://support.microsoft.com/default.aspx?scid=kb;en-us;812935
            if (ZynapWebUtils.isInternetExplorer(request)) {
                response.reset();
            }
            if (forceDownload || !StringUtils.hasText(mimeType)) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            }
            if (StringUtils.hasText(mimeType)) response.setContentType(mimeType);

            int size = data.length;

            if (size < 0) size = 0;

            response.setBufferSize(size);
            response.setContentLength(size);

            ouputStream = response.getOutputStream();
            ouputStream.write(data, 0, size);

        } catch (IOException e) {
            throw new ServletException(e.getMessage(), e);

        } finally {
            if (ouputStream != null) {
                try {
                    ouputStream.flush();
                    ouputStream.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
    }

    protected static final Log logger = LogFactory.getLog(ResponseUtils.class);
}
