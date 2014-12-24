package com.zynap.common.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

/**
 * User: amark
 * Date: 24-Oct-2005
 * Time: 17:35:12
 */
public final class XmlUtils {

    /**
     * Logger.
     */
    private static final Log logger = LogFactory.getLog(XmlUtils.class);

    public static NodeList getNodes(Document document, String tagName) {
        return getNodes(document.getDocumentElement(), tagName);
    }

    public static NodeList getNodes(Element element, String tagName) {
        return element.getElementsByTagName(tagName);
    }

    public static Element getFirstElement(Document document, String tagName) {
        return getFirstElement(document.getDocumentElement(), tagName);
    }

    public static Element getFirstElement(Element parentElement, String tagName) {
        final NodeList nodeList = parentElement.getElementsByTagName(tagName);

        Element element = null;
        if (nodeList.getLength() > 0) {
            element = (Element) nodeList.item(0);
        }

        return element;
    }

    public static boolean getBooleanValue(Element element, String attributeName) {
        return Boolean.valueOf(getAttributeValue(element, attributeName));
    }

    public static String getAttributeValue(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);

        if (!StringUtils.hasLength(value)) {
            value = null;
        }

        return value;
    }

    /**
     * Get new Document instance.
     * Throws RuntimeException if cannot instanstiate DocumentBuilderFactory or DocumentBuilder.
     *
     * @return Document
     */
    public static Document newDocument() {
        try {
            return getDocumentBuilderFactory().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method takes an InputSource and filters the resulting document to exclude all white space characters.
     * Validation is done against the provided schema.
     * If this method needs to apply a different filter we should then pass in the filter to use as a parameter.
     *
     * @param inputSource cannot be null
     * @param schemaName  cannot be null
     * @return Document that has had all white space character nodes removed
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     */
    public static Document createFilteredDocument(InputSource inputSource, String schemaName) throws ParserConfigurationException, SAXException, TransformerException {

        final boolean hasSchema = StringUtils.hasText(schemaName);

        SAXParserFactory f = getSAXParserFactory();
        f.setNamespaceAware(true);

        if (hasSchema) {
            f.setValidating(true);
        }

        XMLFilter reader = new XmlWhiteSpaceFilter(f.newSAXParser().getXMLReader());
        reader.setErrorHandler(new DefaultXmlErrorHandler());
        try {
            reader.setProperty(XmlConstants.SCHEMA_ATTRIBUTE_KEY, XmlConstants.XML_SCHEMA_VALUE);
        } catch (SAXNotRecognizedException e) {
            logger.debug(e.getMessage(), e);
        } catch (SAXNotSupportedException e) {
            logger.debug(e.getMessage(), e);
        }

        if (hasSchema) {
            URL schemaURL = XmlUtils.class.getClassLoader().getResource(schemaName);
            reader.setProperty(XmlConstants.SCHEMA_SOURCE_ATTRIBUTE_KEY, schemaURL.getPath());
        }

        return filter(reader, inputSource);
    }

    /**
     * Creates a non validated document that has had white space nodes filtered from it.
     *
     * @param filePath the path and name of the xml to create the document from
     * @return Document  the filtered document
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document createFilteredDocument(String filePath) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        SAXParserFactory f = getSAXParserFactory();
        XMLFilter reader = new XmlWhiteSpaceFilter(f.newSAXParser().getXMLReader());
        URL schemaURL = XmlUtils.class.getClassLoader().getResource(filePath);
        InputStream inputStream = schemaURL.openStream();
        return filter(reader, new InputSource(inputStream));
    }

    /**
     * Builds a dom document given an inputSource.
     * <p/>
     * No validation is done if validation is required please use instead. {@link #createDocument(String, String)}
     *
     * @param xmlFileName
     * @return Document
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document createDocument(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = getDocumentBuilderFactory();
        return parseDocument(documentBuilderFactory, xmlFileName);
    }

    /**
     * Creates a document from an xml formatted string of data, no validation or filtering are done.
     *
     * @param inputSource
     * @return Document
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document createDocument(InputSource inputSource) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = getDocumentBuilderFactory();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        return builder.parse(inputSource);
    }

    /**
     * Creates a document that has been validated.
     *
     * @param xmlFileName the xmlFile to load
     * @param schemaName  the path and name of the schema file to load and use.
     * @return Document   a document that has been validated against the schema.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document createDocument(String xmlFileName, String schemaName) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = getDocumentBuilderFactory();
        documentBuilderFactory.setValidating(true);
        URL schemaURL = XmlUtils.class.getClassLoader().getResource(schemaName);
        documentBuilderFactory.setAttribute(XmlConstants.SCHEMA_ATTRIBUTE_KEY, XmlConstants.XML_SCHEMA_VALUE);
        documentBuilderFactory.setAttribute(XmlConstants.SCHEMA_SOURCE_ATTRIBUTE_KEY, schemaURL.getPath());
        return parseDocument(documentBuilderFactory, xmlFileName);
    }

    /**
     * Build a DOM given the xml data as a string.
     * <p/>
     * This method internally calls {@link #createDocument(org.xml.sax.InputSource)} Creating an inputSource from the
     * xml data that is passed in as a string.
     *
     * @param xmlData should be a well formatted xml string
     * @return Document that has not been filtered or validated against any schema.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document buildDocument(String xmlData) throws IOException, ParserConfigurationException, SAXException {
        return createDocument(new InputSource(new StringReader(xmlData)));
    }

    /**
     * Get SAXParserFactory.
     *
     * @return SAXParserFactory
     */
    private static SAXParserFactory getSAXParserFactory() {
        SAXParserFactory f = SAXParserFactory.newInstance();

        logger.debug("Using SAXParserFactory implementation: " + f.getClass().getName());

        return f;
    }

    /**
     * Get DocumentBuilderFactory.
     *
     * @return DocumentBuilderFactory
     */
    private static DocumentBuilderFactory getDocumentBuilderFactory() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        logger.debug("Using DocumentBuilderFactory implementation: " + documentBuilderFactory.getClass().getName());

        return documentBuilderFactory;
    }

    private static Document filter(XMLFilter reader, InputSource inputSource) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        DOMResult result = new DOMResult();
        transformer.transform(new SAXSource(reader, inputSource), result);
        return (Document) result.getNode();
    }

    private static Document parseDocument(DocumentBuilderFactory documentBuilderFactory, String xmlFileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        builder.setErrorHandler(new DefaultXmlErrorHandler());
        URL rulesURL = XmlUtils.class.getClassLoader().getResource(xmlFileName);
        InputStream inputStream = rulesURL.openStream();
        return builder.parse(new InputSource(inputStream));
    }
}
