/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import de.laures.cewolf.links.CategoryItemLinkGenerator;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Mar-2011 09:07:31
 */
public class SpiderCategoryURLGenerator implements CategoryURLGenerator, CategoryItemLinkGenerator {

    public String generateURL(CategoryDataset dataset, int series, int category) {
        return "testing.htm";
    }

    public String generateLink(Object dataset, int series, Object category) {
        return "testing.htm?series = " +series + "&category=" + category;
    }
}
