package com.gto.gtocore.utils.register;


import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.effect.GTPoisonEffect;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.common.effect.GTOMysteriousBoostEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.IntStream;

import static com.gto.gtocore.GTOCore.MOD_ID;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public class EffectRegisterUtils {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);

    // 直接在这里定义 RegistryObject
    public static final RegistryObject<GTOMysteriousBoostEffect> MYSTERIOUS_BOOST =
            MOB_EFFECTS.register("mysterious_boost",
                    () -> new GTOMysteriousBoostEffect(MobEffectCategory.BENEFICIAL, 0xFF00FF));

    public static void init(IEventBus modBus) {
        MOB_EFFECTS.register(modBus);
    }
}