/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.jasper;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

public class SpiderWebChartDemo1 extends ApplicationFrame {

    public SpiderWebChartDemo1(String s) {
        super(s);
        JPanel jpanel = createDemoPanel();
        jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
    }

    private static CategoryDataset createDataset() {
        String s = "First";
        String s1 = "Second";       // the first 3 are the groups
        String s2 = "Third";

        String s3 = "Category 1";   // these are the answer enumerations
        String s4 = "Category 2";
        String s5 = "Category 3";
        String s6 = "Category 4";
        String s7 = "Category 5";

        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        defaultcategorydataset.addValue(2.0, "One", "Total fails to date");
        defaultcategorydataset.addValue(4.0, "One", "Group Mark 1");
        defaultcategorydataset.addValue(4.0, "One", "Group Mark 2");
        defaultcategorydataset.addValue(2.0, "One", " Individual Mark 1");
        defaultcategorydataset.addValue(6.0, "One", " Individual Mark 2");

        defaultcategorydataset.addValue(7.0, "Two", " Group Mark 1");
        defaultcategorydataset.addValue(9.0, "Two", " Group Mark 2");
        defaultcategorydataset.addValue(2.0, "Two", " Individual Mark 1");
        defaultcategorydataset.addValue(14.0, "Two", " Individual Mark 2");
        defaultcategorydataset.addValue(2.0, "Two", "Total fails to date");

        defaultcategorydataset.addValue(4.0, "Three", " Individual Mark 1");
        defaultcategorydataset.addValue(4.0, "Three", " Individual Mark 2");
        defaultcategorydataset.addValue(2.0, "Three", " Group Mark 1");
        defaultcategorydataset.addValue(2.0, "Three", " Group Mark 2");
        defaultcategorydataset.addValue(5.0, "Three", "Total fails to date");

          // john
//        defaultcategorydataset.addValue(1.0D, s, s3);
//        defaultcategorydataset.addValue(4D, s, s4);
//        defaultcategorydataset.addValue(3D, s, s5);
//        defaultcategorydataset.addValue(5D, s, s6);
//        defaultcategorydataset.addValue(5D, s, s7);
         // mary
//        defaultcategorydataset.addValue(5D, s1, s3);
//        defaultcategorydataset.addValue(7D, s1, s4);
//        defaultcategorydataset.addValue(6D, s1, s5);
//        defaultcategorydataset.addValue(8D, s1, s6);
//        defaultcategorydataset.addValue(4D, s1, s7);        
        // fred
//        defaultcategorydataset.addValue(4D, s2, s3);
//        defaultcategorydataset.addValue(3D, s2, s4);
//        defaultcategorydataset.addValue(2D, s2, s5);
//        defaultcategorydataset.addValue(3D, s2, s6);
//        defaultcategorydataset.addValue(6D, s2, s7);

        return defaultcategorydataset;
    }

    private static JFreeChart createChart(CategoryDataset categorydataset) {
        SpiderWebPlot spiderwebplot = new SpiderWebPlot(categorydataset);
        spiderwebplot.setStartAngle(54D);
        spiderwebplot.setInteriorGap(0.40000000000000002D);
        //spiderwebplot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        JFreeChart jfreechart = new JFreeChart("Spider Web Chart Demo 1", TextTitle.DEFAULT_FONT, spiderwebplot, false);
        LegendTitle legendtitle = new LegendTitle(spiderwebplot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        jfreechart.addSubtitle(legendtitle);
        return jfreechart;
    }

    public static JPanel createDemoPanel() {
        JFreeChart jfreechart = createChart(createDataset());
        BufferedImage image = jfreechart.createBufferedImage(400, 400);

        try {
            FileOutputStream fos = new FileOutputStream("Testing.png");
            ImageIO.write(image, "png", fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ChartPanel(jfreechart);
    }

    public static void main(String args[]) {
        SpiderWebChartDemo1 spiderwebchartdemo1 = new SpiderWebChartDemo1("SpiderWebChartDemo1");
        spiderwebchartdemo1.pack();
        RefineryUtilities.centerFrameOnScreen(spiderwebchartdemo1);
        spiderwebchartdemo1.setVisible(true);
    }

    /*
    // code to use for servlet
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JFreeChart jfreechart = createChart(createDataset());
        BufferedImage image = jfreechart.createBufferedImage(400, 400);

        try {
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            ImageIO.write(image, "png", tmp);
            tmp.close();
            Integer contentLength = tmp.size();

            response.setContentType("image/png");
            response.setHeader("Content-Length", contentLength.toString());
            OutputStream out = response.getOutputStream();
            out.write(tmp.toByteArray());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JFreeChart createChart(CategoryDataset categorydataset) {
        SpiderWebPlot spiderwebplot = new SpiderWebPlot(categorydataset);
        spiderwebplot.setStartAngle(54D);
        spiderwebplot.setInteriorGap(0.40000000000000002D);
        //StandardCategoryToolTipGenerator generator = new StandardCategoryToolTipGenerator();
        //spiderwebplot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        JFreeChart jfreechart = new JFreeChart("Spider Web Chart Demo 1", TextTitle.DEFAULT_FONT, spiderwebplot, false);
        LegendTitle legendtitle = new LegendTitle(spiderwebplot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        jfreechart.addSubtitle(legendtitle);
        return jfreechart;
    }

    private static CategoryDataset createDataset() {
        String s = "First";
        String s1 = "Second";
        String s2 = "Third";
        String s3 = "Category 1";
        String s4 = "Category 2";
        String s5 = "Category 3";
        String s6 = "Category 4";
        String s7 = "Category 5";
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        defaultcategorydataset.addValue(1.0D, s, s3);
        defaultcategorydataset.addValue(4D, s, s4);
        defaultcategorydataset.addValue(3D, s, s5);
        defaultcategorydataset.addValue(5D, s, s6);
        defaultcategorydataset.addValue(5D, s, s7);
        defaultcategorydataset.addValue(5D, s1, s3);
        defaultcategorydataset.addValue(7D, s1, s4);
        defaultcategorydataset.addValue(6D, s1, s5);
        defaultcategorydataset.addValue(8D, s1, s6);
        defaultcategorydataset.addValue(4D, s1, s7);
        defaultcategorydataset.addValue(4D, s2, s3);
        defaultcategorydataset.addValue(3D, s2, s4);
        defaultcategorydataset.addValue(2D, s2, s5);
        defaultcategorydataset.addValue(3D, s2, s6);
        defaultcategorydataset.addValue(6D, s2, s7);
        return defaultcategorydataset;
    }
     */
}