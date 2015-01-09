package com.zynap.talentstudio.util.velocity;

import com.zynap.exception.TalentStudioException;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;

/**
 * User: amark
 * Date: 18-Jul-2006
 * Time: 13:59:35
 */
public final class VelocityUtils {

    public static VelocityEngine getEngine() throws TalentStudioException {

        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("runtime.log.logsystem.class", new org.apache.velocity.runtime.log.LogChute() {
            public void init(RuntimeServices runtimeServices) throws Exception {
                System.out.println("1");
            }

            public void log(int i, String s) {
                System.out.println("2");
            }

            public void log(int i, String s, Throwable throwable) {
                System.out.println("3");
            }

            public boolean isLevelEnabled(int i) {
                return false;
            }
        });
        velocityEngine.addProperty(VelocityEngine.RESOURCE_LOADER, "class");
        velocityEngine.addProperty("class." + VelocityEngine.RESOURCE_LOADER + ".description", "Classpath loader");
        velocityEngine.addProperty("class." + VelocityEngine.RESOURCE_LOADER + ".class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            velocityEngine.init();
        } catch (Exception e) {
            throw new TalentStudioException(e);
        }

        return velocityEngine;
    }
}
