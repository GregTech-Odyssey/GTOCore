package com.gtocore.common.forge;

import com.gtocore.common.ServerCache;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOCommands;
import com.gtocore.common.data.GTOEffects;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.ItemMap;
import com.gtocore.common.machine.multiblock.electric.voidseries.VoidTransporterMachine;
import com.gtocore.common.machine.noenergy.PerformanceMonitorMachine;
import com.gtocore.common.network.ServerMessage;
import com.gtocore.common.saved.*;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.ae2.storage.CellSavaedData;
import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.feature.IVacuumMachine;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.recipe.AsyncRecipeOutputTask;
import com.gtolib.utils.MathUtil;
import com.gtolib.utils.ServerUtils;
import com.gtolib.utils.SphereExplosion;
import com.gtolib.utils.register.BlockRegisterUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.data.WirelessEnergySavaedData;
import earth.terrarium.adastra.common.entities.mob.GlacianRam;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

@Scanned
public final class ForgeCommonEvent {

    public static final Object2IntOpenHashMap<MachineDefinition> SUPER_TANKS = new Object2IntOpenHashMap<>();

    @SubscribeEvent
    public static void registerItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack itemStack = event.getObject();
        if (itemStack.getItem() instanceof MetaMachineItem machineItem) {
            int maxAmount = SUPER_TANKS.getInt(machineItem.getDefinition());
            if (maxAmount > 0) {
                event.addCapability(GTCEu.id("fluid"), new ICapabilityProvider() {

                    @Override
                    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
                        if (capability == ForgeCapabilities.FLUID_HANDLER_ITEM) {
                            var tag = itemStack.getOrCreateTag();
                            var stored = FluidStack.loadFluidStackFromNBT(tag.getCompound("stored"));
                            int amount;
                            FluidStack stack;
                            if (stored.isEmpty()) {
                                tag.put("stored", FluidStack.EMPTY.writeToNBT(new CompoundTag()));
                                tag.put("storedAmount", LongTag.valueOf(0));
                                tag.put("maxAmount", LongTag.valueOf(maxAmount));
                                stack = FluidStack.EMPTY;
                                amount = 0;
                            } else {
                                amount = MathUtil.saturatedCast(tag.getLong("storedAmount"));
                                stack = new FluidStack(stored, 1000);
                                stack.setAmount(amount);
                            }
                            return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, LazyOptional.of(() -> new FluidHandlerItem(stack, maxAmount, tag, amount, itemStack)));
                        }
                        return LazyOptional.empty();
                    }
                });
            }
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
                Configurator.setRootLevel(org.apache.logging.log4j.Level.INFO);
            } else {
                Configurator.setRootLevel(org.apache.logging.log4j.Level.DEBUG);
            }
            if (!GTOConfig.INSTANCE.dev) player.displayClientMessage(Component.translatable("gtocore.dev", Component.literal("GitHub").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/GregTech-Odyssey/GregTech-Odyssey/issues")))), false);
            if (player instanceof IEnhancedPlayer enhancedPlayer) {
                final var data = new CompoundTag();
                data.putUUID(ServerUtils.IDENTIFIER_KEY, ServerUtils.getServerIdentifier());
                ServerMessage.sendData(player.getServer(), player, "loggedIn", data);
                enhancedPlayer.getPlayerData().setDrift(enhancedPlayer.getPlayerData().disableDrift);
            }
        }
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level && !ServerCache.initialized) {
            ServerLevel serverLevel = level.getServer().getLevel(Level.OVERWORLD);
            if (serverLevel == null) return;
            WirelessEnergyContainer.server = serverLevel.getServer();
            ServerCache.initialized = true;
            CellSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(CellSavaedData::new, CellSavaedData::new, "storage_cell_data");
            DysonSphereSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(DysonSphereSavaedData::new, DysonSphereSavaedData::new, "dyson_sphere_data");
            WirelessEnergySavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(ExtendWirelessEnergySavaedData::new, ExtendWirelessEnergySavaedData::new, "wireless_energy_data");
            CommonSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(CommonSavaedData::new, CommonSavaedData::new, "common_data");
            RecipeRunLimitSavaedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(RecipeRunLimitSavaedData::new, RecipeRunLimitSavaedData::new, " recipe_run_limit_data");
            WirelessManaSavaedData.INSTANCE = level.getDataStorage().computeIfAbsent(WirelessManaSavaedData::new, WirelessManaSavaedData::new, "wireless_mana_data");
            if (GTOConfig.INSTANCE.selfRestraint && !ServerUtils.getPersistentData().getBoolean("srm")) {
                ServerUtils.getPersistentData().putBoolean("srm", true);
                CommonSavaedData.INSTANCE.setDirty();
            }
            int difficulty = ServerUtils.getPersistentData().getInt("difficulty");
            if (difficulty == 0) {
                ServerUtils.getPersistentData().putInt("difficulty", GTOCore.difficulty);
                CommonSavaedData.INSTANCE.setDirty();
            } else if (difficulty != GTOCore.difficulty) {
                String error = "Current difficulty: " + GTOCore.difficulty + " | World difficulty: " + difficulty;
                GTOCore.difficulty = difficulty;
                GTOCore.LOGGER.error(error);
            }
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
        AsyncRecipeOutputTask.releaseExecutorService();
        ServerCache.initialized = false;
        ServerCache.observe = false;
    }

    @RegisterLanguage(valuePrefix = "gtocore.lang", en = "Channel mode command banned in expert", cn = "在专家模式下，频道模式命令被禁止")
    public static final String CHANNEL_MODE_COMMAND_BANNED = "banned";

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

    private record FluidHandlerItem(FluidStack stack, int maxAmount, CompoundTag tag, int amount, ItemStack itemStack) implements IFluidHandlerItem {

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return stack;
        }

        @Override
        public int getTankCapacity(int tank) {
            return maxAmount;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            FluidStack fluidStack = resource.copy();
            fluidStack.setAmount(Math.min(resource.getAmount(), maxAmount - amount));
            if (stack.isEmpty()) {
                if (action.execute()) {
                    tag.put("stored", fluidStack.writeToNBT(new CompoundTag()));
                    tag.putLong("storedAmount", fluidStack.getAmount());
                }
                return fluidStack.getAmount();
            } else if (fluidStack.getFluid() == stack.getFluid()) {
                if (action.execute()) {
                    tag.putLong("storedAmount", fluidStack.getAmount() + amount);
                }
                return fluidStack.getAmount();
            }
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.getFluid() == stack.getFluid()) {
                int a = Math.min(amount, resource.getAmount());
                if (action.execute()) {
                    tag.getCompound("stored").putInt("Amount", amount - a);
                    tag.putLong("storedAmount", amount - a);
                }
                return new FluidStack(resource.getFluid(), a);
            }
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (!stack.isEmpty()) {
                maxDrain = Math.min(amount, maxDrain);
                if (action.execute()) {
                    tag.getCompound("stored").putInt("Amount", amount - maxDrain);
                    tag.putLong("storedAmount", amount - maxDrain);
                }
                return new FluidStack(stack.getFluid(), maxDrain);
            }
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack getContainer() {
            return itemStack;
        }
    }
}
