package com.zynap.talentstudio.help;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

/**
 * User: amark
 * Date: 07-Aug-2006
 * Time: 17:10:33
 */
public interface IHelpTextDao extends IFinder, IModifiable {

    void saveOrUpdate(HelpTextItem helpTextItem) throws TalentStudioException;

    void delete(Long id) throws TalentStudioException;
}
