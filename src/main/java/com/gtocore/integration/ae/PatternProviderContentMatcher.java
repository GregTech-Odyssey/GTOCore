package com.gtocore.integration.ae;

import com.gtocore.common.machine.multiblock.part.ProgrammableHatchPartMachine.ProgrammableCircuitHandler;
import com.gtocore.common.machine.noenergy.VirtualItemProviderMachine;
import com.gtocore.common.machine.tesseract.IMultiTesseract;
import com.gtocore.common.machine.tesseract.TesseractMachine;
import com.gtocore.mixin.ae2.storage.NetworkStorageAccessor;
import com.gtocore.mixin.ae2.storage.NetworkStorageMountAccessor;
import com.gtocore.mixin.gtm.machine.NotifiableFluidTankAccessor;

import com.gtolib.api.ae2.PatternProviderTargetCache;
import com.gtolib.api.ae2.PatternProviderTargetCache.WrapMeStorage;
import com.gtolib.api.machine.trait.MEOutputFluidHandler;
import com.gtolib.api.machine.trait.MEOutputItemHandler;
import com.gtolib.api.machine.trait.VoidOutputFluidHandler;
import com.gtolib.api.machine.trait.VoidOutputItemHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.FluidTankProxyTrait;
import com.gregtechceu.gtceu.api.machine.trait.ItemHandlerProxyTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.fluid.ICustomFluidStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;
import com.gregtechceu.gtceu.core.ILevel;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import appeng.api.config.Actionable;
import appeng.api.config.IncludeExclude;
import appeng.api.config.Settings;
import appeng.api.networking.IGrid;
import appeng.api.parts.IPartHost;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;
import appeng.capabilities.Capabilities;
import appeng.core.definitions.AEItems;
import appeng.helpers.InterfaceLogicHost;
import appeng.me.storage.NetworkStorage;
import appeng.parts.AEBasePart;
import appeng.parts.storagebus.StorageBusPart;
import appeng.util.prioritylist.IPartitionList;

import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Compares the concrete inputs selected by the crafting CPU with the contents exposed by a pattern provider target.
 * Physical contents must be an exact non-negative integer multiple of one base-pattern input vector. Virtual item
 * providers configure a non-consumable slot and are validated separately when physical contents already exist.
 */
public final class PatternProviderContentMatcher {

    private PatternProviderContentMatcher() {}

    public static boolean isComplexTarget(WrapMeStorage target) {
        if (target.storage() instanceof NetworkStorage) return true;
        if (target.targetBlockEntity() == null || target.targetSide() == null) return true;
        return getMetaMachine(resolveSingleTargetHost(target.targetBlockEntity(), target.targetSide())) instanceof IMultiTesseract;
    }

    public static boolean shouldBlock(WrapMeStorage target, List<GenericStack> orderedInputs,
                                      long patternMultiplier) {
        var expected = readExpected(orderedInputs, patternMultiplier);
        if (!expected.valid) return true;

        Object targetHost = null;
        if (target.targetBlockEntity() != null && target.targetSide() != null) {
            targetHost = resolveSingleTargetHost(target.targetBlockEntity(), target.targetSide());
        }
        if (targetHost instanceof InterfaceLogicHost interfaceHost && isSubnetStorage(target.storage(), interfaceHost)) {
            return matchInterface(target, interfaceHost, expected, false, false).blocked;
        }
        if (hasHiddenOutputStorage(targetHost, target.targetSide())) return true;

        var actual = readActual(target.storage(), isVirtualItemProviderHost(targetHost));
        if (!actual.valid) return true;

        long existingMultiplier = getIntegerMultiplier(actual.contents, expected.contents);
        if (existingMultiplier < 0) return true;
        if (getMetaMachine(targetHost) instanceof IMultiTesseract) {
            return matchAggregatedTarget(target, expected, existingMultiplier > 0);
        }
        if (!expected.virtualInput.present) return false;

        var circuitState = readProgrammableInput(targetHost);
        if (!circuitState.supported) return true;
        boolean matches = matchesVirtualInput(circuitState, expected.virtualInput);
        return existingMultiplier > 0 ? !matches : !matches && !circuitState.reconfigurable;
    }

    public static boolean shouldBlockDistributed(List<TargetInput> targetInputs, long patternMultiplier) {
        var groups = new ArrayList<DistributedTarget>();
        for (var targetInput : targetInputs) {
            var target = targetInput.target;
            if (target.targetBlockEntity() == null || target.targetSide() == null) return true;

            DistributedTarget group = null;
            for (var existing : groups) {
                if (isSameDistributedTarget(existing.target, target)) {
                    group = existing;
                    break;
                }
                if (hasAmbiguousSharedTarget(existing.target, target)) return true;
            }
            if (group == null) {
                group = new DistributedTarget(target);
                groups.add(group);
            }
            group.inputs.add(targetInput.input);
            group.targetInputs.add(targetInput);
        }
        if (hasOverlappingDistributedEndpoints(groups, patternMultiplier)) return true;

        var virtualTargets = new ArrayList<DistributedVirtualTarget>();
        long commonMultiplier = -1;
        for (var group : groups) {
            var expected = readExpected(group.inputs, patternMultiplier);
            if (!expected.valid) return true;

            Object targetHost = resolveSingleTargetHost(group.target.targetBlockEntity(), group.target.targetSide());
            long groupMultiplier;
            if (targetHost instanceof InterfaceLogicHost interfaceHost &&
                    isSubnetStorage(group.target.storage(), interfaceHost)) {
                var match = matchInterface(group.target, interfaceHost, expected, false, true);
                if (match.blocked) return true;
                groupMultiplier = match.multiplier;
            } else {
                var match = matchDirectDistributedTarget(group.targetInputs, expected, false, true);
                if (match.blocked) return true;
                groupMultiplier = match.multiplier;
            }

            if (groupMultiplier < 0) return true;
            if (!expected.contents.isEmpty()) {
                if (commonMultiplier < 0) commonMultiplier = groupMultiplier;
                else if (commonMultiplier != groupMultiplier) return true;
            }
            if (expected.virtualInput.present) {
                // Different directed targets may intentionally configure different programmable slots. Conflicting
                // virtual inputs are only invalid inside one target group, which readExpected(...) already enforces.
                virtualTargets.add(new DistributedVirtualTarget(group, expected));
            }
        }

        for (var virtualTarget : virtualTargets) {
            var group = virtualTarget.target;
            var target = group.target;
            var expected = virtualTarget.expected;
            Object targetHost = resolveSingleTargetHost(target.targetBlockEntity(), target.targetSide());
            if (targetHost instanceof InterfaceLogicHost interfaceHost &&
                    isSubnetStorage(target.storage(), interfaceHost)) {
                if (matchInterface(target, interfaceHost, expected, commonMultiplier > 0, true).blocked) return true;
            } else if (matchDirectDistributedTarget(group.targetInputs, expected, commonMultiplier > 0, true).blocked) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasOverlappingDistributedEndpoints(List<DistributedTarget> groups,
                                                              long patternMultiplier) {
        if (groups.size() < 2) return false;

        var footprints = new ArrayList<List<ResolvedTarget>>(groups.size());
        for (var group : groups) {
            var footprint = resolveDistributedFootprint(group, patternMultiplier);
            if (footprint == null) return true;
            footprints.add(footprint);
        }

        for (int first = 0; first < footprints.size(); first++) {
            for (int second = first + 1; second < footprints.size(); second++) {
                for (var firstTarget : footprints.get(first)) {
                    for (var secondTarget : footprints.get(second)) {
                        if (isSameResolvedEndpoint(firstTarget, secondTarget) ||
                                firstTarget.blockEntity == secondTarget.blockEntity) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    private static List<ResolvedTarget> resolveDistributedFootprint(DistributedTarget group,
                                                                    long patternMultiplier) {
        var expected = readExpected(group.inputs, patternMultiplier);
        if (!expected.valid) return null;

        var result = new ArrayList<ResolvedTarget>();
        for (var targetInput : group.targetInputs) {
            var providerTarget = targetInput.target;
            var blockEntity = providerTarget.targetBlockEntity();
            var side = providerTarget.targetSide();
            if (blockEntity == null || side == null) return null;

            Object targetHost = resolveSingleTargetHost(blockEntity, side);
            if (targetHost instanceof InterfaceLogicHost interfaceHost &&
                    isSubnetStorage(providerTarget.storage(), interfaceHost)) {
                var subnetTargets = traceInterfaceFootprint(providerTarget, interfaceHost, expected);
                if (subnetTargets == null) return null;
                result.addAll(subnetTargets);
                break;
            } else {
                var path = Collections.newSetFromMap(new IdentityHashMap<BlockEntity, Boolean>());
                if (!collectResolvedTargets(blockEntity, side, true, providerTarget, result, path)) return null;
            }
        }
        return List.copyOf(result);
    }

    @Nullable
    private static List<ResolvedTarget> traceInterfaceFootprint(WrapMeStorage providerTarget,
                                                                InterfaceLogicHost interfaceHost,
                                                                ExpectedContent expected) {
        IGrid grid = getGrid(interfaceHost);
        if (grid == null || providerTarget.storage() != grid.getStorageService().getInventory() ||
                !(providerTarget.storage() instanceof NetworkStorage networkStorage) ||
                !((Object) networkStorage instanceof NetworkStorageAccessor networkAccessor)) {
            return null;
        }

        var storageBusMounts = new IdentityHashMap<MEStorage, StorageBusPart>();
        var visitedStorageBuses = Collections.newSetFromMap(new IdentityHashMap<StorageBusPart, Boolean>());
        for (var node : grid.getNodes()) {
            var provider = node.getService(IStorageProvider.class);
            if (provider instanceof StorageBusPart storageBus && visitedStorageBuses.add(storageBus)) {
                storageBus.mountInventories((storage, priority) -> storageBusMounts.put(storage, storageBus));
            }
        }

        var endpointPool = new EndpointSimulationPool(providerTarget);
        var busSimulations = new IdentityHashMap<StorageBusPart, StorageBusSimulation>();
        for (var input : expected.dispatchedInputs) {
            boolean virtualProvider = decodeVirtualInput(input.what()) != null;
            long remaining = input.amount();
            for (var mountObject : networkAccessor.gtocore$getPriorityInventory()) {
                if (!(mountObject instanceof NetworkStorageMountAccessor mount)) return null;

                var mountedStorage = mount.gtocore$getStorage();
                var storageBus = storageBusMounts.get(mountedStorage);
                if (storageBus == null) {
                    long accepted = simulateInsert(mountedStorage, input.what(), remaining, providerTarget);
                    if (accepted < 0 || accepted > 0) return null;
                    continue;
                }

                var simulation = busSimulations.computeIfAbsent(storageBus,
                        bus -> new StorageBusSimulation(bus, providerTarget, endpointPool));
                long accepted = simulation.insert(input.what(), remaining, virtualProvider);
                if (accepted < 0) return null;
                remaining -= accepted;
                if (remaining == 0) break;
            }
            if (remaining != 0) return null;
        }

        var result = new ArrayList<ResolvedTarget>();
        for (int i = 0; i < endpointPool.targets.size(); i++) {
            if (endpointPool.simulations.get(i).used) result.add(endpointPool.targets.get(i));
        }
        return List.copyOf(result);
    }

    private static boolean hasAmbiguousSharedTarget(WrapMeStorage first, WrapMeStorage second) {
        if (first.targetSide() == second.targetSide()) return false;
        if (first.targetBlockEntity() == second.targetBlockEntity()) return true;

        Object firstHost = resolveSingleTargetHost(first.targetBlockEntity(), first.targetSide());
        Object secondHost = resolveSingleTargetHost(second.targetBlockEntity(), second.targetSide());
        return firstHost != null && firstHost == secondHost;
    }

    private static boolean isSameDistributedTarget(WrapMeStorage first, WrapMeStorage second) {
        // Two different interfaces may expose the same subnet inventory. More generally, wrappers that delegate to
        // the exact same MEStorage must share one stateful routing simulation or sparse inputs could independently
        // reserve the same physical slot.
        if (first.storage() == second.storage()) return true;
        if (first.targetBlockEntity() == second.targetBlockEntity() && first.targetSide() == second.targetSide()) {
            return true;
        }

        // Separate single-target tesseracts may still resolve to the same physical inventory.
        Object firstHost = resolveSingleTargetHost(first.targetBlockEntity(), first.targetSide());
        Object secondHost = resolveSingleTargetHost(second.targetBlockEntity(), second.targetSide());
        if (firstHost != null && firstHost == secondHost && first.targetSide() == second.targetSide()) return true;

        // GT machines commonly expose the same handler instance on multiple faces. Treating those faces as separate
        // targets lets each sparse input reserve the same slot independently. Only merge when the complete Forge
        // item/fluid capability pair is identical; different sided wrappers may have independent filters and must
        // retain their own routing simulation.
        var firstTarget = resolveSingleTargetBlockEntity(first.targetBlockEntity());
        var secondTarget = resolveSingleTargetBlockEntity(second.targetBlockEntity());
        return firstTarget != null && firstTarget == secondTarget && exposesSameForgeStorage(
                firstTarget, first.targetSide(), secondTarget, second.targetSide());
    }

    private static boolean exposesSameForgeStorage(BlockEntity firstTarget, Direction firstSide,
                                                   BlockEntity secondTarget, Direction secondSide) {
        var firstItem = firstTarget.getCapability(ForgeCapabilities.ITEM_HANDLER, firstSide).orElse(null);
        var secondItem = secondTarget.getCapability(ForgeCapabilities.ITEM_HANDLER, secondSide).orElse(null);
        var firstFluid = firstTarget.getCapability(ForgeCapabilities.FLUID_HANDLER, firstSide).orElse(null);
        var secondFluid = secondTarget.getCapability(ForgeCapabilities.FLUID_HANDLER, secondSide).orElse(null);
        return (firstItem != null || firstFluid != null) && firstItem == secondItem && firstFluid == secondFluid;
    }

    private static ExpectedContent readExpected(List<GenericStack> orderedInputs, long patternMultiplier) {
        if (patternMultiplier <= 0) return ExpectedContent.invalid();

        var contents = new KeyCounter();
        var dispatchedInputs = new ArrayList<GenericStack>(orderedInputs.size());
        var virtualInput = VirtualInput.absent();
        boolean valid = true;
        for (var input : orderedInputs) {
            if (input == null || input.what() == null || input.amount() <= 0) {
                valid = false;
                continue;
            }
            dispatchedInputs.add(input);

            var decodedVirtualInput = decodeVirtualInput(input.what());
            if (decodedVirtualInput != null) {
                if (!decodedVirtualInput.valid) valid = false;
                else if (virtualInput.present && !Objects.equals(virtualInput.configured, decodedVirtualInput.input.configured)) valid = false;
                else virtualInput = decodedVirtualInput.input;
                continue;
            }

            if (input.amount() % patternMultiplier != 0) {
                valid = false;
                continue;
            }
            long baseAmount = input.amount() / patternMultiplier;
            if (baseAmount <= 0 || !addExact(contents, input.what(), baseAmount)) valid = false;
        }
        if (contents.isEmpty() && !virtualInput.present) valid = false;
        return new ExpectedContent(contents, List.copyOf(dispatchedInputs), virtualInput, valid);
    }

    private static ActualContent readActual(MEStorage storage, boolean ignoreVirtualProviders) {
        var snapshot = new KeyCounter();
        storage.getAvailableStacks(snapshot);

        var contents = new KeyCounter();
        boolean valid = true;
        for (var entry : snapshot) {
            long amount = entry.getLongValue();
            if (amount < 0) {
                valid = false;
                continue;
            }
            if (amount == 0) continue;
            var decodedVirtualInput = decodeVirtualInput(entry.getKey());
            if (decodedVirtualInput != null) {
                if (!decodedVirtualInput.valid) valid = false;
                else if (!ignoreVirtualProviders) valid = false;
                continue;
            }
            if (!addExact(contents, entry.getKey(), amount)) valid = false;
        }
        return new ActualContent(contents, valid);
    }

    private static boolean addExact(KeyCounter counter, AEKey key, long amount) {
        try {
            counter.set(key, Math.addExact(counter.get(key), amount));
            return true;
        } catch (ArithmeticException ignored) {
            return false;
        }
    }

    /**
     * Returns the shared non-negative integer multiplier, or {@code -1} if the vectors do not match exactly.
     */
    private static long getIntegerMultiplier(KeyCounter actual, KeyCounter expected) {
        if (actual.isEmpty()) return 0;
        if (expected.isEmpty() || actual.size() != expected.size()) return -1;

        long multiplier = -1;
        for (var entry : expected) {
            long expectedAmount = entry.getLongValue();
            long actualAmount = actual.get(entry.getKey());
            if (expectedAmount <= 0 || actualAmount <= 0 || actualAmount % expectedAmount != 0) return -1;

            long currentMultiplier = actualAmount / expectedAmount;
            if (currentMultiplier <= 0) return -1;
            if (multiplier < 0) multiplier = currentMultiplier;
            else if (multiplier != currentMultiplier) return -1;
        }

        for (var entry : actual) {
            if (entry.getLongValue() <= 0 || expected.get(entry.getKey()) <= 0) return -1;
        }
        return multiplier;
    }

    @Nullable
    private static DecodedVirtualInput decodeVirtualInput(AEKey key) {
        if (!(key instanceof AEItemKey itemKey) || itemKey.getItem() != CustomItems.VIRTUAL_ITEM_PROVIDER.get()) {
            return null;
        }
        var tag = itemKey.getTag();
        if (tag == null || !tag.contains("n")) {
            return new DecodedVirtualInput(new VirtualInput(true, null), false);
        }

        ItemStack virtualStack = VirtualItemProviderBehavior.getVirtualItem(itemKey.getReadOnlyStack());
        if (!virtualStack.isEmpty()) {
            return new DecodedVirtualInput(new VirtualInput(true, AEItemKey.of(virtualStack)), true);
        }
        boolean explicitlyEmpty = "air".equals(tag.getString("n")) && tag.getString("m").isEmpty();
        return new DecodedVirtualInput(new VirtualInput(true, null), explicitlyEmpty);
    }

    private static boolean isSubnetStorage(MEStorage storage, InterfaceLogicHost interfaceHost) {
        IGrid grid = getGrid(interfaceHost);
        return grid != null && storage == grid.getStorageService().getInventory();
    }

    private static InterfaceMatch matchInterface(WrapMeStorage providerTarget, InterfaceLogicHost interfaceHost,
                                                 ExpectedContent expected, boolean requireVirtualMatch,
                                                 boolean requireCommonMultiplier) {
        IGrid grid = getGrid(interfaceHost);
        if (grid == null || providerTarget.storage() != grid.getStorageService().getInventory() ||
                !(providerTarget.storage() instanceof NetworkStorage networkStorage) ||
                !((Object) networkStorage instanceof NetworkStorageAccessor networkAccessor)) {
            return InterfaceMatch.blockedResult();
        }

        var storageBusMounts = new IdentityHashMap<MEStorage, StorageBusPart>();
        var visitedStorageBuses = Collections.newSetFromMap(new IdentityHashMap<StorageBusPart, Boolean>());
        for (var node : grid.getNodes()) {
            var provider = node.getService(IStorageProvider.class);
            if (provider instanceof StorageBusPart storageBus && visitedStorageBuses.add(storageBus)) {
                storageBus.mountInventories((storage, priority) -> storageBusMounts.put(storage, storageBus));
            }
        }

        var busSimulations = new IdentityHashMap<StorageBusPart, StorageBusSimulation>();
        var endpointPool = new EndpointSimulationPool(providerTarget);
        for (var input : expected.dispatchedInputs) {
            boolean virtualProvider = decodeVirtualInput(input.what()) != null;
            long remaining = input.amount();
            for (var mountObject : networkAccessor.gtocore$getPriorityInventory()) {
                if (!(mountObject instanceof NetworkStorageMountAccessor mount)) {
                    return InterfaceMatch.blockedResult();
                }

                var mountedStorage = mount.gtocore$getStorage();
                var storageBus = storageBusMounts.get(mountedStorage);
                if (storageBus == null) {
                    // Drives, cells, formation planes, crafting CPUs and other mounts cannot be mapped to one
                    // validated physical processing container.
                    long accepted = simulateInsert(mountedStorage, input.what(), remaining, providerTarget);
                    if (accepted < 0 || accepted > 0) return InterfaceMatch.blockedResult();
                    continue;
                }

                var simulation = busSimulations.computeIfAbsent(storageBus,
                        bus -> new StorageBusSimulation(bus, providerTarget, endpointPool));
                long accepted = simulation.insert(input.what(), remaining, virtualProvider);
                if (accepted < 0) return InterfaceMatch.blockedResult();
                remaining -= accepted;
                if (remaining == 0) break;
            }
            if (remaining != 0) return InterfaceMatch.blockedResult();
        }

        long commonMultiplier = -1;
        boolean foundRoutedInput = false;
        for (var simulation : busSimulations.values()) {
            if (!simulation.used) continue;
            foundRoutedInput = true;

            var match = simulation.validate(expected, requireVirtualMatch, requireCommonMultiplier);
            if (match.blocked) return InterfaceMatch.blockedResult();
            if (!simulation.physicalInputs.isEmpty()) {
                if (commonMultiplier < 0) commonMultiplier = match.multiplier;
                else if (requireCommonMultiplier && commonMultiplier != match.multiplier)
                    return InterfaceMatch.blockedResult();
            }
        }
        if (!foundRoutedInput) return InterfaceMatch.blockedResult();
        return new InterfaceMatch(false, Math.max(0, commonMultiplier));
    }

    private static boolean matchAggregatedTarget(WrapMeStorage providerTarget,
                                                 ExpectedContent expected,
                                                 boolean requireVirtualMatch) {
        if (providerTarget.targetBlockEntity() == null || providerTarget.targetSide() == null) return true;

        var resolvedTargets = new ArrayList<ResolvedTarget>();
        var path = Collections.newSetFromMap(new IdentityHashMap<BlockEntity, Boolean>());
        if (!collectResolvedTargets(providerTarget.targetBlockEntity(), providerTarget.targetSide(), true,
                providerTarget, resolvedTargets, path) || resolvedTargets.isEmpty()) {
            return true;
        }

        var endpointPool = new EndpointSimulationPool(providerTarget);
        var endpoints = new ArrayList<EndpointSimulation>(resolvedTargets.size());
        for (var target : resolvedTargets) {
            var endpoint = endpointPool.getOrCreate(target);
            if (endpoint == null) return true;
            endpoints.add(endpoint);
        }

        var physicalInputs = new KeyCounter();
        for (var input : expected.dispatchedInputs) {
            boolean virtualProvider = decodeVirtualInput(input.what()) != null;
            long acceptedByTarget = simulateInsert(providerTarget.storage(), input.what(), input.amount(),
                    providerTarget);
            if (acceptedByTarget != input.amount()) return true;

            long remaining = acceptedByTarget;
            for (var endpoint : endpoints) {
                long accepted = endpoint.insert(input.what(), remaining, virtualProvider);
                if (accepted < 0) return true;
                remaining -= accepted;
                if (remaining == 0) break;
            }
            if (remaining != 0) return true;
            if (!virtualProvider && !addExact(physicalInputs, input.what(), acceptedByTarget)) return true;
        }
        if (getIntegerMultiplier(physicalInputs, expected.contents) < 0) return true;
        if (!expected.virtualInput.present) return false;

        boolean foundVirtualTarget = false;
        for (var endpoint : endpoints) {
            boolean virtualRoutedHere = !endpoint.virtualProviders.isEmpty();
            boolean physicalRoutedHere = !endpoint.physicalInputs.isEmpty();
            if (!virtualRoutedHere && !physicalRoutedHere) continue;

            if (virtualRoutedHere) foundVirtualTarget = true;
            if (!endpoint.initialCircuit.supported) return true;

            boolean matches = matchesVirtualInput(endpoint.initialCircuit, expected.virtualInput);
            boolean mustAlreadyMatch = requireVirtualMatch || physicalRoutedHere && !virtualRoutedHere;
            if (!matches && (mustAlreadyMatch || virtualRoutedHere && !endpoint.initialCircuit.reconfigurable)) {
                return true;
            }
        }
        return !foundVirtualTarget;
    }

    private static InterfaceMatch matchDirectDistributedTarget(List<TargetInput> targetInputs,
                                                               ExpectedContent expected,
                                                               boolean requireVirtualMatch,
                                                               boolean requireCommonMultiplier) {
        var physicalInputs = new KeyCounter();
        if (targetInputs.isEmpty()) return InterfaceMatch.blockedResult();
        var endpointPool = new EndpointSimulationPool(targetInputs.getFirst().target);
        for (var targetInput : targetInputs) {
            var providerTarget = targetInput.target;
            var input = targetInput.input;
            if (providerTarget.targetBlockEntity() == null || providerTarget.targetSide() == null) {
                return InterfaceMatch.blockedResult();
            }

            var routeTargets = new ArrayList<ResolvedTarget>();
            var path = Collections.newSetFromMap(new IdentityHashMap<BlockEntity, Boolean>());
            if (!collectResolvedTargets(providerTarget.targetBlockEntity(), providerTarget.targetSide(), true,
                    providerTarget, routeTargets, path) || routeTargets.isEmpty()) {
                return InterfaceMatch.blockedResult();
            }

            var routeEndpoints = new ArrayList<EndpointSimulation>(routeTargets.size());
            for (var routeTarget : routeTargets) {
                var endpoint = endpointPool.getOrCreate(routeTarget);
                if (endpoint == null) return InterfaceMatch.blockedResult();
                routeEndpoints.add(endpoint);
            }

            boolean virtualProvider = decodeVirtualInput(input.what()) != null;
            long acceptedByTarget = simulateInsert(providerTarget.storage(), input.what(), input.amount(),
                    providerTarget);
            if (acceptedByTarget != input.amount()) return InterfaceMatch.blockedResult();

            long remaining = acceptedByTarget;
            for (var endpoint : routeEndpoints) {
                long accepted = endpoint.insert(input.what(), remaining, virtualProvider);
                if (accepted < 0) return InterfaceMatch.blockedResult();
                remaining -= accepted;
                if (remaining == 0) break;
            }
            if (remaining != 0) return InterfaceMatch.blockedResult();
            if (!virtualProvider && !addExact(physicalInputs, input.what(), acceptedByTarget)) {
                return InterfaceMatch.blockedResult();
            }
        }

        if (getIntegerMultiplier(physicalInputs, expected.contents) < 0) {
            return InterfaceMatch.blockedResult();
        }

        long commonMultiplier = -1;
        boolean foundRoutedInput = false;
        for (var endpoint : endpointPool.simulations) {
            if (!endpoint.used) continue;
            foundRoutedInput = true;

            var match = endpoint.validate(expected, requireVirtualMatch);
            if (match.blocked) return InterfaceMatch.blockedResult();
            if (!endpoint.physicalInputs.isEmpty()) {
                if (commonMultiplier < 0) commonMultiplier = match.multiplier;
                else if (requireCommonMultiplier && commonMultiplier != match.multiplier) {
                    return InterfaceMatch.blockedResult();
                }
            }
        }
        if (!foundRoutedInput) return InterfaceMatch.blockedResult();
        return new InterfaceMatch(false, Math.max(0, commonMultiplier));
    }

    private static boolean isSameResolvedEndpoint(ResolvedTarget first, ResolvedTarget second) {
        if (first.storage == second.storage) return true;
        if (first.blockEntity == second.blockEntity && first.side == second.side) return true;
        return exposesSameForgeStorage(first.blockEntity, first.side, second.blockEntity, second.side);
    }

    private static StorageBusRoute createStorageBusRoute(StorageBusPart storageBus) {
        var filterBuilder = IPartitionList.builder();
        if (storageBus.isUpgradedWith(AEItems.FUZZY_CARD)) {
            filterBuilder.fuzzyMode(storageBus.getConfigManager().getSetting(Settings.FUZZY_MODE));
        }

        int slotsToUse = 18 + storageBus.getInstalledUpgrades(AEItems.CAPACITY_CARD) * 9;
        var config = storageBus.getConfig();
        for (int slot = 0; slot < config.size() && slot < slotsToUse; slot++) {
            filterBuilder.add(config.getKey(slot));
        }

        var filterMode = storageBus.isUpgradedWith(AEItems.INVERTER_CARD) ? IncludeExclude.BLACKLIST : IncludeExclude.WHITELIST;
        boolean allowInsertion = storageBus.getConfigManager().getSetting(Settings.ACCESS).isAllowInsertion();
        return new StorageBusRoute(filterBuilder.build(), filterMode, allowInsertion,
                storageBus.isUpgradedWith(AEItems.VOID_CARD));
    }

    private static long simulateInsert(MEStorage storage, AEKey key, long amount,
                                       WrapMeStorage providerTarget) {
        if (amount <= 0) return -1;
        long accepted = storage.insert(key, amount, Actionable.SIMULATE, providerTarget.src());
        return accepted < 0 || accepted > amount ? -1 : accepted;
    }

    private static TargetResolution resolveStorageBusTargets(StorageBusPart storageBus,
                                                             WrapMeStorage providerTarget) {
        var blockEntity = storageBus.getBlockEntity();
        var level = blockEntity.getLevel();
        if (level == null) return TargetResolution.invalid();

        Direction side = storageBus.getSide();
        var targetBlockEntity = ILevel.getCachedBlockEntity(level, blockEntity.getBlockPos().relative(side));
        if (targetBlockEntity == null) return TargetResolution.invalid();

        var targets = new ArrayList<ResolvedTarget>();
        var path = Collections.newSetFromMap(new IdentityHashMap<BlockEntity, Boolean>());
        boolean valid = collectResolvedTargets(targetBlockEntity, side.getOpposite(), false,
                providerTarget, targets, path);
        return new TargetResolution(List.copyOf(targets), valid);
    }

    private static boolean collectResolvedTargets(BlockEntity target, Direction targetSide, boolean forgeOnly,
                                                  WrapMeStorage providerTarget, List<ResolvedTarget> targets,
                                                  Set<BlockEntity> path) {
        if (!path.add(target)) return false;

        MetaMachine machine = MetaMachine.getMachine(target);
        if (machine instanceof TesseractMachine tesseract) {
            var level = tesseract.getLevel();
            if (level == null || tesseract.pos == null) {
                path.remove(target);
                return false;
            }
            var next = ILevel.getCachedBlockEntity(level, tesseract.pos);
            boolean valid = next != null && collectResolvedTargets(next, targetSide, true,
                    providerTarget, targets, path);
            path.remove(target);
            return valid;
        }

        if (machine instanceof IMultiTesseract multiTesseract) {
            int totalTargets = multiTesseract.getTotalBlockEntities();
            if (totalTargets <= 0) {
                path.remove(target);
                return false;
            }
            for (int i = 0; i < totalTargets; i++) {
                var next = multiTesseract.getBlockEntity(i);
                var nextSide = multiTesseract.getSideForBlockEntity(i, targetSide);
                // IMultiTesseract only aggregates GT custom Forge handlers. MEStorage-only endpoints are not part of
                // the capability exposed to the storage bus and must not affect route validation.
                if (next == null || nextSide == null || !hasMultiTesseractCapability(next, nextSide)) continue;
                if (!collectResolvedTargets(next, nextSide, true, providerTarget, targets, path)) {
                    path.remove(target);
                    return false;
                }
            }
            path.remove(target);
            return true;
        }

        Object host = target;
        if (target instanceof IPartHost partHost) {
            var part = partHost.getPart(targetSide);
            if (part != null) host = part;
        }

        MEStorage storage = forgeOnly ? null : target.getCapability(Capabilities.STORAGE, targetSide).orElse(null);
        boolean forgeStorage = storage == null;
        if (storage == null) {
            var patternTarget = PatternProviderTargetCache.find(target, providerTarget.logic(), targetSide,
                    providerTarget.src(), target.getBlockPos().asLong());
            if (patternTarget instanceof WrapMeStorage wrapped) storage = wrapped.storage();
        }
        if (storage == null) {
            path.remove(target);
            return false;
        }

        boolean duplicate = false;
        for (var existing : targets) {
            if (existing.blockEntity == target && existing.side == targetSide) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) targets.add(new ResolvedTarget(target, targetSide, host, storage, forgeStorage));
        path.remove(target);
        return true;
    }

    private static boolean hasMultiTesseractCapability(BlockEntity target, Direction targetSide) {
        var itemHandler = target.getCapability(ForgeCapabilities.ITEM_HANDLER, targetSide).orElse(null);
        if (itemHandler instanceof ICustomItemStackHandler) return true;
        var fluidHandler = target.getCapability(ForgeCapabilities.FLUID_HANDLER, targetSide).orElse(null);
        return fluidHandler instanceof ICustomFluidStackHandler;
    }

    @Nullable
    private static IGrid getGrid(InterfaceLogicHost interfaceHost) {
        if (interfaceHost instanceof AEBasePart part) {
            return part.getMainNode().getGrid();
        }
        var node = interfaceHost.getInterfaceLogic().getActionableNode();
        return node == null ? null : node.getGrid();
    }

    @Nullable
    private static Object resolveSingleTargetHost(BlockEntity initialTarget, Direction targetSide) {
        BlockEntity target = resolveSingleTargetBlockEntity(initialTarget);
        if (target == null) return null;
        if (target instanceof IPartHost partHost) {
            var part = partHost.getPart(targetSide);
            if (part != null) return part;
        }
        return target;
    }

    @Nullable
    private static BlockEntity resolveSingleTargetBlockEntity(BlockEntity initialTarget) {
        Set<BlockEntity> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        BlockEntity target = initialTarget;
        while (true) {
            if (!visited.add(target)) return null;
            MetaMachine machine = MetaMachine.getMachine(target);
            if (!(machine instanceof TesseractMachine tesseract)) {
                return target;
            }
            if (tesseract.getLevel() == null || tesseract.pos == null) return null;
            BlockEntity next = ILevel.getCachedBlockEntity(tesseract.getLevel(), tesseract.pos);
            if (next == null) return null;
            target = next;
        }
    }

    private static CircuitState readProgrammableInput(@Nullable Object targetHost) {
        MetaMachine machine = getMetaMachine(targetHost);
        if (machine == null) return CircuitState.unsupported();

        int found = 0;
        @Nullable
        AEItemKey configured = null;
        for (var trait : machine.getTraits()) {
            if (!(trait instanceof ProgrammableCircuitHandler circuitHandler)) continue;

            ItemStack stack = circuitHandler.storage.getStackInSlot(0);
            AEItemKey current = stack.isEmpty() ? null : AEItemKey.of(stack);
            if (found > 0 && !Objects.equals(configured, current)) return CircuitState.unsupported();
            found++;
            configured = current;
        }
        return found > 0 ? new CircuitState(true, found == 1, configured) : CircuitState.unsupported();
    }

    @Nullable
    private static MetaMachine getMetaMachine(@Nullable Object targetHost) {
        return switch (targetHost) {
            case MetaMachineBlockEntity blockEntity -> blockEntity.getMetaMachine();
            case MetaMachine metaMachine -> metaMachine;
            default -> null;
        };
    }

    private static boolean hasHiddenOutputStorage(@Nullable Object targetHost, @Nullable Direction side) {
        var machine = getMetaMachine(targetHost);
        if (machine == null || side == null) return false;

        for (var handler : GTCapabilityHelper.getCapabilitiesFromTraits(machine.getTraits(), side,
                ICustomItemStackHandler.class)) {
            if (handler instanceof VoidOutputItemHandler || handler instanceof MEOutputItemHandler) return true;
        }
        for (var handler : GTCapabilityHelper.getCapabilitiesFromTraits(machine.getTraits(), side,
                ICustomFluidStackHandler.class)) {
            if (handler instanceof VoidOutputFluidHandler || handler instanceof MEOutputFluidHandler) return true;
        }
        return false;
    }

    private static boolean isVirtualItemProviderHost(@Nullable Object targetHost) {
        return switch (targetHost) {
            case MetaMachineBlockEntity blockEntity -> blockEntity.getMetaMachine() instanceof VirtualItemProviderMachine;
            case MetaMachine metaMachine -> metaMachine instanceof VirtualItemProviderMachine;
            default -> false;
        };
    }

    private static boolean matchesVirtualInput(CircuitState actual, VirtualInput expected) {
        return actual.supported && expected.present && Objects.equals(actual.configured, expected.configured);
    }

    private record ExpectedContent(KeyCounter contents, List<GenericStack> dispatchedInputs,
                                   VirtualInput virtualInput,
                                   boolean valid) {

        private static ExpectedContent invalid() {
            return new ExpectedContent(new KeyCounter(), List.of(), VirtualInput.absent(), false);
        }
    }

    private record ActualContent(KeyCounter contents, boolean valid) {}

    private record VirtualInput(boolean present, @Nullable AEItemKey configured) {

        private static VirtualInput absent() {
            return new VirtualInput(false, null);
        }
    }

    private record DecodedVirtualInput(VirtualInput input, boolean valid) {}

    private record CircuitState(boolean supported, boolean reconfigurable, @Nullable AEItemKey configured) {

        private static CircuitState unsupported() {
            return new CircuitState(false, false, null);
        }
    }

    private record ResolvedTarget(BlockEntity blockEntity, Direction side, Object host, MEStorage storage,
                                  boolean forgeStorage) {}

    private record TargetResolution(List<ResolvedTarget> targets, boolean valid) {

        private static TargetResolution invalid() {
            return new TargetResolution(List.of(), false);
        }
    }

    private record InterfaceMatch(boolean blocked, long multiplier) {

        private static InterfaceMatch blockedResult() {
            return new InterfaceMatch(true, -1);
        }
    }

    private record StorageBusMatch(boolean blocked, long multiplier) {

        private static StorageBusMatch blockedResult() {
            return new StorageBusMatch(true, -1);
        }
    }

    private record StorageBusRoute(IPartitionList filter, IncludeExclude filterMode,
                                   boolean allowInsertion, boolean voidOverflow) {

        private boolean accepts(AEKey key) {
            return allowInsertion && filter.matchesFilter(key, filterMode);
        }
    }

    private static final class StorageBusSimulation {

        private final StorageBusPart storageBus;
        private final WrapMeStorage providerTarget;
        private final EndpointSimulationPool endpointPool;
        private final StorageBusRoute route;
        private final KeyCounter physicalInputs = new KeyCounter();
        private final KeyCounter virtualProviders = new KeyCounter();
        private List<EndpointSimulation> endpoints = List.of();
        private boolean initialized;
        private boolean valid = true;
        private boolean used;

        private StorageBusSimulation(StorageBusPart storageBus, WrapMeStorage providerTarget,
                                     EndpointSimulationPool endpointPool) {
            this.storageBus = storageBus;
            this.providerTarget = providerTarget;
            this.endpointPool = endpointPool;
            this.route = createStorageBusRoute(storageBus);
        }

        private long insert(AEKey key, long amount, boolean virtualProvider) {
            if (amount <= 0) return -1;
            if (!route.accepts(key)) return 0;

            long rawLimit = simulateInsert(storageBus.getInternalHandler(), key, amount, providerTarget);
            if (rawLimit < 0) return -1;
            if (rawLimit == 0) return route.voidOverflow ? -1 : 0;
            if (!initialize()) return -1;

            long remaining = rawLimit;
            for (var endpoint : endpoints) {
                long accepted = endpoint.insert(key, remaining, virtualProvider);
                if (accepted < 0) return -1;
                remaining -= accepted;
                if (remaining == 0) break;
            }

            // The mounted StorageBusInventory delegates exactly rawLimit to the physical handler. If the endpoint
            // snapshots cannot account for that full amount, continuing with a lower-priority mount would predict a
            // route that NetworkStorage will not take.
            if (remaining != 0 || rawLimit < amount && route.voidOverflow) return -1;

            used = true;
            var counter = virtualProvider ? virtualProviders : physicalInputs;
            if (!addExact(counter, key, rawLimit)) return -1;
            return rawLimit;
        }

        private boolean initialize() {
            if (initialized) return valid;
            initialized = true;

            var resolution = resolveStorageBusTargets(storageBus, providerTarget);
            if (!resolution.valid || resolution.targets.isEmpty()) {
                valid = false;
                return false;
            }
            var simulations = new ArrayList<EndpointSimulation>(resolution.targets.size());
            for (var target : resolution.targets) {
                var simulation = endpointPool.getOrCreate(target);
                if (simulation == null) {
                    valid = false;
                    return false;
                }
                simulations.add(simulation);
            }
            endpoints = List.copyOf(simulations);
            return true;
        }

        private StorageBusMatch validate(ExpectedContent expected, boolean requireVirtualMatch,
                                         boolean requireCommonMultiplier) {
            if (!valid) {
                return StorageBusMatch.blockedResult();
            }

            long commonMultiplier = -1;
            for (var endpoint : endpoints) {
                if (!endpoint.used) continue;
                var match = endpoint.validate(expected, requireVirtualMatch);
                if (match.blocked) return StorageBusMatch.blockedResult();
                if (!endpoint.physicalInputs.isEmpty()) {
                    if (commonMultiplier < 0) commonMultiplier = match.multiplier;
                    else if (requireCommonMultiplier && commonMultiplier != match.multiplier) {
                        return StorageBusMatch.blockedResult();
                    }
                }
            }
            return new StorageBusMatch(false, Math.max(0, commonMultiplier));
        }
    }

    private static final class EndpointSimulationPool {

        private final WrapMeStorage providerTarget;
        private final List<ResolvedTarget> targets = new ArrayList<>();
        private final List<EndpointSimulation> simulations = new ArrayList<>();

        private EndpointSimulationPool(WrapMeStorage providerTarget) {
            this.providerTarget = providerTarget;
        }

        @Nullable
        private EndpointSimulation getOrCreate(ResolvedTarget target) {
            for (int i = 0; i < targets.size(); i++) {
                if (isSameResolvedEndpoint(targets.get(i), target)) return simulations.get(i);
                // Different sided wrappers on one block entity may still share backing state. If capability identity
                // cannot prove aliasing or independence, separate reservations would be unsafe.
                if (targets.get(i).blockEntity == target.blockEntity) return null;
            }

            var simulation = new EndpointSimulation(target, providerTarget);
            if (!simulation.valid) return null;
            targets.add(target);
            simulations.add(simulation);
            return simulation;
        }
    }

    private static final class EndpointSimulation {

        private final ResolvedTarget target;
        private final WrapMeStorage providerTarget;
        @Nullable
        private final IItemHandler itemHandler;
        @Nullable
        private final IFluidHandler fluidHandler;
        private final List<ItemRouteGroup> itemGroups;
        private final List<FluidRouteGroup> fluidGroups;
        private final AEItemKey[] itemKeys;
        private final long[] itemAmounts;
        private final long[] itemCapacities;
        private final FluidStack[] fluidTanks;
        private final boolean genericStorage;
        private final CircuitState initialCircuit;
        private final KeyCounter physicalInputs = new KeyCounter();
        private final KeyCounter virtualProviders = new KeyCounter();
        @Nullable
        private AEKey genericKey;
        private long genericRemaining;
        private boolean valid = true;
        private boolean used;

        private EndpointSimulation(ResolvedTarget target, WrapMeStorage providerTarget) {
            this.target = target;
            this.providerTarget = providerTarget;
            this.initialCircuit = readProgrammableInput(target.host);
            boolean hiddenOutputStorage = hasHiddenOutputStorage(target.host, target.side);

            if (target.host instanceof InterfaceLogicHost && target.storage instanceof NetworkStorage) {
                this.itemHandler = null;
                this.fluidHandler = null;
                this.itemGroups = List.of();
                this.fluidGroups = List.of();
                this.itemKeys = new AEItemKey[0];
                this.itemAmounts = new long[0];
                this.itemCapacities = new long[0];
                this.fluidTanks = new FluidStack[0];
                this.genericStorage = false;
                this.valid = false;
                return;
            }

            if (target.forgeStorage) {
                this.itemHandler = target.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, target.side)
                        .orElse(null);
                this.fluidHandler = target.blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, target.side)
                        .orElse(null);
                this.itemGroups = createItemRouteGroups(target, itemHandler);
                this.fluidGroups = createFluidRouteGroups(target, fluidHandler);
                this.itemKeys = snapshotItemKeys(itemHandler);
                this.itemAmounts = snapshotItemAmounts(itemHandler);
                this.itemCapacities = new long[itemAmounts.length];
                java.util.Arrays.fill(itemCapacities, -1);
                this.fluidTanks = snapshotFluids(fluidHandler);
                this.genericStorage = false;
                if (itemHandler == null && fluidHandler == null) this.valid = false;
            } else {
                this.itemHandler = null;
                this.fluidHandler = null;
                this.itemGroups = List.of();
                this.fluidGroups = List.of();
                this.itemKeys = new AEItemKey[0];
                this.itemAmounts = new long[0];
                this.itemCapacities = new long[0];
                this.fluidTanks = new FluidStack[0];
                this.genericStorage = true;
            }
            if (hiddenOutputStorage) this.valid = false;
        }

        private long insert(AEKey key, long amount, boolean virtualProvider) {
            if (!valid || amount <= 0) return -1;

            long accepted;
            if (target.forgeStorage) {
                long endpointLimit = simulateInsert(target.storage, key, amount, providerTarget);
                if (endpointLimit < 0) return -1;

                if (virtualProvider && initialCircuit.supported) {
                    accepted = endpointLimit;
                } else if (key instanceof AEItemKey itemKey && itemHandler != null) {
                    accepted = insertItem(itemKey, endpointLimit);
                } else if (key instanceof AEFluidKey fluidKey && fluidHandler != null) {
                    accepted = insertFluid(fluidKey, endpointLimit);
                } else {
                    accepted = 0;
                }
                if (accepted != endpointLimit) accepted = -1;
            } else if (genericStorage) {
                accepted = insertGeneric(key, amount);
            } else {
                accepted = 0;
            }

            if (accepted < 0) {
                valid = false;
                return -1;
            }
            if (accepted > 0) {
                used = true;
                var counter = virtualProvider ? virtualProviders : physicalInputs;
                if (!addExact(counter, key, accepted)) {
                    valid = false;
                    return -1;
                }
            }
            return accepted;
        }

        private long insertItem(AEItemKey key, long amount) {
            int request = (int) Math.min(amount, Integer.MAX_VALUE);
            int remaining = request;
            for (var group : itemGroups) {
                if (remaining <= 0) break;

                if (group.mode == ItemRouteMode.OPAQUE) {
                    if (group.used) return -1;
                    int accepted = group.handler.insertExternal(key, remaining, Actionable.SIMULATE);
                    if (accepted < 0 || accepted > remaining) return -1;
                    int unallocated = insertItemIntoSlots(key, accepted, group, false);
                    if (unallocated != 0) return -1;
                    if (accepted > 0) group.used = true;
                    remaining -= accepted;
                    continue;
                }
                if (group.mode == ItemRouteMode.REJECT) return -1;

                remaining = insertItemIntoSlots(key, remaining, group,
                        group.mode == ItemRouteMode.INPUT_LIMITED);
            }
            return request - remaining;
        }

        private int insertItemIntoSlots(AEItemKey key, int remaining, ItemRouteGroup group,
                                        boolean inputLimited) {
            for (int slot = group.firstSlot; slot < group.endSlot && remaining > 0; slot++) {
                if (inputLimited && hasSameItemInAnotherSlot(group, slot, key)) continue;
                var existingKey = itemKeys[slot];
                if (existingKey != null && !existingKey.equals(key)) continue;

                var input = key.toStack(remaining);
                var remainder = itemHandler.insertItem(slot, input.copy(), true);
                if (!remainder.isEmpty() && !key.matches(remainder)) return -1;
                int handlerAccepted = input.getCount() - remainder.getCount();
                if (handlerAccepted < 0 || handlerAccepted > input.getCount()) return -1;

                if (itemCapacities[slot] < 0) {
                    var capacityProbe = key.toStack(Integer.MAX_VALUE);
                    var capacityRemainder = itemHandler.insertItem(slot, capacityProbe.copy(), true);
                    if (!capacityRemainder.isEmpty() && !key.matches(capacityRemainder)) return -1;
                    int additionalCapacity = capacityProbe.getCount() - capacityRemainder.getCount();
                    if (additionalCapacity < 0 || additionalCapacity > capacityProbe.getCount()) return -1;
                    itemCapacities[slot] = itemAmounts[slot] + additionalCapacity;
                }

                long available = Math.max(0, itemCapacities[slot] - itemAmounts[slot]);
                int accepted = (int) Math.min(handlerAccepted, available);
                if (accepted <= 0) continue;

                if (existingKey == null) itemKeys[slot] = key;
                itemAmounts[slot] += accepted;
                remaining -= accepted;
                if (inputLimited) break;
            }
            return remaining;
        }

        private boolean hasSameItemInAnotherSlot(ItemRouteGroup group, int slot, AEItemKey key) {
            for (int other = group.firstSlot; other < group.endSlot; other++) {
                if (other == slot) continue;
                var otherKey = itemKeys[other];
                if (otherKey != null && otherKey.getItem() == key.getItem()) return true;
            }
            return false;
        }

        private long insertFluid(AEFluidKey key, long amount) {
            int request = (int) Math.min(amount, Integer.MAX_VALUE);
            int remaining = request;
            for (var group : fluidGroups) {
                if (remaining <= 0) break;

                int handlerLimit = group.handler.fill(key.toStack(remaining), IFluidHandler.FluidAction.SIMULATE);
                if (handlerLimit < 0 || handlerLimit > remaining) return -1;
                if (handlerLimit == 0) continue;

                int unallocated;
                if (group.mode == FluidRouteMode.REJECT) {
                    return -1;
                } else if (group.mode == FluidRouteMode.OPAQUE) {
                    if (group.used) return -1;
                    unallocated = insertFluidInOrder(key, handlerLimit, group);
                    if (unallocated != 0) return -1;
                    group.used = true;
                } else if (group.mode == FluidRouteMode.ONE_TANK_PER_FLUID) {
                    unallocated = insertFluidLimited(key, handlerLimit, group);
                } else {
                    unallocated = insertFluidInOrder(key, handlerLimit, group);
                }

                int accepted = handlerLimit - unallocated;
                if (group.voidOverflow && accepted < handlerLimit) return -1;
                remaining -= accepted;
            }
            return request - remaining;
        }

        private int insertFluidLimited(AEFluidKey key, int remaining, FluidRouteGroup group) {
            for (int tank = group.firstTank; tank < group.endTank; tank++) {
                var existing = fluidTanks[tank];
                if (!existing.isEmpty() && key.matches(existing)) {
                    return insertFluidIntoTank(key, remaining, tank);
                }
            }

            for (int tank = group.firstTank; tank < group.endTank && remaining > 0; tank++) {
                int after = insertFluidIntoTank(key, remaining, tank);
                if (after < remaining) return after;
            }
            return remaining;
        }

        private int insertFluidInOrder(AEFluidKey key, int remaining, FluidRouteGroup group) {
            for (int tank = group.firstTank; tank < group.endTank && remaining > 0; tank++) {
                remaining = insertFluidIntoTank(key, remaining, tank);
            }
            return remaining;
        }

        private int insertFluidIntoTank(AEFluidKey key, int remaining, int tank) {
            var existing = fluidTanks[tank];
            if (!existing.isEmpty() && !key.matches(existing)) return remaining;
            if (!fluidHandler.isFluidValid(tank, key.toStack(1))) return remaining;
            if (fluidHandler instanceof ICustomFluidStackHandler custom && !custom.supportsFill(tank)) {
                return remaining;
            }

            int available = Math.max(0, fluidHandler.getTankCapacity(tank) - existing.getAmount());
            int accepted = Math.min(remaining, available);
            if (accepted <= 0) return remaining;

            if (existing.isEmpty()) {
                fluidTanks[tank] = key.toStack(accepted);
            } else {
                existing.grow(accepted);
            }
            return remaining - accepted;
        }

        private long insertGeneric(AEKey key, long amount) {
            if (genericKey == null) {
                genericKey = key;
                genericRemaining = simulateInsert(target.storage, key, Long.MAX_VALUE, providerTarget);
                if (genericRemaining < 0) return -1;
            } else if (!genericKey.equals(key)) {
                // A generic MEStorage exposes no slot/tank topology. Multiple distinct keys may share bytes or types,
                // so their cumulative capacity cannot be proven without a transactional API.
                return -1;
            }

            long accepted = Math.min(amount, genericRemaining);
            genericRemaining -= accepted;
            return accepted;
        }

        private StorageBusMatch validate(ExpectedContent expected, boolean requireVirtualMatch) {
            if (!valid || getIntegerMultiplier(physicalInputs, expected.contents) < 0) {
                return StorageBusMatch.blockedResult();
            }

            var actual = readActual(target.storage, isVirtualItemProviderHost(target.host));
            if (!actual.valid) return StorageBusMatch.blockedResult();
            long existingMultiplier = getIntegerMultiplier(actual.contents, expected.contents);
            if (existingMultiplier < 0) return StorageBusMatch.blockedResult();

            if (expected.virtualInput.present) {
                if (!initialCircuit.supported) return StorageBusMatch.blockedResult();
                boolean virtualRoutedHere = !virtualProviders.isEmpty();
                boolean mustAlreadyMatch = existingMultiplier > 0 || requireVirtualMatch ||
                        !physicalInputs.isEmpty() && !virtualRoutedHere;
                boolean matches = matchesVirtualInput(initialCircuit, expected.virtualInput);
                if (!matches && (mustAlreadyMatch || virtualRoutedHere && !initialCircuit.reconfigurable)) {
                    return StorageBusMatch.blockedResult();
                }
            } else if (!virtualProviders.isEmpty()) {
                return StorageBusMatch.blockedResult();
            }
            return new StorageBusMatch(false, existingMultiplier);
        }

        private static List<ItemRouteGroup> createItemRouteGroups(ResolvedTarget target,
                                                                  @Nullable IItemHandler handler) {
            if (handler == null || handler.getSlots() == 0) return List.of();

            var machine = getMetaMachine(target.host);
            if (machine != null) {
                var traits = GTCapabilityHelper.getCapabilitiesFromTraits(machine.getTraits(), target.side,
                        ICustomItemStackHandler.class);
                var groups = new ArrayList<ItemRouteGroup>(traits.size());
                int firstSlot = 0;
                for (var trait : traits) {
                    int endSlot = firstSlot + trait.getSlots();
                    groups.add(new ItemRouteGroup(firstSlot, endSlot, classifyItemRoute(trait), trait));
                    firstSlot = endSlot;
                }
                if (!groups.isEmpty() && firstSlot == handler.getSlots()) return List.copyOf(groups);
            }

            if (handler instanceof ICustomItemStackHandler custom && handler.getSlots() > 1) {
                return List.of(new ItemRouteGroup(0, handler.getSlots(), ItemRouteMode.OPAQUE, custom));
            }
            return List.of(new ItemRouteGroup(0, handler.getSlots(), ItemRouteMode.STANDARD,
                    handler instanceof ICustomItemStackHandler custom ? custom : null));
        }

        private static ItemRouteMode classifyItemRoute(ICustomItemStackHandler handler) {
            if (handler instanceof VoidOutputItemHandler || handler instanceof MEOutputItemHandler) {
                return ItemRouteMode.REJECT;
            }
            if (handler instanceof NotifiableItemStackHandler notifiable) {
                return notifiable.storage.isInputLimited ? ItemRouteMode.INPUT_LIMITED : ItemRouteMode.STANDARD;
            }
            if (handler instanceof ItemHandlerProxyTrait proxy && proxy.proxy != null) {
                return classifyItemRoute(proxy.proxy);
            }
            return handler.getSlots() <= 1 ? ItemRouteMode.STANDARD : ItemRouteMode.OPAQUE;
        }

        private static List<FluidRouteGroup> createFluidRouteGroups(ResolvedTarget target,
                                                                    @Nullable IFluidHandler handler) {
            if (handler == null || handler.getTanks() == 0) return List.of();

            var machine = getMetaMachine(target.host);
            if (machine != null) {
                var traits = GTCapabilityHelper.getCapabilitiesFromTraits(machine.getTraits(), target.side,
                        ICustomFluidStackHandler.class);
                var groups = new ArrayList<FluidRouteGroup>(traits.size());
                int firstTank = 0;
                for (var trait : traits) {
                    int endTank = firstTank + trait.getTanks();
                    groups.add(new FluidRouteGroup(firstTank, endTank, classifyFluidRoute(trait), trait,
                            hasFluidVoidOverflow(trait)));
                    firstTank = endTank;
                }
                if (!groups.isEmpty() && firstTank == handler.getTanks()) return List.copyOf(groups);
            }

            var mode = handler.getTanks() == 1 ? FluidRouteMode.IN_ORDER : FluidRouteMode.OPAQUE;
            return List.of(new FluidRouteGroup(0, handler.getTanks(), mode, handler, false));
        }

        private static FluidRouteMode classifyFluidRoute(ICustomFluidStackHandler handler) {
            if (handler instanceof VoidOutputFluidHandler || handler instanceof MEOutputFluidHandler) {
                return FluidRouteMode.REJECT;
            }
            if (handler instanceof NotifiableFluidTank notifiable) {
                if ((Object) notifiable instanceof NotifiableFluidTankAccessor accessor) {
                    return accessor.gtocore$getAllowSameFluids() ? FluidRouteMode.IN_ORDER : FluidRouteMode.ONE_TANK_PER_FLUID;
                }
                return FluidRouteMode.OPAQUE;
            }
            if (handler instanceof FluidTankProxyTrait proxy && proxy.proxy != null) {
                return classifyFluidRoute(proxy.proxy);
            }
            return handler.getTanks() <= 1 ? FluidRouteMode.IN_ORDER : FluidRouteMode.OPAQUE;
        }

        private static boolean hasFluidVoidOverflow(ICustomFluidStackHandler handler) {
            if (handler instanceof NotifiableFluidTank notifiable) return notifiable.isVoiding;
            if (handler instanceof FluidTankProxyTrait proxy && proxy.proxy != null) {
                return hasFluidVoidOverflow(proxy.proxy);
            }
            return false;
        }

        private static AEItemKey[] snapshotItemKeys(@Nullable IItemHandler handler) {
            if (handler == null) return new AEItemKey[0];
            var result = new AEItemKey[handler.getSlots()];
            for (int slot = 0; slot < result.length; slot++) {
                var stack = handler.getStackInSlot(slot);
                result[slot] = stack.isEmpty() ? null : AEItemKey.of(stack);
            }
            return result;
        }

        private static long[] snapshotItemAmounts(@Nullable IItemHandler handler) {
            if (handler == null) return new long[0];
            var result = new long[handler.getSlots()];
            for (int slot = 0; slot < result.length; slot++) {
                result[slot] = Math.max(0, handler.getStackInSlot(slot).getCount());
            }
            return result;
        }

        private static FluidStack[] snapshotFluids(@Nullable IFluidHandler handler) {
            if (handler == null) return new FluidStack[0];
            var result = new FluidStack[handler.getTanks()];
            for (int tank = 0; tank < result.length; tank++) {
                result[tank] = handler.getFluidInTank(tank).copy();
            }
            return result;
        }

        private enum ItemRouteMode {
            STANDARD,
            INPUT_LIMITED,
            OPAQUE,
            REJECT
        }

        private enum FluidRouteMode {
            IN_ORDER,
            ONE_TANK_PER_FLUID,
            OPAQUE,
            REJECT
        }

        private static final class ItemRouteGroup {

            private final int firstSlot;
            private final int endSlot;
            private final ItemRouteMode mode;
            @Nullable
            private final ICustomItemStackHandler handler;
            private boolean used;

            private ItemRouteGroup(int firstSlot, int endSlot, ItemRouteMode mode,
                                   @Nullable ICustomItemStackHandler handler) {
                this.firstSlot = firstSlot;
                this.endSlot = endSlot;
                this.mode = mode;
                this.handler = handler;
            }
        }

        private static final class FluidRouteGroup {

            private final int firstTank;
            private final int endTank;
            private final FluidRouteMode mode;
            private final IFluidHandler handler;
            private final boolean voidOverflow;
            private boolean used;

            private FluidRouteGroup(int firstTank, int endTank, FluidRouteMode mode,
                                    IFluidHandler handler, boolean voidOverflow) {
                this.firstTank = firstTank;
                this.endTank = endTank;
                this.mode = mode;
                this.handler = handler;
                this.voidOverflow = voidOverflow;
            }
        }
    }

    public record TargetInput(WrapMeStorage target, GenericStack input) {}

    private record DistributedVirtualTarget(DistributedTarget target, ExpectedContent expected) {}

    private static final class DistributedTarget {

        private final WrapMeStorage target;
        private final List<GenericStack> inputs = new ArrayList<>();
        private final List<TargetInput> targetInputs = new ArrayList<>();

        private DistributedTarget(WrapMeStorage target) {
            this.target = target;
        }
    }
}
