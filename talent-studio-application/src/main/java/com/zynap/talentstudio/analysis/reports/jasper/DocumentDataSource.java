package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.util.FormatterFactory;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * User: amark
 * Date: 20-Sep-2006
 * Time: 14:04:00
 * <p/>
 * Datasource for use in tabular reports for with associated arefacts.
 */
public class DocumentDataSource extends JRCollectionDataSource {

    public DocumentDataSource(Report report, Collection<Object> portfolioItems, JasperDataSourceFactory jasperDataSourceFactory, User user) {
        super(report, portfolioItems, null, jasperDataSourceFactory, null, null, 0, user);
        // sort the portfolio items by the last modified date
        Collections.sort(this.records, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                PortfolioItem i1 = (PortfolioItem) o1;
                PortfolioItem i2 = (PortfolioItem) o2;
                return i1.getLastModified().compareTo(i2.getLastModified());
            }
        });
    }

    public Object getFieldValue(JRField field) {

        Object value = null;

        if (currentRecord instanceof PortfolioItem) {
            PortfolioItem portfolioItem = (PortfolioItem) currentRecord;
            value = getValueFromPortfolioItem(field, portfolioItem);
        }

        return value;
    }

    private Object getValueFromPortfolioItem(JRField field, PortfolioItem portfolioItem) {

        Object value = null;
        String fieldName = field.getName();
        try {
            value = PropertyUtils.getProperty(portfolioItem, fieldName);
            if(value instanceof Date) {
                value = FormatterFactory.getDateFormatter().formatDateAsString((Date) value);
            }
        } catch (Exception e) {
            logger.error("Failed to get value from associated node for field: " + field.getName() + ", " + field.getValueClassName(), e);
        }
        return value;
    }
}