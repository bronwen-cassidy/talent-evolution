package com.zynap.talentstudio.middleware.soap.taapi;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.integration.adapter.TaapiMappingUtils;
import com.zynap.talentstudio.middleware.soap.taapi.client.GenderType;
import com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType;
import com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType;
import com.zynap.talentstudio.middleware.soap.taapi.client.TAAPIBindingStub;
import com.zynap.talentstudio.middleware.soap.taapi.client.holders.RespondentInfoTypeHolder;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by bronwen.
 * Date: 25/02/12
 * Time: 10:36
 */
public class TaapiClient implements Serializable {

    public TaapiClient() {
    }

    public String createRespondent(Subject s, Long populationId) throws TalentStudioException {

        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            binding.setTimeout(60000);
            // authentication
            addAuth(binding);
            RespondentInfoType value = new RespondentInfoType();
            setValues(s, value, populationId);
            binding.createRespondent(new RespondentInfoTypeHolder(value));
            List<RespondentInfoType> respondents = findAllRespondents(s.getId() + "", s.getEmail());
            if(respondents.size() > 0) {
                return respondents.get(0).getRespondentID();
            } else {
                throw new TalentStudioException("Unable to get the respondent id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TalentStudioException(e.getMessage(), e);
        }
    }

    public void updateRespondent(String respondentId, Subject s, Long populationId) throws TalentStudioException {
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            binding.setTimeout(60000);
            // authentication
            addAuth(binding);
            RespondentInfoType value = new RespondentInfoType();
            setValues(s, value, populationId);
            value.setRespondentID(respondentId);
            binding.updateRespondentInfo(respondentId, value);

        } catch (Exception e) {
            throw new RespondentNotFoundException("Respodent Not found", e);
        }
    }

    public void updateRespondent(RespondentInfoType respondent, Long nodeId) throws TalentStudioException {
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            binding.setTimeout(60000);
            // authentication
            addAuth(binding);
            if (respondent.getCallbackURL() == null) {
                respondent.setCallbackURL(callbackUrl + "?command.node.id=" + nodeId);
            }
            binding.updateRespondentInfo(respondent.getRespondentID(), respondent);
        } catch (Exception e) {
            throw new TalentStudioException(e);
        }
    }

    public List<RespondentContentType> getContentInfo(String[] respondentIds, String contentKey, String format) throws TalentStudioException {
        List<RespondentContentType> result = new ArrayList<RespondentContentType>();
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            // Time out after a minute
            binding.setTimeout(60000);
            addAuth(binding);

            RespondentContentType[] content = binding.getContent(respondentIds, contentKey, format);
            result.addAll(Arrays.asList(content));
        } catch (Exception jre) {
            jre.printStackTrace();
            throw new TalentStudioException(jre.getMessage(), jre);
        }
        return result;
    }

    public RespondentInfoType findRespondent(String respondentId) throws TalentStudioException {
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            // Time out after a minute
            binding.setTimeout(60000);
            addAuth(binding);
            RespondentInfoType[] value = binding.getRespondentInfo(new String[]{respondentId}, null);
            if (value != null && value.length > 0) {
                return value[0];
            }
            return null;
        } catch (Exception jre) {
            jre.printStackTrace();
            throw new TalentStudioException(jre.getMessage(), jre);
        }
    }

    public List<RespondentInfoType> findAllRespondents(Long populationId) throws TalentStudioException {
        List<RespondentInfoType> result = new ArrayList<RespondentInfoType>();
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            // Time out after a minute
            binding.setTimeout(60000);
            addAuth(binding);
            RespondentInfoType type = new RespondentInfoType();
            type.setLabels(new String[] {client, POPULATION_PREFIX + populationId});
            RespondentInfoType[] value = binding.findRespondents(type);
            result.addAll(Arrays.asList(value));
        } catch (Exception jre) {
            jre.printStackTrace();
            throw new TalentStudioException(jre.getMessage(), jre);
        }
        return result;
    }

    public List<RespondentInfoType> findAllRespondents(String externalId, String email) throws TalentStudioException {
        List<RespondentInfoType> result = new ArrayList<RespondentInfoType>();
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            // Time out after a minute
            binding.setTimeout(60000);
            addAuth(binding);
            RespondentInfoType type = new RespondentInfoType();
            type.setExtID(externalId);
            type.setEMail(email);
            RespondentInfoType[] value = binding.findRespondents(type);
            result.addAll(Arrays.asList(value));
        } catch (Exception jre) {
            jre.printStackTrace();
            throw new TalentStudioException(jre.getMessage(), jre);
        }
        return result;
    }

    public List<RespondentInfoType> findAllRespondents() throws TalentStudioException {
        List<RespondentInfoType> result = new ArrayList<RespondentInfoType>();
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            // Time out after a minute
            binding.setTimeout(60000);
            addAuth(binding);
            RespondentInfoType type = new RespondentInfoType();
            type.setLabels(new String[] {client});
            RespondentInfoType[] value = binding.findRespondents(type);
            result.addAll(Arrays.asList(value));
        } catch (Exception jre) {
            jre.printStackTrace();
            throw new TalentStudioException(jre.getMessage(), jre);
        }
        return result;
    }

    public RespondentInfoType[] findResponentInfo(String[] respondentIds) throws TalentStudioException {
        RespondentInfoType[] respondentInfo = null;
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            // Time out after a minute
            binding.setTimeout(60000);
            addAuth(binding);
            respondentInfo = binding.getRespondentInfo(respondentIds, fieldList);
        } catch (Exception e) {
            e.printStackTrace();
            // ignored as we don't care if this information is null
        }
        return respondentInfo;
    }

    public void clearInfo(String[] respondentIds) throws TalentStudioException {
        try {
            TAAPIBindingStub binding = (TAAPIBindingStub) new com.zynap.talentstudio.middleware.soap.taapi.client.TAAPILocator().getTAAPIPort();
            // Time out after a minute
            binding.setTimeout(60000);
            addAuth(binding);
            binding.removeRespondentLabels(respondentIds, new String[] {client});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValues(Subject s, RespondentInfoType value, Long populationId) {

        value.setCallbackURL(callbackUrl + "?command.node.id=" + s.getId());
        value.setFirstName(s.getFirstName());
        value.setEMail(s.getEmail());
        value.setLastName(s.getSecondName());
        Set<NodeExtendedAttribute> attributes = s.getExtendedAttributes();
        assignGender(value, attributes);
        value.setExtID("" + s.getId());
        if (populationId != null) {
            value.setLabels(new String[]{s.getCurrentJobInfo(), client, POPULATION_PREFIX + populationId});
        } else {
            value.setLabels(new String[]{s.getCurrentJobInfo(), client});
        }
    }

    private void assignGender(RespondentInfoType value, Set<NodeExtendedAttribute> attributes) {
        GenderType type = GenderType.NA;
        NodeExtendedAttribute attr = TaapiMappingUtils.getValue("gender", attributes);
        if (attr != null) {
            AttributeValue val = AttributeValue.create(attr);
            LookupValue value1 = val.getSelectionLookupValue();
            if ("MALE".equals(value1.getValueId())) {
                type = GenderType.M;
            } else {
                type = GenderType.F;
            }
        }
        value.setGender(type);
    }

    private void addAuth(TAAPIBindingStub binding) throws SOAPException {
        SOAPHeaderElement authHeader = new SOAPHeaderElement("", "AuthHeader");
        SOAPElement node = authHeader.addChildElement("Username");
        //"bronwen.cassidy@talentscope.com"
        node.addTextNode(username);

        SOAPElement passwordElem = authHeader.addChildElement("Password");
        //"4321TS1234"
        passwordElem.addTextNode(password);

        binding.setHeader(authHeader);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    private String username;
    private String password;
    private String client;
    private String callbackUrl;

    public static final String POPULATION_PREFIX = "populationId_";

    /* licensed scores */
    public static final String[] fieldList = {"ACT_C","ACT_O","ACT_R","ACT_E","MATCH_AMB","MATCH_CORE","MATCH_TOTAL","MOD_C","MOD_O","MOD_R","MOD_E",
            "RANK_ALT","RANK_AUT", "RANK_CRE", "RANK_ECO","RANK_IND","RANK_POL","RANK_THE",
            "SCORE_ALT","SCORE_AUT","SCORE_CRE","SCORE_ECO","SCORE_IND","SCORE_POL","SCORE_THE"};
    public static final String[] core_scores = {"ACT_C","ACT_O","ACT_R","ACT_E","MOD_C","MOD_O","MOD_R","MOD_E"};
    public static final String[] match_to_benchmark = {"MATCH_AMB","MATCH_CORE","MATCH_TOTAL"};
    public static final String[] ambitions_scores = {"RANK_ALT","RANK_AUT", "RANK_CRE", "RANK_ECO","RANK_IND","RANK_POL","RANK_THE",
            "SCORE_ALT","SCORE_AUT","SCORE_CRE","SCORE_ECO","SCORE_IND","SCORE_POL","SCORE_THE"};

}
