package com.gtocore.integration.eio;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

import appeng.blockentity.AEBaseBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import com.enderio.core.CoreNBTKeys;
import com.enderio.machines.common.travel.AnchorTravelTarget;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static com.gtolib.utils.ServerUtils.getServer;

@MethodsReturnNonnullByDefault
public class PatternTravelTarget extends AnchorTravelTarget {

    @Nullable
    private final PatternProviderLogicHost patternProviderLogicHost;
    @Nullable
    private final MEPatternPartMachineKt<?> patternBufferHost;
    private final boolean isClient;
    /// NotNull on server side
    @Nullable
    private final ResourceKey<Level> dimension;
    int hashCache = 0;

    public PatternTravelTarget(PatternProviderLogicHost host) {
        super(host.getBlockEntity().getBlockPos(),
                getPlayerCustomName(host),
                getAdjacentMachineIcon(),
                host.isVisibleInTerminal());
        this.patternProviderLogicHost = host;
        this.patternBufferHost = null;
        this.isClient = !(host.getBlockEntity().getLevel() instanceof ServerLevel);
        this.dimension = Optional.ofNullable(host.getBlockEntity().getLevel()).map(Level::dimension).orElse(null);
    }

    public PatternTravelTarget(MEPatternPartMachineKt<?> host) {
        super(host.getHolder().getBlockPos(),
                getPlayerCustomName(host),
                getAdjacentMachineIcon(),
                host.getShowInTravelNetwork());
        this.patternBufferHost = host;
        this.patternProviderLogicHost = null;
        this.isClient = !(host.getHolder().getLevel() instanceof ServerLevel);
        this.dimension = Optional.ofNullable(host.getHolder().getLevel()).map(Level::dimension).orElse(null);
    }

    private static String getPlayerCustomName(Object host) {
        if (host instanceof PatternProviderLogicHost logicHost) {
            BlockEntity be = logicHost.getBlockEntity();
            if (be instanceof AEBaseBlockEntity aeBe) {
                Component customNameComponent = aeBe.getCustomName();
                if (customNameComponent != null) {
                    return customNameComponent.getString();
                }
            }
        } else if (host instanceof MEPatternPartMachineKt<?> partHost) {
            return partHost.getCustomName();
        }
        return "";
    }

    private static Item getAdjacentMachineIcon() {
        return Items.AIR;
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PatternTravelTarget that = (PatternTravelTarget) o;
        return hashCode() == that.hashCode() && Objects.equals(patternProviderLogicHost, that.patternProviderLogicHost) && Objects.equals(patternBufferHost, that.patternBufferHost);
    }

    @Override
    public int hashCode() {
        if (hashCache != 0) return hashCache;
        return hashCache = Objects.hash(getPos().hashCode(), patternProviderLogicHost, patternBufferHost);
    }

    @Override
    public String getName() {
        if (isClient || (dimension != null && !Objects.requireNonNull(getServer().getLevel(dimension)).isLoaded(getPos()))) {
            // on client side the patternProviderLogicHost is not available in some cases (e.g. when coming from a world
            // load)
            // so we fall back to the name stored in the parent class
            return super.getName();
        }
        return patternBufferHost != null ? getPlayerCustomName(patternBufferHost) :
                getPlayerCustomName(patternProviderLogicHost);
    }

    @Override
    public BlockPos getPos() {
        if (isClient) {
            return super.getPos();
        }
        return patternBufferHost != null ? patternBufferHost.getHolder().getBlockPos() :
                patternProviderLogicHost.getBlockEntity().getBlockPos();
    }

    @Override
    public Item getIcon() {
        if (isClient || (dimension != null && !Objects.requireNonNull(getServer().getLevel(dimension)).isLoaded(getPos()))) {
            return super.getIcon();
        }
        return getAdjacentMachineIcon();
    }

    @Override
    public boolean getVisibility() {
        if (isClient || (dimension != null && !Objects.requireNonNull(getServer().getLevel(dimension)).isLoaded(getPos()))) {
            return super.getVisibility();
        }
        return patternBufferHost != null ? patternBufferHost.getShowInTravelNetwork() :
                patternProviderLogicHost.isVisibleInTerminal();
    }

    @Override
    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.put(CoreNBTKeys.BLOCK_POS, NbtUtils.writeBlockPos(getPos()));
        nbt.putString(CoreNBTKeys.ANCHOR_NAME, getName());
        nbt.putString(CoreNBTKeys.ANCHOR_ICON, String.valueOf(ForgeRegistries.ITEMS.getKey(getIcon())));
        nbt.putBoolean(CoreNBTKeys.ANCHOR_VISIBILITY, getVisibility());
        return nbt;
    }
}
