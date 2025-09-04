package com.gtocore.common.machine.generator;

import com.gtocore.common.item.KineticRotorItem;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.part.ItemHatchPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.api.planets.PlanetApi;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes.KEY_AMPERAGE_OUT;

@Scanned
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class WindMillTurbineMachine extends TieredEnergyMachine implements IMachineLife, IFancyUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(WindMillTurbineMachine.class, TieredEnergyMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    @DescSynced
    private float spinSpeed;
    @DynamicInitialValue(key = "wind_mill_turbine.amperage_out", typeKey = KEY_AMPERAGE_OUT, simpleValue = "2", normalValue = "1", expertValue = "1", cn = "输出电流", cnComment = "风力涡轮机的最大输出电流。", en = "Output Amperage", enComment = "The maximum output amperage of the wind turbine.")
    private static int amperage_out = 2;
    private float bladeAngle;
    @DescSynced
    private int material;
    @DescSynced
    private boolean hasRotor;
    @DescSynced
    private boolean obstructed;
    @DescSynced
    private float wind;
    @DescSynced
    private int actualPower;
    @Persisted
    private final NotifiableItemStackHandler inventory;
    private TickableSubscription energySubs;

    public WindMillTurbineMachine(MetaMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
        inventory = createMachineStorage();
    }

    private NotifiableItemStackHandler createMachineStorage() {
        var storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
        storage.setFilter(i -> i.getItem() instanceof KineticRotorItem);
        return storage;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            energySubs = subscribeServerTick(energySubs, this::checkEnergy);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        unsubscribe();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        super.clientTick();
        bladeAngle += spinSpeed;
    }

    private void unsubscribe() {
        if (energySubs != null) {
            energySubs.unsubscribe();
            energySubs = null;
        }
    }

    private static int getMaxWind(int tier) {
        return 10 + 10 * tier;
    }

    private void checkEnergy() {
        if (getOffsetTimer() % 20 == 0) {
            Level level = getLevel();
            if (level == null) return;
            actualPower = 0;
            ItemStack stack = inventory.storage.getStackInSlot(0);
            if (!GTODimensions.isOverworld(level.dimension().location())) {
                Planet planet = PlanetApi.API.getPlanet(level);
                if (planet == null || !planet.oxygen()) {
                    unsubscribe();
                    return;
                }
            }
            BlockPos pos = getPos();
            float multiplier = level.isThundering() ? 2 : level.isRaining() ? 1.5F : 1;
            wind = (float) (multiplier * (Math.sqrt(pos.getY() + (4 * multiplier * GTValues.RNG.nextFloat()))));
            int damage = stack.getDamageValue();
            int maxDamage = stack.getMaxDamage();
            if (damage < maxDamage && stack.getItem() instanceof KineticRotorItem rotorItem) {
                hasRotor = true;
                material = rotorItem.getMaterial();
                obstructed = false;
                Direction facing = getFrontFacing();
                Direction back = facing.getOpposite();
                boolean permuteXZ = back.getAxis() == Direction.Axis.Z;
                BlockPos centerPos = pos.relative(back);
                loop1:
                for (int x = -2; x < 3; x++) {
                    for (int y = -2; y < 3; y++) {
                        if (x == 0 && y == 0) continue;
                        if (getMachine(level, pos.offset(permuteXZ ? x : 0, y, permuteXZ ? 0 : x)) instanceof WindMillTurbineMachine machine && machine.hasRotor && machine.getFrontFacing() == facing) {
                            obstructed = true;
                            break loop1;
                        }
                    }
                }
                loop2:
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        BlockPos blockPos = centerPos.offset(permuteXZ ? x : 0, y, permuteXZ ? 0 : x);
                        List<Entity> entityList = level.getEntitiesOfClass(Entity.class, AABB.ofSize(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 1, 1, 1));
                        for (Entity e : entityList) {
                            if (e instanceof LivingEntity) e.hurt(e.damageSources().genericKill(), 20 * spinSpeed);
                            obstructed = true;
                        }
                        if (!level.getBlockState(blockPos).isAir()) {
                            obstructed = true;
                            break loop2;
                        }
                    }
                }
                if (obstructed) {
                    stack.setDamageValue(damage + (int) (40 * spinSpeed));
                    spinSpeed = 0;
                } else if (wind > rotorItem.getMinWind()) {
                    stack.setDamageValue(damage + (int) Math.pow(Math.ceil(wind / rotorItem.getMaxWind()), 16));
                    spinSpeed = Math.min(0.05F * wind, spinSpeed + 0.04F);
                    actualPower = (int) (GTValues.V[tier] * spinSpeed * 20 * getMaxInputOutputAmperage() / getMaxWind(tier));
                    energyContainer.addEnergy(20L * actualPower);
                }
            } else {
                if (hasRotor) {
                    this.requestSync();
                    inventory.storage.setStackInSlot(0, ItemStack.EMPTY);
                }
                spinSpeed = 0;
                hasRotor = false;
            }
        }
    }

    @Override
    public Widget createUIWidget() {
        return ItemHatchPartMachine.createSLOTWidget(inventory);
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        tooltipsPanel.attachTooltips(new Basic(() -> GuiTextures.INDICATOR_NO_STEAM.get(false), () -> List.of(Component.translatable("gtceu.multiblock.large_combustion_engine.obstructed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))), this::isObstructed, () -> null));
        tooltipsPanel.attachTooltips(new IFancyTooltip.Basic(() -> GuiTextures.INFO_ICON, () -> List.of(Component.translatable("gtocore.machine.wind_mill_turbine.wind", FormattingUtil.formatNumbers(wind)), Component.translatable("gtocore.machine.wind_mill_turbine.actualPower", actualPower)), () -> true, () -> null));
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(inventory.storage);
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = GTValues.V[getTier()];
        NotifiableEnergyContainer energyContainer = NotifiableEnergyContainer.emitterContainer(this, tierVoltage << 6, tierVoltage, getMaxInputOutputAmperage());
        energyContainer.setSideOutputCondition(side -> !hasFrontFacing() || side == getFrontFacing());
        return energyContainer;
    }

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }

    @Override
    protected long getMaxInputOutputAmperage() {
        return amperage_out;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static int getAmperage_out() {
        return WindMillTurbineMachine.amperage_out;
    }

    private boolean isObstructed() {
        return this.obstructed;
    }

    public float getSpinSpeed() {
        return this.spinSpeed;
    }

    public float getBladeAngle() {
        return this.bladeAngle;
    }

    public int getMaterial() {
        return this.material;
    }

    public boolean isHasRotor() {
        return this.hasRotor;
    }
}
