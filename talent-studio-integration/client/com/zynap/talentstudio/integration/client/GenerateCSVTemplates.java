/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.client;

import com.zynap.talentstudio.integration.tools.ITemplateGenerator;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 16-Jul-2007 15:01:34
 */
public class GenerateCSVTemplates extends IntegrationMain {

    public static void main(String[] args){
        processArguments(args);
        loadApp();
        System.out.println(">>>>>>>>>>>>> dbHost = " + dbHost);
        final ITemplateGenerator templateGenerator = (ITemplateGenerator) getBean("templateGenerator");
        templateGenerator.generate();
    }
}
