/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;
/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 12-Feb-2009 15:52:08
 * @version 0.1
 */

import junit.framework.*;

import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractOverlaidChartProducer;

import java.util.Random;
import java.util.Vector;
import java.util.Collection;
import java.util.Collections;

import org.jfree.data.xy.XYSeries;

public class TestAbstractOverlaidChartProducer extends TestCase {

    public void testProduceDataset() throws Exception {


        double mean = getMean();


        final XYSeries series = new XYSeries("Normal Distribution");

        final int n = getN();
        double midY = n / 2;
        double midX = mean;


        final int ySampleCount = Math.round(n / 2);
        double[] leftYSample = new double[ySampleCount * 2];

        double[] ypointsPercent = new double[n];

        double ratioY = 100 / (((double) n) / 2);
        int ypointC = 0;
        for (int i = 0; i <= leftYSample.length - 1; i++) {
            leftYSample[i] = i * ratioY;
            ypointsPercent[ypointC++] = leftYSample[i];
        }


        int xpointC = 0;
        double[] leftXSample = new double[ySampleCount];
        double[] xpointsPercent = new double[n];

        double ratioX = 100 / (mean);
        for (int i = 0; i <= leftXSample.length - 1; i++) {

            leftXSample[i] = i * ratioX;
            ;
            xpointsPercent[xpointC++] = leftXSample[i];
        }

        for (int i = leftXSample.length - 1; i > 0; i--) {
            xpointsPercent[xpointC++] = leftXSample[i];
        }


        double[] xpoints = new double[xpointsPercent.length];
        for (int i = 0; i < xpoints.length; i++) {
            double p = xpointsPercent[i] / 100;
            xpoints[i] = p * midX;

        }
        double[] ypoints = new double[ypointsPercent.length];
        for (int i = 0; i < ypoints.length; i++) {
            double p = ypointsPercent[i] / 100;
            ypoints[i] = p * midY;

        }

        for (int i = 0; i < xpoints.length - 1; i++) {

            series.add(ypoints[i], xpoints[i]);

        }
        assertNotNull(series);
    }

    public void testProduceDatasetSM() throws Exception {


        double mean = getMean();
        double factor = 0.2;

        final XYSeries series = new XYSeries("Normal Distribution");

        Vector<Double> lxaxis = new Vector<Double>();
        double m = mean;
        while (m > 0.05) {

            lxaxis.add(new Double(m));
            System.out.println("m " + m);
            m -= factor;
        }

        Vector<Double> raxis = new Vector<Double>();
        raxis.addAll((Collection<? extends Double>) lxaxis.clone());

        Collections.reverse(lxaxis);

        Vector<Double> allAxis = new Vector<Double>(lxaxis);
        allAxis.addAll(raxis);


        int nx = allAxis.size();
        int n = getN();
        double ns = (double) n / (double) nx;
        Vector<Double> yaxis = new Vector<Double>();
        for (int i = 0; i < allAxis.size(); i++) {
            yaxis.add(new Double(ns * i));

        }

        for (int i = 0; i < allAxis.size(); i++) {

            series.add(yaxis.get(i), allAxis.get(i));

        }

        assertNotNull(allAxis);
        assertNotNull(yaxis);
        assertEquals(allAxis.size(), yaxis.size());

    }

    private double getMean() {
        return 3.50;
    }

    public void testDatasetOkGraph() {
        double mean = getMean();


        final XYSeries series = new XYSeries("Normal Distribution");

        final int n = getN();
        double midY = n / 2;
        double midX = mean;


        double[] samplePointsX = new double[]{0.0, 0.25, 0.5, 0.75, 0.95, 0.92, 0.75, 0.5, 0.25, 0.0};
        double[] samplePointsY = new double[]{0.0, 0.15, 0.3, 0.45, 0.6, 0.75, 0.90, 1.05, 1.2};


        double[] xpoints = new double[samplePointsX.length];
        for (int i = 0; i < xpoints.length; i++) {
            double p = samplePointsX[i];
            xpoints[i] = p * midX;

        }
        double[] ypoints = new double[samplePointsY.length];
        for (int i = 0; i < ypoints.length; i++) {
            double p = samplePointsY[i];
            ypoints[i] = p * midY;

        }


        for (int i = 0; i < xpoints.length - 1; i++) {

            series.add(ypoints[i], xpoints[i]);

        }

        assertNotNull(series);
    }

    public void testDatasetMidGraph() {
        double mean = getMean();


        final XYSeries series = new XYSeries("Normal Distribution");

        final int n = getN();
        double midY = n / 2;
        double midX = mean;


        double[] samplePointsX = new double[]{0.0, 0.25, 0.5};
        double[] samplePointsY = new double[]{0.0, 0.25, 0.5, 0.75, 0.100};


        double[] xpoints = new double[samplePointsX.length];
        for (int i = 0; i < xpoints.length; i++) {
            double p = samplePointsX[i];
            xpoints[i] = p * midX;

        }
        double[] ypoints = new double[samplePointsY.length];
        for (int i = 0; i < ypoints.length; i++) {
            double p = samplePointsY[i];
            ypoints[i] = p * midY;

        }


        for (int i = 0; i < xpoints.length - 1; i++) {

            series.add(ypoints[i], xpoints[i]);

        }

        assertNotNull(series);
    }

    public void testFakeData() {

        final XYSeries series = new XYSeries("Normal Distribution");

        final int nx = 10;
        final int ny = 10;


        double[] samplePointsX = new double[]{0.0, 0.25, 0.5, 0.25, 0.0};
        double[] nPointsX = new double[]{0, 2.5, 5, 2.5, 0};
        double[] samplePointsY = new double[]{0.0, 0.25, 0.5, 0.75, 1.0};
        double[] nPointsY = new double[]{0, 2.5, 5, 7.5, 10};

        double[] genX = new double[samplePointsX.length];
        for (int i = 0; i < samplePointsX.length; i++) {
            genX[i] = nx * samplePointsX[i];

        }

        //perfect -gen 
        double[] genY = new double[samplePointsY.length];
        for (int i = 0; i < samplePointsY.length; i++) {
            genY[i] = ny * samplePointsY[i];

        }
        for (int i = 0; i <= nPointsX.length - 1; i++) {

            series.add(nPointsY[i], nPointsX[i]);

        }

//        assertEquals(genY,nPointsY);
//        assertEquals(genX,nPointsX);

        for (int i = 0; i < genY.length; i++) {
            assertEquals(genY[i], nPointsY[i]);

        }
        for (int i = 0; i < genX.length; i++) {
            assertEquals(genX[i], nPointsX[i]);

        }
        assertNotNull(series);
    }


    private int getN() {
        return 17;
    }

    public double getSum() {
        return 21;
    }


    public void testMyComputation() {
        String[][] myRows = new String[][]{{"8", "185", "112"}, {"36", "180"}, {"3", "1", "-"}};

        System.out.println("\n\n");
        int step = 0;
        int myChildRow = 0;

        for (int i = 0; i < myRows.length * myRows.length; i++) {
            String[] myRow = myRows[step];

            if (myChildRow<myRow.length) {
                System.out.println(" " + myRow[myChildRow]);
            }
            if (step == myRow.length - 1) {
                step = 0;
                myChildRow++;
            } else {
                step++;
            }

        }


    }
}