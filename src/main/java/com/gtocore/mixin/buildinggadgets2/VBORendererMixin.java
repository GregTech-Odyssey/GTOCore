package com.gtocore.mixin.buildinggadgets2;

import net.minecraft.client.renderer.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Building Gadgets draws the cutout part of a preview with its own render type, which alpha tests instead of
 * blending, and draws everything else blended with depth writes. GregTech machines are registered on the
 * cutoutMipped layer and their overlay quads sit exactly on top of the casing quads, so in the blended path the two
 * fight over the depth buffer: the overlay wins, and since its centre is transparent the machine looks like a hollow
 * pane. Draw cutoutMipped the way cutout is drawn.
 */
@Mixin(targets = "com.direwolf20.buildinggadgets2.client.renderer.VBORenderer", remap = false)
public class VBORendererMixin {

    @Redirect(method = "drawRender",
              at = @At(value = "INVOKE", target = "Ljava/lang/Object;equals(Ljava/lang/Object;)Z"))
    private static boolean gtocore$drawCutoutMippedLikeCutout(Object self, Object other) {
        if (self instanceof RenderType renderType && RenderType.cutout().equals(other)) {
            return renderType.equals(RenderType.cutout()) || renderType.equals(RenderType.cutoutMipped());
        }
        return self.equals(other);
    }
}
