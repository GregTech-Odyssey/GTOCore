package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.client.renderer.StructurePattern;
import com.gtocore.client.renderer.StructureVBO;
import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.GTOValues;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.ClientUtil;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.blockentity.ITickSubscription;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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

    private boolean clientRemovedBlocks = false;

    @Persisted
    private short strategy = 0;

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
        timing = 0;
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (isFormed()) {
            if (!clientRemovedBlocks) {
                clientRemovedBlocks = removeBlockFromWorld();
            }
        } else {
            if (clientRemovedBlocks) {
                clientRemovedBlocks = !addBlockToWorld();
            }
        }
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
            checkDimensions();
            timing = 80;
        } else {
            timing--;
        }
    }

    private void increase(Level world) {
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
        if (strategy == 1) {
            stellarm = Math.min(max_capacity, stellarm + 2000L * i);
        }
        // Void维度：solaris 和 lunara 各加5
        else if (strategy == 2) {
            solaris = Math.min(max_capacity, solaris + 500L * i);
            lunara = Math.min(max_capacity, lunara + 500L * i);
        }
        // OTHERSIDE维度：voidflux 加50
        else if (strategy == 3) {
            voidflux = Math.min(max_capacity, voidflux + 5000L * i);
        }
        // ALFHEIM维度：白天 solaris 20，黑夜 lunara + 20
        else if (strategy == 4) {
            if (world.isDay()) {
                solaris = Math.min(max_capacity, solaris + 2000L * i);
            } else if (world.isNight()) {
                lunara = Math.min(max_capacity, lunara + 2000L * i);
            }
        }
        // 主世界/末地的资源增加逻辑
        else if (strategy == 5) {
            voidflux = Math.min(max_capacity, voidflux + 1000L * i);
        } else if (world.isDay()) {
            solaris = Math.min(max_capacity, solaris + 1000L * i);
        } else if (world.isNight()) {
            lunara = Math.min(max_capacity, lunara + 1000L * i);
        }
    }

    private void checkDimensions() {
        Level world = getLevel();
        if (world == null) {
            strategy = 0;
            return;
        }
        ResourceLocation dimLocation = world.dimension().location();
        if (PlanetApi.API.isSpace(world)) {
            strategy = 1;
        } else if (GTODimensions.isVoid(dimLocation)) {
            strategy = 2;
        } else if (GTODimensions.OTHERSIDE == dimLocation) {
            strategy = 3;
        } else if (GTODimensions.ALFHEIM == dimLocation) {
            strategy = 4;
        } else if (world.dimension() == Level.END) {
            strategy = 5;
        }
    }

    private boolean removeBlockFromWorld() {
        String[][] structure = StructurePattern.tinyLight;
        for (int x = 0; x < structure.length; x++) {
            String[] plane = structure[x];
            for (int y = 0; y < plane.length; y++) {
                String row = plane[y];
                for (int z = 0; z < row.length(); z++) {
                    char letter = row.charAt(z);
                    if (letter == ' ') continue;
                    BlockPos realPos = getRealPos(x, y, z);
                    if (!getLevel().isLoaded(realPos)) return false;
                    getLevel().setBlock(realPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_KNOWN_SHAPE);
                    ClientUtil.getPreventUpdate(getLevel()).add(realPos.asLong());
                }
            }
        }
        return true;
    }

    private boolean addBlockToWorld() {
        StructureVBO ringStructure = (new StructureVBO())
                .addMapping('X', GTOBlocks.THE_SOLARIS_LENS.get())
                .addMapping('[', RegistriesUtils.getBlock("ars_nouveau:sky_block"));

        String[][] structure = StructurePattern.tinyLight;
        ringStructure.assignStructure(structure);

        for (int x = 0; x < structure.length; x++) {
            String[] plane = structure[x];
            for (int y = 0; y < plane.length; y++) {
                String row = plane[y];
                for (int z = 0; z < row.length(); z++) {
                    char letter = row.charAt(z);
                    if (letter == ' ') continue;
                    BlockPos realPos = getRealPos(x, y, z);
                    if (!getLevel().isLoaded(realPos)) return false;
                    BlockState blockState = ringStructure.mapper.get(letter).defaultBlockState();
                    ClientUtil.getPreventUpdate(getLevel()).remove(realPos.asLong());
                    getLevel().setBlock(realPos, blockState, Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_KNOWN_SHAPE);
                }
            }
        }
        return true;
    }

    private BlockPos getRealPos(int x, int y, int z) {
        String[][] structure = StructurePattern.tinyLight;
        BlockPos.MutableBlockPos pos = BlockPos.ZERO.offset(5 + structure.length / 2 - x, -structure[0].length / 2 + y + 8, -structure[0][0].length() / 2 + z).mutable();
        switch (getFrontFacing()) {
            case EAST -> pos.set(-pos.getX(), pos.getY(), -pos.getZ());
            case NORTH -> pos.set(-pos.getZ(), pos.getY(), pos.getX());
            case SOUTH -> pos.set(pos.getZ(), pos.getY(), -pos.getX());
        }
        return pos.offset(this.getPos());
    }
}
