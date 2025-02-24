package com.gto.gtocore.api.registries;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.blockentity.ManaMachineBlockEntity;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class GTORegistration extends GTRegistrate {

    public static final GTORegistration REGISTRATE = new GTORegistration();

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private GTORegistration() {
        super(GTOCore.MOD_ID);
    }

    public GTOMachineBuilder manaMachine(String name, Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return new GTOMachineBuilder(this, name, MachineDefinition::createDefinition, metaMachine, MetaMachineBlock::new, MetaMachineItem::new, ManaMachineBlockEntity::createBlockEntity);
    }

    @Override
    public @NotNull GTOMachineBuilder machine(@NotNull String name, @NotNull Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return new GTOMachineBuilder(this, name, MachineDefinition::createDefinition, metaMachine, MetaMachineBlock::new, MetaMachineItem::new, MetaMachineBlockEntity::createBlockEntity);
    }

    @Override
    public @NotNull MultiblockBuilder multiblock(@NotNull String name, @NotNull Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return new MultiblockBuilder(this, name, metaMachine, MetaMachineBlock::new, MetaMachineItem::new, MetaMachineBlockEntity::createBlockEntity);
    }
}
