package com.zynap.talentstudio.web.utils.beans;

import com.zynap.common.util.FileUtils;
import com.zynap.common.util.UploadedFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

/**
* Class or Interface description.
*
* @author jsueiras
* @since 16-Jun-2005 16:02:34
* @version 0.1
*/

public class UploadedFilePropertyEditor extends PropertyEditorSupport {

        private final Log logger = LogFactory.getLog(getClass());

        public void setValue(Object value) {
            if (value instanceof MultipartFile) {
                MultipartFile multipartFile = (MultipartFile) value;

                try {

                    // todo enhancement (removes empty/null checks on every setter for file uploads) if(multipartFile.getBytes() == null || multipartFile.getBytes().length < 1) return;

                    UploadedFile uploadedFile = new UploadedFile();
                    uploadedFile.setBlobValue(multipartFile.getBytes());
                    uploadedFile.setFileExtension(FileUtils.getExtension(multipartFile.getOriginalFilename()));
                    uploadedFile.setFileName(multipartFile.getOriginalFilename());
                    uploadedFile.setFileSize(new Long(multipartFile.getSize()));
                    super.setValue(uploadedFile);
                }
                catch (IOException ex) {
                    logger.error("Cannot read contents of multipart file", ex);
                    throw new IllegalArgumentException("Cannot read contents of multipart file: " + ex.getMessage());
                }
            }
        }

}