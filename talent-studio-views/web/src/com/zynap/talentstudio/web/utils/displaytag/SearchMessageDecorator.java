/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.utils.displaytag;

import org.displaytag.decorator.TableDecorator;
import org.displaytag.exception.DecoratorException;

import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;

/**
 * Class or Interface description.
 *
 * @author acalderwood
 * @version 0.1
 * @since 20-Mar-2007 11:31:07
 */
public class SearchMessageDecorator extends TableDecorator {

    public String getDecoratedLabel() throws DecoratorException {
        PortfolioItem pi = (PortfolioItem) this.getCurrentRowObject();
        String label = pi.getLabel();
        if (pi.isUpload() && pi.getFileSize().longValue() > MAX_FILE_SIZE) {
            label += NOT_SEARCHABLE_MESSAGE;
        }
        return label;
    }

    public static final String NOT_SEARCHABLE_MESSAGE = " (not searchable)";
    public static final long MAX_FILE_SIZE = 8 * 1024 * 1024;
}
