package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.talentstudio.organisation.portfolio.IPortfolioDao;
import com.zynap.talentstudio.util.velocity.VelocityUtils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import org.springframework.beans.factory.InitializingBean;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * Mock implementation of searcher that finds all portfolio items and returns those as results.
 * <br/> For testing only.
 *
 * User: amark
 * Date: 12-Jul-2006
 * Time: 11:53:04
 */
public final class MockPortfolioSearcher implements ISearcher, InitializingBean {

    /**
     * Initialise Velocity Template.
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        final VelocityEngine engine = VelocityUtils.getEngine();
        template = engine.getTemplate(VELOCITY_TEMPLATE);
    }

    public IResultMapper executeTextQuery(ISearchQuery query, IField[] fields) throws ExternalSearchException {
        return getResults(FormattingUtils.splitDataSources(query.getSources()), fields);
    }

    public IResultMapper executeSuggestQuery(ISearchQuery query, IField[] fields) throws ExternalSearchException {
        return getResults(FormattingUtils.splitDataSources(query.getSources()), fields);
    }

    public IResultMapper executeQuery(int documentId, String[] datasources, IField[] fields) throws ExternalSearchException {
        return getResults(datasources, fields);
    }

    public boolean checkVersion() {
        return true;
    }

    public void setPortfolioDao(IPortfolioDao portfolioDao) {
        this.portfolioDao = portfolioDao;
    }

    public void setArtefactTypes(Map artefactTypes) {
        this.artefactTypes = artefactTypes;
    }

    /**
     * Common method that runs query to get portfolio items and builds XML.
     *
     * @param datasources
     * @param fields
     * @return ResultMapper
     */
    private ResultMapper getResults(String[] datasources, IField[] fields) throws ExternalSearchException {

        final String artefactType = determineArtefactType(datasources);

        // get content types from fields
        String[] contentTypes = null;
        if (fields != null) {
            contentTypes = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                IField field = fields[i];
                contentTypes[i] = (String) field.getValue();
            }
        }

        final Collection portfolioItems = portfolioDao.findAllByNodeType(artefactType, contentTypes);
        final String output = generateXML(portfolioItems);

        return new ResultMapper(output);
    }

    /**
     * Use velocity template to build XML.
     *
     * @param portfolioItems
     * @return String (never null)
     */
    private String generateXML(Collection portfolioItems) throws ExternalSearchException {

        VelocityContext context = new VelocityContext();
        context.put(PORTFOLIO_ITEMS_KEY, portfolioItems);
        context.put(PORTFOLIO_ITEMS_COUNT_KEY, new Integer(portfolioItems.size()));        

        Writer writer = new StringWriter();
        try {
            template.merge(context, writer);
        } catch (Exception e) {
            throw new ExternalSearchException("Failed to generate XML", e);
        }

        return writer.toString();
    }

    /**
     * Determine artefact type from datasources.
     * <br/> If datasources is null or has more than 1 value return null which will be treated by the DAO to mean all artefact types.
     * <br/> Otherwise will return either {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_}
     * or {@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_} or null if it can't recognise the datasource.
     *
     * @param datasources
     * @return artefact type or null.
     */
    private String determineArtefactType(String[] datasources) {

        if (artefactTypes != null && datasources != null && datasources.length == 1) {
            return (String) artefactTypes.get(datasources[0]);
        }

        return null;
    }

    /**
     * Map that holds lists of artefact types keyed on datasource name.
     */
    private Map artefactTypes;

    /**
     * Velocity template.
     */
    private Template template;

    /**
     * DAO.
     */
    private IPortfolioDao portfolioDao;

    private static final String PORTFOLIO_ITEMS_KEY = "portfolioItems";
    private static final String PORTFOLIO_ITEMS_COUNT_KEY = "numPortfolioItems";
    private static final String VELOCITY_TEMPLATE = "com/zynap/talentstudio/organisation/portfolio/search/mockPortfolioSearcherTemplate.vm";
}
