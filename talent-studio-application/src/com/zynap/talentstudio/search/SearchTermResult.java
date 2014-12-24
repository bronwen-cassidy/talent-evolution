package com.zynap.talentstudio.search;

import com.zynap.domain.ZynapDomainObject;

/**
 *
 */
public class SearchTermResult extends ZynapDomainObject {

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPermitId() {
        return permitId;
    }

    public void setPermitId(Long permitId) {
        this.permitId = permitId;
    }

    public boolean isManagerRead() {
        return managerRead;
    }

    public void setManagerRead(boolean managerRead) {
        this.managerRead = managerRead;
    }

    public boolean isIndividualRead() {
        return individualRead;
    }

    public void setIndividualRead(boolean individualRead) {
        this.individualRead = individualRead;
    }

    @Override
    public String toString() {
        return "SearchTermResult{" +
                "label='" + label + '\'' +
                ", nodeId=" + nodeId +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }

    private String description;
    private String linkUrl;
    private Long nodeId;
    private Long userId;
    private String name;
    private Long permitId;
    private boolean managerRead;
    private boolean individualRead;

}
