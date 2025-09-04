package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.data.Dimension;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import earth.terrarium.adastra.common.registry.ModFluids;
import earth.terrarium.adastra.common.registry.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DataGeneratorScanned
public final class SatelliteControlCenterMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SatelliteControlCenterMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @RegisterLanguage(en = "Selected planet: ", cn = "已选择的星球：")
    private static final String PLANET = "gtocore.satellite_control_center.planet";

    @RegisterLanguage(en = "The required rocket: ", cn = "需要的火箭：")
    private static final String ROCKET = "gtocore.satellite_control_center.rocket";

    @RegisterLanguage(en = "The required fuel: ", cn = "需要的燃料：")
    private static final String FUEL = "gtocore.satellite_control_center.fuel";

    private boolean launch;

    @Persisted
    private int index;

    public SatelliteControlCenterMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        var buttonText = Component.translatable(PLANET).append(Component.translatable(Wrapper.LIST[index].getKey()));
        buttonText.append(" ");
        buttonText.append(ComponentPanelWidget.withButton(Component.literal("[-]"), "sub"));
        buttonText.append(" ");
        buttonText.append(ComponentPanelWidget.withButton(Component.literal("[+]"), "add"));
        textList.add(buttonText);
        textList.add(Component.translatable("tooltip.avaritia.tier", Wrapper.LIST[index].getTier()));
        Item item = getRocket(Wrapper.LIST[index].getTier());
        if (item != null) {
            textList.add(Component.translatable(ROCKET).append(item.getDescription()));
            textList.add(Component.translatable(FUEL).append(getFuel(Wrapper.LIST[index].getTier()).getDisplayName()));
            textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.space_elevator.set_out"), "set_out"));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (clickData.isRemote) return;
        if ("set_out".equals(componentData)) {
            launch = true;
            getRecipeLogic().updateTickSubscription();
        } else if (!isActive()) {
            index = Mth.clamp(index + (componentData.equals("add") ? 1 : -1), 0, Wrapper.LIST.length - 1);
        }
    }

    @Nullable
    private Recipe getRecipe() {
        if (launch && getTier() > GTValues.MV && getOwnerUUID() != null) {
            launch = false;
            Item item = getRocket(Wrapper.LIST[index].getTier());
            if (item == null) return null;
            Recipe recipe = getRecipeBuilder().duration(6000).inputItems(GTOItems.PLANET_SCAN_SATELLITE.asStack()).outputItems(item).inputFluids(getFuel(Wrapper.LIST[index].getTier())).inputItems(item).inputItems(GTOItems.PLANET_DATA_CHIP.asStack()).outputItems(GTOItems.PLANET_DATA_CHIP.get().getPlanetDataChip(getOwnerUUID(), Wrapper.LIST[index].getLocation())).EUt(getOverclockVoltage()).buildRawRecipe();
            if (RecipeRunner.matchRecipe(this, recipe) && RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static Dimension[] getPlanets() {
        return Wrapper.LIST;
    }

    public static Item getRocket(int tier) {
        return Wrapper.ROCKET.get(tier);
    }

    public static FluidStack getFuel(int tier) {
        return Wrapper.FUEL.get(tier);
    }

    private static class Wrapper {

        private static final Map<Integer, Item> ROCKET = GTCEu.isProd() ? Map.of(
                1, ModItems.TIER_1_ROCKET.get(),
                2, ModItems.TIER_2_ROCKET.get(),
                3, ModItems.TIER_3_ROCKET.get(),
                4, ModItems.TIER_4_ROCKET.get(),
                5, RegistriesUtils.getItem("ad_astra_rocketed:tier_5_rocket"),
                6, RegistriesUtils.getItem("ad_astra_rocketed:tier_6_rocket"),
                7, RegistriesUtils.getItem("ad_astra_rocketed:tier_7_rocket")) :
                Map.of(
                        1, ModItems.TIER_1_ROCKET.get(),
                        2, ModItems.TIER_2_ROCKET.get(),
                        3, ModItems.TIER_3_ROCKET.get(),
                        4, ModItems.TIER_4_ROCKET.get());

        private static final Map<Integer, FluidStack> FUEL = Map.of(
                1, GTMaterials.RocketFuel.getFluid(16000),
                2, GTOMaterials.RocketFuelRp1.getFluid(16000),
                3, GTOMaterials.DenseHydrazineFuelMixture.getFluid(16000),
                4, GTOMaterials.RocketFuelCn3h7o3.getFluid(16000),
                5, GTOMaterials.RocketFuelH8n4c2o4.getFluid(16000),
                6, new FluidStack(ModFluids.CRYO_FUEL.get(), 16000),
                7, GTOMaterials.StellarEnergyRocketFuel.getFluid(16000));

        private static final Dimension[] LIST;

        static {
            List<Dimension> list = new ArrayList<>();
            GTODimensions.forEachPlanet(list::add);
            LIST = list.toArray(new Dimension[0]);
        }
    }
}
