package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.SpaceElevatorConnectorModule;
import com.gtocore.data.IdleReason;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.misc.PlanetManagement;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.ITickSubscription;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.api.planets.PlanetApi;
import earth.terrarium.adastra.common.menus.base.PlanetsMenuProvider;
import earth.terrarium.botarium.common.menu.MenuHooks;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@DataGeneratorScanned
public class SpaceElevatorMachine extends TierCasingMultiblockMachine implements IIWirelessInteractor<SpaceElevatorConnectorModule>, ICustomRecipeLogicHolder {

    private TickableSubscription highSubscription;

    public SpaceElevatorMachine(MetaMachineBlockEntity holder) {
        super(holder, GTORecipeDataKeys.POWER_MODULE_TIER);
    }

    @Getter
    @SyncToClient
    protected double high;
    @Getter
    @SaveToDisk
    @SyncToClient
    protected int spoolCount;
    protected int moduleCount;

    @Getter
    @Setter
    SpaceElevatorConnectorModule netMachineCache;

    protected void update() {
        if (getOffsetTimer() % 80 == 0) {
            if (spoolCount < getMaxSpoolCount()) {
                forEachItems(true, (stack, amount) -> {
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
            updateModuleCount();
        }
    }

    protected void updateModuleCount() {
        moduleCount = 0;
        for (var module : modules) {
            if (module instanceof SpaceElevatorModuleMachine moduleMachine && moduleMachine.isFormed()) {
                moduleCount++;
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
    public void onStructureFormedClient() {
        super.onStructureFormedClient();
        highSubscription = subscribeClientTick(highSubscription, this::clientTick);
    }

    @Override
    public void onStructureInvalidClient() {
        super.onStructureInvalidClient();
        highSubscription = ITickSubscription.unsubscribe(highSubscription);
    }

    @OnlyIn(Dist.CLIENT)
    void clientTick() {
        if (getRecipeLogic().isWorking()) high = 12 * getBaseHigh() + 100 + ((100 + getBaseHigh()) * MathUtil.sin(getOffsetTimer() / 160.0F));
    }

    @Override
    public void onWorking() {
        super.onWorking();
        update();
        if (getRecipeLogic().getLastOriginRecipe() != null && getRecipeLogic().getProgress() > 190) {
            getRecipeLogic().setProgress(1);
            getNetMachine();
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        update();
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
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0.5, 1, 0.5), GTOGuiTextures.PLANET_TELEPORT.getSubTexture(0, 0, 1, 0.5), getRecipeLogic()::isWorking, (clickData, pressed) -> {
            if (!clickData.isRemote && getRecipeLogic().isWorking() && configuratorPanel.getGui() != null && configuratorPanel.getGui().entityPlayer instanceof ServerPlayer player) {
                PlanetManagement.unlock(player.getUUID(), GTODimensions.BARNARDA_C);
                player.addTag("spaceelevatorst");
                MenuHooks.openMenu(player, new PlanetsMenuProvider());
            }
        }).setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.space_elevator.set_out"))));
    }

    @Override
    public Class<SpaceElevatorConnectorModule> getProviderClass() {
        return SpaceElevatorConnectorModule.class;
    }

    @Override
    public boolean testMachine(SpaceElevatorConnectorModule machine) {
        return isFormed() && machine.isFormed() && machine.isWorkspaceReady() && ownerTest(machine);
    }

    private boolean ownerTest(SpaceElevatorConnectorModule module) {
        var moduleOwner = module.getOwner();
        if (moduleOwner == null) return true;
        var machineOwner = getOwner();
        if (machineOwner == null) return true;
        return moduleOwner.isPlayerInTeam(machineOwner.getPlayerUUID());
    }

    @Override
    public boolean firstTestMachine(SpaceElevatorConnectorModule machine) {
        Level level = machine.getLevel();
        if (level != null && testMachine(machine)) {
            machine.registerElevator(this, getCasingTier(GTORecipeDataKeys.POWER_MODULE_TIER));
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
        if (GTODimensions.isVoid(getLevel())) planet = PlanetApi.API.getPlanet(GTODimensions.OVERWORLD);
        if (planet == null) return null;
        Optional<ResourceKey<Level>> orbitLevel = planet.orbit();
        if (orbitLevel.isEmpty()) return null;
        MinecraftServer server = getLevel().getServer();
        if (server == null) return null;
        return server.getLevel(orbitLevel.get());
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (getTier() > GTValues.ZPM) {
            var exCWUt = netMachineCache == null ? 1.0 : 1.5;
            return getRecipeBuilder().duration(400).CWUt((int) (128 * (getTier() - GTValues.ZPM) * exCWUt)).EUt(GTValues.VA[getTier()]).build();
        } else {
            setIdleReason(IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
        }
        return null;
    }
}
