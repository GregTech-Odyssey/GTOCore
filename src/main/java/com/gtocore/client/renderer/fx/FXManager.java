package com.gtocore.client.renderer.fx;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@OnlyIn(Dist.CLIENT)
public class FXManager {

    public static final List<AbstractFX> FX_LIST = new CopyOnWriteArrayList<>();

    public static void dispatchFXs(RenderLevelStageEvent event) {
        BlackHole.beginFrame();
        for (AbstractFX fx : FX_LIST) {
            fx.render(event.getStage(), event.getLevelRenderer(), event.getPoseStack(), event.getProjectionMatrix(), event.getPartialTick(), event.getCamera(), event.getFrustum());
        }
    }

    public static void tickFXs() {
        List<AbstractFX> discarded = new ArrayList<>();
        FX_LIST.removeIf(fx -> {
            fx.tick();
            if (fx.shouldDiscard()) {
                discarded.add(fx);
                return true;
            }
            return false;
        });
        discarded.forEach(AbstractFX::onDiscard);
    }

    public static void addFX(AbstractFX fx) {
        FX_LIST.add(fx);
    }

    public static void clearFXs() {
        List<AbstractFX> discarded = new ArrayList<>(FX_LIST);
        FX_LIST.clear();
        discarded.forEach(AbstractFX::onDiscard);
    }
}
