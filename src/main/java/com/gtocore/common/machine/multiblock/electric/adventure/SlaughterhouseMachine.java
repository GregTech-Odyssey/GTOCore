package com.gtocore.common.machine.multiblock.electric.adventure;

import com.gtocore.data.IdleReason;

import com.gtolib.api.item.ItemStackSet;
import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.machine.trait.TierCasingTrait;
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

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import appeng.util.Platform;
import com.enderio.api.capability.StoredEntityData;
import com.enderio.base.common.init.EIOFluids;
import com.enderio.machines.common.init.MachineBlocks;
import dev.shadowsoffire.apotheosis.adventure.AdventureConfig;
import dev.shadowsoffire.apotheosis.adventure.compat.GameStagesCompat;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.placebo.reload.WeightedDynamicRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snownee.jade.util.CommonProxy;

import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtolib.api.GTOValues.GLASS_TIER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SlaughterhouseMachine extends StorageMultiblockMachine implements ITierCasingMachine {

    private int attackDamage;
    private DamageSource damageSource;
    private ItemStack activeWeapon = ItemStack.EMPTY;
    private StoredEntityData data;
    private String entityId;
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

    private final TierCasingTrait tierCasingTrait;

    public SlaughterhouseMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, i -> i.is(MachineBlocks.POWERED_SPAWNER.asItem()));
        tierCasingTrait = new TierCasingTrait(this, GLASS_TIER);
    }

    @Override
    public boolean hasBatchConfig() {
        return false;
    }

    private static Player getFakePlayer(ServerLevel level) {
        return Platform.getFakePlayer(level, null);
    }

    private DamageSource getDamageSource(ServerLevel level, Player player) {
        if (damageSource == null) {
            damageSource = new DamageSources(level.getServer().registryAccess()).mobAttack(player);
        }
        return damageSource;
    }

    @Override
    public void onMachineChanged() {
        data = null;
        entityId = null;
        ItemStack itemStack = getStorageStack();
        if (itemStack.isEmpty() || !itemStack.hasTag()) return;
        data = new StoredEntityData(itemStack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("EntityStorage").getCompound("Entity"), 1);
        entityId = itemStack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("EntityStorage").getCompound("Entity").getString("id");
    }

    @Override
    public void onContentChanges(RecipeHandlerList handlerList) {
        if (handlerList.getHandlerIO() == IO.IN) {
            attackDamage = 1;
            activeWeapon = ItemStack.EMPTY;
            forEachInputItems((stack, amount) -> {
                if (stack.getItem() instanceof SwordItem swordItem) {
                    if (activeWeapon.isEmpty()) {
                        activeWeapon = stack;
                    }
                    attackDamage += (int) swordItem.getDamage();
                }
                return false;
            });
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GLASS_TIER), tier);
        onMachineChanged();
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("item.gtceu.tool.tooltip.attack_damage", attackDamage));
        textList.add(Component.translatable("gtocore.machine.slaughterhouse.active_weapon", activeWeapon.getDisplayName()));
    }

    @Nullable
    private Recipe getRecipe() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            if (getTier() < 1) {
                setIdleReason(IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
                return null;
            }
            int c = checkingCircuit(false);
            if (c != 1 && c != 2) {
                setIdleReason(IdleReason.SET_CIRCUIT);
                return null;
            }
            Player player = getFakePlayer(serverLevel);
            ItemStackSet itemStacks = new ItemStackSet();
            Vec3 origin = MachineUtils.getOffsetPos(3, 1, getFrontFacing(), getPos()).getCenter();
            Entity entity = null;
            if (data != null) {
                entity = EntityType.loadEntityRecursive(data.getEntityTag(), serverLevel, (entity1) -> {
                    entity1.moveTo(origin.x, origin.y, origin.z, entity1.getYRot(), entity1.getXRot());
                    return entity1;
                });
            }
            boolean isFixed = entity != null;
            String[] mobList = isFixed ? null : c == 1 ? mobList1 : mobList2;
            int parallel = (int) Math.pow(3, tier - 1);
            int tierMultiplier = Math.min(16, parallel);
            int multiplier = Math.max(1, parallel / tierMultiplier);
            long xp = 0;

            var lootParams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                    .withParameter(LootContextParams.DAMAGE_SOURCE, getDamageSource(serverLevel, player))
                    .withParameter(LootContextParams.KILLER_ENTITY, player)
                    .withParameter(LootContextParams.DIRECT_KILLER_ENTITY, player)
                    .withParameter(LootContextParams.ORIGIN, origin)
                    .withParameter(LootContextParams.BLOCK_STATE, getBlockState())
                    .withParameter(LootContextParams.BLOCK_ENTITY, holder)
                    .withParameter(LootContextParams.TOOL, activeWeapon)
                    .withParameter(LootContextParams.EXPLOSION_RADIUS, 0F);

            for (int i = 0; i <= tierMultiplier; i++) {
                if (!isFixed) {
                    entityId = mobList[GTValues.RNG.nextInt(mobList.length)];
                    Optional<EntityType<?>> entityType = EntityType.byString(entityId);
                    if (entityType.isEmpty()) continue;
                    entity = entityType.get().create(serverLevel);
                }
                if (!(entity instanceof Mob mob)) continue;
                if (CommonProxy.isBoss(entity)) continue;
                xp += mob.getExperienceReward() * multiplier;
                String[] mobParts = StringUtils.decompose(entityId);
                if (mobParts.length < 2) continue;
                LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(RLUtils.fromNamespaceAndPath(mobParts[0], "entities/" + mobParts[1]));
                lootTable.getRandomItems(lootParams.withParameter(LootContextParams.THIS_ENTITY, entity).create(lootTable.getParamSet())).forEach(stack -> {
                    int count = stack.getCount();
                    itemStacks.add(isFixed ? stack.copyWithCount(parallel * count) : multiplier > 1 ? stack.copyWithCount(multiplier * count) : stack);
                });
                createRandomGemStack(serverLevel, entity, player, itemStacks, Math.max(1, (int) Math.sqrt(multiplier)));
                if (isFixed) break;
            }
            if (xp > 0) outputFluid(EIOFluids.XP_JUICE.getSource(), xp);
            int duration = Math.max(20, 200 - attackDamage);
            RecipeBuilder builder = getRecipeBuilder().duration(duration).EUt(getOverclockVoltage());
            itemStacks.forEach(builder::outputItems);
            Recipe recipe = builder.buildRawRecipe();
            if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        }
        return null;
    }

    private static void createRandomGemStack(ServerLevel level, Entity entity, Player player, ItemStackSet set, int m) {
        if (entity instanceof Monster) {
            float chance = AdventureConfig.gemDropChance + (entity.getPersistentData().contains("apoth.boss") ? AdventureConfig.gemBossBonus : 0);
            if (player.getRandom().nextFloat() <= chance) {
                var item = GemRegistry.createRandomGemStack(player.getRandom(), level, player.getLuck(), WeightedDynamicRegistry.IDimensional.matches(level), GameStagesCompat.IStaged.matches(player));
                if (item.isEmpty()) return;
                item.setCount(item.getCount() * m);
                set.add(item);
            }
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
