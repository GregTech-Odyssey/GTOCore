package com.gto.gtocore.common.machine.multiblock.electric.viod;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.machine.feature.multiblock.ICheckPatternMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.utils.ServerUtils;
import com.gto.gtocore.utils.register.BlockRegisterUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class VoidTransporterMachine extends ElectricMultiblockMachine {

    private final int id;
    private final int eu;

    private boolean check = true;

    private final BiConsumer<VoidTransporterMachine, Player> consumer;

    public static boolean checkTransporter(BlockPos pos, Level level, int id) {
        return MetaMachine.getMachine(level, pos) instanceof VoidTransporterMachine machine && machine.getRecipeLogic().isWorking() && machine.id == id;
    }

    public static Function<IMachineBlockEntity, VoidTransporterMachine> create(int id, int eu, @Nullable BiConsumer<VoidTransporterMachine, Player> consumer) {
        return holder -> new VoidTransporterMachine(holder, id, eu, consumer);
    }

    public static Function<IMachineBlockEntity, VoidTransporterMachine> create(int id, int eu) {
        return create(id, eu, null);
    }

    private VoidTransporterMachine(IMachineBlockEntity holder, int id, int eu, @Nullable BiConsumer<VoidTransporterMachine, Player> consumer) {
        super(holder);
        this.id = id;
        this.eu = eu;
        this.consumer = consumer;
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

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (!isFormed() && this instanceof ICheckPatternMachine checkPatternMachine) {
            checkPatternMachine.gTOCore$setTime(0);
            check = true;
        }
        if (consumer != null && isFormed() && (eu == 0 || getRecipeLogic().isWorking())) consumer.accept(this, player);
        return false;
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (hasProxies() && eu > 0) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().EUt(eu).duration(200).buildRawRecipe();
            if (recipe.matchTickRecipe(this).isSuccess()) return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
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
}
