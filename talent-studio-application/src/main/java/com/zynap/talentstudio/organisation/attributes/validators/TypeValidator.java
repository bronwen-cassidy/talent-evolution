package com.zynap.talentstudio.organisation.attributes.validators;

import com.zynap.talentstudio.util.IFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 30-Jun-2005
 * Time: 08:53:03
 * To change this template use File | Settings | File Templates.
 */
public class TypeValidator {

    public static boolean isInteger(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDate(String s){
       SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_PATTERN);
        formatter.setLenient(false);
        try {
            formatter.parse(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isTime(String s){
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_TIME_PATTERN);
        formatter.setLenient(false);
        try {
            formatter.parse(s);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isDateTime(String s){
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_TIME_PATTERN);
        formatter.setLenient(false);
        try {
            formatter.parse(s);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
