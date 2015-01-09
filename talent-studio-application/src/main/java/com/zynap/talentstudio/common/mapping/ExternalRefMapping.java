package com.zynap.talentstudio.common.mapping;


import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ExternalRefMapping extends ZynapDomainObject implements Serializable {

    private boolean generated;

    /** persistent field */
    private String internalRef;

    /** persistent field */
    private String internalRefId;

    /** persistent field */
    private String externalRefId;

    /** persistent field */
    private User user;

    /** full constructor */
    public ExternalRefMapping(Long id, String internalRef, String internalRefId, String externalRefId, User user, boolean generated) {
        this.id = id;
        this.internalRef = internalRef;
        this.internalRefId = internalRefId;
        this.externalRefId = externalRefId;
        this.user = user;
        this.generated = generated;
    }

    /** default constructor */
    public ExternalRefMapping() {
    }

    public String getInternalRef() {
        return this.internalRef;
    }

    public void setInternalRef(String internalRef) {
        this.internalRef = internalRef;
    }

    public String getInternalRefId() {
        return this.internalRefId;
    }

    public void setInternalRefId(String internalRefId) {
        this.internalRefId = internalRefId;
    }

    public String getExternalRefId() {
        return this.externalRefId;
    }

    public void setExternalRefId(String externalRefId) {
        this.externalRefId = externalRefId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }
}
