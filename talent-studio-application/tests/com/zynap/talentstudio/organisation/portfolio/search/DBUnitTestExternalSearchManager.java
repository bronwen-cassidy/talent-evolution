package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @since 02-Apr-2007 11:39:33
 * @version 0.1
 */

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.arenas.ArenaMenuHandler;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.users.IUserService;

import java.util.Collection;

public class DBUnitTestExternalSearchManager extends ZynapDatabaseTestCase {

    protected void setUp() throws Exception {

        super.setUp();
        externalSearchManager = (ExternalSearchManager) applicationContext.getBean("searchManager");
        userService = (IUserService) applicationContext.getBean("userService");
        permitManagerDao = (IPermitManagerDao) applicationContext.getBean("permitManDao");
        arenaMenuHandler = new ArenaMenuHandler();
        IArenaManager arenaManager = (IArenaManager) getBean("arenaManager");
        arenaMenuHandler.setArenaManager(arenaManager);
        arenaMenuHandler.afterPropertiesSet();
    }

    public void testSearch() throws Exception {

        UserSession userSession = getUserSession((long) 22);
        UserSessionFactory.setUserSession(userSession);
        ISearchQuery query = new DocumentSearchQuery();
        query.setQueryText("smurfs");
        query.setSources("subjectdata+positiondata");
        IField selectionNode = new SelectionNode("CV");
        IField[] fields = {selectionNode};
        
        try {
            
            // search for 'smurfs', this should return one result
            ISearchResult results = externalSearchManager.search(query, fields);
            assertEquals(1, results.getHits().size());

            // search for 'sarah', this should return one result
            query.setQueryText("sarah");
            results = externalSearchManager.search(query, fields);
            assertEquals(1, results.getHits().size());

            // search for 'gfjdsj', this should return no results
            query.setQueryText("gfjdsj");
            results = externalSearchManager.search(query, fields);
            assertEquals(0, results.getHits().size());
            
        }
        catch (ExternalSearchException ex) {
            logger.error(ex);
        }
    }

    private UserSession getUserSession(Long userId) throws TalentStudioException {
        User user = (User) userService.findById(userId);
        final Collection<IPermit> accessPermits = permitManagerDao.getAccessPermits(userId);
        UserPrincipal userPrincipal = new UserPrincipal(user, accessPermits);
        return new UserSession(userPrincipal, arenaMenuHandler);
    }

    protected String getDataSetFileName() {
        return "dbUnit-search-test-data.xml";
    }

    private ExternalSearchManager externalSearchManager;
    private IUserService userService;
    private IPermitManagerDao permitManagerDao;
    private ArenaMenuHandler arenaMenuHandler;
}