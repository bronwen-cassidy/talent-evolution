/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * ! NOTE: any changes to this class must also be copied to AdminWebapp in Kneobase !
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SearchResult implements ISearchResult {

    public void addSingleResult(ISearchResult singleHit) {
        getHits().add(singleHit);
    }

    public void setReference(String reference) {
        this.reference = trim(reference);
    }

    public String getReference() {
        return reference;
    }

    public List<ISearchResult> getHits() {
        if(hits == null) {
            hits = new ArrayList<ISearchResult>();
        }
        return hits;
    }

    public int getNumHits() {
        return getHits().size();
    }

    public void setDocumentId(String documentId) {
        this.documentId = trim(documentId);
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setResultWeight(String weight) {
        resultWeight = trim(weight);
    }

    public String getResultWeight() {
        return resultWeight;
    }

    public void setArtefactTitle(String artefactTitle) {
        this.artefactTitle = trim(artefactTitle);
    }

    public String getArtefactTitle() {
        return artefactTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = trim(contentTitle);
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setSummary(String summary) {
        this.summary = trim(summary);
    }

    public String getSummary() {
        return summary;
    }

    public void setArtefactId(String artefactId) {
        if(artefactId != null) {
            this.artefactId = new Long(trim(artefactId));
        }
    }

    public Long getArtefactId() {
        return artefactId;
    }

    public void setContent(String content) {
        this.content = trim(content);
    }

    public String getContent() {
        return content;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    private String trim(String weight) {
        return StringUtils.deleteAny(weight, "\n\t");
    }

    public void setCreatedBy(String createdById) {
        this.createdById = new Long(createdById);
    }

    public Long getCreatedById() {
        return createdById;
    }

    public String getScope() {
        return scope;
    }

    public boolean createdBy(Long userId) {
        return userId.equals(createdById);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getArtefactType() {
        return artefactType;
    }

    public void setArtefactType(String artefactType) {
        this.artefactType = artefactType;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSize() {
        return fileSize;
    }

    private Long itemId;
    private String artefactType;
    private String reference;
    private List<ISearchResult> hits;
    private String documentId;
    private String resultWeight;
    private String artefactTitle;
    private String contentTitle;
    private String summary;
    private Long artefactId;
    private String content;
    private Long createdById;
    private String scope;

    private static final long serialVersionUID = -8444544289274161181L;
    private String contentTypeId;
    private String fileSize;
}
