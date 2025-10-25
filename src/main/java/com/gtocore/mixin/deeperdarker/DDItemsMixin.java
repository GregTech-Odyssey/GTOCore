package com.gtocore.mixin.deeperdarker;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;

import com.kyanite.deeperdarker.content.DDItems;
import com.kyanite.deeperdarker.util.DDTiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(DDItems.class)
public class DDItemsMixin {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;", ordinal = 36), remap = false, index = 1)
    private static Supplier<? extends Item> wardenSword(Supplier<? extends Item> sup) {
        return () -> new SwordItem(DDTiers.WARDEN, 300, -2.4F, (new Item.Properties()).rarity(Rarity.RARE).fireResistant());
    }
}
