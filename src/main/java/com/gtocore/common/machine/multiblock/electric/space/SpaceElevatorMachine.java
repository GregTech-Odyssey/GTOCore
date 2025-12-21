package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.SpaceElevatorConnectorModule;
import com.gtocore.data.IdleReason;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.data.GTODimensions;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.api.planets.PlanetApi;
import earth.terrarium.adastra.common.menus.base.PlanetsMenuProvider;
import earth.terrarium.botarium.common.menu.MenuHooks;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gtolib.api.GTOValues.POWER_MODULE_TIER;

@DataGeneratorScanned
public class SpaceElevatorMachine extends TierCasingMultiblockMachine implements IHighlightMachine, IIWirelessInteractor<SpaceElevatorConnectorModule> {

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

    @Getter
    @DescSynced
    protected double high;
    @Getter
    @Persisted
    @DescSynced
    protected int spoolCount;
    protected int moduleCount;
    @DescSynced
    final List<BlockPos> poss = new ArrayList<>();

    @Getter
    @Setter
    SpaceElevatorConnectorModule netMachineCache;

    protected void update(boolean promptly) {
        if (promptly || getOffsetTimer() % 80 == 0) {
            moduleCount = 0;
            if (spoolCount < getMaxSpoolCount()) {
                forEachInputItems((stack, amount) -> {
                    if (stack.getItem() == GTOItems.NANOTUBE_SPOOL.get()) {
                        int count = Math.min(stack.getCount(), getMaxSpoolCount() - spoolCount);
                        if (count < 1) return true;
                        spoolCount += count;
                        inputItem(stack.getItem(), count);
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
                    if (moduleMachine.spaceElevatorMachine != this) moduleMachine.getRecipeLogic().updateTickSubscription();
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
    protected void onStructureFormedAfter() {
        super.onStructureFormedAfter();
        update(true);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        initialize();
        high = getBaseHigh();
        if (!isRemote()) {
            getNetMachine();
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        removeNetMachineCache();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        removeNetMachineCache();
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
            getNetMachine();
        }
        return true;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        update(false);
        if (spoolCount < getMaxSpoolCount()) textList.add(Component.translatable("item.gtocore.nanotube_spool").append(": ").append(Component.translatable("gui.ae2.Missing", getMaxSpoolCount() - spoolCount)));
        textList.add(Component.translatable("gtocore.machine.module", moduleCount));
        if (netMachineCache != null) {
            textList.add(Component.translatable(SpaceElevatorConnectorModule.SPACE_ELEVATOR_CONNECTED_CURRENT_PLANET_TEXT, Math.sqrt(netMachineCache.getDurationMultiplier())));
        } else {
            textList.add(Component.translatable(SpaceElevatorConnectorModule.SPACE_ELEVATOR_NOT_CONNECTED_CURRENT_PLANET_TEXT));
        }
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0.5, 1, 0.5), GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0, 1, 0.5), getRecipeLogic()::isWorking, (clickData, pressed) -> {
            if (!clickData.isRemote && getRecipeLogic().isWorking() && configuratorPanel.getGui() != null && configuratorPanel.getGui().entityPlayer instanceof ServerPlayer player) {
                PlanetManagement.unlock(player.getUUID(), GTODimensions.BARNARDA_C);
                player.addTag("spaceelevatorst");
                MenuHooks.openMenu(player, new PlanetsMenuProvider());
            }
        }).setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.space_elevator.set_out"))));
    }

    @Nullable
    private Recipe getRecipe() {
        if (getTier() > GTValues.ZPM) {
            var exCWUt = netMachineCache == null ? 1.0 : 1.5;
            Recipe recipe = getRecipeBuilder().duration(400).CWUt((int) (128 * (getTier() - GTValues.ZPM) * exCWUt)).EUt(GTValues.VA[getTier()]).buildRawRecipe();
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

    @Override
    public Class<SpaceElevatorConnectorModule> getProviderClass() {
        return SpaceElevatorConnectorModule.class;
    }

    @Override
    public boolean testMachine(SpaceElevatorConnectorModule machine) {
        return isFormed() && machine.isFormed() && machine.isWorkspaceReady();
    }

    @Override
    public boolean firstTestMachine(SpaceElevatorConnectorModule machine) {
        Level level = machine.getLevel();
        if (level != null && testMachine(machine)) {
            machine.registerElevator(this, getCasingTier(POWER_MODULE_TIER));
            return true;
        }
        return false;
    }

    @Override
    public void removeNetMachineCache() {
        if (netMachineCache != null) {
            netMachineCache.unregisterElevator(this);
            netMachineCache = null;
        }
    }

    @Override
    public Level getTargetLevel() {
        if (isRemote() || getLevel() == null) return null;
        Planet planet = PlanetApi.API.getPlanet(getLevel());
        if (planet == null) return null;
        Optional<ResourceKey<Level>> orbitLevel = planet.orbit();
        if (orbitLevel.isEmpty()) return null;
        MinecraftServer server = getLevel().getServer();
        if (server == null) return null;
        return server.getLevel(orbitLevel.get());
    }
}
