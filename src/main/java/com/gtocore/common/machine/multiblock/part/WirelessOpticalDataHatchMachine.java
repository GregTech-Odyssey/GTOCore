package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IDataAccessHatch;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.machine.multiblock.part.OpticalDataHatchMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.capability.IGTMTJadeIF;
import com.hepdd.gtmthings.api.misc.CleanableReferenceSupplier;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class WirelessOpticalDataHatchMachine extends OpticalDataHatchMachine implements IInteractedMachine, IGTMTJadeIF {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            WirelessOpticalDataHatchMachine.class, OpticalDataHatchMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private BlockPos transmitterPos;
    @Persisted
    private BlockPos receiverPos;

    private final CleanableReferenceSupplier<MetaMachine> transmitterMachine = new CleanableReferenceSupplier<>(() -> MetaMachine.getMachine(getLevel(), transmitterPos), MetaMachine::isInValid);

    public WirelessOpticalDataHatchMachine(MetaMachineBlockEntity holder, boolean transmitter) {
        super(holder, transmitter);
    }

    @Override
    protected @Nullable IDataAccessHatch getAccessHatch() {
        Level level = getLevel();
        if (level == null || transmitterPos == null) return null;
        if (transmitterMachine.get() instanceof WirelessOpticalDataHatchMachine machine) {
            return machine;
        }
        return null;
    }

    private static CompoundTag createPos(BlockPos pos) {
        CompoundTag posTag = new CompoundTag();
        posTag.putInt("x", pos.getX());
        posTag.putInt("y", pos.getY());
        posTag.putInt("z", pos.getZ());
        return posTag;
    }

    private static BlockPos getPos(CompoundTag tag) {
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    @Override
    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack is = player.getItemInHand(hand);
        if (is.isEmpty()) return InteractionResult.PASS;
        if (is.is(GTItems.TOOL_DATA_STICK.asItem())) {
            Level level = getLevel();
            if (level == null) return InteractionResult.PASS;
            if (isTransmitter()) {
                var tag = is.getTag();
                if (transmitterPos == null) transmitterPos = pos;
                if (tag != null) {
                    tag.put("transmitterPos", createPos(pos));
                    var bindPos = (CompoundTag) tag.get("receiverPos");
                    if (bindPos != null) {
                        BlockPos recPos = getPos(bindPos);
                        if (getMachine(level, recPos) instanceof WirelessOpticalDataHatchMachine wod && !wod.isTransmitter()) {
                            wod.setTransmitterPos(transmitterPos);
                            receiverPos = pos;
                            tag.remove("transmitterPos");
                            tag.remove("receiverPos");
                            if (level.isClientSide()) {
                                player.sendSystemMessage(Component.translatable("gtocore.machine.wireless_data_hatch.bind"));
                            }
                        }
                    } else {
                        if (level.isClientSide()) {
                            player.sendSystemMessage(Component.translatable("gtocore.machine.wireless_data_transmitter_hatch.to_bind"));
                        }
                    }
                    is.setTag(tag);
                } else {
                    tag = new CompoundTag();
                    tag.put("transmitterPos", createPos(transmitterPos));
                    is.setTag(tag);
                    if (level.isClientSide()) {
                        player.sendSystemMessage(Component.translatable("gtocore.machine.wireless_data_transmitter_hatch.to_bind"));
                    }
                }
            } else {
                if (receiverPos == null) receiverPos = pos;
                var tag = is.getTag();
                if (tag != null) {
                    tag.put("receiverPos", createPos(pos));
                    var bindPos = (CompoundTag) tag.get("transmitterPos");
                    if (bindPos != null) {
                        BlockPos tranPos = new BlockPos(bindPos.getInt("x"), bindPos.getInt("y"), bindPos.getInt("z"));
                        if (getMachine(level, tranPos) instanceof WirelessOpticalDataHatchMachine wod && wod.isTransmitter()) {
                            wod.setReceiverPos(receiverPos);
                            transmitterPos = tranPos;
                            tag.remove("transmitterPos");
                            tag.remove("receiverPos");
                            if (level.isClientSide()) {
                                player.sendSystemMessage(Component.translatable("gtocore.machine.wireless_data_hatch.bind"));
                            }
                        }
                    } else {
                        if (level.isClientSide()) {
                            player.sendSystemMessage(Component.translatable("gtocore.machine.wireless_data_receiver_hatch.to_bind"));
                        }
                    }
                    is.setTag(tag);
                } else {
                    tag = new CompoundTag();
                    tag.put("receiverPos", createPos(receiverPos));
                    is.setTag(tag);
                    if (level.isClientSide()) {
                        player.sendSystemMessage(Component.translatable("gtocore.machine.wireless_data_receiver_hatch.to_bind"));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void setTransmitterPos(BlockPos pos) {
        if (transmitterPos != null) {
            var level = getLevel();
            if (level != null) {
                if (MetaMachine.getMachine(level, transmitterPos) instanceof WirelessOpticalDataHatchMachine machine) {
                    machine.receiverPos = null;
                }
            }
        }
        transmitterPos = pos;
    }

    private void setReceiverPos(BlockPos pos) {
        if (receiverPos != null) {
            var level = getLevel();
            if (level != null) {
                if (MetaMachine.getMachine(level, receiverPos) instanceof WirelessOpticalDataHatchMachine machine) {
                    machine.transmitterPos = null;
                }
            }
        }
        receiverPos = pos;
    }

    @Override
    public boolean isbinded() {
        return transmitterPos != null || receiverPos != null;
    }

    @Override
    public String getBindPos() {
        if (isTransmitter() && receiverPos != null) {
            return receiverPos.toShortString();
        } else if (!isTransmitter() && transmitterPos != null) {
            return transmitterPos.toShortString();
        }
        return "";
    }
}
