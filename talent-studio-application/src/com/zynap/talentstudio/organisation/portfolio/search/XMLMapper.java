/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.VisitorSupport;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultNamespaceContext;

import com.zynap.common.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class XMLMapper implements IMapper {

    public XMLMapper() {
    }

    public ISearchResult map(IResultMapper resultMapper) throws ExternalSearchException {

        ISearchResult result = new SearchResult();

        Document document = getDocument(((ResultMapper) resultMapper).getXMLResults());

        // check for errors
        if (isErrorResponse(document)) {
            getError(document);
        }

        // check for results
        List numHitsNodes = selectNodesWithNamespace(document, NUM_HITS_TAG_SELECTOR);
        if (numHitsNodes != null && numHitsNodes.size() > 0) {
            List hitNodes = selectNodesWithNamespace(document, HITS_TAG_SELECTOR);
            for (Iterator iterator = hitNodes.iterator(); iterator.hasNext();) {
                Element hit = (Element) iterator.next();
                if (hit == null) continue;
                SearchResult singleHit = new SearchResult();
                ResultNodeVisitor resultNodeVisitor = new ResultNodeVisitor(singleHit);
                hit.accept(resultNodeVisitor);
                result.addSingleResult(singleHit);
            }
        }

        return result;
    }

    /**
     * Transforms the xml into a Dom Document object.
     *
     * @param xml - the xml string
     * @return Document  - the read Dom object
     */
    private Document getDocument(String xml) throws MappingException {
        if (xml.indexOf(0xb) != -1) {
            xml = StringUtil.validateXml(xml);
        }
        final SAXReader reader = new SAXReader();
        try {
            InputStreamReader reader1 = new InputStreamReader(new ByteArrayInputStream(xml.getBytes()), DEFAULT_XML_ENCODING);
            return reader.read(reader1);
        } catch (UnsupportedEncodingException e) {
            throw new MappingException(e.getMessage(), e);
        } catch (DocumentException e) {
            throw new MappingException(e.getMessage(), e);
        }
    }

    private void getError(Document document) throws ExternalSearchException {

        Node node = document.selectSingleNode(RESPONSE_XPATH_EXPRESSION);
        ErrorVisitor visitor = new ErrorVisitor();
        node.accept(visitor);

        String errorKeyCode = visitor.getErrorKeyCode();
        if (errorCodes.containsKey(errorKeyCode)) {
            throw (ExternalSearchException) errorCodes.get(errorKeyCode);
        }

        throw new ExternalSearchException("Unknown exception thrown", "unknown.autonomy.error");
    }

    /**
     * Determines if the response failed.
     *
     * @param document - the DOM document
     * @return true if the request produced a "ERROR" request false otherwise
     */
    private boolean isErrorResponse(Document document) {
        XPath errorSelector = DocumentHelper.createXPath(RESPONSE_TAG_EXPRESSION);
        Node errorNode = errorSelector.selectSingleNode(document);
        String error = errorNode.getText();

        return StringUtils.hasText(error) && error.toUpperCase().indexOf(ERROR_TAG) != -1;
    }

    private List selectNodesWithNamespace(Document document, String tagSelector) {
        XPath linkselector = DocumentHelper.createXPath(tagSelector);
        Node node = document.selectSingleNode(RESPONSE_XPATH_EXPRESSION);
        linkselector.setNamespaceContext(DefaultNamespaceContext.create(node));
        return linkselector.selectNodes(document);
    }

    public void setErrorCodes(Map errorCodes) {
        this.errorCodes = errorCodes;
    }

    private Map errorCodes = new HashMap();


    final class ResultNodeVisitor extends VisitorSupport {

        ResultNodeVisitor(SearchResult result) {
            this.result = result;
        }

        public void visit(Element node) {
            final String nodeName = node.getQName().getName();
            final String nodeValue = node.getTextTrim();
            logger.debug("ResultNodeVisitor:visit() -> Find node,value = [" + nodeName + ", " + nodeValue + "]");
            if ("reference".equals(nodeName)) {
                result.setReference(nodeValue);
                result.setDocumentId(nodeValue);
            } else if ("title".equals(nodeName)) {
                result.setArtefactTitle(nodeValue);
            } else if ("weight".equals(nodeName)) {
                result.setResultWeight(nodeValue);
            } else if ("summary".equals(nodeName)) {
                // We need to trim summary down - for now lets say less than 300 characters
                String summary = nodeValue;
                if (summary != null && summary.length() > DEFAULT_SUMMARY_LENGTH) {
                    summary = summary.substring(0, DEFAULT_SUMMARY_LENGTH);
                    summary += "...";
                }
                result.setSummary(summary);
            } else if ("ITEM_ID".equals(nodeName)) {
                result.setItemId(Long.valueOf(nodeValue));
            } else if ("ARTEFACT_ID".equals(nodeName)) {
                result.setArtefactId(nodeValue);
            } else if ("CONTENT_TITLE".equals(nodeName)) {
                result.setContentTitle(nodeValue);
            } else if ("DRECONTENT".equals(nodeName)) {
                result.setContent(nodeValue);
            } else if ("CREATED_BY".equals(nodeName)) {
                result.setCreatedBy(nodeValue);
            } else if ("SCOPE".equals(nodeName)) {
                result.setScope(nodeValue);
            } else if ("TYPE".equals(nodeName)) {
                result.setArtefactType(nodeValue);
            } else {
                logger.info("We don't take care of this node = " + nodeName);
            }
        }

        private final SearchResult result;
        static final int DEFAULT_SUMMARY_LENGTH = 300;
    }

    final class ErrorVisitor extends VisitorSupport {

        ErrorVisitor() {
        }

        public void visit(Element node) {
            String name = node.getQName().getName();
            if (!StringUtils.hasText(name)) return;
            if (ERROR_ID.equals(name)) {
                errorKeyCode = node.getTextTrim();
            }
        }

        public String getErrorKeyCode() {
            return errorKeyCode;
        }

        protected static final String ERROR_KEY = "errorstring";
        protected static final String ERROR_DESCRIPTION = "errordescription";
        static final String ERROR_ID = "errorid";
        private String errorKeyCode;
    }

    private final Log logger = LogFactory.getLog(getClass());

    protected static final String DATABASE_NAME_EXPRESSION = "//database/name";
    private static final String DEFAULT_XML_ENCODING = "UTF8";
    private static final String NUM_HITS_TAG_SELECTOR = "//autn:numhits";
    private static final String HITS_TAG_SELECTOR = "//autn:hit";
    private static final String RESPONSE_XPATH_EXPRESSION = "//responsedata";
    private static final String RESPONSE_TAG_EXPRESSION = "//response";
    private static final String ERROR_TAG = "ERROR";
}
