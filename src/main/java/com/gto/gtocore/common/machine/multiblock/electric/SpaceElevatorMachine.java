package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.TierCasingMachine;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import earth.terrarium.adastra.common.menus.base.PlanetsMenuProvider;
import earth.terrarium.botarium.common.menu.MenuHooks;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpaceElevatorMachine extends TierCasingMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SpaceElevatorMachine.class, TierCasingMachine.MANAGED_FIELD_HOLDER);

    public SpaceElevatorMachine(IMachineBlockEntity holder) {
        super(holder, "power_module_tier");
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Getter
    @Persisted
    @DescSynced
    private double high;
    int mam = 0;

    private ServerPlayer player;

    void update(boolean promptly) {
        if (promptly || getOffsetTimer() % 20 == 0) {
            mam = 0;
            Level level = getLevel();
            if (level == null) return;
            BlockPos blockPos = MachineUtil.getOffsetPos(3, -2, getFrontFacing(), getPos());
            BlockPos[] coordinatess = new BlockPos[] {
                    blockPos.offset(7, 2, 0),
                    blockPos.offset(7, 2, 2),
                    blockPos.offset(7, 2, -2),
                    blockPos.offset(-7, 2, 0),
                    blockPos.offset(-7, 2, 2),
                    blockPos.offset(-7, 2, -2),
                    blockPos.offset(0, 2, 7),
                    blockPos.offset(2, 2, 7),
                    blockPos.offset(-2, 2, 7),
                    blockPos.offset(0, 2, -7),
                    blockPos.offset(2, 2, -7),
                    blockPos.offset(-2, 2, -7) };
            for (BlockPos blockPoss : coordinatess) {
                MetaMachine metaMachine = MetaMachine.getMachine(level, blockPoss);
                if (metaMachine instanceof SpaceElevatorModuleMachine moduleMachine && moduleMachine.isFormed()) {
                    moduleMachine.spaceElevatorMachine = this;
                    mam++;
                }
            }
        }
    }

    int getBaseHigh() {
        return 40;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        high = getBaseHigh();
        update(true);
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        update(false);
        high = 12 * getBaseHigh() + 100 + ((100 + getBaseHigh()) * Math.sin(getOffsetTimer() / 160D));
        return value;
    }

    @Override
    public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                   BlockHitResult hit) {
        if (player instanceof ServerPlayer serverPlayer) {
            this.player = serverPlayer;
        }
        return super.onUse(state, level, pos, player, hand, hit);
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        update(false);
        textList.add(Component.translatable("gtocore.machine.module", mam));
        textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.space_elevator.set_out"), "set_out"));
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (componentData.equals("set_out") && getRecipeLogic().isWorking() && player != null) {
            player.addTag("spaceelevatorst");
            MenuHooks.openMenu(player, new PlanetsMenuProvider());
        }
    }
}
