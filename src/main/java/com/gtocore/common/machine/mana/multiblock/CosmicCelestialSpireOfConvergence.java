package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.GTOValues;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.blockentity.ITickSubscription;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.gtocore.common.machine.mana.CelestialCondenser.*;

public class CosmicCelestialSpireOfConvergence extends ManaMultiblockMachine {

    @Persisted
    private long solaris = 0;
    @Persisted
    private long lunara = 0;
    @Persisted
    private long voidflux = 0;
    @Persisted
    private long stellarm = 0;
    private static final long max_capacity = 5000000000000000000L;

    @Persisted
    private short accelerate = 0;

    private int timing;
    private TickableSubscription tickSubs;

    public CosmicCelestialSpireOfConvergence(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    protected @Nullable Recipe getRealRecipe(@NotNull Recipe recipe) {
        if (!super.beforeWorking(recipe)) return null;
        int solarisCost = recipe.data.getInt(SOLARIS);
        int lunaraCost = recipe.data.getInt(LUNARA);
        int voidfluxCost = recipe.data.getInt(VOIDFLUX);
        int stellarmCost = recipe.data.getInt(STELLARM);
        int anyCost = recipe.data.getInt("any");

        long parallel = 0;
        if (solarisCost > 0) parallel = this.solaris / solarisCost;
        else if (lunaraCost > 0) parallel = this.lunara / lunaraCost;
        else if (voidfluxCost > 0) parallel = this.voidflux / voidfluxCost;
        else if (stellarmCost > 0) parallel = this.stellarm / stellarmCost;
        else if (anyCost > 0) parallel = (this.solaris + this.lunara + this.voidflux + this.stellarm) / anyCost;
        if (parallel == 0) return null;
        recipe = ParallelLogic.accurateParallel(this, recipe, parallel);

        if (recipe == null) return null;
        parallel = recipe.parallels;

        if (solarisCost > 0) {
            this.solaris = Math.max(0L, this.solaris - (solarisCost * parallel));
        } else if (lunaraCost > 0) {
            this.lunara = Math.max(0L, this.lunara - (lunaraCost * parallel));
        } else if (voidfluxCost > 0) {
            this.voidflux = Math.max(0L, this.voidflux - (voidfluxCost * parallel));
        } else if (stellarmCost > 0) {
            this.stellarm = Math.max(0L, this.stellarm - (stellarmCost * parallel));
        } else {
            long remainingCost = anyCost * parallel;
            if (remainingCost > 0 && this.solaris > 0) {
                long deduct = Math.min(this.solaris, remainingCost);
                this.solaris = Math.max(0, this.solaris - deduct);
                remainingCost -= deduct;
            }
            if (remainingCost > 0 && this.lunara > 0) {
                long deduct = Math.min(this.lunara, remainingCost);
                this.lunara = Math.max(0, this.lunara - deduct);
                remainingCost -= deduct;
            }
            if (remainingCost > 0 && this.voidflux > 0) {
                long deduct = Math.min(this.voidflux, remainingCost);
                this.voidflux = Math.max(0, this.voidflux - deduct);
                remainingCost -= deduct;
            }
            if (remainingCost > 0 && this.stellarm > 0) {
                long deduct = Math.min(this.stellarm, remainingCost);
                this.stellarm = Math.max(0, this.stellarm - deduct);
                remainingCost -= deduct;
            }
            if (remainingCost > 0) {
                return null;
            }
        }

        return recipe;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("gtocore.machine.oc_amount", accelerate)
                    .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            Component.translatable("gtocore.machine.steam_parallel_machine.oc")))));

            textList.add(Component.translatable("gtocore.machine.steam_parallel_machine.modification_oc")
                    .append(ComponentPanelWidget.withButton(Component.literal("[-] "), "ocSub"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "ocAdd")));
        }
        if (solaris > 0) textList.add(Component.translatable("gtocore.celestial_condenser." + SOLARIS, solaris));
        if (lunara > 0) textList.add(Component.translatable("gtocore.celestial_condenser." + LUNARA, lunara));
        if (voidflux > 0) textList.add(Component.translatable("gtocore.celestial_condenser." + VOIDFLUX, voidflux));
        if (stellarm > 0) textList.add(Component.translatable("gtocore.celestial_condenser." + STELLARM, stellarm));
        /*
         * textList.add(Component.translatable("gtocore.machine.resonance_flower.stable_operation_times", stableTime));
         * textList.add(Component.translatable("gtocore.machine.resonance_flower.time_fluctuation_coefficient",
         * String.format("%.6f", timeFluctuationCoefficient)));
         * textList.add(Component.translatable("gtocore.machine.resonance_flower.elemental_fluctuation_coefficient",
         * String.format("%.6f", elementalFluctuationCoefficient)));
         * textList.add(Component.translatable("item.gtceu.tool.tooltip.attack_damage", attackDamage));
         * textList.add(Component.translatable("gtocore.machine.slaughterhouse.active_weapon",
         * activeWeapon.getDisplayName()));
         * textList.add(Component.translatable("gtocore.machine.slaughterhouse.filter_nbt").append(ComponentPanelWidget.
         * withButton(Component.literal("[").append(filterNbt ? Component.translatable("gtocore.machine.on") :
         * Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "filter_nbt")));
         */
    }

    @Override
    public void handleDisplayClick(@NotNull String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            accelerate = (short) Mth.clamp(accelerate + ("ocAdd".equals(componentData) ? 1 : -1), 0, 4);
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tickSubs = subscribeServerTick(tickSubs, this::tickUpdate, 10);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        ITickSubscription.unsubscribe(tickSubs);
        tickSubs = null;
    }

    @Override
    public void onUnload() {
        super.onUnload();
        ITickSubscription.unsubscribe(tickSubs);
        tickSubs = null;
    }

    private void tickUpdate() {
        if (!isFormed()) return;
        Level world = getLevel();
        if (world == null) return;
        increase(world);
        if (timing == 0) {
            getRecipeLogic().updateTickSubscription();
            timing = 20;
        } else {
            timing--;
        }
    }

    private void increase(Level world) {
        ResourceLocation dimLocation = world.dimension().location();

        if (accelerate > 0) {
            int cost = GTOValues.MANA[accelerate * 2 + 4] * 2;
            if (cost > removeMana(cost, 1, false)) {
                accelerate = 0;
            } else {
                if (cost > removeMana(cost, 1, true)) {
                    accelerate = 0;
                }
            }
        }

        int i = 1 << (accelerate * 5);
        // 太空维度
        if (PlanetApi.API.isSpace(getLevel())) {
            stellarm = Math.min(max_capacity, stellarm + 2000L * i);
        }
        // Void维度：solaris 和 lunara 各加5
        else if (GTODimensions.isVoid(dimLocation)) {
            solaris = Math.min(max_capacity, solaris + 500L * i);
            lunara = Math.min(max_capacity, lunara + 500L * i);
        }
        // OTHERSIDE维度：voidflux 加50
        else if (GTODimensions.OTHERSIDE == dimLocation) {
            voidflux = Math.min(max_capacity, voidflux + 5000L * i);
        }
        // ALFHEIM维度：白天 solaris 20，黑夜 lunara + 20
        else if (GTODimensions.ALFHEIM == dimLocation) {
            if (world.isDay()) {
                solaris = Math.min(max_capacity, solaris + 2000L * i);
            } else if (world.isNight()) {
                lunara = Math.min(max_capacity, lunara + 2000L * i);
            }
        }
        // 主世界/末地的资源增加逻辑
        else if (world.dimension() == Level.END) {
            voidflux = Math.min(max_capacity, voidflux + 1000L * i);
        } else if (world.isDay()) {
            solaris = Math.min(max_capacity, solaris + 1000L * i);
        } else if (world.isNight()) {
            lunara = Math.min(max_capacity, lunara + 1000L * i);
        }
    }
}
