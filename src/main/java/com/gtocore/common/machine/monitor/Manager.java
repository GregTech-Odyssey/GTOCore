package com.gtocore.common.machine.monitor;

import com.gtocore.common.network.ServerMessage;
import com.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static com.gtocore.client.renderer.machine.MonitorRenderer.gridToNetworkCLIENT;

public final class Manager {

    private static final Queue<Runnable> Loading = new LinkedList<>();
    /**
     * The maximum allowed size for a grid in the system. This limit exists to ensure
     * optimal performance and prevent excessive memory usage. Exceeding this limit
     * may result in undefined behavior or errors during grid operations.
     */
    static final int MAX_GRID_SIZE = 16;
    static final Map<GridFacedPoint, GridNetwork> gridToNetwork = new ConcurrentHashMap<>();

    private Manager() {}

    static void requireQueue(Runnable runnable) {
        if (!Loading.contains(runnable)) {
            Loading.add(runnable);
        }
    }

    private static void poll() {
        while (!Loading.isEmpty()) {
            Loading.poll().run();
        }
    }

    public static void addBlock(MetaMachine be) {
        addBlock(be.getBlockState(), be.getPos(), be.getLevel());
    }

    public static Direction getFrontFacing(BlockState state) {
        Block var3 = state.getBlock();
        if (var3 instanceof MetaMachineBlock machineBlock) {
            return machineBlock.getFrontFacing(state);
        } else {
            throw new IllegalArgumentException("BlockState is not a MetaMachineBlock: " + var3);
        }
    }

    public static void addBlock(BlockState blockState, BlockPos pos, @Nullable Level level, int color) {
        if (level == null || level.isClientSide() || !level.isLoaded(pos)) {
            return;
        }
        requireQueue(() -> {
            var network = GridNetwork.fromBlock(
                    getFrontFacing(blockState),
                    pos,
                    level.dimension(),
                    color);
            network.merge();
            broadcast(level.getServer());
        });
    }

    public static void addBlock(BlockState blockState, BlockPos pos, @Nullable Level level) {
        addBlock(blockState, pos, level, -1);
    }

    public static void removeBlock(BlockState pState, BlockPos pPos, @Nullable Level pLevel) {
        if (pLevel != null && !pLevel.isClientSide) {
            requireQueue(() -> {
                Direction facing = getFrontFacing(pState);
                var point = GridFacing.of(
                        facing,
                        pLevel,
                        GridFacing.getThirdValue(facing, pPos))
                        .getPoint(pPos);
                var network = gridToNetwork.get(point);
                if (network != null) {
                    network.split(point);
                    // if (network.points.isEmpty()) {
                    // gridToNetwork.remove(point);
                    // }
                }
                broadcast(pLevel.getServer());
            });
        }
    }

    public static void removeBlock(MetaMachine be) {
        if (be.getLevel() != null && !be.getLevel().isClientSide()) {
            removeBlock(be.getBlockState(), be.getPos(), be.getLevel());
        }
    }

    enum Direction2D {

        UP(false, true),
        DOWN(false, false),
        LEFT(true, false),
        RIGHT(true, true);

        final boolean isHorizontal;
        final boolean isPositive;

        Direction2D(boolean isHorizontal, boolean isPositive) {
            this.isHorizontal = isHorizontal;
            this.isPositive = isPositive;
        }
    }

    @FunctionalInterface
    public interface GridListener {

        void onGridChanged(GridNetwork gridMain, @Nullable GridNetwork gridOther);
    }

    /**
     * 网格方向类，包含了方向、维度和第三个值（通常是X、Y或Z坐标）。
     * 这三个值可以确定网格所在的平面
     * 
     * @param facing        网格的方向（法向量）
     * @param level         网格所在的维度
     * @param theThirdValue 网格所在平面上的第三个值（通常是X、Y或Z坐标）
     */
    record GridFacing(Direction facing, ResourceKey<Level> level, int theThirdValue) {

        private static final Map<Long, GridFacing> GRID_AXES = new ConcurrentHashMap<>();

        static GridFacing of(Direction facing, Level level, int theThirdValue) {
            return of(facing, level.dimension(), theThirdValue);
        }

        static GridFacing of(Direction facing, ResourceKey<Level> level, int theThirdValue) {
            GridFacing gridFacing = new GridFacing(facing, level, theThirdValue);
            long key = gridFacing.hashCode();
            return GRID_AXES.computeIfAbsent(key, k -> gridFacing);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            GridFacing gridFacing = (GridFacing) o;
            return Objects.equals(level(), gridFacing.level()) && facing() == gridFacing.facing() && theThirdValue() == gridFacing.theThirdValue();
        }

        @Override
        public int hashCode() {
            return ((facing().ordinal() * 31 + level().hashCode()) * 0xFFFFF + theThirdValue());
        }

        static int getThirdValue(Direction facing, BlockPos pos) {
            return switch (facing.getAxis()) {
                case X -> pos.getX();
                case Y -> pos.getY();
                case Z -> pos.getZ();
            };
        }

        GridFacedPoint getPoint(BlockPos pos) {
            switch (facing.getAxis()) {
                case X -> {
                    return new GridFacedPoint(this, pos.getY(), pos.getZ());
                }
                case Y -> {
                    return new GridFacedPoint(this, pos.getZ(), pos.getX());
                }
                case Z -> {
                    return new GridFacedPoint(this, pos.getX(), pos.getY());
                }
                default -> throw new IllegalArgumentException("Unsupported Direction Axis: " + facing.getAxis());
            }
        }
    }

    /**
     * 网格点类，包含了网格方向、X坐标和Y坐标。
     * 
     * @param facing 网格的方向（法向量）
     * @param x      网格点的X坐标
     * @param y      网格点的Y坐标
     */
    public record GridFacedPoint(GridFacing facing, int x, int y) {

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            GridFacedPoint that = (GridFacedPoint) o;
            return x() == that.x() && y() == that.y() && Objects.equals(facing(), that.facing());
        }

        @Override
        public int hashCode() {
            return (facing().hashCode() * 31 + x()) * 0xFFFFF + y();
        }

        @Contract("_, _ -> new")
        private GridFacedPoint shift(int dx, int dy) {
            return new GridFacedPoint(facing(), x() + dx, y() + dy);
        }

        @Contract("_ -> new")
        private GridFacedPoint shift(Direction2D direction) {
            return switch (direction) {
                case UP -> shift(0, 1);
                case DOWN -> shift(0, -1);
                case LEFT -> shift(-1, 0);
                case RIGHT -> shift(1, 0);
            };
        }

        private BlockPos toBlockPos() {
            return switch (facing.facing.getAxis()) {
                case Y -> new BlockPos(y, facing.theThirdValue, x);
                case X -> new BlockPos(facing.theThirdValue, x, y);
                case Z -> new BlockPos(x, y, facing.theThirdValue);
            };
        }
    }

    public static class GridNetwork {

        int fromX;
        int toX;
        int fromY;
        int toY;
        int color = -1;
        // final Set<GridFacedPoint> points = new HashSet<>();
        final GridFacing facing;
        @Nullable
        GridListener listener;

        private GridNetwork(GridFacing facing) {
            this.facing = facing;
        }

        public static GridNetwork fromBlock(Direction facing, BlockPos pos, ResourceKey<Level> level, int color) {
            GridFacing axis = GridFacing.of(facing, level, GridFacing.getThirdValue(facing, pos));
            var point = axis.getPoint(pos);
            if (gridToNetwork.containsKey(point)) {
                return gridToNetwork.get(point); // 如果该点已经存在于网格中，则返回已存在的网格
            }
            return createSingleBlockNetwork(facing, pos, level, color);
        }

        Set<GridFacedPoint> points() {
            Set<GridFacedPoint> points = new HashSet<>();
            for (int i = fromX; i <= toX; i++) {
                for (int j = fromY; j <= toY; j++) {
                    points.add(new GridFacedPoint(facing, i, j));
                }
            }
            return points;
        }

        public static GridNetwork createSingleBlockNetwork(Direction facing, BlockPos pos, ResourceKey<Level> level, int color) {
            GridFacing axis = GridFacing.of(facing, level, GridFacing.getThirdValue(facing, pos));
            var point = axis.getPoint(pos);
            if (gridToNetwork.containsKey(point)) {
                throw new IllegalStateException("GridNetwork already exists for point: " + point);
            }
            GridNetwork network = new GridNetwork(axis);
            network.fromX = point.x;
            network.toX = point.x;
            network.fromY = point.y;
            network.toY = point.y;
            network.color = color;
            // network.points.add(point);
            put(point, network);
            return network;
        }

        @Nullable
        @OnlyIn(Dist.CLIENT)
        public static GridNetwork fromClientBlock(Direction facing, BlockPos pos, Level level) {
            GridFacing axis = GridFacing.of(facing, level, GridFacing.getThirdValue(facing, pos));
            var point = axis.getPoint(pos);
            if (gridToNetworkCLIENT.containsKey(point)) {
                return gridToNetworkCLIENT.get(point); // 如果该点已经存在于网格中，则返回已存在的网格
            }
            return null;
        }

        public void setListener(@Nullable GridListener listener) {
            this.listener = listener;
        }

        public int width() {
            return toX - fromX + 1;
        }

        public int height() {
            return toY - fromY + 1;
        }

        public int width3D() {
            // get the from and to point in the third dimension
            var blockPos1 = getOriginPos();
            var blockPos2 = new GridFacedPoint(facing, toX, toY).toBlockPos();
            return facing.facing.getAxis() == Direction.Axis.X ?
                    Math.abs(blockPos1.getZ() - blockPos2.getZ()) + 1 :
                    Math.abs(blockPos1.getX() - blockPos2.getX()) + 1;
        }

        public int height3D() {
            // get the from and to point in the third dimension
            var blockPos1 = getOriginPos();
            var blockPos2 = new GridFacedPoint(facing, toX, toY).toBlockPos();
            return facing.facing.getAxis() == Direction.Axis.Y ?
                    Math.abs(blockPos1.getX() - blockPos2.getX()) + 1 :
                    Math.abs(blockPos1.getY() - blockPos2.getY()) + 1;
        }

        public BlockPos getOriginPos() {
            return new GridFacedPoint(facing, fromX, fromY).toBlockPos();
        }

        /// 将这些GridNetwork小矩形合成一个大矩形
        /// 逻辑：
        /// 1. 分别向上，下，左，右四个方向扩展网格。
        /// 扩展逻辑：
        /// - 如果扩展方向上有网格，则检测其宽度（当上下扩展时）或高度（当左右扩展时）是否相同。
        /// - 如果相同，则检查是否超过最大网格大小（MAX_GRID_SIZE）。
        /// - 如果没有超过，则将该网格合并到当前网格中。
        /// 2. 如果前一部分扩展成功，则继续尝试上下左右扩展一轮，直到无法扩展为止。
        private void merge() {
            synchronized (gridToNetwork) {
                boolean success = tryMerge(Direction2D.UP) ||
                        tryMerge(Direction2D.DOWN) ||
                        tryMerge(Direction2D.LEFT) ||
                        tryMerge(Direction2D.RIGHT);
                while (success) {
                    success = tryMerge(Direction2D.UP) ||
                            tryMerge(Direction2D.DOWN) ||
                            tryMerge(Direction2D.LEFT) ||
                            tryMerge(Direction2D.RIGHT);
                }
            }
        }

        private boolean canMerge(GridNetwork other, Direction2D facing2D) {
            return other != null && other.facing == facing && other.color == color &&
                    (facing2D.isHorizontal ? other.height() == this.height() && other.width() + this.width() <= MAX_GRID_SIZE :
                            other.width() == this.width() && other.height() + this.height() <= MAX_GRID_SIZE);
        }

        private boolean tryMerge(Direction2D direction) {
            Predicate<GridFacedPoint> pointPredicate = switch (direction) {
                case UP -> point -> point.y == toY;
                case DOWN -> point -> point.y == fromY;
                case LEFT -> point -> point.x == fromX;
                case RIGHT -> point -> point.x == toX;
            };
            var shiftedPoints = points().stream()
                    .filter(pointPredicate)
                    .map(point -> point.shift(direction))
                    .toList();
            if (shiftedPoints.isEmpty()) {
                return false;
            } else {
                var otherNetwork = gridToNetwork.get(shiftedPoints.get(0));
                boolean canMerge = shiftedPoints.stream()
                        .allMatch(newPoint -> gridToNetwork.get(newPoint) == otherNetwork) &&
                        canMerge(otherNetwork, direction);
                if (canMerge) {
                    int fromX1 = Math.min(fromX, otherNetwork.fromX);
                    int toX1 = Math.max(toX, otherNetwork.toX);
                    int fromY1 = Math.min(fromY, otherNetwork.fromY);
                    int toY1 = Math.max(toY, otherNetwork.toY);
                    // 尝试合并所有符合条件的点
                    if (this.listener != null) {
                        listener.onGridChanged(this, otherNetwork);
                    }
                    fromX = fromX1;
                    toX = toX1;
                    fromY = fromY1;
                    toY = toY1;
                    for (GridFacedPoint point : this.points()) {
                        // 将其他网格的点添加到当前网格中
                        put(point, this);
                    }
                    if (GTOConfig.INSTANCE.dev) {
                        if (gridToNetwork.keySet().stream().filter(
                                p -> p.facing == facing && p.x >= fromX && p.x <= toX && p.y >= fromY && p.y <= toY).anyMatch(p -> gridToNetwork.get(p) != this)) {
                            // 如果当前网格仍然有点存在
                            throw new IllegalStateException("GridNetwork still has points after split: " + otherNetwork);
                        }
                        debugCheckValid();
                    }
                }
                return canMerge;
            }
        }

        private static final Lock gridToNetworkLock = new java.util.concurrent.locks.ReentrantLock();

        private static GridNetwork put(GridFacedPoint point, GridNetwork network) {
            gridToNetworkLock.lock();
            try {
                return gridToNetwork.put(point, network);
            } finally {
                gridToNetworkLock.unlock();
            }
        }

        private static GridNetwork remove(GridFacedPoint point) {
            gridToNetworkLock.lock();
            try {
                return gridToNetwork.remove(point);
            } finally {
                gridToNetworkLock.unlock();
            }
        }

        /**
         * 移除指定的点，并从网格中删除该点。
         * 删除该点会导致情况发生：
         * 1. 如果该点是网格的唯一点，则从网格列表中删除该网格。
         * 2. 如果该点是网格的一部分，则从网格中删除该点，并导致大矩形被分为上下左右四个小矩形，
         * 若该点是边界点，则会导致网格被切割成三个小矩形，若为角点，则会导致网格被切割成两个小矩形。
         * 情况是有限的，且最多生成四个新矩形，采取枚举的方式处理。
         * 3. 如果新生成的矩形能向外与其他矩形合并，则会尝试合并。
         * 
         * @param point 要删除的点
         */
        private void split(GridFacedPoint point) {
            // if (!points.remove(point)) {
            // return; // 点不在网格中，直接返回
            // }
            remove(point);
            if (points().size() <= 1) {
                // 如果网格的宽度和高度都小于等于1，则说明该网格只包含一个点，
                // 直接从网格列表中删除该网格
                return;
            }
            final int fromX = this.fromX;
            final int toX = this.toX;
            final int fromY = this.fromY;
            final int toY = this.toY;
            List<GridFacedPoint> created = new ArrayList<>();
            final var oldPoints = new ArrayList<>(points()); // 复制当前网格的点
            for (var direction : Direction2D.values()) {
                final var shifted = point.shift(direction);
                if (oldPoints.contains(shifted)) {
                    int newFromX = fromX;
                    int newToX = toX;
                    int newFromY = fromY;
                    int newToY = toY;
                    switch (direction) {
                        case UP -> newFromY = shifted.y; // 向上分割
                        case DOWN -> newToY = shifted.y; // 向下分割
                        case LEFT -> {
                            newToX = shifted.x;
                            newFromY = shifted.y; // 保留单行
                            newToY = shifted.y;
                        } // 向左分割
                        case RIGHT -> {
                            newFromX = shifted.x;
                            newFromY = shifted.y; // 保留单行
                            newToY = shifted.y;
                        } // 向右分割
                    }
                    // 如果该点是网格的一部分，则重新计算边界
                    var network1 = new GridNetwork(facing);
                    network1.fromX = newFromX;
                    network1.toX = newToX;
                    network1.fromY = newFromY;
                    network1.toY = newToY;
                    // for (int i = newFromX; i <= newToX; i++) {
                    // for (int j = newFromY; j <= newToY; j++) {
                    // var newPoint = new GridFacedPoint(facing, i, j);
                    // if (oldPoints.contains(newPoint)) {
                    //// points.remove(newPoint);
                    //// network1.points.add(newPoint);
                    // }
                    // }
                    // }
                    network1.points().forEach(newPoint -> put(newPoint, network1));
                    if (listener != null) {
                        listener.onGridChanged(network1, null); // selfModified ? null : network1
                    }
                    created.add(shifted);
                }
            }
            if (GTOConfig.INSTANCE.dev) {
                if (gridToNetwork.keySet().stream().anyMatch(p -> gridToNetwork.get(p) == this)) {
                    // 如果当前网格仍然有点存在
                    throw new IllegalStateException("GridNetwork still has points after split: " + this);
                }
                debugCheckValid();
            }
            // 如果创建了新的网格，尝试使他们合并
            for (var p : created) {
                gridToNetwork.get(p).merge(); // 合并新创建的网格
            }
        }

        public AABB aabb() {
            var pointFrom = new GridFacedPoint(facing, fromX, fromY).toBlockPos();
            var pointTo = new GridFacedPoint(facing, toX, toY).toBlockPos();
            return new AABB(
                    pointFrom.getX(), pointFrom.getY(), pointFrom.getZ(),
                    pointTo.getX() + 1, pointTo.getY() + 1, pointTo.getZ() + 1);
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("fromX", fromX);
            tag.putInt("toX", toX);
            tag.putInt("fromY", fromY);
            tag.putInt("toY", toY);
            tag.putInt("facing", facing.facing.ordinal());
            tag.putInt("theThirdValue", facing.theThirdValue);
            tag.putString("level", facing.level().location().toString());
            return tag;
        }

        static GridNetwork deserializeNBT(CompoundTag tag) {
            int fromX = tag.getInt("fromX");
            int toX = tag.getInt("toX");
            int fromY = tag.getInt("fromY");
            int toY = tag.getInt("toY");
            Direction facingDirection = Direction.values()[tag.getInt("facing")];
            int theThirdValue = tag.getInt("theThirdValue");
            ResourceKey<Level> level = Optional.of(ResourceLocation.parse(tag.getString("level")))
                    .map(rl -> ResourceKey.create(Registries.DIMENSION, rl))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid level dimension: " + tag.getString("level")));
            GridFacing facing = GridFacing.of(facingDirection, level, theThirdValue);
            GridNetwork network = new GridNetwork(facing);
            network.fromX = fromX;
            network.toX = toX;
            network.fromY = fromY;
            network.toY = toY;
            return network;
        }

        private final List<IInformationProvider> informationProviders = new ArrayList<>();
        private final List<IDisplayComponent> displayComponentCache = new ArrayList<>();
        private long lastRefreshTime = 0;

        /// 这个将会被客户端所用到！！！
        private void refreshDisplayingMachine(Level level) {
            if (level == null) return;
            if (level.getGameTime() - lastRefreshTime >= 10) {
                lastRefreshTime = level.getGameTime();
                informationProviders.clear();
                for (GridFacedPoint point : points()) {
                    var blockEntity = level.getBlockEntity(point.toBlockPos());
                    if (blockEntity instanceof IMachineBlockEntity be &&
                            be.getMetaMachine() instanceof IInformationProvider provider) {
                        informationProviders.add(provider);
                    }
                }
                displayComponentCache.clear();
                informationProviders.stream()
                        .sorted(Comparator.comparingLong(IInformationProvider::getPriority).reversed())
                        .map(IInformationProvider::provideInformation)
                        .map(DisplayComponentList::sortInner)
                        .forEachOrdered(displayComponentCache::addAll);
            }
        }

        /// 这个将会被客户端所用到！！！
        @NotNull
        public List<IDisplayComponent> getForDisplay() {
            return new ArrayList<>(displayComponentCache);
        }
    }

    /// 这个将会被客户端所用到！！！
    public static void updateAllNetworkDisplayMachines(Level level) {
        if (level == null) return;
        gridToNetworkCLIENT.values().forEach(network -> network.refreshDisplayingMachine(level));
    }

    private static void clearCache(Level level) {
        // 清空网格数据
        var grid2Network = level.isClientSide ? gridToNetworkCLIENT : Manager.gridToNetwork;
        grid2Network.entrySet().removeIf(entry -> {
            GridFacedPoint point = entry.getKey();
            // 如果网格不在当前世界中，或者网格的点不在当前世界中，则移除该网格
            return !point.facing.level.equals(level.dimension()) || !level.isLoaded(point.toBlockPos());
        });
    }

    private static void debugCheckValid() {
        for (var network : gridToNetwork.values()) {
            network.points().forEach(point -> {
                var otherNetwork = gridToNetwork.get(point);
                if (otherNetwork != network) {
                    throw new IllegalStateException("GridNetwork " + network + " contains point " + point + " which belongs to another network: " + otherNetwork);
                }
            });
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    static class Factory {

        @SubscribeEvent
        public static void onServerStopped(ServerStoppedEvent event) {
            // 服务器完全停止时触发
            gridToNetwork.clear();
            Loading.clear();
            GridFacing.GRID_AXES.clear();
        }

        @SubscribeEvent
        public static void onWorldUnload(LevelEvent.Unload event) {
            // 世界卸载时触发（客户端/服务端均会触发）
            clearCache((Level) event.getLevel());
            Loading.clear();
        }

        // static boolean enteredWorld = false;
        @SubscribeEvent
        public static void onLoad(LevelEvent.Load event) {
            // if (!enteredWorld) {
            // enteredWorld = true;
            // }
            clearCache((Level) event.getLevel());
        }

        @SubscribeEvent
        public static void onSave(LevelEvent.Save event) {}

        @SubscribeEvent
        public static void onTick(TickEvent.ServerTickEvent event) {
            // 方块可以tick多次但这个只能tick一次
            if (event.phase == TickEvent.Phase.START) {
                poll();
            }
        }

        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            // 玩家登录时触发
            if (event.getEntity() instanceof ServerPlayer sp) {
                broadcast(sp.getServer());
            }
        }
    }

    public static void broadcast(MinecraftServer server) {
        // check if runtime is in dedicated server mode
        ServerMessage.send(server, null, "monitorChanged", buf -> {
            CompoundTag tag = new CompoundTag();
            AtomicInteger i = new AtomicInteger(0);
            gridToNetwork.values().forEach(network -> {
                CompoundTag networkTag = network.serializeNBT();
                tag.put(String.valueOf(i.getAndIncrement()), networkTag);
            });
            buf.writeNbt(tag);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientReceived(CompoundTag tag) {
        gridToNetworkCLIENT.clear();
        for (String key : tag.getAllKeys()) {
            CompoundTag networkTag = tag.getCompound(key);
            GridNetwork network = GridNetwork.deserializeNBT(networkTag);
            for (GridFacedPoint point : network.points()) {
                gridToNetworkCLIENT.put(point, network);
            }
        }
    }

    public enum MonitorCTM {

        NONE, // 四面均无连接
        SINGLE_ROW_LEFT, // 单行左侧
        SINGLE_ROW_CENTER, // 单行中心
        SINGLE_ROW_RIGHT, // 单行右侧
        SINGLE_COLUMN_TOP,// 单列顶部
        TOP_LEFT, // 左上角
        TOP, // 顶部
        TOP_RIGHT, // 右上角
        SINGLE_COLUMN_CENTER, // 单列中心
        LEFT, // 左侧
        CENTER, // 中心
        RIGHT, // 右侧
        SINGLE_COLUMN_BOTTOM, // 单列底部
        BOTTOM_LEFT, // 左下角
        BOTTOM, // 底部
        BOTTOM_RIGHT, // 右下角
        ;

        private final int u;
        private final int v;

        MonitorCTM() {
            this.u = ordinal() % 4 * 16; // 每个纹理占16个像素
            this.v = ordinal() / 4 * 16; // 每四个纹理一行
        }

        @NotNull
        @OnlyIn(Dist.CLIENT)
        public static MonitorCTM getConnection(Direction facing, BlockPos pos, @Nullable Level level) {
            if (level == null) {
                return NONE; // 物品栏
            }
            GridFacing axis = GridFacing.of(facing, level, GridFacing.getThirdValue(facing, pos));
            var point = axis.getPoint(pos);
            var network = gridToNetworkCLIENT.get(point);
            if (network == null) return NONE;
            ToIntFunction<BlockPos> mappingX = pos0 -> {
                switch (facing) {
                    case NORTH -> {
                        return -pos0.getX();
                    }
                    case SOUTH -> {
                        return pos0.getX();
                    }
                    case WEST -> {
                        return pos0.getZ();
                    }
                    case EAST -> {
                        return -pos0.getZ();
                    }
                    default -> throw new IllegalArgumentException("Vertical Direction: " + facing + "is not supported yet");
                }
            };
            int x = mappingX.applyAsInt(point.toBlockPos());
            int y = point.toBlockPos().getY();
            BlockPos topLeft = new GridFacedPoint(axis, network.fromX, network.fromY).toBlockPos();
            BlockPos topRight = new GridFacedPoint(axis, network.toX, network.toY).toBlockPos();
            int fromX = Stream.of(topLeft, topRight)
                    .mapToInt(mappingX)
                    .min()
                    .orElse(network.fromX);
            int toX = Stream.of(topLeft, topRight)
                    .mapToInt(mappingX)
                    .max()
                    .orElse(network.toX);
            int fromY = Stream.of(topLeft, topRight)
                    .mapToInt(BlockPos::getY)
                    .max()
                    .orElse(network.fromY);
            int toY = Stream.of(topLeft, topRight)
                    .mapToInt(BlockPos::getY)
                    .min()
                    .orElse(network.toY);
            int width = toX - fromX + 1;
            int height = toY - fromY + 1;
            // 单行
            if (height == 1) {
                if (width == 1) return NONE; // 单个方块
                if (x == fromX) return SINGLE_ROW_LEFT;
                if (x == toX) return SINGLE_ROW_RIGHT;
                return SINGLE_ROW_CENTER;
            }
            // 单列
            if (width == 1) {
                if (y == fromY) return SINGLE_COLUMN_TOP;
                if (y == toY) return SINGLE_COLUMN_BOTTOM;
                return SINGLE_COLUMN_CENTER;
            }
            // 四角
            if (x == fromX && y == fromY) return TOP_LEFT;
            if (x == toX && y == fromY) return TOP_RIGHT;
            if (x == fromX && y == toY) return BOTTOM_LEFT;
            if (x == toX && y == toY) return BOTTOM_RIGHT;
            // 边
            if (y == fromY) return TOP;
            if (y == toY) return BOTTOM;
            if (x == fromX) return LEFT;
            if (x == toX) return RIGHT;
            // 中心
            return CENTER;
        }

        public int getV() {
            return v;
        }

        public int getU() {
            return u;
        }
    }
}
