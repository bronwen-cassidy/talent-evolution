package com.zynap.talentstudio.common.hibernate;

/**
 * User: amark
 * Date: 18-Aug-2006
 * Time: 12:18:37
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.common.hibernate.interceptor.strategy.NoOpInterceptorStrategy;
import com.zynap.talentstudio.common.hibernate.interceptor.strategy.NodeInterceptorStrategy;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.areas.Area;

public class TestZynapEntityInterceptor extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        zynapEntityInterceptor = (ZynapEntityInterceptor) getBean("zynapEntityInterceptor");

        entity = DEFAULT_ORG_UNIT;
    }

    public void testInstantiate() throws Exception {
        assertNull(zynapEntityInterceptor.instantiate(entity.getClass(), entity.getId()));
    }

    public void testFindDirty() throws Exception {
        assertNull(zynapEntityInterceptor.findDirty(entity, entity.getId(), null, null, null, null));
    }

    public void testIsUnsaved() throws Exception {
        assertNull(zynapEntityInterceptor.isUnsaved(entity));
    }

    public void testOnSave() throws Exception {
        assertFalse(zynapEntityInterceptor.onSave(entity, entity.getId(), null, null, null));
    }

    public void testOnFlushDirty() throws Exception {
        assertFalse(zynapEntityInterceptor.onFlushDirty(entity, entity.getId(), null, null, null, null));
    }

    public void testOnLoad() throws Exception {
        assertFalse(zynapEntityInterceptor.onLoad(entity, entity.getId(), null, null, null));
    }

    public void testOnDelete() throws Exception {
        zynapEntityInterceptor.onDelete(entity, entity.getId(), null, null, null);
    }

    public void testGetEntityInterceptorStrategy() throws Exception {
        // org units are not audited at the moment
        assertTrue(zynapEntityInterceptor.getEntityInterceptorStrategy(new OrganisationUnit()) instanceof NoOpInterceptorStrategy);
        assertTrue(zynapEntityInterceptor.getEntityInterceptorStrategy(new Position()) instanceof NodeInterceptorStrategy);
        assertTrue(zynapEntityInterceptor.getEntityInterceptorStrategy(new Subject()) instanceof NodeInterceptorStrategy);
        assertTrue(zynapEntityInterceptor.getEntityInterceptorStrategy(new Area()) instanceof NoOpInterceptorStrategy);
    }

    private Node entity;
    private ZynapEntityInterceptor zynapEntityInterceptor;
}