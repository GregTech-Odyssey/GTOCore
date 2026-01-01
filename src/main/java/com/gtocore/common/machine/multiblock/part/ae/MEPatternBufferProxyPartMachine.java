package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.machine.trait.ProxySlotRecipeHandler;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IWailaDisplayProvider;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.WorkableTieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.client.util.TooltipHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine.readBufferTag;
import static com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine.writeBufferTag;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class MEPatternBufferProxyPartMachine extends WorkableTieredIOPartMachine implements IMachineLife, IDataStickInteractable, IWailaDisplayProvider {

    private ProxySlotRecipeHandler proxySlotRecipeHandler = ProxySlotRecipeHandler.DEFAULT;
    @Persisted
    @DescSynced
    @Nullable
    private BlockPos bufferPos;
    @Nullable
    private MEPatternBufferPartMachine buffer = null;
    private boolean bufferResolved = false;

    public MEPatternBufferProxyPartMachine(MetaMachineBlockEntity holder) {
        super(holder, GTValues.LuV, IO.IN);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel level) {
            level.getServer().tell(new TickTask(0, () -> this.setBuffer(bufferPos)));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        var buf = getBuffer();
        if (buf != null) {
            buf.removeProxy(this);
            proxySlotRecipeHandler = ProxySlotRecipeHandler.DEFAULT;
            bufferResolved = false;
        }
    }

    @Override
    public List<RecipeHandlerList> getRecipeHandlers() {
        return proxySlotRecipeHandler.getProxySlotHandlers();
    }

    public void setBuffer(@Nullable BlockPos pos) {
        bufferResolved = true;
        var level = getLevel();
        if (level == null || pos == null) {
            buffer = null;
        } else if (MetaMachine.getMachine(level, pos) instanceof MEPatternBufferPartMachine machine) {
            proxySlotRecipeHandler = new ProxySlotRecipeHandler(this, machine);
            bufferPos = pos;
            buffer = machine;
            machine.addProxy(this);
            if (!isRemote()) {
                proxySlotRecipeHandler.updateProxy(machine);
                for (var controller : getControllers()) {
                    controller.requestCheck();
                }
            }
        } else {
            buffer = null;
        }
    }

    @Nullable
    public MEPatternBufferPartMachine getBuffer() {
        if (!bufferResolved) setBuffer(bufferPos);
        return buffer;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return getBuffer() != null;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        assert getBuffer() != null;
        return getBuffer().createUI(entityPlayer);
    }

    @Override
    public void onMachineRemoved() {
        var buf = getBuffer();
        if (buf != null) {
            buf.removeProxy(this);
            proxySlotRecipeHandler = ProxySlotRecipeHandler.DEFAULT;
        }
    }

    @Override
    public InteractionResult onDataStickUse(Player player, ItemStack dataStick) {
        if (dataStick.hasTag()) {
            assert dataStick.getTag() != null;
            if (dataStick.getTag().contains("pos", Tag.TAG_INT_ARRAY)) {
                var posArray = dataStick.getOrCreateTag().getIntArray("pos");
                var bufferPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
                setBuffer(bufferPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendWailaTooltip(CompoundTag data, ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!data.getBoolean("formed")) return;
        if (!data.getBoolean("bound")) {
            iTooltip.add(Component.translatable("gtceu.top.buffer_not_bound").withStyle(ChatFormatting.RED));
            return;
        }

        int[] pos = data.getIntArray("pos");
        iTooltip.add(Component.translatable("gtceu.top.buffer_bound_pos", pos[0], pos[1], pos[2])
                .withStyle(TooltipHelper.RAINBOW_HSL_SLOW));

        readBufferTag(iTooltip, data);
    }

    @Override
    public void appendWailaData(CompoundTag data, BlockAccessor blockAccessor) {
        if (!isFormed()) {
            data.putBoolean("formed", false);
            return;
        }
        data.putBoolean("formed", true);
        var buffer = getBuffer();
        if (buffer == null) {
            data.putBoolean("bound", false);
            return;
        }
        data.putBoolean("bound", true);

        var pos = buffer.getPos();
        data.putIntArray("pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });
        writeBufferTag(data, buffer);
    }
}
