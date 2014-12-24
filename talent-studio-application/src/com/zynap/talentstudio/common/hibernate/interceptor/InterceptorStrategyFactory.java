package com.zynap.talentstudio.common.hibernate.interceptor;

import com.zynap.talentstudio.common.hibernate.interceptor.strategy.InterceptorStrategy;
import com.zynap.talentstudio.common.hibernate.interceptor.strategy.NoOpInterceptorStrategy;

import java.util.Map;

/**
 * User: amark
 * Date: 18-Aug-2006
 * Time: 14:35:14
 *
 * Factory for {@link InterceptorStrategy} objects.
 */
public final class InterceptorStrategyFactory {

    /**
     * Get InterceptorStrategy based on class name.
     * <br/> Returns {@link NoOpInterceptorStrategy} if none available.
     *
     * @param className
     * @return InterceptorStrategy implementation.
     */
    public InterceptorStrategy getInterceptorStrategy(String className) {

        InterceptorStrategy strategy = (InterceptorStrategy) strategies.get(className);
        if (strategy == null) {
            strategy = new NoOpInterceptorStrategy();
        }

        return strategy;
    }

    public void setStrategies(Map strategies) {
        this.strategies = strategies;
    }

    private Map strategies;
}
