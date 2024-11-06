package com.gto.gtocore.forge;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.IVacuumMachine;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.machine.multiblock.water.WaterPurificationPlantMachine;
import com.gto.gtocore.common.machine.multiblock.water.WaterPurificationUnitMachine;
import com.gto.gtocore.config.GTOConfigHolder;
import com.gto.gtocore.utils.GTOExplosion;
import com.gto.gtocore.utils.NumberUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = GTOCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCommonEventListener {

    @SubscribeEvent
    public static void onPortalSpawnEvent(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == GTOBlocks.CREATE_AGGREGATIONE_CORE.get()) {
            LevelAccessor level = event.getLevel();
            if (level == null) return;
            BlockPos pos = event.getPos().offset(0, 0, 1);
            Block block = level.getBlockState(pos).getBlock();
            if (block == Blocks.COMMAND_BLOCK || block == Blocks.CHAIN_COMMAND_BLOCK || block == Blocks.REPEATING_COMMAND_BLOCK) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity().getItemInHand(event.getHand()).getItem() == GTOItems.BEDROCK_DESTROYER.get()) {
            Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
            if (block == Blocks.BEDROCK) event.getLevel().setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
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
            if (Objects.equals(player.getItemInHand(InteractionHand.OFF_HAND).kjs$getId(), "kubejs:end_data")) {
                player.setItemInHand(InteractionHand.OFF_HAND, itemStack.copyWithCount(itemStack.getCount() - 1));
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
            GTOExplosion nukeExplosion = new GTOExplosion(pos, level, 80);
            nukeExplosion.setBreakBedrock(true);
            nukeExplosion.finalizeExplosion(true);
            return;
        }

        if (item == GTItems.GRAVI_STAR.get() && level.getBlockState(pos).getBlock() == GTOBlocks.LEPTONIC_CHARGE.get()) {
            GTOExplosion nukeExplosion = new GTOExplosion(pos, level, 200);
            nukeExplosion.setBreakBedrock(true);
            nukeExplosion.finalizeExplosion(true);
            return;
        }

        if (item == GTOItems.UNSTABLE_STAR.get() && level.getBlockState(pos).getBlock() == GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.get()) {
            GTOExplosion nukeExplosion = new GTOExplosion(pos, level, 1000);
            nukeExplosion.setBreakBedrock(true);
            nukeExplosion.finalizeExplosion(false);
            return;
        }

        if (item == GTItems.TOOL_DATA_STICK.get() && player.isShiftKeyDown()) {
            CompoundTag nbt = itemStack.getOrCreateTag();
            MetaMachine machine = MetaMachine.getMachine(level, pos);
            if (machine instanceof WaterPurificationPlantMachine waterPurificationPlantMachine) {
                int[] poss = nbt.getIntArray("poss");
                if (poss.length < 3) return;
                nbt.remove("poss");
                BlockPos waterPos = new BlockPos(poss[0], poss[1], poss[2]);
                waterPurificationPlantMachine.getPoss().add(waterPos);
                if (NumberUtils.calculateDistance(waterPos, pos) > 32) {
                    player.displayClientMessage(Component.translatable("chat.wireless_connect.out_of_range"), true);
                } else if (waterPurificationPlantMachine.addWaterPurificationMachine(waterPos, level)) {
                    player.displayClientMessage(Component.translatable("chat.wireless_bind", poss[0], poss[1], poss[2]), true);
                }
            } else if (machine instanceof WaterPurificationUnitMachine) {
                nbt.putIntArray("poss", new int[] { machine.getPos().getX(), machine.getPos().getY(), machine.getPos().getZ() });
                player.displayClientMessage(Component.translatable("ldlib.gui.compass.save_success"), true);
            }
            return;
        }

        if (item.kjs$getId().equals("kubejs:command_wand")) {
            Block block = level.getBlockState(pos).getBlock();
            if (player.isShiftKeyDown()) {
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
            BlockPos offset = pos.offset(0, 1, 0);
            if (block == GTOBlocks.CREATE_AGGREGATIONE_CORE.get() && level.getBlockState(offset).getBlock() == Blocks.AIR) {
                ItemStack offItem = player.getItemInHand(InteractionHand.OFF_HAND);
                if (offItem.getItem() == Blocks.COMMAND_BLOCK.asItem()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, offItem.copyWithCount(offItem.getCount() - 1));
                    level.setBlockAndUpdate(offset, Blocks.COMMAND_BLOCK.defaultBlockState());
                    return;
                }
                if (offItem.getItem() == Blocks.CHAIN_COMMAND_BLOCK.asItem()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, offItem.copyWithCount(offItem.getCount() - 1));
                    level.setBlockAndUpdate(offset, Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState());
                    return;
                }
                if (offItem.getItem() == Blocks.REPEATING_COMMAND_BLOCK.asItem()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, offItem.copyWithCount(offItem.getCount() - 1));
                    level.setBlockAndUpdate(offset, Blocks.REPEATING_COMMAND_BLOCK.defaultBlockState());
                }
            }
            return;
        }

        if ((player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || player.getItemInHand(InteractionHand.MAIN_HAND).kjs$getId().equals("kubejs:nether_data")) && player.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
            Block block = level.getBlockState(pos).getBlock();
            MinecraftServer server = level.getServer();
            if (server == null) return;
            String name = player.getName().getString();
            String dim = level.kjs$getDimension().toString();
            CompoundTag data = player.getPersistentData();
            if (block == Blocks.CRYING_OBSIDIAN) {
                if (!Objects.equals(dim, "kubejs:flat")) {
                    int value = Objects.equals(dim, "kubejs:void") ? 1 : 10;
                    data.putDouble("y_f", player.getY() + 1);
                    data.putString("dim_f", dim);
                    server.kjs$runCommandSilent("execute in kubejs:flat as " + name + " run tp " + pos.getX() * value + " 64 " + pos.getZ() * value);
                    server.kjs$runCommandSilent("execute in kubejs:flat run fill " + pos.getX() * value + " 63 " + pos.getZ() * value + " " + pos.getX() * value + " 63 " + pos.getZ() * value + " minecraft:crying_obsidian");
                } else {
                    String dima = data.getString("dim_f");
                    int value = dima.equals("kubejs:void") ? 1 : 10;
                    server.kjs$runCommandSilent("execute in " + dima + " as " + name + " run tp " + pos.getX() / value + " " + data.getDouble("y_f") + " " + pos.getZ() / value);
                }
                return;
            }

            if (block == Blocks.OBSIDIAN) {
                if (!Objects.equals(dim, "kubejs:void")) {
                    int value = Objects.equals(dim, "kubejs:flat") ? 1 : 10;
                    data.putDouble("y_v", player.getY() + 1);
                    data.putString("dim_v", dim);
                    server.kjs$runCommandSilent("execute in kubejs:void as " + name + " run tp " + pos.getX() * value + " 64 " + pos.getZ() * value);
                    server.kjs$runCommandSilent("execute in kubejs:void run fill " + pos.getX() * value + " 63 " + pos.getZ() * value + " " + pos.getX() * value + " 63 " + pos.getZ() * value + " minecraft:obsidian");
                } else {
                    String dima = data.getString("dim_v");
                    int value = dima.equals("kubejs:flat") ? 1 : 10;
                    server.kjs$runCommandSilent("execute in " + dima + " as " + name + " run tp " + pos.getX() / value + " " + data.getDouble("y_v") + " " + pos.getZ() / value);
                }
                return;
            }

            if (block == GTOBlocks.REACTOR_CORE.get()) {
                if (dim.equals("kubejs:ancient_world") || dim.equals("minecraft:the_nether")) {
                    String dimdata = dim.equals("kubejs:ancient_world") ? "aw" : "ne";
                    server.kjs$runCommandSilent("execute in " + data.getString("dim_" + dimdata) + " as " + name + " run tp " + data.getDouble("pos_" + dimdata + "_x") + " " + data.getDouble("pos_" + dimdata + "_y") + " " + data.getDouble("pos_" + dimdata + "_z"));
                } else {
                    if (checkBlocks(pos, level, ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.Steel), Blocks.DIAMOND_BLOCK)) {
                        data.putDouble("pos_aw_x", player.getX());
                        data.putDouble("pos_aw_y", player.getY());
                        data.putDouble("pos_aw_z", player.getZ());
                        data.putString("dim_aw", dim);
                        server.kjs$runCommandSilent("execute in kubejs:ancient_world as " + name + " run tp 0 128 0");
                        server.kjs$runCommandSilent("execute in kubejs:ancient_world run fill 0 127 0 0 127 0 gtocore:reactor_core");
                    } else if (checkBlocks(pos, level, Blocks.GOLD_BLOCK, Blocks.EMERALD_BLOCK)) {
                        if (player.getItemInHand(InteractionHand.MAIN_HAND).kjs$getId().equals("kubejs:nether_data")) {
                            data.putDouble("pos_ne_x", player.getX());
                            data.putDouble("pos_ne_y", player.getY());
                            data.putDouble("pos_ne_z", player.getZ());
                            data.putString("dim_ne", dim);
                            server.kjs$runCommandSilent("execute in minecraft:the_nether as " + name + " run tp 0 128 0");
                            server.kjs$runCommandSilent("execute in minecraft:the_nether run fill 0 127 0 0 127 0 gtocore:reactor_core");
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

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() == GTOItems.WARPED_ENDER_PEAL.asItem()) {
            Level level = event.getLevel();
            if (level == null) return;
            Player player = event.getEntity();
            MinecraftServer server = level.getServer();
            if (server == null) return;
            String name = player.getName().getString();
            CompoundTag data = player.getPersistentData();
            int pearl = data.getInt("pearl_slot");
            if (player.isShiftKeyDown()) {
                data.putInt("pearl_x_" + pearl, (int) player.getX());
                data.putInt("pearl_y_" + pearl, (int) player.getY());
                data.putInt("pearl_z_" + pearl, (int) player.getZ());
                data.putString("pearl_dim_" + pearl, level.kjs$getDimension().toString());
                server.kjs$runCommandSilent("playsound minecraft:entity.enderman.ambient ambient " + name + " " + player.getX() + " " + player.getY() + " " + player.getZ());
                player.displayClientMessage(Component.translatable("gtocore.position_are_saved_to_this_slot"), true);
            } else {
                server.kjs$runCommandSilent("execute in " + data.getString("pearl_dim_" + pearl) + " run tp " + name + " " + data.getInt("pearl_x_" + pearl) + " " + data.getInt("pearl_y_" + pearl) + " " + data.getInt("pearl_z_" + pearl));
                server.kjs$runCommandSilent("playsound minecraft:entity.enderman.teleport ambient " + name + " " + player.getX() + " " + player.getY() + " " + player.getZ());
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack itemStack = event.getItemStack();
        if (!"gtceu:echoite_vajra".equals(itemStack.kjs$getId())) {
            return;
        }
        Player player = event.getEntity();
        if (!player.isShiftKeyDown() || player.kjs$isMiningBlock()) {
            return;
        }
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            CompoundTag behaviours = tag.getCompound("GT.Behaviours");
            if (behaviours.getByte("RelocateMinedBlocks") == 1) {
                behaviours.remove("RelocateMinedBlocks");
                player.displayClientMessage(Component.translatable("gtocore.relocate_mined_blocks", Component.translatable("gtocore.machine.off")), true);
            } else {
                behaviours.putByte("RelocateMinedBlocks", (byte) 1);
                player.displayClientMessage(Component.translatable("gtocore.relocate_mined_blocks", Component.translatable("gtocore.machine.on")), true);
            }
        }
    }

    @SubscribeEvent
    public static void onFarmlandTrampleEvent(BlockEvent.FarmlandTrampleEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEnderTeleportEvent(EntityTeleportEvent.EnderEntity event) {
        if (event.getEntity() instanceof EnderMan || event.getEntity() instanceof Shulker) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (GTOConfigHolder.INSTANCE.disableDrift && event.phase == TickEvent.Phase.END &&
                event.side == LogicalSide.CLIENT && player.xxa == 0 && player.zza == 0) {
            player.setDeltaMovement(player.getDeltaMovement().multiply(0.5, 1, 0.5));
        }
        if (player.tickCount % 20 == 0) {
            MinecraftServer server = player.getServer();
            if (server == null) return;
            player.onUpdateAbilities();
            Level level = player.level();
            CompoundTag data = player.getPersistentData();
            String name = player.getName().getString();
            String armorSlots = player.getArmorSlots().toString();
            boolean sfa = Objects.equals(armorSlots, "[1 space_fermium_boots, 1 space_fermium_leggings, 1 space_fermium_chestplate, 1 space_fermium_helmet]");
            boolean fa = Objects.equals(armorSlots, "[1 fermium_boots, 1 fermium_leggings, 1 fermium_chestplate, 1 fermium_helmet]");
            boolean ma = Objects.equals(armorSlots, "[1 magneto_hydro_dynamically_con_strained_star_matter_boots, 1 magneto_hydro_dynamically_con_strained_star_matter_leggings, 1 magneto_hydro_dynamically_con_strained_star_matter_chestplate, 1 magneto_hydro_dynamically_con_strained_star_matter_helmet]");
            if (level.kjs$getDimension().toString().equals("kubejs:create")) {
                if (!ma) {
                    server.kjs$runCommandSilent("execute in minecraft:overworld as " + name + " run tp 0 100 0");
                    player.kill();
                }
                server.kjs$runCommandSilent("execute at " + name + " run kill @e[distance=..100,name=!" + name + ",type=!item]");
            }
            data.putBoolean("space_state", sfa || Objects.equals(armorSlots, "[1 quarktech_boots, 1 quarktech_leggings, 1 advanced_quarktech_chestplate, 1 quarktech_helmet]") || Objects.equals(armorSlots, "[1 quarktech_boots, 1 quarktech_leggings, 1 quarktech_chestplate, 1 quarktech_helmet]"));
            data.putBoolean("ma_state", ma);
            if (ma || fa || sfa) {
                player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 200, 0, false, false));
                if (data.getBoolean("night_vision")) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
                }
                if (!player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = true;
                    data.putBoolean("night_vision", false);
                    data.putBoolean("can_fly", true);
                    data.putInt("fly_speed", 1);
                }
            } else if (data.getBoolean("can_fly")) {
                player.getAbilities().mayfly = false;
                data.putBoolean("can_fly", false);
                data.putInt("fly_speed", 1);
                player.getAbilities().setFlyingSpeed(0.05F);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity().getPersistentData().getBoolean("ma_state")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity().getPersistentData().getBoolean("ma_state")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof FallingBlockEntity fallingBlock) {
            fallingBlock.kill();
        }
    }
}
