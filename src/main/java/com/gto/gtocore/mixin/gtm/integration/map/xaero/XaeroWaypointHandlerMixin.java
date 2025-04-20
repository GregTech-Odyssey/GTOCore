package com.gto.gtocore.mixin.gtm.integration.map.xaero;

import com.gregtechceu.gtceu.integration.map.IWaypointHandler;
import com.gregtechceu.gtceu.integration.map.xaeros.XaeroWaypointHandler;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.world.MinimapWorldManager;

@Mixin(XaeroWaypointHandler.class)
public abstract class XaeroWaypointHandlerMixin implements IWaypointHandler {

    @Final
    @Shadow(remap = false)
    private Lazy<Int2ObjectMap<Waypoint>> waypoints;
    @Unique
    private final Lazy<MinimapWorldManager> gtocore$worldManager = Lazy.of(() -> BuiltInHudModules.MINIMAP.getCurrentSession()
            .getWorldManager());

    @Inject(method = "setWaypoint", at = @At("RETURN"), remap = false)
    public void setWaypoint(String key, String name, int color, ResourceKey<Level> dim, int x, int y, int z, CallbackInfo ci) {
        gtocore$worldManager.get().getCurrentWorld().getCurrentWaypointSet().add(waypoints.get().get(getIndex(key)));
    }

    @Shadow(remap = false)
    protected abstract int getIndex(String key);
}
