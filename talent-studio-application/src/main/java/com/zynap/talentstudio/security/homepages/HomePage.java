/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security.homepages;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.common.groups.Group;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author acalderwood
 * @version 0.1
 * @since 25-Jan-2007 16:30:36
 */

public class HomePage extends ZynapDomainObject {

    public HomePage() {
    }

    public HomePage(String arenaId, byte[] data, String url, String label) {
        this.arenaId = arenaId;
        this.data = data;
        this.url = url;
        this.label = label;
    }

    public HomePage(String arenaId, byte[] data, String url, String label, boolean isInternalUrl) {
        this(arenaId, data, url, label);
        this.internalUrl = isInternalUrl;
    }

    public HomePage(String arenaId, String label) {
        this.arenaId = arenaId;
        this.label = label;
    }

    public String getArenaId() {
        return arenaId;
    }

    public void setArenaId(String arenaId) {
        this.arenaId = arenaId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isInternalUrl() {
        return internalUrl;
    }

    public void setInternalUrl(boolean internalUrl) {
        this.internalUrl = internalUrl;
    }

    public boolean isHasData() {
        return data != null && data.length > 0;
    }

    public boolean isHasUrl() {
        return StringUtils.hasText(url);
    }

    public String getContent() {
        return isHasData() ? new String(data) : "";
    }

    public boolean isHasLocalUrl() {
        return (isInternalUrl() && url != null);
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public boolean isVelocityTemplate() {
        return fileExtension != null && fileExtension.endsWith("vm");
    }

	public void setTabView(String tabView) {
		this.tabView = tabView;
	}

	public String getTabView() {
		return tabView;
	}

	private String arenaId;
    private byte[] data;
    private String url;
    private Group group;
    private boolean internalUrl;
    private String fileExtension;
	private String tabView;
}
