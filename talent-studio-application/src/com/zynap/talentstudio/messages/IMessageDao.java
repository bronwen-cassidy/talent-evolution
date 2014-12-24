/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.messages;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 13-Sep-2007 12:20:48
 */
public interface IMessageDao extends IModifiable, IFinder {

    List<MessageItem> findAll(Long userId);

    void delete(String[] messageItemIds);

    Integer countUnreadMessages(Long userId) throws TalentStudioException;
}
