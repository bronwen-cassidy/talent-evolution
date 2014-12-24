package com.zynap.talentstudio.web.portfolio;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.ContentType;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.SecurityAttribute;
import com.zynap.domain.UserSession;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * User: amark
 * Date: 12-Jul-2006
 * Time: 17:56:24
 */
public class PortfolioItemWrapper implements Serializable {

    public PortfolioItemWrapper(PortfolioItem portfolioItem, PortfolioItemFile itemFile) {
        this.portfolioItem = portfolioItem;
        this.itemFile = itemFile;
        this.contentType = this.portfolioItem.getContentType();
        this.contentTypeId = contentType != null ? contentType.getId() : null;
        this.securityAttribute = portfolioItem.getSecurityAttribute();
        if (portfolioItem.isText()) {
            uploadedText = new String(itemFile.getBlobValue());
        } else if (portfolioItem.isURL()) {
            url = new String(itemFile.getBlobValue());
        }
    }

    public PortfolioItemWrapper(PortfolioItem portfolioItem) {
        this.portfolioItem = portfolioItem;
    }

    public Long getId() {
        return portfolioItem.getId();
    }

    public String getOrigFileName() {
        return portfolioItem.getOrigFileName();
    }

    public String getLabel() {
        return portfolioItem.getLabel();
    }

    public void setLabel(String label) {
        portfolioItem.setLabel(label);
    }

    public String getComments() {
        return portfolioItem.getComments();
    }

    public void setComments(String comments) {
        portfolioItem.setComments(comments);
    }

    public String getScope() {
        return portfolioItem.getScope();
    }

    public void setScope(String scope) {
        portfolioItem.setScope(scope);
    }

    public String getContentSubType() {
        return portfolioItem.getContentSubType();
    }

    public void setContentSubType(String contentSubType) {
        portfolioItem.setContentSubType(contentSubType);
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public UploadedFile getFile() {
        return null;
    }

    public void setFile(UploadedFile file) {

        this.dirtyFile = true;

        if (file != null) {

            final Long fileSize = file.getFileSize();
            if (fileSize.longValue() > 0) {
                itemFile.setBlobValue(file.getBlobValue());
                portfolioItem.setOrigFileName(file.getFileName());
                portfolioItem.setFileExtension(file.getFileExtension());
                portfolioItem.setFileSize(fileSize);
            }

            // check dirty file regardless of file size
            dirtyFile = file.isInvalid();
        }
    }

    public String getUploadedText() {
        return portfolioItem.isText() && uploadedText.length() > 0 ? uploadedText : null;
    }

    private boolean hasFileLength() {
        return portfolioItem.getFileSize() != null && portfolioItem.getFileSize() > 0;
    }

    public void setUploadedText(String uploadedText) {
        this.uploadedText = uploadedText;
    }

    public String getUrl() {
        return portfolioItem.isURL() && url.length() > 0 ? url : null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void assignValue() {
        if (StringUtils.hasLength(uploadedText)) {
            setValue(uploadedText);
        } else if (StringUtils.hasLength(url)) {
            setValue(url);
        }
    }

    private void setValue(String input) {
        final byte[] value = input.getBytes();
        itemFile.setBlobValue(value);
        portfolioItem.setFileSize(new Long(value.length));
    }

    public boolean hasFile() {
        return portfolioItem.hasFile();
    }

    public boolean isEmptyFile() {
        final Long fileSize = portfolioItem.getFileSize();
        return fileSize == null || fileSize.longValue() <= 0;
    }

    public boolean isDirtyFile() {
        return dirtyFile;
    }

    public boolean isText() {
        return portfolioItem.isText();
    }

    public boolean isUpload() {
        return portfolioItem.isUpload();
    }

    public boolean isURL() {
        return portfolioItem.isURL();
    }

    public boolean isPublic() {
        return portfolioItem.isPublic();
    }

    public boolean isPrivate() {
        return portfolioItem.isPrivate();
    }

    public String getNodeType() {
        return portfolioItem.getNode().getNodeType();
    }

    public PortfolioItem getModifiedItem() {
        portfolioItem.setLastModified(new Date());
        portfolioItem.setContentType(new ContentType(contentTypeId));
        portfolioItem.setSecurityAttribute(securityAttribute);
        assignValue();
        return portfolioItem;
    }

    public PortfolioItemFile getModifiedFileItem() {
        return itemFile;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Node getNode() {
        return portfolioItem.getNode();
    }

    public void resetId() {
        portfolioItem.setId(null);
        itemFile.setId(null);
        securityAttribute.setId(null);
    }


    public boolean isWritePermission() {

        boolean writePermission;

        // if they created the item or they are an admin then they automatically have r/w permissions so return true
        if (userSession.isAdministrator() || isOwner()) writePermission = true;
        else if (myPortfolio) {
            // they are viewing their own portfolio so check individual write permissions
            writePermission = securityAttribute.isIndividualWrite();
        } else {
            // check manager write permissions
            writePermission = securityAttribute.isManagerWrite();
        }
        return writePermission;
    }


    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public boolean isOwner() {
        return portfolioItem.isCreatedByUser(userSession.getId());
    }

    public void setMyPortfolio(boolean myPortfolio) {
        this.myPortfolio = myPortfolio;
    }

    public boolean isMyPortfolio() {
        return myPortfolio;
    }

    public boolean isIndividualRead() {
        return securityAttribute.isIndividualRead();
    }

    public boolean isIndividualWrite() {
        return securityAttribute.isIndividualWrite();
    }

    public boolean isManagerRead() {
        return securityAttribute.isManagerRead();
    }

    public boolean isManagerWrite() {
        return securityAttribute.isManagerWrite();
    }


    public void setManagerRead(boolean managerRead) {
        securityAttribute.setManagerRead(managerRead);
    }

    public void setManagerWrite(boolean managerWrite) {
        securityAttribute.setManagerWrite(managerWrite);
    }

    public void setIndividualRead(boolean individualRead) {
        securityAttribute.setIndividualRead(individualRead);
    }

    public void setIndividualWrite(boolean individualWrite) {
        securityAttribute.setIndividualWrite(individualWrite);
    }

    public Date getLastModified() {
        return portfolioItem.getLastModified();
    }

    public void setLastModified(Date lastModified) {
        portfolioItem.setLastModified(lastModified);
    }

    public String getTextContent() {
        return portfolioItem.isText() && hasFileLength() ? new String(itemFile.getBlobValue()) : null;
    }

    public boolean isPublicRead() {
        return securityAttribute.isPublicRead();
    }

    public void setPublicRead(boolean publicRead) {
        securityAttribute.setPublicRead(publicRead);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortfolioItemWrapper)) return false;

        PortfolioItemWrapper other = (PortfolioItemWrapper) o;
        if (!other.getModifiedItem().equals(this.getModifiedItem())) return false;
        else {
            // compare scope
            if (!other.getScope().equals(this.getScope())) return false;

            // compare node
            if (!other.getNode().equals(this.getNode())) return false;

            // compare security attributes
            if (other.isWritePermission() != this.isWritePermission()) return false;
            if (other.isIndividualRead() != this.isIndividualRead()) return false;
            if (other.isIndividualWrite() != this.isIndividualWrite()) return false;
            if (other.isManagerRead() != this.isManagerRead()) return false;
            if (other.isManagerWrite() != this.isManagerWrite()) return false;
            if (other.isPublicRead() != this.isPublicRead()) return false;
        }
        return true;
    }

    public int hashCode() {
        int result = getModifiedItem() != null ? getModifiedItem().hashCode() : 0;
        result = 31 * result + (itemFile != null ? itemFile.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (contentTypeId != null ? contentTypeId.hashCode() : 0);
        result = 31 * result + (uploadedText != null ? uploadedText.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (securityAttribute != null ? securityAttribute.hashCode() : 0);
        return result;
    }

    /**
     * The file item being added or edited.
     */
    private final PortfolioItem portfolioItem;
    private PortfolioItemFile itemFile;

    /**
     * The selected content type.
     */
    private ContentType contentType;

    /**
     * Used for binding - used rather than contentType field to avoid pass-by-ref problems.
     */
    private String contentTypeId;

    private String uploadedText;
    private String url;
    /**
     * Indicates if has unsaved file.
     */
    private boolean dirtyFile;

    private SecurityAttribute securityAttribute;

    private boolean myPortfolio;

    private UserSession userSession;
}
