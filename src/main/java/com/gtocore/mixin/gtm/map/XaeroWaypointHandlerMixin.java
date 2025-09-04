package com.gtocore.mixin.gtm.map;

import com.gregtechceu.gtceu.integration.map.IWaypointHandler;
import com.gregtechceu.gtceu.integration.map.xaeros.WaypointWithDimension;
import com.gregtechceu.gtceu.integration.map.xaeros.XaeroWaypointHandler;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.io.MinimapWorldManagerIO;

import java.io.IOException;

import static com.gtolib.GTOCore.LOGGER;
import static xaero.hud.minimap.BuiltInHudModules.MINIMAP;

@Mixin(XaeroWaypointHandler.class)
public abstract class XaeroWaypointHandlerMixin implements IWaypointHandler {

    @Unique
    private static final String XAERO_WAYPOINT_SET = ("gtocore.xaero_waypoint_set");

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void setWaypoint(String key, String name, int color, ResourceKey<Level> dim, int x, int y, int z) {
        MinimapSession session = MINIMAP.getCurrentSession();
        MinimapWorldManagerIO worldMangerIO = session.getWorldManagerIO();
        MinimapWorld world = session.getWorldManager().getCurrentWorld();

        var waypointSet = world.getWaypointSet(XAERO_WAYPOINT_SET);
        if (waypointSet == null) {
            LOGGER.info("Waypoint Set created:{}", XAERO_WAYPOINT_SET);
            world.addWaypointSet(XAERO_WAYPOINT_SET);
            waypointSet = world.getWaypointSet(XAERO_WAYPOINT_SET);
        }
        waypointSet.add(new WaypointWithDimension(dim, x, y, z, name, name.substring(0, 1), WaypointColor.getRandom()));

        try {
            worldMangerIO.saveWorld(world);
        } catch (IOException e) {
            LOGGER.error("save prospector waypoint failed when setting Waypoint [{}] {}", key, name);
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void removeWaypoint(String key) {}
}
