/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.tools;

import com.zynap.exception.TalentStudioException;

import java.io.Writer;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IXsdGenerator {

    void generateSchema(Writer writer) throws TalentStudioException;

    String generateSchema() throws TalentStudioException;
}
