/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.web.organisation.positions.PositionWrapperBean;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 11-Apr-2011 19:36:59
 */
public class PositionBrowseNodeWrapper extends BrowseNodeWrapper {

    public PositionBrowseNodeWrapper(NodeSearchQueryWrapper queryWrapper, List results, PositionWrapperBean nodeWrapperBean) {
        super(queryWrapper, results, nodeWrapperBean);
    }
}