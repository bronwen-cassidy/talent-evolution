package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.populations.PopulationDto;

import java.io.Serializable;
import java.util.Collection;

/**
 * User: bcassidy
 * Date: 20-Apr-2006
 * Time: 10:46:49
 */
public final class ListPopulationWrapperBean implements Serializable {

    public ListPopulationWrapperBean(Collection<PopulationDto> populations) {
        this.populations = populations;
    }

    public Collection<PopulationDto> getPopulations() {
        return populations;
    }

    private final Collection<PopulationDto> populations;
}
