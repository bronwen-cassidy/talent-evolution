/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import org.jfree.chart.encoders.ImageFormat;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.support.ReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MetricChartDashboardController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // fetch the metric and the alignment
        Long metricReportId = RequestUtils.getRequiredLongParameter(request, "m_report_id");
        //String alignment = RequestUtils.getStringParameter(request, "align", "vertical");
        int width = RequestUtils.getIntParameter(request, "width", 500);
        int height = RequestUtils.getIntParameter(request, "height", 500);

        Report metricReport = (Report) reportService.findById(metricReportId);

        // now generate the jasper print with the default population
        RunMetricReportWrapper wrapper = new RunMetricReportWrapper(metricReport);
        reportRunner.run(wrapper, ZynapWebUtils.getUserId(request));

        wrapper.getFilledReport().getJasperPrint();

        // we are going to build the values from the print (the x, y dataset i assume)

        try {

            final boolean forceDownload = !ZynapWebUtils.isInternetExplorer(request);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            final Graphics2D graphics = bufferedImage.createGraphics();

            graphics.setColor(Color.RED);
            graphics.setFont(new Font("Ariel", Font.BOLD, 22));
            graphics.drawString("This is a Test", 50, 50);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, ImageFormat.PNG, baos);
            //ImageIO.write(bufferedImage, ImageFormat.PNG, response.getOutputStream());
            byte[] bytesOut = baos.toByteArray();

            ResponseUtils.writeToResponse(response, request, "chart.png", bytesOut, forceDownload);

            graphics.dispose();

        } catch (Throwable e) {
        }

        return null;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setReportRunner(ReportRunner reportRunner) {
        this.reportRunner = reportRunner;
    }


    private IReportService reportService;
    private ReportRunner reportRunner;
}