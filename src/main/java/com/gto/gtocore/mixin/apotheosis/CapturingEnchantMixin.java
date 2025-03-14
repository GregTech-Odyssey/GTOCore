package com.gto.gtocore.mixin.apotheosis;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.spawn.enchantment.CapturingEnchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CapturingEnchant.class)
public class CapturingEnchantMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void handleCapturing(LivingDropsEvent e) {
        Entity killer = e.getSource().getEntity();
        if (killer instanceof Player player) {
            int level = player.getMainHandItem().getEnchantmentLevel(Apoth.Enchantments.CAPTURING.get());
            LivingEntity killed = e.getEntity();
            if (!EntityType.getKey(killed.getType()).getNamespace().equals("minecraft")) return;
            if (killed.level().random.nextFloat() < level / 40F) {
                Item eggItem = ForgeSpawnEggItem.fromEntityType(killed.getType());
                if (eggItem == null) return;
                ItemStack egg = new ItemStack(eggItem);
                e.getDrops().add(new ItemEntity(killed.level(), killed.getX(), killed.getY(), killed.getZ(), egg));
            }
        }
    }
}
