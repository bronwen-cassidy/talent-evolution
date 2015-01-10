package com.zynap.talentstudio.integration.adapter;

import java.io.Reader;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 14-Oct-2005
 * Time: 14:01:59
 */
public interface IXmlAdapter {

    /**
     * Executes the given data, expected format of the data is a string of xml.
     *
     * @param input a string of xml
     * @param attachments
     * @return String containing XML indicating the results of the commands (if any)
     * @throws Exception
     */
    String execute(String input, byte[][] attachments, String username) throws Exception;

    /**
     * Executes the given data, expected format of the data is a string of xml.
     *
     * @param input an InputStream
     * @param attachments
     * @throws Exception
     */
    void execute(Writer outputWriter, Reader input, byte[][] attachments, String username) throws Exception;

}
