package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.common.data.GTOItems;
import com.gtocore.data.IdleReason;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.machine.feature.multiblock.IHighlightMachine;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.misc.PlanetManagement;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import earth.terrarium.adastra.common.menus.base.PlanetsMenuProvider;
import earth.terrarium.botarium.common.menu.MenuHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.gtolib.api.GTOValues.POWER_MODULE_TIER;

public class SpaceElevatorMachine extends TierCasingMultiblockMachine implements IHighlightMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SpaceElevatorMachine.class, TierCasingMultiblockMachine.MANAGED_FIELD_HOLDER);

    public SpaceElevatorMachine(MetaMachineBlockEntity holder) {
        super(holder, POWER_MODULE_TIER);
    }

    void initialize() {
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
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @DescSynced
    private double high;
    @Persisted
    @DescSynced
    private int spoolCount;
    private int moduleCount;
    @DescSynced
    final List<BlockPos> poss = new ArrayList<>();
    private ServerPlayer player;

    private void update(boolean promptly) {
        if (promptly || getOffsetTimer() % 40 == 0) {
            moduleCount = 0;
            if (spoolCount < getMaxSpoolCount()) {
                forEachInputItems(stack -> {
                    if (stack.getItem() == GTOItems.NANOTUBE_SPOOL.get()) {
                        int count = Math.min(stack.getCount(), getMaxSpoolCount() - spoolCount);
                        if (count < 1) return true;
                        spoolCount += count;
                        stack.shrink(count);
                    }
                    return false;
                });
                return;
            }
            Level level = getLevel();
            if (level == null) return;
            for (BlockPos blockPoss : poss) {
                MetaMachine metaMachine = getMachine(level, blockPoss);
                if (metaMachine instanceof SpaceElevatorModuleMachine moduleMachine && moduleMachine.isFormed()) {
                    moduleMachine.spaceElevatorMachine = this;
                    moduleCount++;
                }
            }
        }
    }

    public int getMaxSpoolCount() {
        return 256;
    }

    int getBaseHigh() {
        return 40;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        initialize();
        high = getBaseHigh();
        update(true);
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getRecipeLogic().isWorking()) high = 12 * getBaseHigh() + 100 + ((100 + getBaseHigh()) * MathUtil.sin(getOffsetTimer() / 160.0F));
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        update(false);
        if (getRecipeLogic().getProgress() > 190) {
            getRecipeLogic().setProgress(1);
        }
        return true;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (player instanceof ServerPlayer serverPlayer) {
            this.player = serverPlayer;
        }
        return super.shouldOpenUI(player, hand, hit);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        update(false);
        if (spoolCount < getMaxSpoolCount()) textList.add(Component.translatable("item.gtocore.nanotube_spool").append(": ").append(Component.translatable("gui.ae2.Missing", getMaxSpoolCount() - spoolCount)));
        textList.add(Component.translatable("gtocore.machine.module", moduleCount));
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0.5, 1, 0.5), GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0, 1, 0.5), getRecipeLogic()::isWorking, (clickData, pressed) -> {
            if (!clickData.isRemote && getRecipeLogic().isWorking() && player != null) {
                PlanetManagement.unlock(player.getUUID(), GTODimensions.BARNARDA_C);
                player.addTag("spaceelevatorst");
                MenuHooks.openMenu(player, new PlanetsMenuProvider());
            }
        }).setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.space_elevator.set_out"))));
    }

    @Nullable
    private Recipe getRecipe() {
        if (getTier() > GTValues.ZPM) {
            Recipe recipe = getRecipeBuilder().duration(400).CWUt(128 * (getTier() - GTValues.ZPM)).EUt(GTValues.VA[getTier()]).buildRawRecipe();
            if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        } else {
            setIdleReason(IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
        }
        return null;
    }

    @Override
    @NotNull
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }

    @Override
    public List<BlockPos> getHighlightPos() {
        return poss;
    }

    public double getHigh() {
        return this.high;
    }

    public int getSpoolCount() {
        return this.spoolCount;
    }
}
