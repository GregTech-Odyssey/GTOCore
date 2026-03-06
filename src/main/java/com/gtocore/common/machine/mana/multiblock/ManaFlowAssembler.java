package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.integration.botania.IClientPylon;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.IManaContainer;
import com.gtolib.api.machine.ManaDistributorMachine;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.misc.ManaContainerList;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeType;
import com.gtolib.utils.FunctionContainer;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.function.ObjLongPredicate;
import com.gregtechceu.gtceu.utils.memoization.GTMemoizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import com.fast.recipesearch.IntLongMap;
import mythicbotany.pylon.BlockAlfsteelPylon;
import mythicbotany.register.ModBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.PylonBlock;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

import static com.gtolib.api.recipe.lookup.MapIngredient.ITEM_CONVERTER;

@DataGeneratorScanned
public class ManaFlowAssembler extends ManaMultiblockMachine {

    private final static int SIZE = 9;
    private final ItemEntityRecipeHandler itemIn = new ItemEntityRecipeHandler();
    private int maxRate = 0;
    private final List<WeakReference<ManaPoolBlockEntity>> manaPools = new ArrayList<>();
    private final InWorldManaContainer inWorldManaContainer = new InWorldManaContainer();
    private final ManaContainerList manaContainerList = new ManaContainerList(inWorldManaContainer);
    private TickableSubscription tickSubscription;
    private TickableSubscription clientTickSubscription;
    private final EnumMap<Direction, Integer> colors = new EnumMap<>(Direction.class);

    public ManaFlowAssembler(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        addHandlerList(RecipeHandlerList.of(IO.IN, itemIn));
        addHandlerList(RecipeHandlerList.of(IO.OUT, itemIn));

        var f = getMultiblockState().getMatchContext().<FunctionContainer<AtomicInteger, ?>>get("maxRate");
        maxRate = f == null ? 0 : f.getValue().get();
        manaPools.clear();
        var f1 = getMultiblockState().getMatchContext().<FunctionContainer<ArrayList<BlockPos>, ?>>get("manaPool");
        var poolPositions = f1 == null ? List.<BlockPos>of() : f1.getValue();
        var level = getLevel();
        if (level != null) {
            for (var pos : poolPositions) {
                if (!level.isLoaded(pos)) {
                    continue;
                }
                var blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof ManaPoolBlockEntity manaPool) {
                    manaPools.add(new WeakReference<>(manaPool));
                }
            }
        }
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        var level = getLevel();
        if (level != null) {
            level.playSound(null, getPos().getX(), getPos().above().getY(), getPos().getZ(), BotaniaSounds.terrasteelCraft, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        maxRate = 0;
        manaPools.clear();
        colors.clear();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        tickSubscription = subscribeServerTick(() -> getRecipeLogic().updateTickSubscription(), 20);
        if (isRemote()) {
            clientTickSubscription = subscribeClientTick(() -> {
                if (isActive() && getLevel() != null) {
                    var center = getPos().above(4).getCenter();
                    var dir = Direction.NORTH;
                    for (int i = 0; i < 4; i++) {
                        var pylonPos = getPos().above(3).relative(dir, 2);
                        dir = dir.getClockWise();
                        pylonPos = pylonPos.relative(dir, 2);
                        BlockPos finalPylonPos = pylonPos;

                        int color = colors.computeIfAbsent(dir, d -> {
                            var blockState = getLevel().getBlockState(finalPylonPos);
                            if (blockState.getBlock() instanceof PylonBlock pb) {
                                return (int) (pb.variant.r * 255 + pb.variant.g * 255 * 256 + pb.variant.b * 255 * 256 * 256);
                            }
                            return 15629312;
                        });

                        IClientPylon.particle(pylonPos, getOffsetTimer(), center, getLevel(), color);
                    }
                }
            }, 1);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        maxRate = 0;
        manaPools.clear();
        colors.clear();
        tickSubscription.unsubscribe();
        tickSubscription = null;
        if (clientTickSubscription != null) {
            clientTickSubscription.unsubscribe();
            clientTickSubscription = null;
        }
    }

    @Override
    public void customText(List<Component> textList) {
        getMultiblockTraits().stream().filter(t -> !(t instanceof ManaTrait)).forEach(trait -> trait.customText(textList));
        textList.add(Component.translatable("gtocore.machine.mana_stored", FormattingUtil.formatNumbers(inWorldManaContainer.getCurrentMana()) + " / " + FormattingUtil.formatNumbers(inWorldManaContainer.getMaxMana())));
        textList.add(Component.translatable("gtocore.machine.mana_consumption", FormattingUtil.formatNumbers(inWorldManaContainer.getMaxIORate()) + " /t"));
    }

    @Override
    protected @Nullable Recipe getRealRecipe(Recipe recipe) {
        if (recipe.eut != 0 || maxRate == 0) return null;
        recipe.duration = Math.toIntExact(recipe.duration * recipe.manat / maxRate);
        if (recipe.duration > 200) {
            this.getEnhancedRecipeLogic().gtolib$setIdleReason(Component.translatable(MANA_FLOW_TOO_WEAK));
            return null;
        }
        recipe.manat = maxRate;
        return super.getRealRecipe(recipe);
    }

    @Override
    public boolean handleTickRecipe(@Nullable Recipe recipe) {
        if (recipe != null) {
            long mana = recipe.manat;
            if (mana != 0) {
                if (!useMana(mana, false)) {
                    IdleReason.setIdleReason(this, recipe.manat < 0 ? IdleReason.INSUFFICIENT_OUT : IdleReason.NO_MANA);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull ManaContainerList getManaContainer() {
        return manaContainerList;
    }

    private List<ItemEntity> getItemEntitiesAbove() {
        var level = getHolder().getLevel();
        if (level == null) {
            return List.of();
        }
        var pos = getPos().above(2);
        var aabb = new AABB(pos).inflate(1);
        var counter = new AtomicInteger();
        return level.getEntitiesOfClass(ItemEntity.class, aabb, itemEntity -> {
            if (itemEntity.isAlive() && !itemEntity.getItem().isEmpty()) {
                return counter.getAndIncrement() < SIZE;
            }
            return false;
        });
    }

    private class ItemEntityRecipeHandler implements IRecipeHandler<ItemIngredient> {

        @Override
        public List<ItemIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<ItemIngredient> left, boolean simulate) {
            if (io == IO.OUT) {
                if (!simulate && getLevel() instanceof ServerLevel level) {
                    var pos = getPos().above(3);
                    var posCenter = pos.getCenter();
                    var random = level.random;
                    left.forEach(ingredient -> {
                        var itemStack = ingredient.getInnerItemStack().copyWithCount((int) ingredient.amount);
                        var itemEntity = new ItemEntity(level, posCenter.x(), posCenter.y(), posCenter.z(), itemStack);
                        itemEntity.setDeltaMovement(random.nextDouble() * 0.2 - 0.1, 0.2, random.nextDouble() * 0.2 - 0.1);
                        level.addFreshEntity(itemEntity);
                    });
                }
                return null;
            }
            var itemEntities = getItemEntitiesAbove();
            if (itemEntities.isEmpty()) {
                return left;
            }
            for (var itemEntity : itemEntities) {
                if (!itemEntity.isAlive() || itemEntity.getItem().isEmpty()) {
                    continue;
                }
                var itemStack = itemEntity.getItem();
                itemStack = simulate ? itemStack.copy() : itemStack;
                var leftConsuming = left.iterator();
                while (itemStack.getCount() > 0 && leftConsuming.hasNext()) {
                    var ingredient = leftConsuming.next();
                    if (ingredient.testItem(itemStack.getItem())) {
                        var toExtract = (int) Math.min(ingredient.amount, itemStack.getCount());
                        ingredient.amount -= toExtract;
                        itemStack.shrink(toExtract);
                        if (ingredient.amount <= 0) {
                            leftConsuming.remove();
                        }
                        if (!simulate && itemStack.isEmpty()) {
                            itemEntity.discard();
                        }
                    }
                }
            }
            if (left.isEmpty()) {
                return null;
            }
            return left;
        }

        @Override
        public int getSize() {
            return SIZE;
        }

        @Override
        public boolean forEachItems(ObjLongPredicate<ItemStack> function) {
            return getItemEntitiesAbove().stream()
                    .filter(ItemEntity::isAlive)
                    .anyMatch(ie -> {
                        var r = function.test(ie.getItem(), ie.getItem().getCount());
                        if (ie.getItem().isEmpty()) {
                            ie.discard();
                        }
                        return r;
                    });
        }

        @Override
        public void fastForEachItems(ObjLongConsumer<ItemStack> function) {
            getItemEntitiesAbove().stream()
                    .filter(ItemEntity::isAlive)
                    .forEach(ie -> {
                        function.accept(ie.getItem(), ie.getItem().getCount());
                        if (ie.getItem().isEmpty()) {
                            ie.discard();
                        }
                    });
        }

        @Override
        public IntLongMap getIngredientMap(@NotNull GTRecipeType type) {
            var intIngredientMap = new IntLongMap();
            boolean specialConverter = ((RecipeType) type).specialConverter;
            for (var i : getItemEntitiesAbove()) {
                if (!i.isAlive()) continue;
                if (specialConverter) {
                    type.convertItem(i.getItem(), i.getItem().getCount(), intIngredientMap);
                } else {
                    ITEM_CONVERTER.convert(i.getItem(), i.getItem().getCount(), intIngredientMap);
                }
            }
            return intIngredientMap;
        }

        @Override
        public boolean isEmpty() {
            return getItemEntitiesAbove().stream().noneMatch(ItemEntity::isAlive);
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public RecipeCapability<ItemIngredient> getCapability() {
            return ItemRecipeCapability.CAP;
        }
    }

    private class InWorldManaContainer implements IManaContainer {

        @Override
        public boolean acceptDistributor() {
            return false;
        }

        @Override
        public MetaMachine getMachine() {
            return ManaFlowAssembler.this;
        }

        @Override
        public long getMaxMana() {
            return manaPools.stream().map(WeakReference::get).filter(Objects::nonNull).filter(m -> !m.isRemoved()).mapToInt(ManaPoolBlockEntity::getMaxMana).sum();
        }

        @Override
        public long getCurrentMana() {
            return manaPools.stream().map(WeakReference::get).filter(Objects::nonNull).filter(m -> !m.isRemoved()).mapToInt(ManaPoolBlockEntity::getCurrentMana).sum();
        }

        @Override
        public void setCurrentMana(long mana) {
            var manaToSet = Mth.clamp(mana, 0, getMaxMana());
            for (var poolRef : manaPools) {
                if (manaToSet <= 0) {
                    break;
                }
                var pool = poolRef.get();
                if (pool != null) {
                    var toSet = Mth.clamp(manaToSet, 0, pool.getMaxMana());
                    var delta = toSet - pool.getCurrentMana();
                    manaToSet -= toSet;
                    pool.receiveMana((int) delta);
                }
            }
        }

        @Override
        public long getMaxIORate() {
            return maxRate;
        }

        @Override
        public ManaDistributorMachine getNetMachineCache() {
            return null;
        }

        @Override
        public void setNetMachineCache(ManaDistributorMachine cache) {}
    }

    public static Supplier<TraceabilityPredicate> MANA_PYLON = GTMemoizer.memoize(
            () -> GTOPredicates.containerBlock(
                    () -> new FunctionContainer<>(new AtomicInteger(),
                            (data, state) -> {
                                if (state.getBlockState().getBlock() instanceof PylonBlock block) {
                                    switch (block.variant) {
                                        case MANA -> data.getAndAdd(8);
                                        case NATURA -> data.getAndAdd(8 << 2);
                                        case GAIA -> data.getAndAdd(8 << 6);
                                    }
                                }
                                if (state.getBlockState().getBlock() instanceof BlockAlfsteelPylon) {
                                    data.getAndAdd(8 << 4);
                                }
                                return data;
                            }),
                    "maxRate", BotaniaBlocks.manaPylon, BotaniaBlocks.naturaPylon, BotaniaBlocks.gaiaPylon, ModBlocks.alfsteelPylon));

    public static Supplier<TraceabilityPredicate> MANA_POOL = GTMemoizer.memoize(
            () -> GTOPredicates.containerBlock(
                    () -> new FunctionContainer<>(new ArrayList<BlockPos>(),
                            (data, state) -> {
                                data.add(state.getPos());
                                return data;
                            }),
                    "manaPool", BotaniaBlocks.manaPool));

    @RegisterLanguage(cn = "魔力流太弱了", en = "Mana flow is too weak")
    public static final String MANA_FLOW_TOO_WEAK = "gtocore.machine.mana_flow_assembler.mana_flow_too_weak";
}
