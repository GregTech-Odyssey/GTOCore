package com.gtocore.common.machine.multiblock.electric.space;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.utils.DummyWorld;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SuperSpaceElevatorMachine extends SpaceElevatorMachine {

    @DescSynced
    final List<BlockPos> megaPoss = new ArrayList<>();
    @DescSynced
    BlockPos center;
    private int megaModuleCount;

    public SuperSpaceElevatorMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    void initialize() {
        poss.clear();
        megaPoss.clear();
        BlockPos blockPos = getPos();
        BlockPos center = blockPos.above(2).relative(getFrontFacing(), -120);
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
        var centerBelow = center.below(2);
        for (BlockPos p : megaPosA) {
            megaPosB.add(mirrorX(p, centerBelow));
            megaPosC.add(mirrorZ(p, centerBelow));
            megaPosD.add(centerSymmetry(p, centerBelow));
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

    private @NotNull List<BlockPos> getMegaPossCornerA(BlockPos blockPos) {
        List<BlockPos> posA = new ArrayList<>(16);
        Direction back = getFrontFacing().getOpposite();
        Direction right = getFrontFacing().getClockWise();
        posA.add(blockPos.relative(back, 28).relative(right, 17));
        posA.add(blockPos.relative(back, 28).relative(right, 25));
        posA.add(blockPos.relative(back, 28).relative(right, 33));
        posA.add(blockPos.relative(back, 28).relative(right, 41));
        posA.add(blockPos.relative(back, 45).relative(right, 49));
        posA.add(blockPos.relative(back, 45).relative(right, 57));
        posA.add(blockPos.relative(back, 63).relative(right, 75));
        posA.add(blockPos.relative(back, 71).relative(right, 75));
        posA.add(blockPos.relative(back, 79).relative(right, 92));
        posA.add(blockPos.relative(back, 87).relative(right, 92));
        posA.add(blockPos.relative(back, 95).relative(right, 92));
        posA.add(blockPos.relative(back, 103).relative(right, 92));
        posA.add(blockPos.relative(back, 54).relative(right, 19));
        posA.add(blockPos.relative(back, 58).relative(right, 29));
        posA.add(blockPos.relative(back, 91).relative(right, 62));
        posA.add(blockPos.relative(back, 101).relative(right, 66));
        return posA;
    }

    private @NotNull List<BlockPos> getPossCornerA(BlockPos blockPos0) {
        List<BlockPos> posA = new ArrayList<>(16);
        Direction back = getFrontFacing().getOpposite();
        Direction right = getFrontFacing().getClockWise();
        BlockPos blockPos = blockPos0.above(2);
        posA.add(blockPos.relative(back, 72).relative(right, 42));
        posA.add(blockPos.relative(back, 65).relative(right, 42));
        posA.add(blockPos.relative(back, 72).relative(right, 40));
        posA.add(blockPos.relative(back, 65).relative(right, 40));
        posA.add(blockPos.relative(back, 72).relative(right, 38));
        posA.add(blockPos.relative(back, 65).relative(right, 38));
        posA.add(blockPos.relative(back, 72).relative(right, 36));
        posA.add(blockPos.relative(back, 65).relative(right, 36));
        posA.add(blockPos.relative(back, 78).relative(right, 48));
        posA.add(blockPos.relative(back, 78).relative(right, 55));
        posA.add(blockPos.relative(back, 80).relative(right, 55));
        posA.add(blockPos.relative(back, 84).relative(right, 55));
        posA.add(blockPos.relative(back, 80).relative(right, 48));
        posA.add(blockPos.relative(back, 82).relative(right, 48));
        posA.add(blockPos.relative(back, 82).relative(right, 55));
        posA.add(blockPos.relative(back, 84).relative(right, 48));
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

    /**
     * 按center点逆时针旋转90度
     */
    private BlockPos rotate90(BlockPos pos, BlockPos center) {
        int dx = pos.getX() - center.getX();
        int dz = pos.getZ() - center.getZ();
        return new BlockPos(center.getX() - dz, pos.getY(), center.getZ() + dx);
    }

    /**
     * 按center点顺时针旋转90度
     */
    private BlockPos rotate270(BlockPos pos, BlockPos center) {
        int dx = pos.getX() - center.getX();
        int dz = pos.getZ() - center.getZ();
        return new BlockPos(center.getX() + dz, pos.getY(), center.getZ() - dx);
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
        return getLevel() instanceof DummyWorld ? 300 : 80;
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getRecipeLogic().isWorking()) high = 8 * getBaseHigh() + 400 + ((400 + getBaseHigh()) * MathUtil.sin(getOffsetTimer() / 160.0F));
    }
}
