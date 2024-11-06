package com.gto.gtocore.common.item;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.Objects;

public class ConfigurationCopyBehavior implements IInteractionItem {

    public static final ConfigurationCopyBehavior INSTANCE = new ConfigurationCopyBehavior();

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        CompoundTag tags = ToolHelper.getBehaviorsTag(stack);
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof MetaMachineBlockEntity machineBlock) {
            MetaMachine metaMachine = machineBlock.getMetaMachine();
            if (metaMachine instanceof SimpleTieredMachine simpleTieredMachine) {
                if (Objects.requireNonNull(context.getPlayer()).isShiftKeyDown()) {
                    getSTMCfg(tags, simpleTieredMachine);
                    context.getPlayer().displayClientMessage(Component.literal("已复制机器数据"), true);
                } else {
                    if (tags.getBoolean("Configuration")) {
                        setSTMCfg(tags, simpleTieredMachine);
                        context.getPlayer().displayClientMessage(Component.literal("已粘贴机器数据"), true);
                    } else {
                        context.getPlayer().displayClientMessage(Component.literal("未找到机器数据"), true);
                    }
                }
            } else if (metaMachine instanceof ItemBusPartMachine itemBusPartMachine) {
                if (Objects.requireNonNull(context.getPlayer()).isShiftKeyDown()) {
                    getBusCfg(tags, itemBusPartMachine);
                    context.getPlayer().displayClientMessage(Component.literal("已复制机器数据"), true);
                } else {
                    if (tags.getBoolean("Configuration")) {
                        setBusCfg(tags, itemBusPartMachine);
                        context.getPlayer().displayClientMessage(Component.literal("已粘贴机器数据"), true);
                    } else {
                        context.getPlayer().displayClientMessage(Component.literal("未找到机器数据"), true);
                    }
                }
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    private void getBusCfg(CompoundTag tags, ItemBusPartMachine metaMachine) {
        tags.putBoolean("Configuration", true);
        tags.putBoolean("isDistinct", metaMachine.isDistinct());
        tags.putBoolean("isWorkingEnabled", metaMachine.isWorkingEnabled());
        NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
        if (circuitInventory.handlerIO.support(IO.IN)) {
            int c = IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.getStackInSlot(0));
            tags.putInt("circuit", c);
        }
    }

    private void setBusCfg(CompoundTag tags, ItemBusPartMachine metaMachine) {
        metaMachine.setDistinct(tags.getBoolean("isDistinct"));
        metaMachine.setWorkingEnabled(tags.getBoolean("isWorkingEnabled"));
        int c = tags.getInt("circuit");
        NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
        if (c > 0 && circuitInventory.handlerIO.support(IO.IN)) {
            circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(c));
        }
    }

    private void getSTMCfg(CompoundTag tags, SimpleTieredMachine metaMachine) {
        tags.putBoolean("Configuration", true);
        boolean hasAutoOutputItem = metaMachine.hasAutoOutputItem();
        boolean hasAutoOutputFluid = metaMachine.hasAutoOutputFluid();
        tags.putBoolean("hasAutoOutputItem", hasAutoOutputItem);
        tags.putBoolean("hasAutoOutputFluid", hasAutoOutputFluid);
        if (hasAutoOutputItem) {
            tags.putString("outputFacingItems", metaMachine.getOutputFacingItems().toString());
        }
        if (hasAutoOutputFluid) {
            tags.putString("outputFacingFluids", metaMachine.getOutputFacingFluids().toString());
        }
        tags.putBoolean("allowInputFromOutputSideItems", metaMachine.isAllowInputFromOutputSideItems());
        tags.putBoolean("allowInputFromOutputSideFluids", metaMachine.isAllowInputFromOutputSideFluids());
        NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
        if (circuitInventory.handlerIO.support(IO.IN)) {
            int c = IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.getStackInSlot(0));
            tags.putInt("circuit", c);
        }
    }

    private void setSTMCfg(CompoundTag tags, SimpleTieredMachine metaMachine) {
        boolean hasAutoOutputItem = tags.getBoolean("hasAutoOutputItem");
        boolean hasAutoOutputFluid = tags.getBoolean("hasAutoOutputFluid");
        metaMachine.setAutoOutputItems(hasAutoOutputItem);
        metaMachine.setAutoOutputFluids(hasAutoOutputFluid);
        if (hasAutoOutputItem) {
            metaMachine.setOutputFacingItems(Direction.byName(tags.getString("outputFacingItems")));
        }
        if (hasAutoOutputFluid) {
            metaMachine.setOutputFacingFluids(Direction.byName(tags.getString("outputFacingFluids")));
        }
        metaMachine.setAllowInputFromOutputSideItems(tags.getBoolean("allowInputFromOutputSideItems"));
        metaMachine.setAllowInputFromOutputSideFluids(tags.getBoolean("allowInputFromOutputSideFluids"));
        int c = tags.getInt("circuit");
        NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
        if (c > 0 && circuitInventory.handlerIO.support(IO.IN)) {
            circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(c));
        }
    }
}
