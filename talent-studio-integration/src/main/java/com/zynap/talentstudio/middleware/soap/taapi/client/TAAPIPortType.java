/**
 * TAAPIPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zynap.talentstudio.middleware.soap.taapi.client;

public interface TAAPIPortType extends java.rmi.Remote {

    /**
     * Creates an RDC request for the specified user, email address
     * is required as part of the given respondent info input. All other
     * supplied info is added at creation time. Returns the respondent info.
     */
    public void createRespondent(com.zynap.talentstudio.middleware.soap.taapi.client.holders.RespondentInfoTypeHolder respondentInfo) throws java.rmi.RemoteException;

    /**
     * Returns the respondent info for the given id. Restricts output
     * only to supplied fields. If no fields are supplied, returns the entire
     * info object.
     */
    public com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[] getRespondentInfo(java.lang.String[] respondentIDList, java.lang.String[] fieldList) throws java.rmi.RemoteException;

    /**
     * Updates a single respondent info for the given id. Only overwrites
     * fields that are present in the passed in respondent info package.
     */
    public boolean updateRespondentInfo(java.lang.String respondentID, com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType respondentInfo) throws java.rmi.RemoteException;

    /**
     * Applies the supplied list of labels to each respondent in the
     * given list.
     */
    public boolean setRespondentLabels(java.lang.String[] respondentIDList, java.lang.String[] respondentLabelList) throws java.rmi.RemoteException;

    /**
     * Removes the supplied list of labels from each respondent in
     * the given list.
     */
    public boolean removeRespondentLabels(java.lang.String[] respondentIDList, java.lang.String[] respondentLabelList) throws java.rmi.RemoteException;

    /**
     * Returns a list of labels for for the given id as set by the
     * user.
     */
    public java.lang.String[] getRespondentLabels(java.lang.String respondentID) throws java.rmi.RemoteException;

    /**
     * Returns the content for a list of respondents given a list
     * and content key. Takes an optional format descriptor (html by default)
     */
    public com.zynap.talentstudio.middleware.soap.taapi.client.RespondentContentType[] getContent(java.lang.String[] respondentIDList, java.lang.String contentKey, java.lang.String format) throws java.rmi.RemoteException;

    /**
     * Does a search for respondents based on supplied info.
     */
    public com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType[] findRespondents(com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType respondentInfo) throws java.rmi.RemoteException;
}
