package com.gtocore.mixin.apotheosis;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import dev.shadowsoffire.apotheosis.adventure.AdventureConfig;
import dev.shadowsoffire.apotheosis.adventure.AdventureEvents;
import dev.shadowsoffire.apotheosis.adventure.compat.GameStagesCompat;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.placebo.reload.WeightedDynamicRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AdventureEvents.class)
public class AdventureEventsMixin {

    /**
     * @author .
     * @reason .
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    @Overwrite(remap = false)
    public void dropsHigh(LivingDropsEvent e) {
        if (e.getSource().getEntity() instanceof ServerPlayer p && e.getEntity() instanceof Monster) {
            float chance = AdventureConfig.gemDropChance + (e.getEntity().getPersistentData().contains("apoth.boss") ? AdventureConfig.gemBossBonus : 0);
            if (p.getRandom().nextFloat() <= chance) {
                Entity ent = e.getEntity();
                e.getDrops().add(new ItemEntity(ent.level(), ent.getX(), ent.getY(), ent.getZ(), GemRegistry.createRandomGemStack(p.getRandom(), (ServerLevel) p.level(), p.getLuck(), WeightedDynamicRegistry.IDimensional.matches(p.level()), GameStagesCompat.IStaged.matches(p)), 0, 0, 0));
            }
        }
    }
}
