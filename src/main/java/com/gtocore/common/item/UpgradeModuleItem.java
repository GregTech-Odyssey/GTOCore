package com.gtocore.common.item;

import com.gtocore.api.gui.graphic.GTOToolTipComponent;
import com.gtocore.api.gui.graphic.GTOTooltipComponentItem;
import com.gtocore.api.gui.graphic.impl.GTOComponentTooltipComponent;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOItemTooltips;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.IUpgradeMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@DataGeneratorScanned
public final class UpgradeModuleItem extends Item implements GTOTooltipComponentItem {

    @RegisterLanguage(cn = "需要10级经验", en = "Requires 10 levels of experience")
    public static final String experience_not_enough = "gtocore.machine.upgrade.experience_not_enough";

    static {
        if (GTCEu.isDataGen()) {
            GTOItemTooltips.INSTANCE.getSpeedUpgradeModuleTooltips().invoke(0D, 0D).getArray();
            GTOItemTooltips.INSTANCE.getEnergyUpgradeModuleTooltips().invoke(0D, 0D).getArray();
        }
    }

    public UpgradeModuleItem(Properties properties) {
        super(properties);
    }

    private static ItemStack speed(int tier) {
        var item = GTOItems.SPEED_UPGRADE_MODULE.get().getDefaultInstance();
        item.getOrCreateTag().putDouble("speed", randomMultiple(tier));
        return item;
    }

    private static ItemStack energy(int tier) {
        var item = GTOItems.ENERGY_UPGRADE_MODULE.get().getDefaultInstance();
        item.getOrCreateTag().putDouble("energy", randomMultiple(tier));
        return item;
    }

    private static double randomMultiple(int tier) {
        return 1D - (GTValues.RNG.nextDouble() / (20D / tier));
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var player = context.getPlayer();
        if (player instanceof ServerPlayer) {
            var item = player.getItemInHand(context.getHand());
            var tag = item.getTag();
            if (tag != null) {
                var machine = MetaMachine.getMachine(context.getLevel(), context.getClickedPos());
                if (machine instanceof IUpgradeMachine upgradeMachine && upgradeMachine.gtolib$canUpgraded()) {
                    if (tag.contains("speed")) {
                        double speed = upgradeMachine.gtolib$getSpeed();
                        if (speed < 1) {
                            speed = Math.max(speed, randomMultiple(10)) * tag.getDouble("speed");
                        } else {
                            speed = tag.getDouble("speed");
                        }
                        upgradeMachine.gtolib$setSpeed(Math.max(0.5, speed));
                        player.setItemInHand(context.getHand(), item.copyWithCount(item.getCount() - 1));
                        return InteractionResult.CONSUME;
                    }
                    if (tag.contains("energy")) {
                        double energy = upgradeMachine.gtolib$getEnergy();
                        if (energy < 1) {
                            energy = Math.max(energy, randomMultiple(10)) * tag.getDouble("energy");
                        } else {
                            energy = tag.getDouble("energy");
                        }
                        upgradeMachine.gtolib$setEnergy(Math.max(0.5, energy));
                        player.setItemInHand(context.getHand(), item.copyWithCount(item.getCount() - 1));
                        return InteractionResult.CONSUME;
                    }
                }
            } else {
                if (player.experienceLevel > 10) {
                    player.setItemInHand(context.getHand(), item.copyWithCount(item.getCount() - 1));
                    ItemStack itemStack;
                    if (this == GTOItems.SPEED_UPGRADE_MODULE.get()) {
                        itemStack = speed(Math.min(10, player.experienceLevel / 10));
                    } else {
                        itemStack = energy(Math.min(10, player.experienceLevel / 10));
                    }
                    player.experienceLevel = 0;
                    player.totalExperience = 0;
                    context.getLevel().addFreshEntity(new ItemEntity(context.getLevel(), player.getX(), player.getY(), player.getZ(), itemStack));
                } else {
                    player.sendSystemMessage(Component.translatable(experience_not_enough));
                    return InteractionResult.PASS;
                }
            }
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void attachGTOTooltip(ItemStack itemStack, List<GTOToolTipComponent> tooltips) {
        var tag = itemStack.getTag();
        if (tag != null) {
            if (tag.contains("speed")) {
                for (Component component : GTOItemTooltips.INSTANCE.getSpeedUpgradeModuleTooltips().invoke(Math.max(0.5, tag.getDouble("speed")), tag.getDouble("speed")).getArray()) {
                    tooltips.add(new GTOComponentTooltipComponent(component));
                }
            }
            if (tag.contains("energy")) {
                for (Component component : GTOItemTooltips.INSTANCE.getEnergyUpgradeModuleTooltips().invoke(Math.max(0.5, tag.getDouble("energy")), tag.getDouble("energy")).getArray()) {
                    tooltips.add(new GTOComponentTooltipComponent(component));
                }
            }
        } else {
            if (this == GTOItems.SPEED_UPGRADE_MODULE.get()) {
                for (Component component : GTOItemTooltips.INSTANCE.getSpeedUpgradeModuleTooltips().invoke(0d, 0d).getArray())
                    tooltips.add(new GTOComponentTooltipComponent(component));
            } else {
                for (Component component : GTOItemTooltips.INSTANCE.getEnergyUpgradeModuleTooltips().invoke(0d, 0d).getArray()) {
                    tooltips.add(new GTOComponentTooltipComponent(component));
                }

            }
        }
    }
}
