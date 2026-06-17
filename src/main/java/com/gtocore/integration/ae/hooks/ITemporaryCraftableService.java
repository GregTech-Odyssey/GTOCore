package com.gtocore.integration.ae.hooks;

import appeng.api.crafting.IPatternDetails;

public interface ITemporaryCraftableService {

    IPatternDetails gto$getTempPatternDetails();

    void gto$setTempPatternDetails(IPatternDetails patternDetails);
}
