package com.gtocore.common.machine.multiblock.electric.voidseries;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.EnergyContainerTrait;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.ServerUtils;
import com.gtolib.utils.register.BlockRegisterUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

import com.gto.datasynclib.annotations.SaveToDisk;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class VoidTransporterMachine extends ElectricMultiblockMachine implements ICustomRecipeLogicHolder {

    public static boolean checkTransporter(BlockPos pos, Level level, int id) {
        return !(MetaMachine.getMachine(level, pos.offset(0, -1, 0)) instanceof VoidTransporterMachine machine) || !machine.isFormed() || machine.id != id || !machine.check();
    }

    public static Function<MetaMachineBlockEntity, VoidTransporterMachine> create(int id, int eu, @Nullable BiConsumer<VoidTransporterMachine, Player> consumer) {
        return holder -> new VoidTransporterMachine(holder, id, eu, consumer);
    }

    public static Function<MetaMachineBlockEntity, VoidTransporterMachine> create(int id, int eu) {
        return create(id, eu, null);
    }

    public static BiConsumer<VoidTransporterMachine, Player> teleportToDimension(ResourceKey<Level> dim, BlockPos pos) {
        return (m, player) -> {
            Level level = m.getLevel();
            if (level == null) return;
            MinecraftServer server = level.getServer();
            if (server == null) return;
            ServerLevel serverLevel = server.getLevel(dim);
            if (serverLevel == null) return;
            CompoundTag data = player.getPersistentData();
            data.putDouble("pos_x_" + m.id, player.getX());
            data.putDouble("pos_y_" + m.id, player.getY());
            data.putDouble("pos_z_" + m.id, player.getZ());
            data.putString("dim_" + m.id, level.dimension().location().toString());
            serverLevel.setBlockAndUpdate(pos.offset(0, -1, 0), BlockRegisterUtils.REACTOR_CORE.get().defaultBlockState());
            if (!m.setup) {
                m.setup = true;
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 2; y++) {
                        for (int z = -1; z <= 1; z++) {
                            GTOUtils.fastRemoveBlock(serverLevel, pos.offset(x, y, z), true, false);
                        }
                    }
                }
                for (var x : new int[] { -1, 0, 1 }) {
                    for (var z : new int[] { -1, 0, 1 }) {
                        serverLevel.setBlockAndUpdate(pos.offset(x, 3, z), Blocks.GLASS.defaultBlockState());
                        if (x != 0 || z != 0) {
                            serverLevel.setBlockAndUpdate(pos.offset(x, -1, z), Blocks.GLASS.defaultBlockState());
                        }
                        for (var y = 0; y <= 2; y++) {
                            if (x == 0 && z == 0) {
                                continue;
                            }
                            serverLevel.setBlockAndUpdate(pos.offset(x, y, z), Blocks.GLASS_PANE.defaultBlockState());
                        }
                    }
                }
            }
            ServerUtils.teleportToDimension(serverLevel, player, pos.getCenter());
        };
    }

    private final int id;
    private final int eu;

    @SaveToDisk
    private final EnergyContainerTrait energyContainer;

    @SaveToDisk
    private boolean setup = false;
    private final BiConsumer<VoidTransporterMachine, Player> consumer;

    private VoidTransporterMachine(MetaMachineBlockEntity holder, int id, int eu, @Nullable BiConsumer<VoidTransporterMachine, Player> consumer) {
        super(holder);
        this.id = id;
        this.eu = eu;
        this.consumer = consumer;
        this.energyContainer = createEnergyContainer();
        toldNotFormed = true;
    }

    private EnergyContainerTrait createEnergyContainer() {
        return eu == 0 ? new EnergyContainerTrait(this, 0) : new EnergyContainerTrait(this, 409600);
    }

    private boolean check() {
        getRecipeLogic().updateTickSubscription();
        return energyContainer.getEnergyStored() >= 409600 && energyContainer.removeEnergy(409600) == 409600;
    }

    @Override
    public void onWorking() {
        super.onWorking();
        energyContainer.addEnergy(getOverclockVoltage());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        energyContainer.setEnergyStored(0);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (!isFormed()) {
            setWaitingTime(0);
        }
        if (consumer != null && isFormed() && (eu == 0 || check())) consumer.accept(this, player);
        return false;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (eu < getOverclockVoltage() && energyContainer.getEnergyStored() < 409600) {
            return getRecipeBuilder().EUt(getOverclockVoltage()).duration((int) Math.max(1, (409600 - energyContainer.getEnergyStored()) / getOverclockVoltage())).build();
        }
        return null;
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }
}
