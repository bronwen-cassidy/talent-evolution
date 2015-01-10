package com.zynap.talentstudio.integration.delegate;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.mapping.ExternalRefMapping;
import com.zynap.talentstudio.common.mapping.IExternalRefMappingDao;
import com.zynap.talentstudio.security.UserSessionFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 18-Oct-2005
 * Time: 11:14:39
 * <p/>
 * Class that maps external ids to internal ones and vice versa.
 * <br/> By external id we mean an id that a 3rd party system uses as its primary key for an artefact that exists in our application.
 * <br/> An internal id is thus the primary key used by our application for the same artefact.
 */
public class ZynapIdMapper {

    /**
     * Get internal id.
     *
     * @param externalId      The external id
     * @param internalRefName The reference name, this is the fully qualified name of the node being added, example
     *                        <pre>OrganisationUnit</pre>
     * @return The internal id. If no internal id has been found for the given external id null is returned.
     */
    public Serializable getInternalId(Serializable externalId, String internalRefName, Collection<String> classNames) {

        // check if id is one of the preset ids that refer to artefacts like the default org unit or position.
        Serializable id = (Serializable) defaultPresetIds.get(externalId);

        if (id != null) return id;

        List names = getPossibleClassName(internalRefName, classNames);
        for (Iterator iterator = names.iterator(); iterator.hasNext();) {
            String reference = (String) iterator.next();
            final ExternalRefMapping mappingByExternalId = externalRefMappingDao.getMappingByExternalId(externalId.toString(), UserSessionFactory.getUserSession().getId(), reference);
            if (mappingByExternalId != null)
                return mappingByExternalId.getInternalRefId();
        }
        // no internal id found for the given external id
        return null;
    }

    private List getPossibleClassName(String internalRefName, Collection<String> classNames) {
        List<String> names = new ArrayList<String>();
        try {
            Class objectClass = Class.forName(internalRefName);
            for (Iterator<String> iterator = classNames.iterator(); iterator.hasNext();) {
                String s = iterator.next();
                if (objectClass.isAssignableFrom(Class.forName(s)))
                    names.add(s);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return names;
    }

    /**
     * Get external id.
     * <br/> If not found, will create an association between the internal and external ids and return the new external id.
     *
     * @param internalId      The internal id
     * @param internalRefName
     * @return ExternalRefMapping
     * @throws TalentStudioException
     */
    public ExternalRefMapping getExternalId(Serializable internalId, String internalRefName) throws TalentStudioException {

        final User user = UserSessionFactory.getUserSession().getUser();
        return getExternalId(internalId, internalRefName, user);
    }

    public ExternalRefMapping getExternalId(Serializable internalId, String internalRefName, User user, boolean generateNew) throws TalentStudioException {

        final String id = internalId.toString();

        ExternalRefMapping value = externalRefMappingDao.getMappingByInternalId(id, user.getId(), internalRefName);
        // todo need to have a flag to identify whether we have generated the external mapping id or it was provided.
        if (value == null && generateNew) {
            value = new ExternalRefMapping(null, internalRefName, id, id, user, true);
            externalRefMappingDao.create(value);
        }

        return value;
    }

    public ExternalRefMapping getExternalId(Serializable internalId, String internalRefName, boolean generateNew) throws TalentStudioException {
        final User user = UserSessionFactory.getUserSession().getUser();
        return  getExternalId(internalId, internalRefName, user, generateNew);
    }

    public ExternalRefMapping getExternalId(Serializable internalId, String internalRefName, User user) throws TalentStudioException {
        return  getExternalId(internalId, internalRefName, user, true);
    }

    /**
     * Create external reference mappings for all objects in collection.
     *
     * @param createdReferences A map of IDomainObjects  - the keys are ExternalRefMapping objects
     * @throws TalentStudioException
     */
    public void createExternalReferences(Map<IDomainObject, ExternalRefMapping> createdReferences) throws TalentStudioException {
        final User user = UserSessionFactory.getUserSession().getUser();
        createExternalReferences(createdReferences, user);
    }

    public void createExternalReferences(Map<IDomainObject, ExternalRefMapping> createdReferences, User user) throws TalentStudioException {
        Set<Map.Entry<IDomainObject, ExternalRefMapping>> references = createdReferences.entrySet();

        for (Iterator<Map.Entry<IDomainObject, ExternalRefMapping>> iterator = references.iterator(); iterator.hasNext(); ) {
            Map.Entry<IDomainObject, ExternalRefMapping> ref = iterator.next();
            IDomainObject domainObject = ref.getKey();
            ExternalRefMapping mapping = ref.getValue();
            mapping.setInternalRefId(domainObject.getId().toString());
            mapping.setUser(user);
            externalRefMappingDao.create(mapping);
        }
    }

    public void updateId(String externalId, String updatedExternalId, String className) throws TalentStudioException {
        ExternalRefMapping externalRefMapping = externalRefMappingDao.getMappingByExternalId(externalId, UserSessionFactory.getUserSession().getId(), className);
        externalRefMapping.setExternalRefId(updatedExternalId);
        externalRefMapping.setGenerated(false);
        externalRefMappingDao.update(externalRefMapping);
    }

    public IExternalRefMappingDao getExternalRefMappingDao() {
        return externalRefMappingDao;
    }

    public void setExternalRefMappingDao(IExternalRefMappingDao externalRefMappingDao) {
        this.externalRefMappingDao = externalRefMappingDao;
    }

    public Map getDefaultPresetIds() {
        return defaultPresetIds;
    }

    public void setDefaultPresetIds(Map defaultPresetIds) {
        this.defaultPresetIds = defaultPresetIds;
    }

    /**
     * The DAO.
     */
    private IExternalRefMappingDao externalRefMappingDao;

    /**
     * Contains the list of external ids that represent the default artefacts in the system - eg: the default org unit and the default position.
     */
    private Map defaultPresetIds;
}
