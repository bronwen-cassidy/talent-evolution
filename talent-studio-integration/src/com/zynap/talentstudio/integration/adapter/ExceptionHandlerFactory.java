package com.zynap.talentstudio.integration.adapter;

import java.util.Map;

/**
 * Factory for finding appropriate {@link ExceptionHandler} implementation based on exception being processed.
 *
 * User: amark
 * Date: 19-Oct-2005
 * Time: 16:15:18
 */
public final class ExceptionHandlerFactory {

    /**
     * Default constructor.
     */
    public ExceptionHandlerFactory() {
    }

    /**
     * Set Map of mappings.
     *
     * @param mappings
     */
    public void setMappings(Map mappings) {
        this.mappings = mappings;
    }

    /**
     * Get the appropriate {@link ExceptionHandler} implementation based on exception.
     * <br/> If none found, return instance of {@link DefaultExceptionHandler}.
     * @param t The throwable
     * @return ExceptionHandler
     */
    public ExceptionHandler getExceptionHandler(Throwable t) {
        ExceptionHandler exceptionHandler = (ExceptionHandler) mappings.get(t.getClass().getName());

        if (exceptionHandler == null) {
            exceptionHandler = new DefaultExceptionHandler();
        }

        return exceptionHandler;
    }

    /**
     * Map of {@link ExceptionHandler} implementations (keyed on fully qualified class name for exception.)
     */
    private Map mappings;
}
