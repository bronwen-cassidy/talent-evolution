package com.zynap.talentstudio.util;

import com.zynap.talentstudio.security.UserSessionFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version 0.1
 * @since 13-Apr-2005 09:44:38
 */
public class FormatterFactory implements BeanFactoryAware {

    private static String localeDefault;
    private static String defaultDisplayPattern;
    /* regardless of locale we store dates in a particular format */
    private static final String databasePattern = "yyyy-MM-dd";
    private static FormatterFactory instance;

    private Properties formatProperties = null;
    private Map<String, Formatter> dateFormatters;


    public FormatterFactory(String locale, String displayPatternParam) {
        localeDefault = locale;
        defaultDisplayPattern = displayPatternParam;
        dateFormatters = new HashMap<String, Formatter>();
    }


    public static IFormatter getDateFormatter() {
        Locale locale;
        if (instance == null) {
            instance = (FormatterFactory) beanFactory.getBean("formatterFactory");
        }
        try {
            locale = UserSessionFactory.getUserSession().getLocale();
        } catch (IllegalStateException e) {
            locale = new Locale(localeDefault); 
        }
        return instance.getDateFormatterInternal(locale);
    }

    public static IFormatter getDateFormatter(Locale locale) {
        if (instance == null) {
            instance = (FormatterFactory) beanFactory.getBean("formatterFactory");
        }
        return instance.getDateFormatterInternal(locale);
    }

    private Formatter getDateFormatterInternal(Locale locale) {

        if (locale == null) locale = new Locale(localeDefault);
        Formatter formatter = dateFormatters.get(locale.getLanguage());

        if (formatter == null) {

            if (formatProperties == null) {
                try {
                    formatProperties = new Properties();

                    ResourceBundle myResource;
                    if (new Locale(localeDefault).equals(locale)) {
                        myResource = ResourceBundle.getBundle("messages");
                    } else {
                        myResource = ResourceBundle.getBundle("messages", locale);
                    }
                    final String dateFormat = myResource.getString(DATE_FORMAT_KEY);
                    if(dateFormat != null) formatProperties.setProperty(DATE_FORMAT_KEY, dateFormat);
                    else formatProperties.setProperty(DATE_FORMAT_KEY, defaultDisplayPattern);
                } catch (Exception e) {
                    if(formatProperties == null) formatProperties = new Properties();
                    formatProperties.setProperty(DATE_FORMAT_KEY, defaultDisplayPattern);
                }
            }
            formatter = new Formatter(locale, (String) formatProperties.get(DATE_FORMAT_KEY), databasePattern);
            dateFormatters.put(locale.getLanguage(), formatter);
        }
        return formatter;
    }

    /**
     * Callback that supplies the owning factory to a bean instance.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     *
     * @param beanFactory owning BeanFactory (may not be null).
     *                    The bean can immediately call methods on the factory.
     * @throws org.springframework.beans.BeansException
     *          in case of initialization errors
     * @see org.springframework.beans.factory.BeanInitializationException
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        FormatterFactory.beanFactory = beanFactory;
    }

    static BeanFactory beanFactory;
    private static final String DATE_FORMAT_KEY = "date.format";
}
