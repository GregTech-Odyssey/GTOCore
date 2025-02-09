package com.gto.gtocore.mixin.gtmt;

import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.TeamUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(WirelessEnergyContainer.class)
public class WirelessEnergyContainerMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static WirelessEnergyContainer getOrCreateContainer(UUID uuid) {
        return WirelessEnergyContainer.GLOBAL_CACHE.computeIfAbsent(TeamUtil.getTeamUUID(uuid), ExtendWirelessEnergyContainer::new);
    }
}
