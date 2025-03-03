package com.gto.gtocore.common.machine.multiblock.electric.voidseries;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.machine.feature.multiblock.ICheckPatternMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.utils.ServerUtils;
import com.gto.gtocore.utils.register.BlockRegisterUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class VoidTransporterMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            VoidTransporterMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static boolean checkTransporter(BlockPos pos, Level level, int id) {
        return !(MetaMachine.getMachine(level, pos) instanceof VoidTransporterMachine machine) || machine.id != id || !machine.check();
    }

    public static Function<IMachineBlockEntity, VoidTransporterMachine> create(int id, int eu, @Nullable BiConsumer<VoidTransporterMachine, Player> consumer) {
        return holder -> new VoidTransporterMachine(holder, id, eu, consumer);
    }

    public static Function<IMachineBlockEntity, VoidTransporterMachine> create(int id, int eu) {
        return create(id, eu, null);
    }

    public static BiConsumer<VoidTransporterMachine, Player> teleportToDimension(ResourceLocation dim, BlockPos pos) {
        return (m, player) -> {
            Level level = m.getLevel();
            if (level == null) return;
            MinecraftServer server = level.getServer();
            if (server == null) return;
            ServerLevel serverLevel = server.getLevel(GTODimensions.getDimensionKey(dim));
            if (serverLevel == null) return;
            CompoundTag data = player.getPersistentData();
            data.putDouble("pos_x_" + m.id, player.getX());
            data.putDouble("pos_y_" + m.id, player.getY());
            data.putDouble("pos_z_" + m.id, player.getZ());
            data.putString("dim_" + m.id, level.dimension().location().toString());
            serverLevel.setBlockAndUpdate(pos.offset(0, -1, 0), BlockRegisterUtils.REACTOR_CORE.get().defaultBlockState());
            ServerUtils.teleportToDimension(serverLevel, player, pos.getCenter());
        };
    }

    private final int id;
    private final int eu;

    private boolean check = true;

    @Persisted
    private final NotifiableEnergyContainer energyContainer;

    private final BiConsumer<VoidTransporterMachine, Player> consumer;

    private VoidTransporterMachine(IMachineBlockEntity holder, int id, int eu, @Nullable BiConsumer<VoidTransporterMachine, Player> consumer) {
        super(holder);
        this.id = id;
        this.eu = eu;
        this.consumer = consumer;
        this.energyContainer = createEnergyContainer();
    }

    private NotifiableEnergyContainer createEnergyContainer() {
        var container = eu == 0 ? new NotifiableEnergyContainer(this, 0, 0, 0, 0, 0) : new NotifiableEnergyContainer(this, 409600, 2048, 2, 409600, 1);
        container.setCapabilityValidator(Objects::isNull);
        return container;
    }

    private boolean check() {
        return energyContainer.getEnergyStored() >= 409600 && energyContainer.removeEnergy(409600) == 409600;
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            if (energyContainer.getEnergyStored() >= 409600) return false;
            energyContainer.addEnergy(getOverclockVoltage());
            return true;
        }
        return false;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        energyContainer.setEnergyStored(0);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (!isFormed() && this instanceof ICheckPatternMachine checkPatternMachine) {
            checkPatternMachine.gTOCore$setTime(0);
            check = true;
        }
        if (consumer != null && isFormed() && (eu == 0 || check())) consumer.accept(this, player);
        return false;
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (hasProxies() && eu < getOverclockVoltage() && energyContainer.getEnergyStored() < 409600) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().EUt(getOverclockVoltage()).duration(200).buildRawRecipe();
            if (recipe.matchTickRecipe(this).isSuccess()) return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public boolean checkPattern() {
        if (check) if (super.checkPattern()) {
            return true;
        } else {
            check = false;
        }
        return false;
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
