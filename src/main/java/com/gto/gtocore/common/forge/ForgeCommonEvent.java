package com.gto.gtocore.common.forge;

import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.api.machine.feature.IVacuumMachine;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOCommands;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.item.ItemMap;
import com.gto.gtocore.common.machine.multiblock.electric.viod.VoidTransporterMachine;
import com.gto.gtocore.common.saved.DysonSphereSavaedData;
import com.gto.gtocore.common.saved.ExtendWirelessEnergySavaedData;
import com.gto.gtocore.common.saved.InfinityCellSavaedData;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.utils.ServerUtils;
import com.gto.gtocore.utils.SphereExplosion;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.hepdd.gtmthings.data.WirelessEnergySavaedData;
import earth.terrarium.adastra.common.registry.ModBlocks;

import java.util.List;
import java.util.Objects;

public final class ForgeCommonEvent {

    @SubscribeEvent
    public static void onFoodConsume(LivingEntityUseItemEvent event) {
        if (GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeat) {
            if (event.getEntity() instanceof Player player && Objects.equals(20, event.getDuration()) && !player.level().isClientSide()) {
                int distacne = GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeatRange;
                hurtAnimalsNearPlayer(player, Items.BEEF, Cow.class, event, distacne);
                hurtAnimalsNearPlayer(player, Items.COOKED_BEEF, Cow.class, event, distacne);

                hurtAnimalsNearPlayer(player, Items.CHICKEN, Chicken.class, event, distacne);
                hurtAnimalsNearPlayer(player, Items.COOKED_CHICKEN, Chicken.class, event, distacne);

                hurtAnimalsNearPlayer(player, Items.PORKCHOP, Pig.class, event, distacne);
                hurtAnimalsNearPlayer(player, Items.COOKED_PORKCHOP, Pig.class, event, distacne);

                hurtAnimalsNearPlayer(player, Items.MUTTON, Sheep.class, event, distacne);
                hurtAnimalsNearPlayer(player, Items.COOKED_MUTTON, Sheep.class, event, distacne);
            }
        }
    }

    private static <T extends Animal> void hurtAnimalsNearPlayer(Player player, Item foodItem, Class<T> animalClass, LivingEntityUseItemEvent event, float distance) {
        if (event.getItem().is(foodItem)) {
            Level level = player.level();
            List<T> animalEntities = level.getEntitiesOfClass(animalClass, player.getBoundingBox().inflate(distance));
            for (T animal : animalEntities) {
                animal.hurt(player.damageSources().playerAttack(player), Math.max(animal.getMaxHealth() / 20, 0.5F));
            }
        }
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

        if (item == Items.ENDER_EYE && level.getBlockState(pos).getBlock() == Blocks.END_PORTAL_FRAME) {
            ItemStack stack = player.getOffhandItem();
            if (stack.is(GTOItems.DIMENSION_DATA.get()) && stack.hasTag() && stack.getOrCreateTag().getString("dim").equals(GTOWorldGenLayers.THE_END.toString())) {
                player.setItemInHand(InteractionHand.OFF_HAND, stack.copyWithCount(stack.getCount() - 1));
                return;
            }
            event.setCanceled(true);
            return;
        }

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

        if ((player.getMainHandItem().isEmpty() || player.getMainHandItem().is(GTOItems.DIMENSION_DATA.get())) && player.getOffhandItem().isEmpty()) {
            Block block = level.getBlockState(pos).getBlock();
            MinecraftServer server = level.getServer();
            if (server == null) return;
            String name = player.getName().getString();
            String dim = level.dimension().location().toString();
            CompoundTag data = player.getPersistentData();
            if (block == Blocks.CRYING_OBSIDIAN) {
                if (!Objects.equals(dim, "gtocore:flat")) {
                    if (checkTransporter(pos, level)) return;
                    int value = Objects.equals(dim, "gtocore:void") ? 1 : 10;
                    data.putDouble("y_f", player.getY() + 1);
                    data.putString("dim_f", dim);
                    ServerUtils.runCommandSilent(server, "execute in gtocore:flat as " + name + " run tp " + pos.getX() * value + " 64 " + pos.getZ() * value);
                    ServerUtils.runCommandSilent(server, "execute in gtocore:flat run fill " + pos.getX() * value + " 63 " + pos.getZ() * value + " " + pos.getX() * value + " 63 " + pos.getZ() * value + " minecraft:crying_obsidian");
                } else {
                    String dima = data.getString("dim_f");
                    int value = "gtocore:void".equals(dima) ? 1 : 10;
                    ServerUtils.runCommandSilent(server, "execute in " + dima + " as " + name + " run tp " + pos.getX() / value + " " + data.getDouble("y_f") + " " + pos.getZ() / value);
                }
                return;
            }

            if (block == Blocks.OBSIDIAN) {
                if (!Objects.equals(dim, "gtocore:void")) {
                    if (checkTransporter(pos, level)) return;
                    int value = Objects.equals(dim, "gtocore:flat") ? 1 : 10;
                    data.putDouble("y_v", player.getY() + 1);
                    data.putString("dim_v", dim);
                    ServerUtils.runCommandSilent(server, "execute in gtocore:void as " + name + " run tp " + pos.getX() * value + " 64 " + pos.getZ() * value);
                    ServerUtils.runCommandSilent(server, "execute in gtocore:void run fill " + pos.getX() * value + " 63 " + pos.getZ() * value + " " + pos.getX() * value + " 63 " + pos.getZ() * value + " minecraft:obsidian");
                } else {
                    String dima = data.getString("dim_v");
                    int value = "gtocore:flat".equals(dima) ? 1 : 10;
                    ServerUtils.runCommandSilent(server, "execute in " + dima + " as " + name + " run tp " + pos.getX() / value + " " + data.getDouble("y_v") + " " + pos.getZ() / value);
                }
                return;
            }

            if (block == GTOBlocks.REACTOR_CORE.get()) {
                if ("gtocore:ancient_world".equals(dim) || "minecraft:the_nether".equals(dim)) {
                    String dimdata = "gtocore:ancient_world".equals(dim) ? "aw" : "ne";
                    ServerUtils.runCommandSilent(server, "execute in " + data.getString("dim_" + dimdata) + " as " + name + " run tp " + data.getDouble("pos_" + dimdata + "_x") + " " + data.getDouble("pos_" + dimdata + "_y") + " " + data.getDouble("pos_" + dimdata + "_z"));
                } else {
                    if (checkBlocks(pos, level, ModBlocks.STEEL_BLOCK.get(), Blocks.DIAMOND_BLOCK)) {
                        data.putDouble("pos_aw_x", player.getX());
                        data.putDouble("pos_aw_y", player.getY());
                        data.putDouble("pos_aw_z", player.getZ());
                        data.putString("dim_aw", dim);
                        ServerUtils.runCommandSilent(server, "execute in gtocore:ancient_world as " + name + " run tp 0 128 0");
                        ServerUtils.runCommandSilent(server, "execute in gtocore:ancient_world run fill 0 127 0 0 127 0 gtocore:reactor_core");
                    } else if (checkBlocks(pos, level, Blocks.GOLD_BLOCK, Blocks.EMERALD_BLOCK)) {
                        ItemStack stack = player.getOffhandItem();
                        if (stack.is(GTOItems.DIMENSION_DATA.get()) && stack.hasTag() && stack.getOrCreateTag().getString("dim").equals(GTOWorldGenLayers.THE_NETHER.toString())) {
                            data.putDouble("pos_ne_x", player.getX());
                            data.putDouble("pos_ne_y", player.getY());
                            data.putDouble("pos_ne_z", player.getZ());
                            data.putString("dim_ne", dim);
                            ServerUtils.runCommandSilent(server, "execute in minecraft:the_nether as " + name + " run tp 0 128 0");
                            ServerUtils.runCommandSilent(server, "execute in minecraft:the_nether run fill 0 127 0 0 127 0 gtocore:reactor_core");
                        } else {
                            player.displayClientMessage(Component.translatable("gtocore.handheld_data_required"), true);
                        }
                    } else {
                        player.displayClientMessage(Component.translatable("gtocore.structural_error"), true);
                    }
                }
            }
        }
    }

    private static boolean checkBlocks(BlockPos pos, Level level, Block block1, Block block2) {
        BlockPos[] offsets = {
                pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
                pos.offset(0, 0, 1),
                pos.offset(0, 0, -1),
                pos.offset(1, 0, 1),
                pos.offset(1, 0, -1),
                pos.offset(-1, 0, 1),
                pos.offset(-1, 0, -1)
        };
        Block[] blocks = { block1, block2 };
        for (int i = 0; i < offsets.length; i++) {
            if (level.getBlockState(offsets[i]).getBlock() != blocks[i / 4]) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkTransporter(BlockPos pos, Level level) {
        return !(MetaMachine.getMachine(level, pos.offset(0, -1, 0)) instanceof VoidTransporterMachine machine && machine.getRecipeLogic().isWorking());
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
        event.getEntity().displayClientMessage(Component.translatable("gtocore.dev"), false);
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            ServerLevel serverLevel = level.getServer().getLevel(Level.OVERWORLD);
            if (serverLevel == null) return;
            InfinityCellSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(InfinityCellSavaedData::readNbt, InfinityCellSavaedData::new, "infinite_storage_cell_data");
            DysonSphereSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(DysonSphereSavaedData::new, DysonSphereSavaedData::new, "dyson_sphere_data");
            WirelessEnergySavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(ExtendWirelessEnergySavaedData::new, ExtendWirelessEnergySavaedData::new, "wireless_energy_data");
        }
    }

    @SubscribeEvent
    public static void onServerStartedEvent(ServerStartedEvent event) {
        if (GTOConfig.INSTANCE.selfRestraint) ServerUtils.getPersistentData(event.getServer()).putBoolean("srm", true);
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        GTOCommands.init(event.getDispatcher());
    }
}
