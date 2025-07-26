package com.gtocore.common.forge;

import com.gtocore.common.ServerCache;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOCommands;
import com.gtocore.common.data.GTOEffects;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.ItemMap;
import com.gtocore.common.machine.multiblock.electric.voidseries.VoidTransporterMachine;
import com.gtocore.common.network.ServerMessage;
import com.gtocore.common.network.SyncFieldManager;
import com.gtocore.common.saved.*;
import com.gtocore.utils.OrganUtilsKt;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.feature.IVacuumMachine;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.ServerUtils;
import com.gtolib.utils.SphereExplosion;
import com.gtolib.utils.register.BlockRegisterUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.hollingsworth.arsnouveau.common.items.SpellBook;
import earth.terrarium.adastra.common.entities.mob.GlacianRam;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Objects;
import java.util.Optional;

@Scanned
public final class ForgeCommonEvent {

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
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof GlacianRam glacianRam) {
            var level = glacianRam.level();
            var server = level.getServer();
            if (server != null && glacianRam.getRandom().nextInt(20) == 1) {
                level.addFreshEntity(new ItemEntity(level, glacianRam.getX(), glacianRam.getY(), glacianRam.getZ(), GTOItems.GLACIO_SPIRIT.asStack()));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel) {
            OrganUtilsKt.ktFreshOrganState(IEnhancedPlayer.of(player).getPlayerData());
            Optional.ofNullable(player.getEffect(GTOEffects.MYSTERIOUS_BOOST.get())).ifPresent(effect -> {
                if (MetaMachine.getMachine(serverLevel, player.getOnPos()) instanceof WorkableTieredMachine machine && machine.getRecipeLogic().isWorking()) {
                    RecipeLogic recipeLogic = machine.getRecipeLogic();
                    int currentProgress = recipeLogic.getProgress();
                    int maxProgress = recipeLogic.getMaxProgress();
                    Optional.ofNullable(recipeLogic.getLastRecipe()).ifPresent(recipe -> {
                        int recipeEUtTier = RecipeHelper.getRecipeEUtTier(recipe);
                        if (effect.getAmplifier() >= recipeEUtTier) {
                            int progress = Math.min(currentProgress + Math.min((int) (((double) 1 / GTValues.RNG.nextInt(6, 10)) * maxProgress), 20 * 30), maxProgress - 1);
                            effect.duration -= progress - currentProgress;
                            recipeLogic.setProgress(progress);
                            serverLevel.sendParticles(ParticleTypes.FIREWORK, machine.getPos().getX(), machine.getPos().getY() + 6, machine.getPos().getZ(),
                                    3,  // 粒子数量
                                    0.3, // X方向扩散
                                    0.2, // Y方向扩散
                                    0.3, // Z方向扩散
                                    0.02 // 粒子速度
                            );
                        }
                    });
                }
            });
        }
    }

    @SubscribeEvent
    @SuppressWarnings("all")
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level == null) return;
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (item instanceof SpellBook spellBook) {
            if (spellBook.use(level, player, hand).getResult().consumesAction()) {
                event.setCanceled(true);
            }
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
            SphereExplosion.explosion(pos, level, 800, true, true);
            return;
        }

        if (item == GTOItems.UNSTABLE_STAR.get() && level.getBlockState(pos).getBlock() == GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.get()) {
            SphereExplosion.explosion(pos, level, 2000, true, true);
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
            if (GTCEu.isProd()) {
                player.displayClientMessage(Component.translatable("gtocore.gtm", Component.literal("GitHub").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/GregTech-Odyssey/GregTech-Odyssey/issues")))), false);
                player.displayClientMessage(Component.translatable("gtocore.dev", Component.literal("GitHub").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/GregTech-Odyssey/GregTech-Odyssey/issues")))), false);
                Configurator.setRootLevel(org.apache.logging.log4j.Level.INFO);
            } else {
                Configurator.setRootLevel(org.apache.logging.log4j.Level.DEBUG);
            }
            if (player instanceof IEnhancedPlayer enhancedPlayer) {
                ServerMessage.send(player.getServer(), player, "loggedIn", buf -> buf.writeUUID(ServerUtils.getServerIdentifier()));
                enhancedPlayer.getPlayerData().setDrift(enhancedPlayer.getPlayerData().disableDrift);
                OrganUtilsKt.ktFreshOrganState(enhancedPlayer.getPlayerData());
            }
        }
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            ServerLevel serverLevel = level.getServer().getLevel(Level.OVERWORLD);
            if (serverLevel == null) return;
            DysonSphereSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(DysonSphereSavaedData::new, DysonSphereSavaedData::new, "dyson_sphere_data");
            RecipeRunLimitSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(RecipeRunLimitSavaedData::new, RecipeRunLimitSavaedData::new, " recipe_run_limit_data");
            serverLevel.getDataStorage().computeIfAbsent(MEWirelessSavedData.INSTANCE::load, (() -> MEWirelessSavedData.INSTANCE), "me_wireless_connection_manager");
        }
    }

    @SubscribeEvent
    public static void onServerStoppingEvent(ServerStoppingEvent event) {
        SyncFieldManager.INSTANCE.clear();
        ServerCache.observe = false;
    }

    @RegisterLanguage(valuePrefix = "gtocore.lang", en = "Channel mode command banned in expert", cn = "在专家模式下，频道模式命令被禁止")
    private static final String CHANNEL_MODE_COMMAND_BANNED = "banned";

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void onCommandExecution(CommandEvent event) {
        var command = event.getParseResults().getReader().getString();
        if (command.contains("ae2") && command.contains("channelmode")) {
            if (GTOCore.isExpert()) {
                event.setCanceled(true);
                if (event.getParseResults().getContext().getSource().isPlayer()) {
                    Player player = event.getParseResults().getContext().getSource().getPlayer();
                    player.sendSystemMessage(Component.translatable("gtocore.lang." + CHANNEL_MODE_COMMAND_BANNED));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        GTOCommands.init(event.getDispatcher());
    }
}
