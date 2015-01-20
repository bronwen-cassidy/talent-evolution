package com.zynap.talentstudio.web.portfolio;

/**
 * User: amark
 * Date: 31-May-2005
 * Time: 11:19:13
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.ContentType;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;
import com.zynap.talentstudio.organisation.portfolio.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestDocumentSearchWrapper extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        documentSearchWrapper = new DocumentSearchWrapper(new DocumentSearchQuery());

        // set databases
        final HashMap databaseNodes = new HashMap();
        databaseNodes.put(Node.POSITION_UNIT_TYPE_, "positiondata");
        databaseNodes.put(Node.SUBJECT_UNIT_TYPE_, "subjectdata");
        documentSearchWrapper.setDataSources(databaseNodes);

        portfolioService = (IPortfolioService) applicationContext.getBean("portfolioService");

        Collection contentTypes = getAvailableContentTypes();
        documentSearchWrapper.setContentTypes(contentTypes);
    }

    public void testClearResults() throws Exception {

        documentSearchWrapper.setCurrentNodes(new ArrayList());
        documentSearchWrapper.setMappedResults(new HashMap());
        documentSearchWrapper.setItemWrapper(new PortfolioItemWrapperBean(new PortfolioItem(), new PortfolioItemFile()));

        assertNotNull(documentSearchWrapper.getCurrentNodes());
        assertNotNull(documentSearchWrapper.getMappedResults());
        assertNotNull(documentSearchWrapper.getItemWrapper());

        documentSearchWrapper.clearResults();
        assertNull(documentSearchWrapper.getCurrentNodes());
        assertNull(documentSearchWrapper.getMappedResults());
        assertNull(documentSearchWrapper.getItemWrapper());
    }

    public void testSetSelectedContent() throws Exception {

        // select the first content type
        Collection allContentTypes = getAvailableContentTypes();
        final ContentType selectedContentType = (ContentType) allContentTypes.iterator().next();
        String[] selected = {selectedContentType.getId()};

        // set on wrapper - should only be 1 selected content type
        documentSearchWrapper.setSelectedContent(selected);
        final List selectedContentTypes = documentSearchWrapper.getSelectedContentTypes();
        assertEquals(1, selectedContentTypes.size());
        final SelectionNode actualContentType = (SelectionNode) selectedContentTypes.iterator().next();
        assertEquals(selectedContentType.getId(), actualContentType.getValue());
    }

    public void testSetSelectedContentMany() throws Exception {
        Collection allContentTypes = getAvailableContentTypes();
        int index = allContentTypes.size() / 2;

        String[] selected = new String[index];
        int passIndex = index - 1;
        for (Iterator iterator = allContentTypes.iterator(); iterator.hasNext(); passIndex--) {
            ContentType contentType = (ContentType) iterator.next();
            if (passIndex >= 0) {
                selected[passIndex] = contentType.getId();
            } else {
                break;
            }
        }

        // set on wrapper - should only be 1 selected content type
        documentSearchWrapper.setSelectedContent(selected);
        final List selectedContentTypes = documentSearchWrapper.getSelectedContentTypes();
        assertEquals(index, selectedContentTypes.size());
    }

    public void testSetContentTypesWrapped() throws Exception {

        Collection allContentTypes = getAvailableContentTypes();
        documentSearchWrapper.setContentTypes(allContentTypes);
        Collection wrappedContentTypes = documentSearchWrapper.getContentTypes();
        for (Iterator iterator = wrappedContentTypes.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            Class nodeClass = SelectionNode.class;
            assertTrue(nodeClass.isInstance(o));
        }
    }

    public void testSetContentTypes() throws Exception {

        // content types are set in setUp - check that all content types returned by wrapper are from the original list
        Collection allContentTypes = getAvailableContentTypes();
        final Collection contentTypes = documentSearchWrapper.getContentTypes();

        // check there are the same number of items in both collections
        assertEquals(allContentTypes.size(), contentTypes.size());
        for (Iterator iterator = contentTypes.iterator(); iterator.hasNext();) {
            SelectionNode node = (SelectionNode) iterator.next();
            final String contentTypeId = (String) node.getValue();
            boolean found = false;
            for (Iterator iterator1 = allContentTypes.iterator(); iterator1.hasNext();) {
                ContentType contentType = (ContentType) iterator1.next();
                found = (contentTypeId.equals(contentType.getId()));
                if (found) break;
            }

            assertTrue(found);
        }
    }

    public void testGetSelectedSources() throws Exception {
        assertNull(documentSearchWrapper.getSelectedSources());
    }

    public void testGetDataSources() throws Exception {

        // dataSources are set in setUp()
        final Collection dataSources = documentSearchWrapper.getDataSources();
        for (Iterator iterator = dataSources.iterator(); iterator.hasNext();) {
            SelectionNode database = (SelectionNode) iterator.next();
            final Object messageKey = database.getMessageKey();
            assertTrue(messageKey.equals(Node.SUBJECT_UNIT_TYPE_) || messageKey.equals(Node.POSITION_UNIT_TYPE_));
        }
    }

    public void testGetSelectedContentTypes() throws Exception {

        // if no content types have been selected the list should not be null, but empty
        final List selectedContentTypes = documentSearchWrapper.getSelectedContentTypes();
        assertTrue(selectedContentTypes != null && selectedContentTypes.isEmpty());
    }

    public void testSetSelectedItems() throws Exception {

        // add two portfolio items - ids must be unique
        Collection newPortfolioItems = new HashSet();
        final PortfolioItem item1 = new PortfolioItem();
        final Long item1Id = new Long(1);
        item1.setId(item1Id);
        newPortfolioItems.add(item1);

        final PortfolioItem item2 = new PortfolioItem();
        final Long item2Id = new Long(2);
        item2.setId(item2Id);
        newPortfolioItems.add(item2);

        documentSearchWrapper.setPortfolioItems(newPortfolioItems);

        // set selected to null and check that all items are deselected
        documentSearchWrapper.setSelectedItems(null);
        Collection currentPortfolioItems = documentSearchWrapper.getPortfolioItems();
        for (Iterator iterator = currentPortfolioItems.iterator(); iterator.hasNext();) {
            SelectionNode selectionNode = (SelectionNode) iterator.next();
            assertFalse(selectionNode.isSelected());
        }

        // list of example document ids must be empty since selected was set to null
        String exampleDocumentIds = documentSearchWrapper.getSearchQuery().getExampleDocumentIds();
        assertNull(exampleDocumentIds);

        // only select 1 item and check that item is selected
        Long[] items = {item1Id};
        documentSearchWrapper.setSelectedItems(items);
        currentPortfolioItems = documentSearchWrapper.getPortfolioItems();

        // should have same number of items in both collections
        assertEquals(newPortfolioItems.size(), currentPortfolioItems.size());
        for (Iterator iterator = currentPortfolioItems.iterator(); iterator.hasNext();) {
            SelectionNode selectionNode = (SelectionNode) iterator.next();
            final Long id = (Long) selectionNode.getValue();

            // check that the selected item is marked as selected and that the other one is not selected
            if (id.equals(item1Id)) {
                assertTrue(selectionNode.isSelected());
            } else {
                assertFalse(selectionNode.isSelected());
            }
        }

        // list of example document ids must contain only the first item's id
        exampleDocumentIds = documentSearchWrapper.getSearchQuery().getExampleDocumentIds();
        assertEquals(item1Id.toString(), exampleDocumentIds);
    }

    public void testIsNotEmpty() throws Exception {
        assertFalse(documentSearchWrapper.isNotEmpty());
    }

    public void testGetSelectedItems() throws Exception {
        final Long[] selectedItems = documentSearchWrapper.getSelectedItems();
        assertEquals(0, selectedItems.length);
    }

    public void testSetSelectedSources() throws Exception {

        // check datasources are not originally selected
        Collection dataSources = documentSearchWrapper.getDataSources();
        for (Iterator iterator = dataSources.iterator(); iterator.hasNext();) {
            SelectionNode selectionNode = (SelectionNode) iterator.next();
            assertFalse(selectionNode.isSelected());
        }

        // set selected to be subject datasource and check that it is selected
        final String selectedDataSource = Node.SUBJECT_UNIT_TYPE_;
        documentSearchWrapper.setSelectedSources(selectedDataSource);
        dataSources = documentSearchWrapper.getDataSources();
        for (Iterator iterator = dataSources.iterator(); iterator.hasNext();) {
            SelectionNode selectionNode = (SelectionNode) iterator.next();
            if (selectionNode.getValue().equals(selectedDataSource))
                assertTrue(selectionNode.isSelected());
            else
                assertFalse(selectionNode.isSelected());
        }

        // check that the sources in the document filter only contains the selected data source
        final String sources = documentSearchWrapper.getSearchQuery().getSources();
        assertEquals(selectedDataSource, sources);
    }

    public void testGetSelectedResults() throws Exception {
        assertEquals(0, documentSearchWrapper.getSelectedResults().length);
    }

    public void testSetSelectedResults() throws Exception {

        // add 1 item belonging to one artefact
        final List searchResults = new ArrayList();
        final SearchResult searchResult1 = new SearchResult();
        final Long firstArtefactId = new Long(1);
        searchResult1.setArtefactId(firstArtefactId.toString());
        searchResult1.setItemId(new Long(1));
        searchResults.add(searchResult1);

        // add 2 items belonging to second artefact
        final SearchResult searchResult2 = new SearchResult();
        final Long secondArtefactId = new Long(2);
        searchResult2.setArtefactId(secondArtefactId.toString());
        searchResult2.setItemId(new Long(2));
        searchResults.add(searchResult2);
        final SearchResult searchResult3 = new SearchResult();
        searchResult3.setArtefactId(secondArtefactId.toString());
        searchResult3.setItemId(new Long(3));
        searchResults.add(searchResult3);

        // sorted map should contain 2 entries - one per artefact
        final List itemIds = new ArrayList();
        final Map mappedSearchResults = PortfolioItemHelper.mapResults(searchResults, itemIds);
        assertEquals(2, mappedSearchResults.size());

        // set on wrapper
        documentSearchWrapper.setMappedResults(mappedSearchResults);

        // set first element as selected result and check that it is added to the list of portfolio items
        documentSearchWrapper.setSelectedResults(new Long[]{searchResult1.getItemId()});
        final Collection portfolioItems = documentSearchWrapper.getPortfolioItems();
        assertEquals(1, portfolioItems.size());
        for (Iterator iterator = portfolioItems.iterator(); iterator.hasNext();) {
            SelectionNode selectionNode = (SelectionNode) iterator.next();
            assertEquals(searchResult1.getItemId(), selectionNode.getValue());
        }
    }

    private Collection getAvailableContentTypes() {
        return portfolioService.getAllContentTypes(SecurityConstants.VIEW_ACTION);
    }

    DocumentSearchWrapper documentSearchWrapper;
    private IPortfolioService portfolioService;
}