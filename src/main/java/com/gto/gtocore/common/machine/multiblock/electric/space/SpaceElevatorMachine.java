package com.gto.gtocore.common.machine.multiblock.electric.space;

import com.gto.gtocore.api.gui.GTOGuiTextures;
import com.gto.gtocore.api.machine.feature.multiblock.IHighlightMachine;
import com.gto.gtocore.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import earth.terrarium.adastra.common.menus.base.PlanetsMenuProvider;
import earth.terrarium.botarium.common.menu.MenuHooks;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static com.gto.gtocore.api.GTOValues.POWER_MODULE_TIER;

public class SpaceElevatorMachine extends TierCasingMultiblockMachine implements IHighlightMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SpaceElevatorMachine.class, TierCasingMultiblockMachine.MANAGED_FIELD_HOLDER);

    public SpaceElevatorMachine(IMachineBlockEntity holder) {
        super(holder, POWER_MODULE_TIER);
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
    private int mam;
    @DescSynced
    final Set<BlockPos> poss = new ObjectOpenHashSet<>();

    private ServerPlayer player;

    private void update(boolean promptly) {
        if (promptly || getOffsetTimer() % 40 == 0) {
            mam = 0;
            Level level = getLevel();
            if (level == null) return;
            for (BlockPos blockPoss : poss) {
                MetaMachine metaMachine = getMachine(level, blockPoss);
                if (metaMachine instanceof SpaceElevatorModuleMachine moduleMachine && moduleMachine.isFormed()) {
                    moduleMachine.spaceElevatorMachine = this;
                    mam++;
                }
            }
        }
    }

    int getBaseHigh() {
        poss.clear();
        BlockPos blockPos = MachineUtils.getOffsetPos(3, -2, getFrontFacing(), getPos());
        poss.add(blockPos.offset(7, 2, 0));
        poss.add(blockPos.offset(7, 2, 2));
        poss.add(blockPos.offset(7, 2, -2));
        poss.add(blockPos.offset(-7, 2, 0));
        poss.add(blockPos.offset(-7, 2, 2));
        poss.add(blockPos.offset(-7, 2, -2));
        poss.add(blockPos.offset(0, 2, 7));
        poss.add(blockPos.offset(2, 2, 7));
        poss.add(blockPos.offset(-2, 2, 7));
        poss.add(blockPos.offset(0, 2, -7));
        poss.add(blockPos.offset(2, 2, -7));
        poss.add(blockPos.offset(-2, 2, -7));
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
        if (!super.onWorking()) return false;
        update(false);
        high = 12 * getBaseHigh() + 100 + ((100 + getBaseHigh()) * Math.sin(getOffsetTimer() / 160.0D));
        return true;
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
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        update(false);
        textList.add(Component.translatable("gtocore.machine.module", mam));
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0.5, 1, 0.5),
                GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0, 1, 0.5),
                getRecipeLogic()::isWorking, (clickData, pressed) -> {
                    if (getRecipeLogic().isWorking() && player != null) {
                        player.addTag("spaceelevatorst");
                        MenuHooks.openMenu(player, new PlanetsMenuProvider());
                    }
                })
                .setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.space_elevator.set_out"))));
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (hasProxies()) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(400).CWUt(128 * (getTier() - GTValues.ZPM)).EUt(GTValues.VA[getTier()]).buildRawRecipe();
            if (RecipeRunner.matchRecipeTickInput(this, recipe)) return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }

    @Override
    public Set<BlockPos> getHighlightPos() {
        return poss;
    }
}
