package com.gto.gtocore.common.forge;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.entity.IEnhancedPlayer;
import com.gto.gtocore.api.machine.feature.IVacuumMachine;
import com.gto.gtocore.api.recipe.AsyncRecipeOutputTask;
import com.gto.gtocore.api.recipe.AsyncRecipeSearchTask;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOCommands;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.item.ItemMap;
import com.gto.gtocore.common.machine.multiblock.electric.voidseries.VoidTransporterMachine;
import com.gto.gtocore.common.machine.noenergy.PerformanceMonitorMachine;
import com.gto.gtocore.common.network.ServerMessage;
import com.gto.gtocore.common.saved.*;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.utils.ServerUtils;
import com.gto.gtocore.utils.SphereExplosion;
import com.gto.gtocore.utils.register.BlockRegisterUtils;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.hepdd.gtmthings.data.WirelessEnergySavaedData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ForgeCommonEvent {

    public static class FoodHurtLogic{
        public static final Map<Item, Class<?>> foodToEntityClass = new HashMap<>();
        public static boolean initialized = false;

        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent event) throws IllegalAccessException {
            FoodHurtLogic.initialize(event.getServer());
        }

        public static void initialize(MinecraftServer server) throws IllegalAccessException {
            if(initialized)return;;
            mapFoodToEntity_SoftCode(server);
            initialized = true;
        }

        private static void mapFoodToEntity_SoftCode(MinecraftServer server) {
            if (server == null) {
                return;
            }

            // 清空现有映射，确保不受之前的影响
            foodToEntityClass.clear();

            int mappedCount = 0;
            int entityCount = 0;
            int processedEntities = 0;

            try {

                Map<String, Class<?>>  foodEntityMapping = new HashMap<>();

                foodEntityMapping.put("pork", Pig.class);
                foodEntityMapping.put("beef", Cow.class);
                foodEntityMapping.put("chicken", Chicken.class);
                foodEntityMapping.put("mutton", Sheep.class);
                foodEntityMapping.put("rabbit", Rabbit.class);
                foodEntityMapping.put("cod", Cod.class);
                foodEntityMapping.put("salmon", Salmon.class);
                foodEntityMapping.put("fish", TropicalFish.class);

                // 遍历所有注册的物品而不是实体
                for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
                    Item item = entry.getValue();
                    entityCount++;

                    if (!item.isEdible()) continue;

                    String itemId = entry.getKey().location().toString();

                    for (Map.Entry<String, Class<?>> mapping : foodEntityMapping.entrySet()) {
                        if (itemId.contains(mapping.getKey())) {
                            Class<?> entityType = mapping.getValue();
                            foodToEntityClass.put(item, entityType);
                            mappedCount++;
                            GTOCore.LOGGER.info("映射: " + itemId + " -> " + entityType.getName());
                            break;
                        }
                    }

                    processedEntities++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @SubscribeEvent
        public static void onFoodConsume(LivingEntityUseItemEvent event) {
            if (GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeat) {
                if (event.getEntity() instanceof Player player && Objects.equals(10, event.getDuration()) && !player.level().isClientSide()) {
                    int distance = GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeatRange;

                    foodToEntityClass.forEach((item, entityClass) -> {
                        if(event.getItem().is(item)){
                            hurtAnimalsNearPlayer(player,  entityClass, distance);
                        }
                    });
                }
            }
        }

        private static <T extends LivingEntity> void hurtAnimalsNearPlayer(Player player, Class<?> entityClass, float distance) {
            Level level = player.level();
            List<? extends LivingEntity> entitiesOfClass = level.getEntitiesOfClass((Class<? extends LivingEntity>) entityClass, player.getBoundingBox().inflate(distance));
            entitiesOfClass.forEach(entity -> {entity.hurt(player.damageSources().playerAttack(player), Math.max(entity.getMaxHealth() / 40, 0.25F));});
        }
    }


    @SubscribeEvent
    public static void onDropsEvent(LivingDropsEvent e) {
        dev.shadowsoffire.apotheosis.Apoth.Enchantments.CAPTURING.get().handleCapturing(e);
    }

    @SubscribeEvent
    public static void onPortalSpawnEvent(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof FallingBlockEntity fallingBlock) {
            fallingBlock.discard();
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level == null) return;
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (item == GTOItems.RAW_VACUUM_TUBE.get() && player.isShiftKeyDown() && MetaMachine.getMachine(level, pos) instanceof IVacuumMachine vacuumMachine && vacuumMachine.getVacuumTier() > 0) {
            player.setItemInHand(hand, itemStack.copyWithCount(itemStack.getCount() - 1));
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(), GTItems.VACUUM_TUBE.asStack()));
            return;
        }

        if (item == GTItems.QUANTUM_STAR.get() && level.getBlockState(pos).getBlock() == GTOBlocks.NAQUADRIA_CHARGE.get()) {
            SphereExplosion.explosion(pos, level, 200, true, true);
            return;
        }

        if (item == GTItems.GRAVI_STAR.get() && level.getBlockState(pos).getBlock() == GTOBlocks.LEPTONIC_CHARGE.get()) {
            SphereExplosion.explosion(pos, level, 200, true, true);
            return;
        }

        if (item == GTOItems.UNSTABLE_STAR.get() && level.getBlockState(pos).getBlock() == GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.get()) {
            SphereExplosion.explosion(pos, level, 200, true, true);
            return;
        }

        if (player.isShiftKeyDown()) {
            if (item == GTOItems.COMMAND_WAND.get()) {
                Block block = level.getBlockState(pos).getBlock();
                if (block == Blocks.COMMAND_BLOCK) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(), Blocks.COMMAND_BLOCK.asItem().getDefaultInstance()));
                    return;
                }
                if (block == Blocks.CHAIN_COMMAND_BLOCK) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(), Blocks.CHAIN_COMMAND_BLOCK.asItem().getDefaultInstance()));
                    return;
                }
                if (block == Blocks.REPEATING_COMMAND_BLOCK) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(), Blocks.REPEATING_COMMAND_BLOCK.asItem().getDefaultInstance()));
                    return;
                }
            }
        }

        if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
            Block block = level.getBlockState(pos).getBlock();
            MinecraftServer server = level.getServer();
            if (server == null) return;
            String dim = level.dimension().location().toString();
            CompoundTag data = player.getPersistentData();
            if (block == Blocks.CRYING_OBSIDIAN) {
                if (!Objects.equals(dim, "gtocore:flat")) {
                    if (VoidTransporterMachine.checkTransporter(pos, level, 0)) return;
                    ServerLevel serverLevel = server.getLevel(GTODimensions.getDimensionKey(GTODimensions.FLAT));
                    if (serverLevel != null) {
                        int value = Objects.equals(dim, "gtocore:void") ? 1 : 10;
                        data.putDouble("y_f", player.getY() + 1);
                        data.putString("dim_f", dim);
                        BlockPos blockPos = new BlockPos(pos.getX() * value, 64, pos.getZ() * value);
                        serverLevel.setBlockAndUpdate(blockPos.offset(0, -1, 0), Blocks.CRYING_OBSIDIAN.defaultBlockState());
                        ServerUtils.teleportToDimension(serverLevel, player, blockPos.getCenter());
                    }
                } else {
                    String dima = data.getString("dim_f");
                    int value = "gtocore:void".equals(dima) ? 1 : 10;
                    ServerUtils.teleportToDimension(server.getLevel(GTODimensions.getDimensionKey(new ResourceLocation(dima))), player, new Vec3((double) pos.getX() / value, data.getDouble("y_f"), (double) pos.getZ() / value));
                }
                return;
            }

            if (block == Blocks.OBSIDIAN) {
                if (!Objects.equals(dim, "gtocore:void")) {
                    if (VoidTransporterMachine.checkTransporter(pos, level, 0)) return;
                    ServerLevel serverLevel = server.getLevel(GTODimensions.getDimensionKey(GTODimensions.VOID));
                    if (serverLevel != null) {
                        int value = Objects.equals(dim, "gtocore:flat") ? 1 : 10;
                        data.putDouble("y_v", player.getY() + 1);
                        data.putString("dim_v", dim);
                        BlockPos blockPos = new BlockPos(pos.getX() * value, 64, pos.getZ() * value);
                        serverLevel.setBlockAndUpdate(blockPos.offset(0, -1, 0), Blocks.OBSIDIAN.defaultBlockState());
                        ServerUtils.teleportToDimension(serverLevel, player, blockPos.getCenter());
                    }
                } else {
                    String dima = data.getString("dim_v");
                    int value = "gtocore:flat".equals(dima) ? 1 : 10;
                    ServerUtils.teleportToDimension(server.getLevel(GTODimensions.getDimensionKey(new ResourceLocation(dima))), player, new Vec3((double) pos.getX() / value, data.getDouble("y_v"), (double) pos.getZ() / value));
                }
                return;
            }

            if (block == BlockRegisterUtils.REACTOR_CORE.get()) {
                if ("gtocore:ancient_world".equals(dim) || "minecraft:the_nether".equals(dim)) {
                    int dimdata = "gtocore:ancient_world".equals(dim) ? 1 : 2;
                    ServerUtils.teleportToDimension(server, player, new ResourceLocation(data.getString("dim_" + dimdata)), new Vec3(data.getDouble("pos_x_" + dimdata), data.getDouble("pos_y_" + dimdata), data.getDouble("pos_z_" + dimdata)));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        if (level == null) return;
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        Player player = event.getEntity();
        if (item == GTOItems.SCRAP_BOX.asItem()) {
            int count = itemStack.getCount();
            if (player.isShiftKeyDown()) {
                for (int i = 0; i < count; i++) {
                    level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), ItemMap.getScrapItem()));
                }
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
            } else {
                level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), ItemMap.getScrapItem()));
                player.setItemInHand(event.getHand(), itemStack.copyWithCount(count - 1));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!GTOConfig.INSTANCE.dev) player.displayClientMessage(Component.translatable("gtocore.dev", Component.literal("GitHub")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/GregTech-Odyssey/GregTech-Odyssey/issues")))),
                    false);
            if (player instanceof IEnhancedPlayer enhancedPlayer) {
                ServerMessage.sendData(player.getServer(), player, "loggedIn", null);
                enhancedPlayer.gtocore$setDrift(enhancedPlayer.gTOCore$isDisableDrift());
            }
        }
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            ServerLevel serverLevel = level.getServer().getLevel(Level.OVERWORLD);
            if (serverLevel == null) return;
            InfinityCellSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(InfinityCellSavaedData::readNbt, InfinityCellSavaedData::new, "infinite_storage_cell_data");
            DysonSphereSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(DysonSphereSavaedData::new, DysonSphereSavaedData::new, "dyson_sphere_data");
            WirelessEnergySavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(ExtendWirelessEnergySavaedData::new, ExtendWirelessEnergySavaedData::new, "wireless_energy_data");
            CommonSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(CommonSavaedData::new, CommonSavaedData::new, "common_data");
            RecipeRunLimitSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(RecipeRunLimitSavaedData::new, RecipeRunLimitSavaedData::new, " recipe_run_limit_data");
            if (GTOConfig.INSTANCE.selfRestraint) ServerUtils.getPersistentData().putBoolean("srm", true);
        }
    }

    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PerformanceMonitorMachine.observe = false;
        }
    }

    @SubscribeEvent
    public static void onServerStoppingEvent(ServerStoppingEvent event) {
        AsyncRecipeSearchTask.releaseExecutorService();
        AsyncRecipeOutputTask.releaseExecutorService();
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        GTOCommands.init(event.getDispatcher());
    }
}
