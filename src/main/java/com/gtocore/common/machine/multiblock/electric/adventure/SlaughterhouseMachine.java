package com.gtocore.common.machine.multiblock.electric.adventure;

import com.gtocore.data.IdleReason;

import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.item.ItemStackSet;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.StringUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

import appeng.util.Platform;
import com.enderio.base.common.init.EIOFluids;
import com.enderio.base.common.util.ExperienceUtil;
import com.enderio.machines.common.init.MachineBlocks;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snownee.jade.util.CommonProxy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SlaughterhouseMachine extends StorageMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SlaughterhouseMachine.class, StorageMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final Object2ObjectOpenHashMap<ResourceLocation, Set<SlaughterhouseMachine>> MACHINES = new O2OOpenCacheHashMap<>();

    private int attackDamage;
    @Persisted
    private boolean isSpawn;
    @Persisted
    private boolean noAI;
    @Persisted
    private boolean muffled;
    private DamageSource damageSource;
    private ItemStack activeWeapon = ItemStack.EMPTY;
    private static final String[] mobList1 = {
            "minecraft:chicken",
            "minecraft:rabbit",
            "minecraft:sheep",
            "minecraft:cow",
            "minecraft:horse",
            "minecraft:pig",
            "minecraft:donkey",
            "minecraft:skeleton_horse",
            "minecraft:iron_golem",
            "minecraft:wolf",
            "minecraft:goat",
            "minecraft:parrot",
            "minecraft:camel",
            "minecraft:cat",
            "minecraft:fox",
            "minecraft:llama",
            "minecraft:panda",
            "minecraft:polar_bear"
    };

    private static final String[] mobList2 = {
            "minecraft:ghast",
            "minecraft:zombie",
            "minecraft:pillager",
            "minecraft:zombie_villager",
            "minecraft:skeleton",
            "minecraft:drowned",
            "minecraft:witch",
            "minecraft:spider",
            "minecraft:creeper",
            "minecraft:husk",
            "minecraft:wither_skeleton",
            "minecraft:blaze",
            "minecraft:zombified_piglin",
            "minecraft:slime",
            "minecraft:vindicator",
            "minecraft:enderman"
    };

    public SlaughterhouseMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, i -> i.is(MachineBlocks.POWERED_SPAWNER.asItem()));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static Player getFakePlayer(ServerLevel level) {
        return Platform.getFakePlayer(level, null);
    }

    private DamageSource getDamageSource(ServerLevel level) {
        if (damageSource == null) {
            damageSource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC_KILL), getFakePlayer(level));
        }
        return damageSource;
    }

    @Override
    public void onContentChanges(RecipeHandlerList handlerList) {
        if (handlerList.getHandlerIO() == IO.IN) {
            attackDamage = 1;
            int c = checkingCircuit(false);
            activeWeapon = ItemStack.EMPTY;

            if (c == 3) {
                forEachInputItems((stack, amount) -> {
                    if (stack.getItem() instanceof SwordItem swordItem) {
                        int attack = (int) swordItem.getDamage();
                        if (attackDamage < attack) {
                            attackDamage = attack;
                            activeWeapon = stack.copy();
                        }
                    }
                    return false;
                });
            } else {
                forEachInputItems((stack, amount) -> {
                    if (stack.getItem() instanceof SwordItem swordItem) {
                        attackDamage += (int) swordItem.getDamage();
                    }
                    return false;
                });
            }
        }
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("item.gtceu.tool.tooltip.attack_damage", attackDamage));
        int c = checkingCircuit(false);
        if (c == 3) textList.add(Component.translatable("gtocore.machine.slaughterhouse.active_weapon", activeWeapon.getDisplayName()));
        else textList.add(Component.translatable("gtocore.machine.slaughterhouse.is_spawn").append(ComponentPanelWidget.withButton(Component.literal("[").append(isSpawn ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "spawn_switch")));
        textList.add(Component.translatable("stat.apotheosis.no_ai").append(ComponentPanelWidget.withButton(Component.literal("[").append(noAI ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "noai_switch")));
        textList.add(Component.translatable("stat.apotheosis.silent").append(ComponentPanelWidget.withButton(Component.literal("[").append(muffled ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "muffle_switch")));
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            switch (componentData) {
                case "spawn_switch" -> isSpawn = !isSpawn;
                case "noai_switch" -> noAI = !noAI;
                case "muffle_switch" -> muffled = !muffled;
                default -> super.handleDisplayClick(componentData, clickData);
            }
        }
    }

    @Nullable
    private Recipe getRecipe() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            if (getTier() < 2) {
                setIdleReason(IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
                return null;
            }
            int c = checkingCircuit(false);
            if (c != 1 && c != 2 && c != 3) {
                setIdleReason(IdleReason.SET_CIRCUIT);
                return null;
            }
            ItemStackSet itemStacks = new ItemStackSet();
            BlockPos blockPos = MachineUtils.getOffsetPos(3, 1, getFrontFacing(), getPos());
            ItemStack itemStack = getStorageStack();
            boolean isFixed = !itemStack.isEmpty();
            String[] mobList = isFixed ? null : (c == 1 ? mobList1 : (c == 2 ? mobList2 : null));
            int parallel = Math.max(1, (getTier() - 2) << 3);
            int tierMultiplier = Math.min(16, parallel);
            int multiplier = parallel / tierMultiplier;

            List<Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, new AABB(
                    blockPos.getX() - 3.5,
                    blockPos.getY() - 1,
                    blockPos.getZ() - 3.5,
                    blockPos.getX() + 3.5,
                    blockPos.getY() + 6,
                    blockPos.getZ() + 3.5));

            long xp = 0;
            int killedCount = 0;
            final int MAX_KILLS_PER_RUN = 20;

            for (Entity entity : entities) {
                entity.getSelfAndPassengers().filter(e -> !(e instanceof Player)).filter(Mob.class::isInstance).map(Mob.class::cast).forEach(mob -> {
                    mob.setNoAi(noAI);
                    mob.setSilent(muffled);
                });
                if (c == 3 && killedCount >= MAX_KILLS_PER_RUN) continue;
                if (entity instanceof LivingEntity livingEntity) {
                    if (c != 3 && CommonProxy.isBoss(entity)) continue;
                    if (c == 3) {
                        if (livingEntity.isAlive()) {
                            Player fakePlayer = getFakePlayer(serverLevel);
                            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, activeWeapon.isEmpty() ? ItemStack.EMPTY : activeWeapon.copy());
                            livingEntity.hurt(getDamageSource(serverLevel), attackDamage);
                            if (!livingEntity.isAlive()) {
                                String[] mobParts = StringUtils.decompose(EntityType.getKey(livingEntity.getType()).toString());
                                if (mobParts.length >= 2) {
                                    LootTable lootTable = serverLevel.getServer().getLootData()
                                            .getLootTable(RLUtils.fromNamespaceAndPath(mobParts[0], "entities/" + mobParts[1]));
                                    LootParams lootParams = new LootParams.Builder(serverLevel)
                                            .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, fakePlayer)
                                            .withParameter(LootContextParams.THIS_ENTITY, livingEntity)
                                            .withParameter(LootContextParams.DAMAGE_SOURCE, getDamageSource(serverLevel))
                                            .withParameter(LootContextParams.ORIGIN, blockPos.getCenter())
                                            .withParameter(LootContextParams.TOOL, activeWeapon)
                                            .create(lootTable.getParamSet());
                                    lootTable.getRandomItems(lootParams).forEach(stack -> itemStacks.add(stack.copyWithCount(stack.getCount() * multiplier)));
                                }
                                killedCount++;
                            }
                            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    } else {
                        livingEntity.hurt(getDamageSource(serverLevel), attackDamage);
                    }
                } else if (entity instanceof ItemEntity itemEntity) {
                    itemStacks.add(itemEntity.getItem());
                    itemEntity.discard();
                } else if (entity instanceof ExperienceOrb experienceOrb) {
                    xp += experienceOrb.value;
                    experienceOrb.discard();
                }
            }

            if (xp > 0) outputFluid(EIOFluids.XP_JUICE.getSource(), xp * ExperienceUtil.EXP_TO_FLUID);

            if (c != 3) {
                for (int i = 0; i <= tierMultiplier; i++) {
                    String mob = isFixed ? itemStack.getOrCreateTag().getCompound("BlockEntityTag")
                            .getCompound("EntityStorage").getCompound("Entity").getString("id") : mobList[GTValues.RNG.nextInt(mobList.length)];
                    Optional<EntityType<?>> entityType = EntityType.byString(mob);
                    if (entityType.isEmpty()) continue;
                    Entity entity = entityType.get().create(serverLevel);
                    if (!(entity instanceof Mob mob1)) continue;
                    if (CommonProxy.isBoss(entity)) continue;
                    if (isSpawn) {
                        mob1.setPos(blockPos.getCenter());
                        mob1.setNoAi(true);
                        serverLevel.addFreshEntity(mob1);
                    } else {
                        String[] mobParts = StringUtils.decompose(mob);
                        if (mobParts.length < 2) continue;
                        LootTable lootTable = serverLevel.getServer().getLootData()
                                .getLootTable(RLUtils.fromNamespaceAndPath(mobParts[0], "entities/" + mobParts[1]));
                        LootParams lootParams = new LootParams.Builder(serverLevel)
                                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, getFakePlayer(serverLevel))
                                .withParameter(LootContextParams.THIS_ENTITY, entity)
                                .withParameter(LootContextParams.DAMAGE_SOURCE, getDamageSource(serverLevel))
                                .withParameter(LootContextParams.ORIGIN, blockPos.getCenter())
                                .create(lootTable.getParamSet());
                        lootTable.getRandomItems(lootParams).forEach(stack -> {
                            int count = stack.getCount();
                            itemStacks.add(isFixed ? stack.copyWithCount(parallel * count) : multiplier > 1 ? stack.copyWithCount(multiplier * count) : stack);
                        });
                        if (isFixed) break;
                    }
                }
            }

            int duration = (c == 3) ? Math.max(20, 150 - attackDamage) : (isSpawn ? 20 : Math.max(20, 200 - attackDamage));
            RecipeBuilder builder = getRecipeBuilder().duration(duration).EUt(getOverclockVoltage());
            itemStacks.forEach(builder::outputItems);
            Recipe recipe = builder.buildRawRecipe();
            if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IIWirelessInteractor.addToNet(MACHINES, this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        IIWirelessInteractor.removeFromNet(MACHINES, this);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        IIWirelessInteractor.removeFromNet(MACHINES, this);
    }

    public static boolean isEntityInAnySlaughterhouse(Entity entity) {
        if (MACHINES.isEmpty()) return false;
        for (SlaughterhouseMachine machine : MACHINES.getOrDefault(entity.level().dimension().location(), Collections.emptySet())) {
            BlockPos blockPos = MachineUtils.getOffsetPos(3, 1, machine.getFrontFacing(), machine.getPos());
            AABB aabb = new AABB(
                    blockPos.getX() - 4,
                    blockPos.getY() - 1,
                    blockPos.getZ() - 4,
                    blockPos.getX() + 4,
                    blockPos.getY() + 6,
                    blockPos.getZ() + 4);
            if (aabb.contains(entity.position())) return true;
        }
        return false;
    }
}
