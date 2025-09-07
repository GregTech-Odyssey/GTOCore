package com.gtocore.common.machine.multiblock.electric.space;

import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SuperSpaceElevatorMachine extends SpaceElevatorMachine {

    @DescSynced
    final List<BlockPos> megaPoss = new ArrayList<>();
    @DescSynced
    BlockPos center;
    private int megaModuleCount;
    private final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SuperSpaceElevatorMachine.class, SpaceElevatorMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public SuperSpaceElevatorMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    void initialize() {
        poss.clear();
        megaPoss.clear();
        BlockPos blockPos = MachineUtils.getOffsetPos(3, -2, getFrontFacing(), getPos()).offset(0, 2, -3);
        BlockPos center = blockPos.offset(0, 2, 120);
        this.center = center;
        List<BlockPos> posA = getPossCornerA(blockPos);
        List<BlockPos> posB = new ArrayList<>(16); // x轴镜像
        List<BlockPos> posC = new ArrayList<>(16); // z轴镜像
        List<BlockPos> posD = new ArrayList<>(16); // 中心对称
        for (BlockPos p : posA) {
            posB.add(mirrorX(p, center));
            posC.add(mirrorZ(p, center));
            posD.add(centerSymmetry(p, center));
        }
        poss.addAll(posA);
        poss.addAll(posB);
        poss.addAll(posC);
        poss.addAll(posD);
        List<BlockPos> megaPosA = getMegaPossCornerA(blockPos);
        List<BlockPos> megaPosB = new ArrayList<>(16); // x轴镜像
        List<BlockPos> megaPosC = new ArrayList<>(16); // z轴镜像
        List<BlockPos> megaPosD = new ArrayList<>(16); // 中心对称
        for (BlockPos p : megaPosA) {
            megaPosB.add(mirrorX(p, center));
            megaPosC.add(mirrorZ(p, center));
            megaPosD.add(centerSymmetry(p, center));
        }
        megaPoss.addAll(megaPosA);
        megaPoss.addAll(megaPosB);
        megaPoss.addAll(megaPosC);
        megaPoss.addAll(megaPosD);

        requestSync();
    }

    @Override
    protected void update(boolean promptly) {
        super.update(promptly);
        if (promptly || getOffsetTimer() % 40 == 0) {
            megaModuleCount = 0;
            Level level = getLevel();
            if (level == null) return;
            for (BlockPos blockPoss : megaPoss) {
                MetaMachine metaMachine = getMachine(level, blockPoss);
                if (metaMachine instanceof MegaSpaceElevatorModuleMachine moduleMachine && moduleMachine.isFormed()) {
                    moduleMachine.spaceElevatorMachine = this;
                    moduleCount++;
                    megaModuleCount++;
                }
            }
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.module.base", moduleCount - megaModuleCount));
        textList.add(Component.translatable("gtocore.machine.module.mega", megaModuleCount));
    }

    private static @NotNull List<BlockPos> getMegaPossCornerA(BlockPos blockPos) {
        List<BlockPos> posA = new ArrayList<>(16);
        posA.add(blockPos.offset(-17, 2, 28));
        posA.add(blockPos.offset(-25, 2, 28));
        posA.add(blockPos.offset(-33, 2, 28));
        posA.add(blockPos.offset(-41, 2, 28));
        posA.add(blockPos.offset(-49, 2, 45));
        posA.add(blockPos.offset(-57, 2, 45));
        posA.add(blockPos.offset(-75, 2, 63));
        posA.add(blockPos.offset(-75, 2, 71));
        posA.add(blockPos.offset(-92, 2, 79));
        posA.add(blockPos.offset(-92, 2, 87));
        posA.add(blockPos.offset(-92, 2, 95));
        posA.add(blockPos.offset(-92, 2, 103));
        posA.add(blockPos.offset(-19, 2, 54));
        posA.add(blockPos.offset(-29, 2, 58));
        posA.add(blockPos.offset(-62, 2, 91));
        posA.add(blockPos.offset(-66, 2, 101));
        return posA;
    }

    private static @NotNull List<BlockPos> getPossCornerA(BlockPos blockPos) {
        List<BlockPos> posA = new ArrayList<>(16);
        posA.add(blockPos.offset(-42, 2, 72));
        posA.add(blockPos.offset(-42, 2, 65));
        posA.add(blockPos.offset(-40, 2, 72));
        posA.add(blockPos.offset(-40, 2, 65));
        posA.add(blockPos.offset(-38, 2, 72));
        posA.add(blockPos.offset(-38, 2, 65));
        posA.add(blockPos.offset(-36, 2, 72));
        posA.add(blockPos.offset(-36, 2, 65));
        posA.add(blockPos.offset(-48, 2, 78));
        posA.add(blockPos.offset(-55, 2, 78));
        posA.add(blockPos.offset(-55, 2, 80));
        posA.add(blockPos.offset(-55, 2, 84));
        posA.add(blockPos.offset(-48, 2, 80));
        posA.add(blockPos.offset(-48, 2, 82));
        posA.add(blockPos.offset(-55, 2, 82));
        posA.add(blockPos.offset(-48, 2, 84));
        return posA;
    }

    @Override
    public List<BlockPos> getHighlightPos() {
        var l = super.getHighlightPos();
        // if (this.center != null) {
        // l.add(this.center);
        // }
        l.addAll(megaPoss);
        return l;
    }

    /**
     * 按center的x轴镜像
     */
    private BlockPos mirrorX(BlockPos pos, BlockPos center) {
        int dx = center.getX() - (pos.getX() - center.getX());
        return new BlockPos(dx, pos.getY(), pos.getZ());
    }

    /**
     * 按center的z轴镜像
     */
    private BlockPos mirrorZ(BlockPos pos, BlockPos center) {
        int dz = center.getZ() - (pos.getZ() - center.getZ());
        return new BlockPos(pos.getX(), pos.getY(), dz);
    }

    /**
     * 按center点中心对称
     */
    private BlockPos centerSymmetry(BlockPos pos, BlockPos center) {
        int sx = 2 * center.getX() - pos.getX();
        int sy = 2 * center.getY() - pos.getY();
        int sz = 2 * center.getZ() - pos.getZ();
        return new BlockPos(sx, sy, sz);
    }

    @Override
    public int getHighlightMilliseconds() {
        return 1000 * 45;
    }

    @Override
    public int getMaxSpoolCount() {
        return 1024;
    }

    @Override
    int getBaseHigh() {
        return 80;
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getRecipeLogic().isWorking()) high = 8 * getBaseHigh() + 400 + ((400 + getBaseHigh()) * MathUtil.sin(getOffsetTimer() / 160.0F));
    }
}
