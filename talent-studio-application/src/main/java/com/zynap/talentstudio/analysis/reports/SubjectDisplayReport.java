/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Apr-2010 21:12:38
 */
public class SubjectDisplayReport extends Report {

    public SubjectDisplayReport() {
        super();
    }

    public SubjectDisplayReport(Long id) {
        super(id);
    }

    public SubjectDisplayReport(String label, String description, String access) {
        super(label, description, access);
    }
}