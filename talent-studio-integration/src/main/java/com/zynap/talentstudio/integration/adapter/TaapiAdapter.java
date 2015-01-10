package com.zynap.talentstudio.integration.adapter;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.mapping.ExternalRefMapping;
import com.zynap.talentstudio.integration.delegate.ZynapIdMapper;
import com.zynap.talentstudio.middleware.soap.taapi.TaapiClient;
import com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType;
import com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.util.AuditUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bronwen.
 * Date: 21/03/12
 * Time: 11:20
 */
public class TaapiAdapter implements IAdapter {

    public static final User WEBSERVICE_USER = new User(new Long(2));
    private static final Log logger = LogFactory.getLog(TaapiAdapter.class);
    public static final String TIPS_FOR_COMMUNICATING = "tips_for_communicating";

    public void create(IDomainObject obj, Population population) throws TalentStudioException {

        if (obj instanceof Subject) {
            Long populationId = null;
            if (population != null)  populationId = population.getId();

            try {
                String externalRefId = taapiClient.createRespondent((Subject) obj, populationId);

                Map<IDomainObject, ExternalRefMapping> refs = new HashMap<IDomainObject, ExternalRefMapping>();
                ExternalRefMapping mapping = new ExternalRefMapping(null, Subject.class.getName(), obj.getId().toString(), externalRefId, null, false);
                refs.put(obj, mapping);
                idMapper.createExternalReferences(refs);
            } catch (TalentStudioException e) {
                logger.error(e.getMessage());
                // nothing to do let us log the error and continue processing
                throw e;
            }
        }
    }

    public AdaptorResults exportPopulation(Population population) throws TalentStudioException {
        TaapiResults adaptorResults = new TaapiResults();
        // load the population check to see if any already have been exported - mark for update
        List<SubjectDTO> subjects = populationEngine.findSubjects(population, IDomainObject.ROOT_USER_ID);
        for (SubjectDTO subject : subjects) {
            ExternalRefMapping refMapping = idMapper.getExternalId(subject.getId(), Subject.class.getName(), false);
            Subject node = subjectService.findById(subject.getId());
            if (refMapping != null) {
                // update the subject
                taapiClient.updateRespondent(refMapping.getExternalRefId(), node, population.getId());
                adaptorResults.addModified(node);
            } else {
                try {
                    create(node, population);
                    adaptorResults.addAdded(node);
                } catch (TalentStudioException e) {
                    adaptorResults.addErrored(node);
                }
            }
        }
        return adaptorResults;
    }

    public void update(IDomainObject obj, Population population) throws TalentStudioException {
        ExternalRefMapping ref = idMapper.getExternalId(obj.getId(), Subject.class.getName(), false);
        Long populationId = null;
        if (population != null) {
            populationId = population.getId();
        }
        if (ref != null) {
            String respondentId = ref.getExternalRefId();
            taapiClient.updateRespondent(respondentId, (Subject) obj, populationId);
        }
    }

    public AdaptorResults importPopulation(Population population) throws TalentStudioException {
        if (attributes == null) attributes = dynamicAttributeService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        List<RespondentInfoType> respondents = taapiClient.findAllRespondents(population.getId());

        Map<IDomainObject, ExternalRefMapping> refs = new HashMap<IDomainObject, ExternalRefMapping>();
        TaapiResults adaptorResults = new TaapiResults();
        List<SubjectDTO> subjects = populationEngine.findSubjects(population, UserSessionFactory.getUserSession().getId());

        for (RespondentInfoType respondent : respondents) {
            try {
                // find the subject
                SubjectDTO dto = new SubjectDTO(new Long(respondent.getExtID()), respondent.getFirstName(), respondent.getLastName(), null);
                if(!subjects.contains(dto)) continue;
                if(respondent.getStatus().equalsIgnoreCase("pending")) {
                    // don't process but add to pending list
                    adaptorResults.addPending(subjects.get(subjects.indexOf(dto)));
                    continue;
                }
                // completed
                Subject s = subjectService.findById(new Long(respondent.getExtID()));

                RespondentInfoType[] details = taapiClient.findResponentInfo(new String[]{respondent.getRespondentID()});
                if(details != null) {
                    // update the attributes
                    TaapiMappingUtils.mapSubjectAttributes(s, attributes, details[0]);
                }

                // get the special content
                List<RespondentContentType> info = taapiClient.getContentInfo(new String[]{respondent.getRespondentID()}, TIPS_FOR_COMMUNICATING, "text");
                if(info != null && info.size() > 0) {
                    RespondentContentType contentType = info.get(0);
                    String text = contentType.getText();
                    TaapiMappingUtils.assignAttributeValue(s, attributes, text, TIPS_FOR_COMMUNICATING);
                }
                subjectService.update(s);
                adaptorResults.addModified(s);

            } catch (TalentStudioException e) {
                e.printStackTrace();
                logger.error("Exception caused importing subject information: " + e.getMessage());
                // subject was null
                assignAndUpdate(refs, adaptorResults.getAddedResults(), respondent);
            } catch (NumberFormatException e) {
                respondent.setExtID(null);
                assignAndUpdate(refs, adaptorResults.getAddedResults(), respondent);
            }
        }

        createExternalRefs(refs);
        return adaptorResults;
    }

    private void createExternalRefs(Map<IDomainObject, ExternalRefMapping> refs) throws TalentStudioException {
        idMapper.createExternalReferences(refs, WEBSERVICE_USER);
    }

    public void update(IDomainObject obj, String respondentId) throws TalentStudioException {
        update(obj, respondentId, null);
    }

    public void update(IDomainObject obj, String respondentId, Population population) throws TalentStudioException {
        Long popuationId = null;
        if (population != null) {
            popuationId = population.getId();
        }

        taapiClient.updateRespondent(respondentId, (Subject) obj, popuationId);
    }

    public void update(IDomainObject obj) throws TalentStudioException {
        update(obj, (Population) null);
    }

    public void update(Long nodeId) throws TalentStudioException {
        ExternalRefMapping ref = idMapper.getExternalId(nodeId, Subject.class.getName(), WEBSERVICE_USER, false);
        if (ref != null) {
            RespondentInfoType respondent = taapiClient.findRespondent(ref.getExternalRefId());
            if (respondent != null) {
                Subject s = subjectService.findById(nodeId);
                assignValues(respondent, s);
                subjectService.update(s);
            }
        }
    }

    public void delete(IDomainObject obj) {
    }

    public List<Node> synchroniseSystems() throws TalentStudioException {

        if (attributes == null) attributes = dynamicAttributeService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        List<RespondentInfoType> respondents = taapiClient.findAllRespondents();
        Map<IDomainObject, ExternalRefMapping> refs = new HashMap<IDomainObject, ExternalRefMapping>();
        List<Node> subjects = new ArrayList<Node>();
        for (RespondentInfoType respondent : respondents) {
            if (!StringUtils.hasText(respondent.getExtID())) {
                // create the subject in our database
                assignAndUpdate(refs, subjects, respondent);
            } else {
                try {
                    // find the subject
                    Subject s = subjectService.findById(new Long(respondent.getExtID()));
                    // update the attributes
                    TaapiMappingUtils.mapSubjectAttributes(s, attributes, respondent);
                    subjectService.update(s);
                    subjects.add(s);
                    if (!StringUtils.hasText(respondent.getCallbackURL())) {
                        // add in the callback url
                        taapiClient.updateRespondent(respondent, s.getId());
                    }
                } catch (TalentStudioException e) {
                    // subject was null
                    assignAndUpdate(refs, subjects, respondent);
                } catch (NumberFormatException e) {
                    respondent.setExtID(null);
                    assignAndUpdate(refs, subjects, respondent);
                }
            }
        }
        createExternalRefs(refs);

        return subjects;
    }

    private void assignAndUpdate(Map<IDomainObject, ExternalRefMapping> refs, List<Node> subjects, RespondentInfoType respondent) throws TalentStudioException {
        Subject s = createSubject(respondent);
        subjects.add(s);
        ExternalRefMapping ref = new ExternalRefMapping(null, Subject.class.getName(), s.getId().toString(), respondent.getRespondentID(), null, false);
        refs.put(s, ref);
        // updates the extId
        update(s, respondent.getRespondentID());
    }

    public void setIdMapper(ZynapIdMapper idMapper) {
        this.idMapper = idMapper;
    }

    public void setTaapiClient(TaapiClient taapiClient) {
        this.taapiClient = taapiClient;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    private Subject createSubject(RespondentInfoType respondent) throws TalentStudioException {
        if (attributes == null) attributes = dynamicAttributeService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        Subject s = new Subject();
        TaapiMappingUtils.removeTaapiAttributes(s, attributes);
        assignValues(respondent, s);
        subjectService.create(s);
        return s;
    }

    private void assignValues(RespondentInfoType respondent, Subject s) {
        if (attributes == null) attributes = dynamicAttributeService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        s.setEmail(respondent.getEMail());
        s.setFirstName(respondent.getFirstName());
        s.setSecondName(respondent.getLastName());
        TaapiMappingUtils.mapSubjectAttributes(s, attributes, respondent);
        AuditUtils.setNodeAudit(s, WEBSERVICE_USER);
    }

    private ZynapIdMapper idMapper;
    private TaapiClient taapiClient;
    private IDynamicAttributeService dynamicAttributeService;
    private ISubjectService subjectService;
    private Collection<DynamicAttribute> attributes = null;
    private IPopulationEngine populationEngine;
}
