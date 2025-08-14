package com.gtocore.common.data;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.ITemperatureMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

@DataGeneratorScanned
public final class GTODamageTypes {

    // keys
    private static final ResourceKey<DamageType> GENERIC = ResourceKey.create(Registries.DAMAGE_TYPE, GTOCore.id("generic"));
    private static final ResourceKey<DamageType> MACHINE_HEAT_WAVE = ResourceKey.create(Registries.DAMAGE_TYPE, GTOCore.id("machine_heat_wave"));
    private static final ResourceKey<DamageType> BLAST_FURNACE = ResourceKey.create(Registries.DAMAGE_TYPE, GTOCore.id("blast_furnace"));

    public static final RegistrySetBuilder DAMAGE_TYPES_BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, GTODamageTypes::bootstrap);

    private static void bootstrap(BootstapContext<DamageType> context) {
        context.register(GENERIC, new DamageType("generic", DamageScaling.NEVER, 0.0f, DamageEffects.HURT, DeathMessageType.DEFAULT));
        context.register(MACHINE_HEAT_WAVE, new DamageType("machine_heat_wave", DamageScaling.NEVER, 0.0f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
        context.register(BLAST_FURNACE, new DamageType("blast_furnace", DamageScaling.NEVER, 0.0f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
    }

    public static DamageSource getGenericDamageSource(Entity entity, Component customComponent, Runnable onDeath) {
        return new GenericDamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(GENERIC), entity, customComponent, onDeath);
    }

    public static DamageSource getMachineHeatWaveDamageSource(Entity entity, ITemperatureMachine machine) {
        return new MachineHeatWaveDamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(MACHINE_HEAT_WAVE), entity, machine);
    }

    public static DamageSource getBlastFurnaceDamageSource(Entity entity) {
        return new BlastFurnaceDamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BLAST_FURNACE), entity);
    }

    @RegisterLanguage(cn = "员工 %s 在高炉里顷刻炼化", en = "Player %s melted in a blast furnace.")
    private static final String BLAST_FURNACE_MSG_ID_1 = "gtocore.death.attack.blast_furnace.1";
    @RegisterLanguage(cn = "员工 %s 尝试在高炉里蒸桑拿", en = "Player %s tired to have a sauna in a blast furnace.")
    private static final String BLAST_FURNACE_MSG_ID_2 = "gtocore.death.attack.blast_furnace.2";

    private static final class BlastFurnaceDamageSource extends DamageSource {

        private BlastFurnaceDamageSource(Holder<DamageType> type, Entity entity) {
            super(type, entity);
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity livingEntity) {
            if (livingEntity instanceof Player player) {
                return Component.translatable(GTValues.RNG.nextBoolean() ? BLAST_FURNACE_MSG_ID_1 : BLAST_FURNACE_MSG_ID_2, player.getDisplayName());
            }
            return super.getLocalizedDeathMessage(livingEntity);
        }
    }

    @RegisterLanguage(cn = "悲，员工 %s 死于%s %sK的热浪", en = "Sad, employee %s died from a heat wave of %s %sK")
    private static final String MACHINE_HEAT_WAVE_MSG_ID = "gtocore.death.attack.machine_heat_wave";

    private static final class MachineHeatWaveDamageSource extends DamageSource {

        private final ITemperatureMachine machine;

        private MachineHeatWaveDamageSource(Holder<DamageType> type, Entity entity, ITemperatureMachine machine) {
            super(type, entity);
            this.machine = machine;
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity livingEntity) {
            if (livingEntity instanceof Player player && machine instanceof MetaMachine metaMachine) {
                return Component.translatable(MACHINE_HEAT_WAVE_MSG_ID, player.getDisplayName(), metaMachine.getBlockState().getBlock().getName(), machine.getTemperature());
            }
            return super.getLocalizedDeathMessage(livingEntity);
        }
    }

    private static final class GenericDamageSource extends DamageSource {

        private final Component customComponent;
        private final Runnable onDeath;

        private GenericDamageSource(Holder<DamageType> type, Entity entity, Component customComponent, Runnable onDeath) {
            super(type, entity);
            this.customComponent = customComponent;
            this.onDeath = onDeath;
        }

        @Override
        public net.minecraft.network.chat.@NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity livingEntity) {
            this.onDeath.run();
            return this.customComponent;
        }
    }
}
