package com.gtocore.common.machine.multiblock.electric.miner;

import com.gtolib.IItem;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.cover.filter.Filter;
import com.gregtechceu.gtceu.api.cover.filter.FluidFilter;
import com.gregtechceu.gtceu.api.cover.filter.ItemFilter;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DigitalMinerLogic extends CustomRecipeLogic {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(DigitalMinerLogic.class, CustomRecipeLogic.MANAGED_FIELD_HOLDER);

    @Persisted
    protected int x = Integer.MAX_VALUE;
    @Persisted
    protected int y = Integer.MAX_VALUE;
    @Persisted
    protected int z = Integer.MAX_VALUE;
    @Persisted
    protected int startX = Integer.MAX_VALUE;
    @Persisted
    protected int startZ = Integer.MAX_VALUE;
    @Persisted
    protected int startY = Integer.MAX_VALUE;
    @Persisted
    protected int mineX = Integer.MAX_VALUE;
    @Persisted
    protected int mineZ = Integer.MAX_VALUE;
    @Persisted
    protected int mineY = Integer.MAX_VALUE;
    @Persisted
    private boolean isDone;

    private AABB area;

    protected final DigitalMiner miner;
    private int silk;
    private final int speed;
    private final LinkedList<BlockPos> oresToMine = new LinkedList<>();
    private int minBuildHeight = Integer.MAX_VALUE;
    private boolean isInventoryFull;
    private int oreAmount;
    private Filter<?, ?> filter;
    private DigitalMiner.FluidMode fluidMode;
    private final Map<BlockState, List<ItemStack>> lootCache = new Reference2ReferenceOpenHashMap<>();

    // ===================== 矿块搜索线程相关 =====================
    private Thread minerSearchThread;
    private volatile boolean isSearchingBlocks = false;

    public DigitalMinerLogic(@NotNull IRecipeLogicMachine machine,
                             AABB aabb, int silk, Filter<?, ?> filter, DigitalMiner.FluidMode fluidMode) {
        super(machine, () -> null);
        this.miner = (DigitalMiner) machine;
        this.silk = silk;
        this.speed = miner.getSpeed();
        this.isDone = false;
        this.area = aabb;
        this.filter = filter;
        this.fluidMode = fluidMode;
    }

    // ===================== Getter/Setter =====================
    public DigitalMiner getMiner() {
        return miner;
    }

    public int getSilk() {
        return silk;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartZ() {
        return startZ;
    }

    public int getStartY() {
        return startY;
    }

    public int getMineX() {
        return mineX;
    }

    public int getMineZ() {
        return mineZ;
    }

    public int getMineY() {
        return mineY;
    }

    public int getMinBuildHeight() {
        return minBuildHeight;
    }

    public int getMinY() {
        return (int) area.minY;
    }

    public int getMaxY() {
        return (int) area.maxY;
    }

    public int getMinX() {
        return (int) area.minX;
    }

    public int getMaxX() {
        return (int) area.maxX;
    }

    public int getMinZ() {
        return (int) area.minZ;
    }

    public int getMaxZ() {
        return (int) area.maxZ;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isInventoryFull() {
        return isInventoryFull;
    }

    public int getOreAmount() {
        return oreAmount;
    }

    public Filter<?, ?> getFilter() {
        return filter;
    }

    // ===================== 生命周期相关方法 =====================
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onMachineUnLoad() {
        super.onMachineUnLoad();
        stopSearching();
    }

    public void stopSearching() {
        if (this.minerSearchThread != null && this.minerSearchThread.isAlive()) {
            this.minerSearchThread.interrupt();
            this.minerSearchThread = null;
        }
    }

    // ===================== 逻辑相关方法 =====================
    @Override
    public void resetRecipeLogic() {
        super.resetRecipeLogic();
        stopSearching();
        resetArea(false);
        this.setWorkingEnabled(false);
    }

    public void resetRecipeLogic(AABB aabb, int silk, Filter<?, ?> filter, DigitalMiner.FluidMode fluidMode) {
        this.silk = silk;
        this.area = aabb;
        this.filter = filter;
        this.oresToMine.clear();
        this.oreAmount = 0;
        this.fluidMode = fluidMode;
        this.resetRecipeLogic();
    }

    protected boolean isSilkTouchMode() {
        return silk == 1;
    }

    private boolean checkCoordinatesInvalid() {
        return x == Integer.MAX_VALUE && y == Integer.MAX_VALUE && z == Integer.MAX_VALUE;
    }

    protected boolean checkCanMine() {
        if (!miner.isFormed()) return false;
        if (!isDone && checkCoordinatesInvalid()) {
            initPos();
        }
        return !isDone && miner.drainInput(true);
    }

    public void initPos() {
        x = getMinX();
        z = getMinZ();
        y = getMaxY();
        startX = getMinX();
        startZ = getMinZ();
        startY = getMaxY();
        mineX = getMinX();
        mineZ = getMinZ();
        mineY = getMaxY();
    }

    public BlockPos getMiningPos() {
        return getMachine().getPos();
    }

    public void resetArea(boolean checkToMine) {
        initPos();
        if (this.isDone) this.setWorkingEnabled(false);
        this.isDone = false;
        if (checkToMine) {
            oresToMine.clear();
            checkBlocksToMine();
        }
    }

    public void onBlocksFound(List<BlockPos> foundBlocks) {
        synchronized (oresToMine) {
            oresToMine.clear();
            oresToMine.addAll(foundBlocks);
            oreAmount = oresToMine.size();
            isSearchingBlocks = false;
        }
        if (oresToMine.isEmpty()) {
            x = mineX;
            y = mineY;
            z = mineZ;
            this.isDone = true;
            this.setStatus(Status.IDLE);
            this.setWorkingEnabled(false);
        }
    }

    private boolean isInMultiblock(BlockPos pos) {
        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();
        var facing = getMachine().getFrontFacing();
        var bottomCenter = getMachine().getPos().offset(-facing.getStepX() * 2, 0, -facing.getStepZ() * 2);
        return x >= bottomCenter.getX() - 2 && x <= bottomCenter.getX() + 2 &&
                y >= bottomCenter.getY() && y <= bottomCenter.getY() + 7 &&
                z >= bottomCenter.getZ() - 2 && z <= bottomCenter.getZ() + 2;
    }

    // ===================== 矿块搜索线程相关方法 =====================
    public void checkBlocksToMine() {
        if (oresToMine.isEmpty() && !isSearchingBlocks) {
            synchronized (oresToMine) {
                isSearchingBlocks = true;
                minerSearchThread = new SearcherThread();
                minerSearchThread.start();
            }
        }
    }

    private class SearcherThread extends Thread {

        private PathNavigationRegion chunkCache;

        public SearcherThread() {
            super("DigitalMiner-BlockSearchThread at " + DigitalMinerLogic.this.getMachine().getPos().toShortString());
            if (minBuildHeight == Integer.MAX_VALUE)
                minBuildHeight = getMachine().getLevel().getMinBuildHeight();
            this.chunkCache = new PathNavigationRegion(
                    getMiner().getLevel(),
                    new BlockPos(startX - 1, startY - 1, startZ - 1),
                    new BlockPos((int) (area.maxX + 1), (int) (area.maxY + 1), (int) (area.maxZ + 1)));
            setDaemon(true);
        }

        @Override
        public void run() {
            List<BlockPos> foundBlocks = getBlocksToMine();
            chunkCache = null;
            onBlocksFound(foundBlocks);
        }

        private LinkedList<BlockPos> getBlocksToMine() {
            LinkedList<BlockPos> blocks = new LinkedList<>();
            int calcAmount = Integer.MAX_VALUE;
            int calculated = 0;
            int x = startX, y = startY, z = startZ;
            final int endX = getMaxX(), endZ = getMaxZ(), minHeight = getMinY();
            ItemFilter itemFilter = (DigitalMinerLogic.this.filter instanceof ItemFilter filter) ? filter : null;
            FluidFilter fluidFilter = (DigitalMinerLogic.this.filter instanceof FluidFilter filter) ? filter : null;
            while (calculated < calcAmount) {
                if (y >= minHeight) {
                    if (z < endZ) {
                        if (x < endX) {
                            BlockPos blockPos = new BlockPos(x, y, z);
                            BlockState state = chunkCache.getBlockState(blockPos);
                            if (!isInMultiblock(blockPos) &&
                                    state.getBlock() != Blocks.AIR &&
                                    (!state.hasBlockEntity() || (itemFilter != null && itemFilter.test(((IItem) state.getBlock().asItem()).gtolib$getReadOnlyStack()))) &&
                                    state.getBlock().defaultDestroyTime() >= 0) {
                                if (state.getBlock() instanceof LiquidBlock liq && fluidMode != DigitalMiner.FluidMode.Ignore && itemFilter == null) {
                                    if (fluidFilter == null || fluidFilter.test(new FluidStack(liq.getFluidState(state).getType(), 1))) {
                                        blocks.addLast(blockPos);
                                    }
                                } else if (fluidFilter == null) {
                                    if (itemFilter == null || itemFilter.test(((IItem) state.getBlock().asItem()).gtolib$getReadOnlyStack())) {
                                        blocks.addLast(blockPos);
                                    }
                                }
                            }
                            ++x;
                        } else {
                            x = startX;
                            ++z;
                        }
                    } else {
                        z = startZ;
                        --y;
                    }
                } else
                    return blocks;
                if (!blocks.isEmpty())
                    calculated = blocks.size();
            }
            return blocks;
        }
    }

    // ===================== 挖矿相关方法 =====================
    @Override
    public void serverTick() {
        if (!isSuspend() && getMachine().getLevel() instanceof ServerLevel serverLevel && checkCanMine()) {
            if (!isInventoryFull()) {
                miner.drainInput(false);
                setStatus(Status.WORKING);
                if (lastRecipe == null) lastRecipe = RecipeBuilder.ofRaw().EUt(miner.getEnergyPerTick()).buildRawRecipe();
            } else {
                if (this.isWorking()) {
                    setWaiting(Component.translatable("gtceu.recipe_logic.insufficient_out").append(": ")
                            .append(ItemRecipeCapability.CAP.getName()));
                }
            }
            checkBlocksToMine();
            if (!oresToMine.isEmpty() && miner.getOffsetTimer() % speed == 0) {
                int loops = miner.getParallelMining();
                lootCache.clear();
                do {
                    BlockPos blockPos = oresToMine.getFirst();
                    BlockState blockState = serverLevel.getBlockState(blockPos);
                    if (mineAndInsertFluids(blockState, serverLevel, blockPos)) {
                        continue;
                    }
                    NonNullList<ItemStack> blockDrops = NonNullList.create();
                    if (isSilkTouchMode()) {
                        getSilkTouchDrops(blockDrops, blockState);
                    } else {
                        getRegularBlockDrops(blockDrops, blockState, blockPos);
                    }
                    mineAndInsertItems(blockDrops, serverLevel);
                    this.oreAmount = oresToMine.size();
                } while (--loops > 0 && !oresToMine.isEmpty() && !isInventoryFull());
            }
            if (oresToMine.isEmpty() && !isSearchingBlocks) {
                x = mineX;
                y = mineY;
                z = mineZ;
                this.isDone = true;
                this.setStatus(Status.IDLE);
                this.oreAmount = oresToMine.size();
            }
        } else {
            this.setStatus(Status.IDLE);
            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }
            this.lastRecipe = null;
        }
    }

    protected void getRegularBlockDrops(NonNullList<ItemStack> blockDrops, BlockState blockState, BlockPos blockPos) {
        Function<BlockState, List<ItemStack>> mappingFunction = state -> Block.getDrops(state, (ServerLevel) getMachine().getLevel(), blockPos, null).stream().map(ItemStack::copy).toList();
        blockDrops.addAll(blockState.hasBlockEntity() ? mappingFunction.apply(blockState) : lootCache.computeIfAbsent(blockState, mappingFunction));
    }

    protected static void getSilkTouchDrops(NonNullList<ItemStack> blockDrops, BlockState blockState) {
        blockDrops.add(new ItemStack(blockState.getBlock()));
    }

    private void mineAndInsertItems(NonNullList<ItemStack> blockDrops, ServerLevel serverLevel) {
        if (MachineUtils.outputItem(miner, blockDrops.toArray(new ItemStack[0]))) {
            var blockPos = oresToMine.removeFirst();
            GTOUtils.fastRemoveBlock(serverLevel, blockPos, false, false);
            mineX = blockPos.getX();
            mineY = blockPos.getY();
            mineZ = blockPos.getZ();
            isInventoryFull = false;
            return;
        }
        isInventoryFull = true;
    }

    private boolean mineAndInsertFluids(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos) {
        if (blockState.getBlock() instanceof LiquidBlock liq && fluidMode != DigitalMiner.FluidMode.Ignore) {
            boolean isSource = liq.getFluidState(blockState).isSource();
            if (isSource && fluidMode == DigitalMiner.FluidMode.Harvest && !MachineUtils.outputFluid(miner, liq.getFluidState(blockState).getType(), 1000)) {
                this.isInventoryFull = true;
                return true;
            }
            this.isInventoryFull = false;
            GTOUtils.fastRemoveBlock(serverLevel, blockPos, false, false);
            oresToMine.removeFirst();
            this.oreAmount = oresToMine.size();
            return true;
        }
        return false;
    }
}
