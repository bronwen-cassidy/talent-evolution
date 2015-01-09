/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.ISubjectDao;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.SecurityAttribute;
import com.zynap.talentstudio.security.permits.AccessPermit;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class TestHibernatePortfolioDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {

        super.setUp();
        hibernatePortfolioDao = (HibernatePortfolioDao) applicationContext.getBean("hibPortfolioDao");
        hibernateSubjectDao = (ISubjectDao) applicationContext.getBean("hibSubjectDao");
    }

    public void testGetContentTypes() throws Exception {

        final String subjectContentType = Node.SUBJECT_UNIT_TYPE_;
        Collection subjectItems = hibernatePortfolioDao.getContentTypes(subjectContentType);
        for (Iterator iterator = subjectItems.iterator(); iterator.hasNext();) {
            ContentType contentType = (ContentType) iterator.next();
            assertEquals(subjectContentType, contentType.getType());
        }
    }

    public void testGetContentTypesPermissions() throws Exception {

        final String subjectContentType = Node.SUBJECT_UNIT_TYPE_;
        final Collection<IPermit> contentTypePermits = new HashSet<IPermit>();
        contentTypePermits.add(createPermitContent("CV"));
        contentTypePermits.add(createPermitContent("SS"));
        contentTypePermits.add(createPermitContent("TR"));
        Collection subjectItems = hibernatePortfolioDao.getContentTypes(subjectContentType, contentTypePermits);
        assertEquals(3, subjectItems.size());
    }

    public void testGetContentTypesEmpty() throws Exception {

        final Collection contentItems = hibernatePortfolioDao.getContentTypes(Node.POSITION_UNIT_TYPE_, new HashSet());
        assertTrue(contentItems.isEmpty());
    }

    public void testCreateTextItem() throws Exception {

        final Subject target = new Subject(new CoreDetail("Mr", "Joe", "Smith"));
        target.setComments("This is a comment for Joe");
        target.setActive(true);
        hibernateSubjectDao.create(target);

        //final String content = "Content";
        final PortfolioItem itemOne = createTextPortfolioItem("Test CV 1", "comments", "SS");
        itemOne.setNode(target);
        hibernatePortfolioDao.create(itemOne);

        final Long portfolioId = itemOne.getId();
        final PortfolioItem result = (PortfolioItem) hibernatePortfolioDao.findByID(portfolioId);
        assertEquals(itemOne, result);
        assertTrue(result.isText());
        assertNull(result.getFileExtension());
    }

    public void testCreateUploadItem() throws Exception {

        final Subject target = new Subject(new CoreDetail("Mr", "Joe", "Smith"));
        target.setComments("This is a comment for Joe");
        target.setActive(true);
        hibernateSubjectDao.create(target);

        final PortfolioItem itemOne = createPortfolioItem("Subject CV", "/hibernate-mappings/Node.hbm.xml", "CV");
        itemOne.setNode(target);
        hibernatePortfolioDao.create(itemOne);

        final Long portfolioId = itemOne.getId();
        final PortfolioItem result = (PortfolioItem) hibernatePortfolioDao.findByID(portfolioId);
        assertEquals(itemOne, result);
        assertTrue(result.isUpload());
        assertEquals(".xml", itemOne.getFileExtension());
    }

    public void testCreateURLItem() throws Exception {

        final Subject target = new Subject(new CoreDetail("Mr", "Joe", "Smith"));
        target.setComments("This is a comment for Joe");
        target.setActive(true);
        hibernateSubjectDao.create(target);

        //String url = "http://www.yahoo.co.uk";
        final PortfolioItem itemOne = createURLPortfolioItem("Subject CV", "comments", "CV");
        itemOne.setNode(target);
        hibernatePortfolioDao.create(itemOne);

        final Long portfolioId = itemOne.getId();
        final PortfolioItem result = (PortfolioItem) hibernatePortfolioDao.findByID(portfolioId);
        assertEquals(itemOne, result);
        assertTrue(result.isURL());
        assertNull(itemOne.getFileExtension());
    }

    public void testGetPortfolioItemsFromSubject() throws Exception {

        final Subject target = new Subject(new CoreDetail("Mr", "Joe", "Smith"));
        target.setComments("This is a comment for Joe");
        target.setActive(true);

        final String label = "Test2";
        final String comments = "some comments";
        final PortfolioItem portfolioItem = createTextPortfolioItem(label, comments, "SS");
        target.addPortfolioItem(portfolioItem);
        hibernateSubjectDao.create(target);

        final Subject expected = (Subject) hibernateSubjectDao.findByID(target.getId());
        final PortfolioItem found = expected.getPortfolioItems().iterator().next();
        assertEquals(portfolioItem, found);
    }

    public void testGetContentType() throws Exception {

        final ContentType actual = hibernatePortfolioDao.getContentType("CV");
        assertEquals("Curriculum Vitae", actual.getLabel());
    }

    public void testFindAllByNodeType() throws Exception {

        final String nodeType = Node.POSITION_UNIT_TYPE_;
        final String[] contentTypes = {"CF", "SS"};

        Collection all = hibernatePortfolioDao.findAllByNodeType(null, null);
        assertNotNull(all);

        all = hibernatePortfolioDao.findAllByNodeType(nodeType, null);
        assertNotNull(all);

        all = hibernatePortfolioDao.findAllByNodeType(nodeType, contentTypes);
        assertNotNull(all);

        all = hibernatePortfolioDao.findAllByNodeType(null, contentTypes);
        assertNotNull(all);
    }

    public void testFindAllByPositionNodeType() throws Exception {

        final Subject target = new Subject(new CoreDetail("Mr", "Joe", "Smith"));
        target.setComments("This is a comment for Joe");
        target.setActive(true);
        hibernateSubjectDao.create(target);

        final PortfolioItem subjectPortfolioItem = createTextPortfolioItem("subject portfolio item 1", "comments", "SS");
        // set attributes to all individual/manager read, write and public search
        subjectPortfolioItem.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        target.addPortfolioItem(subjectPortfolioItem);
        hibernatePortfolioDao.create(subjectPortfolioItem);

        final PortfolioItem positionPortfolioItem1 = createTextPortfolioItem("position portfolio item 1", "comments", "SS");
        positionPortfolioItem1.setNode(DEFAULT_POSITION);
        positionPortfolioItem1.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        hibernatePortfolioDao.create(positionPortfolioItem1);

        final PortfolioItem positionPortfolioItem2 = createTextPortfolioItem("position portfolio item 2", "comments", "CV");
        positionPortfolioItem2.setNode(DEFAULT_POSITION);
        positionPortfolioItem2.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        hibernatePortfolioDao.create(positionPortfolioItem2);

        // search will return 1 item
        String[] contentTypes = new String[]{"SS"};
        final Collection all = hibernatePortfolioDao.findAllByNodeType(Node.POSITION_UNIT_TYPE_, contentTypes);
        assertTrue(all.contains(positionPortfolioItem1));
        assertFalse(all.contains(positionPortfolioItem2));
        assertFalse(all.contains(subjectPortfolioItem));
    }

    private PortfolioItem createURLPortfolioItem(String label, String comments, String contentTypeId) {

        final ContentType contentType = hibernatePortfolioDao.getContentType(contentTypeId);
        final PortfolioItem itemOne = new PortfolioItem(label, comments, PortfolioItem.STATUS_LIVE, PortfolioItem.PUBLIC_SCOPE, new Date(), PortfolioItem.URL_SUBTYPE);
        itemOne.setContentType(contentType);
        itemOne.setCreatedById(ADMINISTRATOR_USER_ID);

        return itemOne;
    }

    private PortfolioItem createTextPortfolioItem(final String label, final String comments, final String contentTypeId) {

        final ContentType contentType = hibernatePortfolioDao.getContentType(contentTypeId);
        final PortfolioItem itemOne = new PortfolioItem(label, comments, PortfolioItem.STATUS_LIVE, PortfolioItem.PUBLIC_SCOPE, new Date(), PortfolioItem.TEXT_SUBTYPE);
        itemOne.setContentType(contentType);
        itemOne.setCreatedById(ADMINISTRATOR_USER_ID);
        itemOne.setLastModifiedById(ADMINISTRATOR_USER_ID);
        itemOne.setLastModified(new Date());
        return itemOne;
    }

    private PortfolioItem createPortfolioItem(String label, String fileName, String contentTypeId) throws Exception {

//        final InputStream resource = getClass().getResourceAsStream(fileName);
//        final byte[] fileBytes = new byte[40000];
//        int bytesRead = resource.read(fileBytes);
//        byte[] newBytes = new byte[bytesRead];
//        System.arraycopy(fileBytes, 0, newBytes, 0, bytesRead);

        final String fileExtension = fileName.substring(fileName.lastIndexOf(""));
        PortfolioItem itemOne = new PortfolioItem(label, "This is a comment", PortfolioItem.STATUS_LIVE, PortfolioItem.PUBLIC_SCOPE, new Date(), PortfolioItem.UPLOAD_SUBTYPE, fileName, fileExtension);
        itemOne.setContentType(new ContentType(contentTypeId));
        itemOne.setCreatedById(ADMINISTRATOR_USER_ID);

        return itemOne;
    }

    private AccessPermit createPermitContent(String contentType) {

        AccessPermit accessPermit = new AccessPermit();
        accessPermit.setContent(contentType);
        return accessPermit;
    }

    private ISubjectDao hibernateSubjectDao;
    private HibernatePortfolioDao hibernatePortfolioDao;
}
