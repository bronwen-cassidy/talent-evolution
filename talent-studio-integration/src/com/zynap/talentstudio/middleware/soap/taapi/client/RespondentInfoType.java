/**
 * RespondentInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zynap.talentstudio.middleware.soap.taapi.client;

public class RespondentInfoType  implements java.io.Serializable {
    private java.lang.String respondentID;

    private java.lang.String extID;

    private java.lang.String callbackURL;

    private java.lang.String firstName;

    private java.lang.String lastName;

    private java.lang.String EMail;

    private com.zynap.talentstudio.middleware.soap.taapi.client.GenderType gender;

    private java.lang.String status;

    private Double MATCH_TOTAL;

    private Double MATCH_CORE;

    private Double MATCH_AMB;

    private java.lang.String[] labels;

    private Double ACT_C;

    private Double ACT_O;

    private Double ACT_R;

    private Double ACT_E;

    private Double MOD_C;

    private Double MOD_O;

    private Double MOD_R;

    private Double MOD_E;

    private Double SCORE_ECO;

    private Double SCORE_ALT;

    private Double SCORE_THE;

    private Double SCORE_AUT;

    private Double SCORE_POL;

    private Double SCORE_IND;

    private Double SCORE_CRE;

    private Double RANK_ECO;

    private Double RANK_ALT;

    private Double RANK_THE;

    private Double RANK_AUT;

    private Double RANK_POL;

    private Double RANK_IND;

    private Double RANK_CRE;

    public RespondentInfoType() {
    }

    public RespondentInfoType(
           java.lang.String respondentID,
           java.lang.String extID,
           java.lang.String callbackURL,
           java.lang.String firstName,
           java.lang.String lastName,
           java.lang.String EMail,
           com.zynap.talentstudio.middleware.soap.taapi.client.GenderType gender,
           java.lang.String status,
           Double MATCH_TOTAL,
           Double MATCH_CORE,
           Double MATCH_AMB,
           java.lang.String[] labels,
           Double ACT_C,
           Double ACT_O,
           Double ACT_R,
           Double ACT_E,
           Double MOD_C,
           Double MOD_O,
           Double MOD_R,
           Double MOD_E,
           Double SCORE_ECO,
           Double SCORE_ALT,
           Double SCORE_THE,
           Double SCORE_AUT,
           Double SCORE_POL,
           Double SCORE_IND,
           Double SCORE_CRE,
           Double RANK_ECO,
           Double RANK_ALT,
           Double RANK_THE,
           Double RANK_AUT,
           Double RANK_POL,
           Double RANK_IND,
           Double RANK_CRE) {
           this.respondentID = respondentID;
           this.extID = extID;
           this.callbackURL = callbackURL;
           this.firstName = firstName;
           this.lastName = lastName;
           this.EMail = EMail;
           this.gender = gender;
           this.status = status;
           this.MATCH_TOTAL = MATCH_TOTAL;
           this.MATCH_CORE = MATCH_CORE;
           this.MATCH_AMB = MATCH_AMB;
           this.labels = labels;
           this.ACT_C = ACT_C;
           this.ACT_O = ACT_O;
           this.ACT_R = ACT_R;
           this.ACT_E = ACT_E;
           this.MOD_C = MOD_C;
           this.MOD_O = MOD_O;
           this.MOD_R = MOD_R;
           this.MOD_E = MOD_E;
           this.SCORE_ECO = SCORE_ECO;
           this.SCORE_ALT = SCORE_ALT;
           this.SCORE_THE = SCORE_THE;
           this.SCORE_AUT = SCORE_AUT;
           this.SCORE_POL = SCORE_POL;
           this.SCORE_IND = SCORE_IND;
           this.SCORE_CRE = SCORE_CRE;
           this.RANK_ECO = RANK_ECO;
           this.RANK_ALT = RANK_ALT;
           this.RANK_THE = RANK_THE;
           this.RANK_AUT = RANK_AUT;
           this.RANK_POL = RANK_POL;
           this.RANK_IND = RANK_IND;
           this.RANK_CRE = RANK_CRE;
    }


    /**
     * Gets the respondentID value for this RespondentInfoType.
     * 
     * @return respondentID
     */
    public java.lang.String getRespondentID() {
        return respondentID;
    }


    /**
     * Sets the respondentID value for this RespondentInfoType.
     * 
     * @param respondentID
     */
    public void setRespondentID(java.lang.String respondentID) {
        this.respondentID = respondentID;
    }


    /**
     * Gets the extID value for this RespondentInfoType.
     * 
     * @return extID
     */
    public java.lang.String getExtID() {
        return extID;
    }


    /**
     * Sets the extID value for this RespondentInfoType.
     * 
     * @param extID
     *
     */
    public void setExtID(java.lang.String extID) {
        this.extID = extID;
    }


    /**
     * Gets the callbackURL value for this RespondentInfoType.
     * 
     * @return callbackURL
     */
    public java.lang.String getCallbackURL() {
        return callbackURL;
    }


    /**
     * Sets the callbackURL value for this RespondentInfoType.
     * 
     * @param callbackURL
     */
    public void setCallbackURL(java.lang.String callbackURL) {
        this.callbackURL = callbackURL;
    }


    /**
     * Gets the firstName value for this RespondentInfoType.
     * 
     * @return firstName
     */
    public java.lang.String getFirstName() {
        return firstName;
    }


    /**
     * Sets the firstName value for this RespondentInfoType.
     * 
     * @param firstName
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }


    /**
     * Gets the lastName value for this RespondentInfoType.
     * 
     * @return lastName
     */
    public java.lang.String getLastName() {
        return lastName;
    }


    /**
     * Sets the lastName value for this RespondentInfoType.
     * 
     * @param lastName
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }


    /**
     * Gets the EMail value for this RespondentInfoType.
     * 
     * @return EMail
     */
    public java.lang.String getEMail() {
        return EMail;
    }


    /**
     * Sets the EMail value for this RespondentInfoType.
     * 
     * @param EMail
     */
    public void setEMail(java.lang.String EMail) {
        this.EMail = EMail;
    }


    /**
     * Gets the gender value for this RespondentInfoType.
     * 
     * @return gender
     */
    public com.zynap.talentstudio.middleware.soap.taapi.client.GenderType getGender() {
        return gender;
    }


    /**
     * Sets the gender value for this RespondentInfoType.
     * 
     * @param gender
     */
    public void setGender(com.zynap.talentstudio.middleware.soap.taapi.client.GenderType gender) {
        this.gender = gender;
    }


    /**
     * Gets the status value for this RespondentInfoType.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this RespondentInfoType.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the MATCH_TOTAL value for this RespondentInfoType.
     * 
     * @return MATCH_TOTAL
     */
    public Double getMATCH_TOTAL() {
        return MATCH_TOTAL;
    }


    /**
     * Sets the MATCH_TOTAL value for this RespondentInfoType.
     * 
     * @param MATCH_TOTAL
     */
    public void setMATCH_TOTAL(Double MATCH_TOTAL) {
        this.MATCH_TOTAL = MATCH_TOTAL;
    }


    /**
     * Gets the MATCH_CORE value for this RespondentInfoType.
     * 
     * @return MATCH_CORE
     */
    public Double getMATCH_CORE() {
        return MATCH_CORE;
    }


    /**
     * Sets the MATCH_CORE value for this RespondentInfoType.
     * 
     * @param MATCH_CORE
     */
    public void setMATCH_CORE(Double MATCH_CORE) {
        this.MATCH_CORE = MATCH_CORE;
    }


    /**
     * Gets the MATCH_AMB value for this RespondentInfoType.
     * 
     * @return MATCH_AMB
     */
    public Double getMATCH_AMB() {
        return MATCH_AMB;
    }


    /**
     * Sets the MATCH_AMB value for this RespondentInfoType.
     * 
     * @param MATCH_AMB
     */
    public void setMATCH_AMB(Double MATCH_AMB) {
        this.MATCH_AMB = MATCH_AMB;
    }


    /**
     * Gets the labels value for this RespondentInfoType.
     * 
     * @return labels
     */
    public java.lang.String[] getLabels() {
        return labels;
    }


    /**
     * Sets the labels value for this RespondentInfoType.
     * 
     * @param labels
     */
    public void setLabels(java.lang.String[] labels) {
        this.labels = labels;
    }


    /**
     * Gets the ACT_C value for this RespondentInfoType.
     * 
     * @return ACT_C
     */
    public Double getACT_C() {
        return ACT_C;
    }


    /**
     * Sets the ACT_C value for this RespondentInfoType.
     * 
     * @param ACT_C
     */
    public void setACT_C(Double ACT_C) {
        this.ACT_C = ACT_C;
    }


    /**
     * Gets the ACT_O value for this RespondentInfoType.
     * 
     * @return ACT_O
     */
    public Double getACT_O() {
        return ACT_O;
    }


    /**
     * Sets the ACT_O value for this RespondentInfoType.
     * 
     * @param ACT_O
     */
    public void setACT_O(Double ACT_O) {
        this.ACT_O = ACT_O;
    }


    /**
     * Gets the ACT_R value for this RespondentInfoType.
     * 
     * @return ACT_R
     */
    public Double getACT_R() {
        return ACT_R;
    }


    /**
     * Sets the ACT_R value for this RespondentInfoType.
     * 
     * @param ACT_R
     */
    public void setACT_R(Double ACT_R) {
        this.ACT_R = ACT_R;
    }


    /**
     * Gets the ACT_E value for this RespondentInfoType.
     * 
     * @return ACT_E
     */
    public Double getACT_E() {
        return ACT_E;
    }


    /**
     * Sets the ACT_E value for this RespondentInfoType.
     * 
     * @param ACT_E
     */
    public void setACT_E(Double ACT_E) {
        this.ACT_E = ACT_E;
    }


    /**
     * Gets the MOD_C value for this RespondentInfoType.
     * 
     * @return MOD_C
     */
    public Double getMOD_C() {
        return MOD_C;
    }


    /**
     * Sets the MOD_C value for this RespondentInfoType.
     * 
     * @param MOD_C
     */
    public void setMOD_C(Double MOD_C) {
        this.MOD_C = MOD_C;
    }


    /**
     * Gets the MOD_O value for this RespondentInfoType.
     * 
     * @return MOD_O
     */
    public Double getMOD_O() {
        return MOD_O;
    }


    /**
     * Sets the MOD_O value for this RespondentInfoType.
     * 
     * @param MOD_O
     */
    public void setMOD_O(Double MOD_O) {
        this.MOD_O = MOD_O;
    }


    /**
     * Gets the MOD_R value for this RespondentInfoType.
     * 
     * @return MOD_R
     */
    public Double getMOD_R() {
        return MOD_R;
    }


    /**
     * Sets the MOD_R value for this RespondentInfoType.
     * 
     * @param MOD_R
     */
    public void setMOD_R(Double MOD_R) {
        this.MOD_R = MOD_R;
    }


    /**
     * Gets the MOD_E value for this RespondentInfoType.
     * 
     * @return MOD_E
     */
    public Double getMOD_E() {
        return MOD_E;
    }


    /**
     * Sets the MOD_E value for this RespondentInfoType.
     * 
     * @param MOD_E
     */
    public void setMOD_E(Double MOD_E) {
        this.MOD_E = MOD_E;
    }


    /**
     * Gets the SCORE_ECO value for this RespondentInfoType.
     * 
     * @return SCORE_ECO
     */
    public Double getSCORE_ECO() {
        return SCORE_ECO;
    }


    /**
     * Sets the SCORE_ECO value for this RespondentInfoType.
     * 
     * @param SCORE_ECO
     */
    public void setSCORE_ECO(Double SCORE_ECO) {
        this.SCORE_ECO = SCORE_ECO;
    }


    /**
     * Gets the SCORE_ALT value for this RespondentInfoType.
     * 
     * @return SCORE_ALT
     */
    public Double getSCORE_ALT() {
        return SCORE_ALT;
    }


    /**
     * Sets the SCORE_ALT value for this RespondentInfoType.
     * 
     * @param SCORE_ALT
     */
    public void setSCORE_ALT(Double SCORE_ALT) {
        this.SCORE_ALT = SCORE_ALT;
    }


    /**
     * Gets the SCORE_THE value for this RespondentInfoType.
     * 
     * @return SCORE_THE
     */
    public Double getSCORE_THE() {
        return SCORE_THE;
    }


    /**
     * Sets the SCORE_THE value for this RespondentInfoType.
     * 
     * @param SCORE_THE
     */
    public void setSCORE_THE(Double SCORE_THE) {
        this.SCORE_THE = SCORE_THE;
    }


    /**
     * Gets the SCORE_AUT value for this RespondentInfoType.
     * 
     * @return SCORE_AUT
     */
    public Double getSCORE_AUT() {
        return SCORE_AUT;
    }


    /**
     * Sets the SCORE_AUT value for this RespondentInfoType.
     * 
     * @param SCORE_AUT
     */
    public void setSCORE_AUT(Double SCORE_AUT) {
        this.SCORE_AUT = SCORE_AUT;
    }


    /**
     * Gets the SCORE_POL value for this RespondentInfoType.
     * 
     * @return SCORE_POL
     */
    public Double getSCORE_POL() {
        return SCORE_POL;
    }


    /**
     * Sets the SCORE_POL value for this RespondentInfoType.
     * 
     * @param SCORE_POL
     */
    public void setSCORE_POL(Double SCORE_POL) {
        this.SCORE_POL = SCORE_POL;
    }


    /**
     * Gets the SCORE_IND value for this RespondentInfoType.
     * 
     * @return SCORE_IND
     */
    public Double getSCORE_IND() {
        return SCORE_IND;
    }


    /**
     * Sets the SCORE_IND value for this RespondentInfoType.
     * 
     * @param SCORE_IND
     */
    public void setSCORE_IND(Double SCORE_IND) {
        this.SCORE_IND = SCORE_IND;
    }


    /**
     * Gets the SCORE_CRE value for this RespondentInfoType.
     * 
     * @return SCORE_CRE
     */
    public Double getSCORE_CRE() {
        return SCORE_CRE;
    }


    /**
     * Sets the SCORE_CRE value for this RespondentInfoType.
     * 
     * @param SCORE_CRE
     */
    public void setSCORE_CRE(Double SCORE_CRE) {
        this.SCORE_CRE = SCORE_CRE;
    }


    /**
     * Gets the RANK_ECO value for this RespondentInfoType.
     * 
     * @return RANK_ECO
     */
    public Double getRANK_ECO() {
        return RANK_ECO;
    }


    /**
     * Sets the RANK_ECO value for this RespondentInfoType.
     * 
     * @param RANK_ECO
     */
    public void setRANK_ECO(Double RANK_ECO) {
        this.RANK_ECO = RANK_ECO;
    }


    /**
     * Gets the RANK_ALT value for this RespondentInfoType.
     * 
     * @return RANK_ALT
     */
    public Double getRANK_ALT() {
        return RANK_ALT;
    }


    /**
     * Sets the RANK_ALT value for this RespondentInfoType.
     * 
     * @param RANK_ALT
     */
    public void setRANK_ALT(Double RANK_ALT) {
        this.RANK_ALT = RANK_ALT;
    }


    /**
     * Gets the RANK_THE value for this RespondentInfoType.
     * 
     * @return RANK_THE
     */
    public Double getRANK_THE() {
        return RANK_THE;
    }


    /**
     * Sets the RANK_THE value for this RespondentInfoType.
     * 
     * @param RANK_THE
     */
    public void setRANK_THE(Double RANK_THE) {
        this.RANK_THE = RANK_THE;
    }


    /**
     * Gets the RANK_AUT value for this RespondentInfoType.
     * 
     * @return RANK_AUT
     */
    public Double getRANK_AUT() {
        return RANK_AUT;
    }


    /**
     * Sets the RANK_AUT value for this RespondentInfoType.
     * 
     * @param RANK_AUT
     */
    public void setRANK_AUT(Double RANK_AUT) {
        this.RANK_AUT = RANK_AUT;
    }


    /**
     * Gets the RANK_POL value for this RespondentInfoType.
     * 
     * @return RANK_POL
     */
    public Double getRANK_POL() {
        return RANK_POL;
    }


    /**
     * Sets the RANK_POL value for this RespondentInfoType.
     * 
     * @param RANK_POL
     */
    public void setRANK_POL(Double RANK_POL) {
        this.RANK_POL = RANK_POL;
    }


    /**
     * Gets the RANK_IND value for this RespondentInfoType.
     * 
     * @return RANK_IND
     */
    public Double getRANK_IND() {
        return RANK_IND;
    }


    /**
     * Sets the RANK_IND value for this RespondentInfoType.
     * 
     * @param RANK_IND
     */
    public void setRANK_IND(Double RANK_IND) {
        this.RANK_IND = RANK_IND;
    }


    /**
     * Gets the RANK_CRE value for this RespondentInfoType.
     * 
     * @return RANK_CRE
     */
    public Double getRANK_CRE() {
        return RANK_CRE;
    }


    /**
     * Sets the RANK_CRE value for this RespondentInfoType.
     * 
     * @param RANK_CRE
     */
    public void setRANK_CRE(Double RANK_CRE) {
        this.RANK_CRE = RANK_CRE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespondentInfoType)) return false;
        RespondentInfoType other = (RespondentInfoType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.respondentID==null && other.getRespondentID()==null) || 
             (this.respondentID!=null &&
              this.respondentID.equals(other.getRespondentID()))) &&
            ((this.extID==null && other.getExtID()==null) || 
             (this.extID!=null &&
              this.extID.equals(other.getExtID()))) &&
            ((this.callbackURL==null && other.getCallbackURL()==null) || 
             (this.callbackURL!=null &&
              this.callbackURL.equals(other.getCallbackURL()))) &&
            ((this.firstName==null && other.getFirstName()==null) || 
             (this.firstName!=null &&
              this.firstName.equals(other.getFirstName()))) &&
            ((this.lastName==null && other.getLastName()==null) || 
             (this.lastName!=null &&
              this.lastName.equals(other.getLastName()))) &&
            ((this.EMail==null && other.getEMail()==null) || 
             (this.EMail!=null &&
              this.EMail.equals(other.getEMail()))) &&
            ((this.gender==null && other.getGender()==null) || 
             (this.gender!=null &&
              this.gender.equals(other.getGender()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            this.MATCH_TOTAL == other.getMATCH_TOTAL() &&
            this.MATCH_CORE == other.getMATCH_CORE() &&
            this.MATCH_AMB == other.getMATCH_AMB() &&
            ((this.labels==null && other.getLabels()==null) || 
             (this.labels!=null &&
              java.util.Arrays.equals(this.labels, other.getLabels()))) &&
            this.ACT_C == other.getACT_C() &&
            this.ACT_O == other.getACT_O() &&
            this.ACT_R == other.getACT_R() &&
            this.ACT_E == other.getACT_E() &&
            this.MOD_C == other.getMOD_C() &&
            this.MOD_O == other.getMOD_O() &&
            this.MOD_R == other.getMOD_R() &&
            this.MOD_E == other.getMOD_E() &&
            this.SCORE_ECO == other.getSCORE_ECO() &&
            this.SCORE_ALT == other.getSCORE_ALT() &&
            this.SCORE_THE == other.getSCORE_THE() &&
            this.SCORE_AUT == other.getSCORE_AUT() &&
            this.SCORE_POL == other.getSCORE_POL() &&
            this.SCORE_IND == other.getSCORE_IND() &&
            this.SCORE_CRE == other.getSCORE_CRE() &&
            this.RANK_ECO == other.getRANK_ECO() &&
            this.RANK_ALT == other.getRANK_ALT() &&
            this.RANK_THE == other.getRANK_THE() &&
            this.RANK_AUT == other.getRANK_AUT() &&
            this.RANK_POL == other.getRANK_POL() &&
            this.RANK_IND == other.getRANK_IND() &&
            this.RANK_CRE == other.getRANK_CRE();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRespondentID() != null) {
            _hashCode += getRespondentID().hashCode();
        }
        if (getExtID() != null) {
            _hashCode += getExtID().hashCode();
        }
        if (getCallbackURL() != null) {
            _hashCode += getCallbackURL().hashCode();
        }
        if (getFirstName() != null) {
            _hashCode += getFirstName().hashCode();
        }
        if (getLastName() != null) {
            _hashCode += getLastName().hashCode();
        }
        if (getEMail() != null) {
            _hashCode += getEMail().hashCode();
        }
        if (getGender() != null) {
            _hashCode += getGender().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        _hashCode += getMATCH_TOTAL() != null ? getMATCH_TOTAL().hashCode() : 0;
        _hashCode += getMATCH_CORE() != null ? getMATCH_CORE().hashCode() : 0;
        _hashCode += getMATCH_AMB() != null ? getMATCH_AMB().hashCode() : 0;
        if (getLabels() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLabels());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLabels(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getACT_C() != null ? getACT_C().hashCode() : 0;
        _hashCode += getACT_O() != null ? getACT_O().hashCode() : 0;
        _hashCode += getACT_R() != null ? getACT_R().hashCode() : 0;
        _hashCode += getACT_E() != null ? getACT_E().hashCode() : 0;
        _hashCode += getMOD_C() != null ? getMOD_C().hashCode() : 0;
        _hashCode += getMOD_O() != null ? getMOD_O().hashCode() : 0;
        _hashCode += getMOD_R() != null ? getMOD_R().hashCode() : 0;
        _hashCode += getMOD_E() != null ? getMOD_E().hashCode() : 0;
        _hashCode += getSCORE_ECO() != null ? getSCORE_ECO().hashCode() : 0;
        _hashCode += getSCORE_ALT() != null ? getSCORE_ALT().hashCode() : 0;
        _hashCode += getSCORE_THE() != null ? getSCORE_THE().hashCode() : 0;
        _hashCode += getSCORE_AUT() != null ? getSCORE_AUT().hashCode() : 0;
        _hashCode += getSCORE_POL() != null ? getSCORE_POL().hashCode() : 0;
        _hashCode += getSCORE_IND() != null ? getSCORE_IND().hashCode() : 0;
        _hashCode += getSCORE_CRE() != null ? getSCORE_CRE().hashCode() : 0;
        _hashCode += getRANK_ECO() != null ? getRANK_ECO().hashCode() : 0;
        _hashCode += getRANK_ALT() != null ? getRANK_ALT().hashCode() : 0;
        _hashCode += getRANK_THE() != null ? getRANK_THE().hashCode() : 0;
        _hashCode += getRANK_AUT() != null ? getRANK_AUT().hashCode() : 0;
        _hashCode += getRANK_POL() != null ? getRANK_POL().hashCode() : 0;
        _hashCode += getRANK_IND() != null ? getRANK_IND().hashCode() : 0;
        _hashCode += getRANK_CRE() != null ? getRANK_CRE().hashCode() : 0;
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespondentInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "RespondentInfoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("respondentID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RespondentID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ExtID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callbackURL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallbackURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FirstName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LastName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EMail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EMail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gender");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Gender"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://api.talentanalytics.com/2012-01-01", "GenderType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MATCH_TOTAL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MATCH_TOTAL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MATCH_CORE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MATCH_CORE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MATCH_AMB");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MATCH_AMB"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("labels");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Labels"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACT_C");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ACT_C"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACT_O");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ACT_O"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACT_R");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ACT_R"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACT_E");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ACT_E"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MOD_C");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MOD_C"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MOD_O");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MOD_O"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MOD_R");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MOD_R"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MOD_E");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MOD_E"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCORE_ECO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SCORE_ECO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCORE_ALT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SCORE_ALT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCORE_THE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SCORE_THE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCORE_AUT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SCORE_AUT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCORE_POL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SCORE_POL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCORE_IND");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SCORE_IND"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCORE_CRE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SCORE_CRE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RANK_ECO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RANK_ECO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RANK_ALT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RANK_ALT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RANK_THE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RANK_THE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RANK_AUT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RANK_AUT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RANK_POL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RANK_POL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RANK_IND");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RANK_IND"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RANK_CRE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RANK_CRE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "Double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
