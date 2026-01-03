package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.client.renderer.StructurePattern;
import com.gtocore.client.renderer.StructureVBO;
import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.GTOValues;
import com.gtolib.api.data.Dimension;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.ClientUtil;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
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

    private final ConditionalSubscriptionHandler tickSubs;
    private Mode mode = Mode.OVERWORLD;

    private boolean clientRemovedBlocks = false;

    public CosmicCelestialSpireOfConvergence(MetaMachineBlockEntity holder) {
        super(holder);
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, 10, this::isFormed);
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
        tickSubs.initialize(getLevel());
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getLevel() == null || getLevel() instanceof TrackedDummyWorld) return;
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

    private void tickUpdate() {
        increase();
        if (getOffsetTimer() % 40 == 0) {
            getRecipeLogic().updateTickSubscription();
        }
        tickSubs.updateSubscription();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() != null) {
            initMode();
        }
    }

    private void initMode() {
        var world = getLevel();
        var dim = world.dimension();

        if (PlanetApi.API.isSpace(getLevel())) {
            mode = Mode.SPACE;
            return;
        } else if (GTODimensions.isVoid(dim.location())) {
            mode = Mode.VOID;
            return;
        }
        switch (Dimension.from(dim)) {
            case OTHERSIDE -> mode = Mode.OTHERSIDE;
            case ALFHEIM -> mode = Mode.ALFHEIM;
            case THE_END -> mode = Mode.END;
            default -> mode = Mode.OVERWORLD;
        }
    }

    private void increase() {
        if (getLevel() == null) return;
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

        switch (mode) {
            case SPACE -> stellarm = Math.min(max_capacity, stellarm + 2000L * i);
            case VOID -> {
                solaris = Math.min(max_capacity, solaris + 500L * i);
                lunara = Math.min(max_capacity, lunara + 500L * i);
            }
            case ALFHEIM -> {
                if (getLevel().isDay()) {
                    solaris = Math.min(max_capacity, solaris + 2000L * i);
                } else if (getLevel().isNight()) {
                    lunara = Math.min(max_capacity, lunara + 2000L * i);
                }
            }
            case OTHERSIDE -> voidflux = Math.min(max_capacity, voidflux + 5000L * i);
            case END -> voidflux = Math.min(max_capacity, voidflux + 1000L * i);
            case OVERWORLD -> {
                if (getLevel().isDay()) {
                    solaris = Math.min(max_capacity, solaris + 1000L * i);
                } else if (getLevel().isNight()) {
                    lunara = Math.min(max_capacity, lunara + 1000L * i);
                }
            }
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

    private enum Mode {
        VOID,
        OTHERSIDE,
        SPACE,
        ALFHEIM,
        END,
        OVERWORLD

    }
}
