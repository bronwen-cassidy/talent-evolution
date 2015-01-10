package com.zynap.talentstudio.integration.dto.config;

import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.integration.dto.processors.IPostProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 26-Oct-2005
 * Time: 09:39:00
 * To change this template use File | Settings | File Templates.
 */
public class ZynapResourcesConfig {

    private  IPostProcessor postProcessor;
    private  String className;
    private  String alias;
    private  IZynapService service;


    public IPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(IPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public IZynapService getService() {
        return service;
    }

    public void setService(IZynapService service) {
        this.service = service;
    }

}
