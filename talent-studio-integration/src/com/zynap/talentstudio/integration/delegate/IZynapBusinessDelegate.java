package com.zynap.talentstudio.integration.delegate;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.integration.common.IZynapCommand;
import com.zynap.talentstudio.integration.common.IZynapCommandResult;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Oct-2005
 * Time: 14:46:52
 */
public interface IZynapBusinessDelegate {

    /**
     * Execute command.
     *
     * @param command
     * @param username
     * @return IZynapCommandResult
     * @throws TalentStudioException
     */
    IZynapCommandResult invoke(IZynapCommand command, String username) throws TalentStudioException;
}
