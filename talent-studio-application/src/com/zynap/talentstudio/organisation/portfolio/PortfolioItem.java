package com.zynap.talentstudio.organisation.portfolio;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.SecurityAttribute;
import com.zynap.talentstudio.util.IFileDetail;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * @author Hibernate CodeGenerator
 */
public class PortfolioItem extends ZynapDomainObject implements IFileDetail {

    /**
     * default constructor
     */
    public PortfolioItem() {
    }

    public PortfolioItem(String label, String comments, String status, String scope, Date lastModified, String contentSubType) {
        this.label = label;
        this.comments = comments;
        this.status = status;
        this.scope = scope;
        this.lastModified = lastModified;
        this.contentSubType = contentSubType;
    }

    public PortfolioItem(String label, String comments, String status,
                         String scope, Date lastModified, String contentSubType, String origFileName, String fileExtension) {
        this(label, comments, status, scope, lastModified, contentSubType);

        this.origFileName = origFileName;
        this.fileExtension = fileExtension;
    }

    /**
     * Does user have access to item.
     * <br> Only applies to public and private items as restricted items are handled differently if accessed through one's
     * personal portfolio and through another artefact's portfolio view.
     *
     * @param userId The user id
     * @return true if the item has public scope, or it has private scope but the user created it; otherwise false.
     */
    public boolean hasAccess(Long userId) {
        return (isPublic() || (isPrivate() && isCreatedByUser(userId)));
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(Long lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }



    public boolean isPublic() {
        return PortfolioItem.PUBLIC_SCOPE.equals(getScope());
    }

    public boolean isRestricted() {
        return RESTRICTED_SCOPE.equals(getScope());
    }

    public boolean isPrivate() {
        return PRIVATE_SCOPE.equals(getScope());
    }

    public boolean isCreatedByUser(Long userId) {
        return createdById != null && userId.equals(createdById);
    }

    public String getOrigFileName() {
        return this.origFileName;
    }

    public void setOrigFileName(String origFileName) {
        this.origFileName = origFileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isSearchable() {
        return this.fileSize.longValue() < MAX_SEARCHABLE_FILE_SIZE;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * Has uploaded file.
     *
     * @return true if file has an original file name.
     */
    public boolean hasFile() {                            
        return StringUtils.hasText(origFileName);
    }

    public String getContentSubType() {
        return this.contentSubType;
    }

    public void setContentSubType(String contentSubType) {
        this.contentSubType = contentSubType;
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public boolean isUpload() {
        return UPLOAD_SUBTYPE.equals(getContentSubType());
    }

    public boolean isText() {
        return TEXT_SUBTYPE.equals(getContentSubType());
    }

    public boolean isURL() {
        return URL_SUBTYPE.equals(getContentSubType());
    }

    public final String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .toString();
    }

    public SecurityAttribute getSecurityAttribute() {
        return securityAttribute;
    }

    public void setSecurityAttribute(SecurityAttribute securityAttribute) {
        this.securityAttribute = securityAttribute;
    }

    public boolean isManagerRead() {
        return securityAttribute.isManagerRead();
    }

    /**
     * nullable persistent field
     */
    private String comments;

    /**
     * nullable persistent field
     */
    private String status = STATUS_LIVE;

    /**
     * nullable persistent field
     */
    private String scope;

    /**
     * nullable persistent field
     */
    private Date lastModified;

    private User lastModifiedBy;

    private User createdBy;

    /**
     * persistent field
     */
    private Node node;

    /**
     * persistent field
     */
    private Long createdById;

    private Long lastModifiedById;

    /**
     * persistent field
     */
    private String origFileName;

    /**
     * nullable persistent field
     */
    private Long fileSize;

    /**
     * nullable persistent field
     */
    private String fileExtension;

    /**
     * persistent field
     */
    private String contentSubType;

    /**
     * persistent field
     */
    private ContentType contentType = new ContentType();

    private SecurityAttribute securityAttribute;

    public static final String RESTRICTED_SCOPE = "RESTRICTED";
    public static final String PRIVATE_SCOPE = "PRIVATE";
    public static final String PUBLIC_SCOPE = "PUBLIC";

    public static final String UPLOAD_SUBTYPE = "UPLOAD";
    public static final String URL_SUBTYPE = "URL";
    public static final String TEXT_SUBTYPE = "TEXT";

    public static final String STATUS_LIVE = "LIVE";
    public static final long MAX_SEARCHABLE_FILE_SIZE = 8000000;
}
