package com.zynap.talentstudio.help;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;

/**
 * User: amark
 * Date: 07-Aug-2006
 * Time: 17:09:32
 */
public interface IHelpTextService extends IZynapService {

    HelpTextItem findByID(Long id) throws TalentStudioException;
}
