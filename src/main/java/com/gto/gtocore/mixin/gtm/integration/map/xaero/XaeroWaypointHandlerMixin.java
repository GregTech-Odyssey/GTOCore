package com.gto.gtocore.mixin.gtm.integration.map.xaero;

import com.gregtechceu.gtceu.integration.map.IWaypointHandler;
import com.gregtechceu.gtceu.integration.map.xaeros.WaypointWithDimension;
import com.gregtechceu.gtceu.integration.map.xaeros.XaeroWaypointHandler;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

import org.spongepowered.asm.mixin.*;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.world.MinimapWorldManager;

@Mixin(XaeroWaypointHandler.class)
public abstract class XaeroWaypointHandlerMixin implements IWaypointHandler {

    @Unique
    private final Lazy<MinimapWorldManager> gtocore$worldManager = Lazy.of(() -> BuiltInHudModules.MINIMAP.getCurrentSession()
            .getWorldManager());

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void setWaypoint(String key, String name, int color, ResourceKey<Level> dim, int x, int y, int z) {
        gtocore$worldManager.get().getCurrentWorld().getCurrentWaypointSet().add(new WaypointWithDimension(dim, x, y, z, name, name.substring(0, 1), WaypointColor.WHITE));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void removeWaypoint(String key) {}
}
