package com.gtocore.mixin.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.item.material.RuneItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.Optional;

@Mixin(RunicAltarBlockEntity.class)
public abstract class RunicAltarBlockEntityMixin extends SimpleInventoryBlockEntity implements ManaReceiver, Wandable {

    public RunicAltarBlockEntityMixin(BlockPos pos, BlockState state) {
        super(BotaniaBlockEntities.RUNE_ALTAR, pos, state);
    }

    @Shadow(remap = false)
    private static final int SET_KEEP_TICKS_EVENT = 0;
    @Shadow(remap = false)
    private static final int SET_COOLDOWN_EVENT = 1;
    @Shadow(remap = false)
    private static final int CRAFT_EFFECT_EVENT = 2;

    @Shadow(remap = false)
    private RunicAltarRecipe currentRecipe;

    @Shadow(remap = false)
    public int manaToGet = 0;
    @Shadow(remap = false)
    private int mana = 0;

    @Shadow(remap = false)
    private List<ItemStack> lastRecipe = null;

    @Shadow(remap = false)
    private int recipeKeepTicks = 0;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
        if (level.isClientSide) {
            return true;
        }

        RunicAltarRecipe recipe = null;

        if (currentRecipe != null) {
            recipe = currentRecipe;
        } else {
            Optional<RunicAltarRecipe> maybeRecipe = level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, getItemHandler(), level);
            if (maybeRecipe.isPresent()) {
                recipe = maybeRecipe.get();
            }
        }

        if (recipe != null && manaToGet > 0 && mana >= manaToGet) {
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition, worldPosition.offset(1, 1, 1)));
            ItemEntity livingrock = null;
            for (ItemEntity item : items) {
                if (item.isAlive() && !item.getItem().isEmpty() && item.getItem().is(BotaniaBlocks.livingrock.asItem())) {
                    livingrock = item;
                    break;
                }
            }

            if (livingrock != null) {
                int mana = recipe.getManaUsage();
                receiveMana(-mana);
                ItemStack output = recipe.assemble(getItemHandler(), getLevel().registryAccess());
                ItemEntity outputItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, output);
                XplatAbstractions.INSTANCE.itemFlagsComponent(outputItem).runicAltarSpawned = true;
                if (player != null) {
                    player.triggerRecipeCrafted(recipe, List.of(output));
                    output.onCraftedBy(level, player, output.getCount());
                }
                level.addFreshEntity(outputItem);
                currentRecipe = null;
                level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, getBlockPos());
                level.blockEvent(getBlockPos(), BotaniaBlocks.runeAltar, SET_COOLDOWN_EVENT, 60);
                level.blockEvent(getBlockPos(), BotaniaBlocks.runeAltar, CRAFT_EFFECT_EVENT, 0);

                saveLastRecipe();
                for (int i = 0; i < inventorySize(); i++) {
                    ItemStack stack = getItemHandler().getItem(i);
                    if (!stack.isEmpty()) {
                        if (!recipe.getId().getNamespace().equals("gtocore")) {
                            if (stack.getItem() instanceof RuneItem && (player == null || !player.getAbilities().instabuild)) {
                                ItemEntity outputRune = new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.5, getBlockPos().getZ() + 0.5, stack.copy());
                                XplatAbstractions.INSTANCE.itemFlagsComponent(outputRune).runicAltarSpawned = true;
                                level.addFreshEntity(outputRune);
                            }
                        }
                        getItemHandler().setItem(i, ItemStack.EMPTY);
                    }
                }

                EntityHelper.shrinkItem(livingrock);
            }
        }

        return true;
    }

    @Shadow(remap = false)
    private void saveLastRecipe() {
        lastRecipe = new ObjectArrayList<>();
        for (int i = 0; i < inventorySize(); i++) {
            ItemStack stack = getItemHandler().getItem(i);
            if (stack.isEmpty()) {
                break;
            }
            lastRecipe.add(stack.copy());
        }
        recipeKeepTicks = 400;
        level.blockEvent(getBlockPos(), BotaniaBlocks.runeAltar, SET_KEEP_TICKS_EVENT, 400);
    }
}
