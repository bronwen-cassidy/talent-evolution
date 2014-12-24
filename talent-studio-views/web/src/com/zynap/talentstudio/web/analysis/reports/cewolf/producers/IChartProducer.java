package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;


/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 02-Jan-2007 13:39:47
 */
public interface IChartProducer{

    boolean isNonZeroValue(Double value);

    boolean isNotTotalOrNA(String str);

    boolean isNotTotal(String str);

    boolean isNotNA(String str);

   // Double convertValue(String str);
}
