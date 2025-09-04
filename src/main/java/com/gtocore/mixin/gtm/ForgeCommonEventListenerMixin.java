package com.gtocore.mixin.gtm;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.ToggleEnergyConsumerBehavior;
import com.gregtechceu.gtceu.forge.ForgeCommonEventListener;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ForgeCommonEventListener.class)
public final class ForgeCommonEventListenerMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob entity = event.getEntity();
        if (entity.getRandom().nextBoolean()) return;
        if (entity instanceof Zombie zombie && zombie.getMainHandItem().isEmpty()) {
            if (zombie.getRandom().nextInt(50) > 48) {
                ItemStack itemStack = GTItems.NANO_SABER.get().getInfiniteChargedStack();
                ToggleEnergyConsumerBehavior.setItemActive(itemStack, true);
                zombie.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
                zombie.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
            } else if (zombie.getRandom().nextBoolean()) {
                zombie.setItemSlot(EquipmentSlot.MAINHAND, BuiltInRegistries.ITEM.getOrCreateTag(ItemTags.SWORDS).getRandomElement(zombie.getRandom()).orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.STICK)).value().getDefaultInstance());
                zombie.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
            }
        }
    }
}
