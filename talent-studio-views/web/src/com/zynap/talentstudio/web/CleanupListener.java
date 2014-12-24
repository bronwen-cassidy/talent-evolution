package com.zynap.talentstudio.web;

import org.quartz.Scheduler;

import org.apache.log4j.LogManager;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.beans.Introspector;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Enumeration;

public class CleanupListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
    }

    public void contextDestroyed(ServletContextEvent event) {

        System.out.println("Starting cleanup at " + new Date());

        try {
            // load context
            Introspector.flushCaches();
            System.out.println("Introspector cache flushed");

            for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements();) {
                Driver driver = (Driver) e.nextElement();
                if (driver.getClass().getClassLoader() == getClass().getClassLoader()) {
                    DriverManager.deregisterDriver(driver);
                }
            }
            System.out.println("JDBC drivers deregistered");

            LogManager.shutdown();
            System.out.println("LogManager shutdown");

            System.out.println("Completed cleanup at " + new Date());

        } catch (Throwable e) {
            System.err.println("Failed to cleanup ClassLoader for webapp at " + new Date());
            e.printStackTrace();
        }
    }

}
