package com.gto.gtocore.world;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.config.GTOConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = GTOCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FoodConsumeEventHandler {
    @SubscribeEvent
    public static void onFoodConsume(LivingEntityUseItemEvent event) {
        if(GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeat){
            if(event.getEntity() instanceof Player player && List.of(20).contains(event.getDuration()) && !player.level().isClientSide()){
                hurtAnimalsNearPlayer(player, Items.BEEF, Cow.class,event);
                hurtAnimalsNearPlayer(player, Items.COOKED_BEEF, Cow.class,event);

                hurtAnimalsNearPlayer(player, Items.CHICKEN, Chicken.class,event);
                hurtAnimalsNearPlayer(player, Items.COOKED_CHICKEN, Chicken.class,event);

                hurtAnimalsNearPlayer(player, Items.PORKCHOP, Pig.class,event);
                hurtAnimalsNearPlayer(player, Items.COOKED_PORKCHOP, Pig.class,event);

                hurtAnimalsNearPlayer(player, Items.MUTTON, Sheep.class,event);
                hurtAnimalsNearPlayer(player, Items.COOKED_MUTTON, Sheep.class,event);
            }
        }
    }
    private static <T extends Animal> void hurtAnimalsNearPlayer(Player player, Item foodItem, Class<T> animalClass,LivingEntityUseItemEvent event) {
        if (event.getItem().is(foodItem)) {
            Level level = player.level();
            List<T> animalEntities = level.getEntitiesOfClass(animalClass, player.getBoundingBox().inflate(GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeatRange));
            for (T animal : animalEntities) {
                animal.hurt(player.damageSources().playerAttack(player), Math.max(animal.getMaxHealth()/20,0.5F));
                animal.level().addParticle(ParticleTypes.ANGRY_VILLAGER, animal.getX(), animal.getY()+0.5F, animal.getZ(), 0.1, 0.1, 0.1);
            }
        }
    }
}
