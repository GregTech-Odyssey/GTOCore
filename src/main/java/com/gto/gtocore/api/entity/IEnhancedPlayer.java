package com.gto.gtocore.api.entity;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.common.item.armor.SpaceArmorComponentItem;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.item.armor.ArmorComponentItem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface IEnhancedPlayer {

    boolean gTOCore$canFly();

    boolean gTOCore$isSpaceState();

    boolean gTOCore$isWardenState();

    boolean gTOCore$isDisableDrift();

    boolean gTOCore$isAmprosium();

    void gtocore$setDrift(boolean drift);

    static boolean spaceTick(ServerLevel level, LivingEntity entity) {
        if (entity instanceof IEnhancedPlayer player) {
            if (player.gTOCore$isSpaceState()) return false;
            var chestplate = ((Player) entity).getInventory().getArmor(2);
            if (chestplate.getItem() instanceof SpaceArmorComponentItem && SpaceArmorComponentItem.hasOxygen(entity)) {
                for (var a : entity.getArmorSlots()) {
                    if (a.getItem() instanceof ArmorComponentItem item && (ItemUtils.getIdLocation(item).getPath().contains("nanomuscle") || ItemUtils.getIdLocation(item).getPath().contains("quarktech"))) continue;
                    return true;
                }
                return false;
            }
            return !(player.gTOCore$isWardenState() && level.dimension().location().equals(GTODimensions.OTHERSIDE));
        }
        return true;
    }

    static float gravity(Entity entity, float gravity) {
        if (entity instanceof IEnhancedPlayer player && player.gTOCore$isAmprosium()) {
            return 0;
        }
        return gravity;
    }
}
