package com.gtocore.utils;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.fml.ModList;

import appeng.api.client.AEKeyRendering;
import appeng.api.stacks.AmountFormat;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.common.StackSizeRenderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import org.joml.Matrix4f;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

/**
 * Sends desktop notifications through the OS-native {@code gtonotify} JNI library.
 * <p>
 * The notifier is stateful: {@link #init} registers a persistent tray icon (Windows) carrying an
 * icon decoded from a PNG resource on the classpath and a hover tooltip name (e.g.
 * {@code "GTO Client"}); {@link #notify} then reuses it, optionally overriding the icon and type per
 * call. On macOS there is no tray icon and no notification type — those are ignored; the bare
 * notification's supported customizations are title, subtitle, body and a per-call content image.
 * On any other platform every call is a no-op.
 * <p>
 * Icons are read straight from the mod jar: the PNG resource is decoded to ARGB pixels with
 * {@link ImageIO} and handed to the native side, which builds an {@code HICON} (Windows) or
 * {@code NSImage} (macOS). No third-party dependency is used. The native library itself ships under
 * {@code /natives/} and is extracted to a temp file because {@code System.loadLibrary} cannot see
 * into a jar.
 */
public final class NotificationUtils {

    private NotificationUtils() {}

    /**
     * Balloon icon shown by the OS; maps to the Windows {@code NIIF_*} flags. Ignored on macOS.
     */
    public enum Type {

        NONE(0),
        INFO(1),
        WARNING(2),
        ERROR(3);

        private final int flag;

        Type(int flag) {
            this.flag = flag;
        }
    }

    private static final String DEFAULT_APP_NAME = "GTO Client";
    /**
     * Stable AppUserModelID; required for custom-icon toast delivery, and its registered
     * DisplayName is the Windows toast app name.
     */
    private static final String DEFAULT_APP_ID = "GregTechOdyssey.GTOClient";
    /**
     * Classpath PNG used as the default tray icon.
     */
    public static final String DEFAULT_ICON = "assets/gtocore/textures/item/tools/iv_vajra.png";
    private static final int MIN_ICON_SIZE = 64;
    private static final int RENDER_ICON_SIZE = 64;

    private static final boolean LOADED = loadNative();
    private static long handle;
    private static boolean shutdownHookInstalled;

    private static native long nativeInit(int[] argb, int width, int height, String appName, String appId);

    private static native boolean nativeNotify(long handle, String title, String subtitle, String text, int type, int[] argb, int width, int height);

    private static native void nativeDispose(long handle);

    /**
     * Decoded icon pixels in packed {@code 0xAARRGGBB} form.
     */
    private record Icon(int[] argb, int width, int height) {}

    /**
     * Registers the persistent tray icon. Safe to call again to replace it; the previous
     * registration is disposed first. A no-op returning {@code false} when the native library is
     * unavailable.
     *
     * @param iconResource classpath path to a PNG (e.g. {@link #DEFAULT_ICON}), or {@code null} for
     *                     the default; ignored on macOS, which has no tray icon
     * @param appName      tray hover tooltip / display name, e.g. {@code "GTO Client"}
     */
    public static synchronized boolean init(String iconResource, String appName) {
        if (!LOADED) return false;
        if (handle != 0) {
            nativeDispose(handle);
            handle = 0;
        }
        Icon icon = loadIcon(iconResource == null ? DEFAULT_ICON : iconResource);
        String name = appName == null ? DEFAULT_APP_NAME : appName;
        try {
            handle = icon == null ? nativeInit(null, 0, 0, name, DEFAULT_APP_ID) : nativeInit(icon.argb(), icon.width(), icon.height(), name, DEFAULT_APP_ID);
        } catch (Throwable t) {
            handle = 0;
        }
        if (handle != 0) installShutdownHook();
        return handle != 0;
    }

    /**
     * Full {@link #notify} with {@link Type#INFO}, no subtitle and default icon.
     */
    public static boolean notify(String title, String text) {
        return notify(title, null, text, Type.INFO, DEFAULT_ICON);
    }

    /**
     * Full {@link #notify} with no subtitle and default icon.
     */
    public static boolean notify(String title, String text, Type type) {
        return notify(title, null, text, type, DEFAULT_ICON);
    }

    /**
     * Shows a notification, lazily initialising the tray icon with defaults if {@link #init} was not
     * called.
     *
     * @param subtitle     macOS subtitle line shown under the title; ignored on Windows
     * @param type         balloon icon type (Windows); ignored on macOS
     * @param iconResource classpath path to a per-call PNG icon, or {@code null}; on Windows it
     *                     overrides {@code type} as a large balloon icon, on macOS it becomes the
     *                     content image
     * @return {@code true} if the native call completed, {@code false} on an unsupported platform or
     *         any failure
     */
    public static synchronized boolean notify(String title, String subtitle, String text, Type type, String iconResource) {
        if (!LOADED) return false;
        if (handle == 0 && !init(null, DEFAULT_APP_NAME)) return false;
        Icon icon = loadIcon(iconResource);
        return notifyWithIcon(title, subtitle, text, type, icon);
    }

    public static boolean notify(String title, String subtitle, String text, Type type, GenericStack renderStack) {
        if (!LOADED) return false;
        if (renderStack == null || renderStack.what() == null) {
            return notify(title, subtitle, text, type, DEFAULT_ICON);
        }

        Minecraft minecraft = Minecraft.getInstance();

        CompletableFuture<Boolean> result = new CompletableFuture<>();
        Runnable task = () -> result.complete(notifyRenderedStackInternal(title, subtitle, text, type, renderStack));
        if (minecraft.isSameThread()) {
            task.run();
        } else {
            minecraft.tell(task);
        }
        try {
            return result.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            GTOCore.LOGGER.debug("Timed out waiting for rendered notification icon", e);
            return notify(title, subtitle, text, type, DEFAULT_ICON);
        }
    }

    private static synchronized boolean notifyWithIcon(String title, String subtitle, String text, Type type, Icon icon) {
        if (!LOADED) return false;
        if (handle == 0 && !init(null, DEFAULT_APP_NAME)) return false;
        int flag = type == null ? Type.INFO.flag : type.flag;
        try {
            return icon == null ? nativeNotify(handle, safe(title), safe(subtitle), safe(text), flag, null, 0, 0) : nativeNotify(handle, safe(title), safe(subtitle), safe(text), flag, icon.argb(), icon.width(), icon.height());
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Removes the tray icon and releases native resources. Idempotent.
     */
    public static synchronized void shutdown() {
        if (handle != 0) {
            try {
                nativeDispose(handle);
            } catch (Throwable ignored) {
                // best-effort cleanup
            }
            handle = 0;
        }
    }

    private static Icon loadIcon(String resource) {
        if (resource == null || resource.isEmpty()) return null;
        String path = resource.startsWith("/") ? resource : "/" + resource;
        try (InputStream in = NotificationUtils.class.getResourceAsStream(path)) {
            if (in == null) return loadModResourceIcon(resource);
            return readIcon(in);
        } catch (Throwable t) {
            return null;
        }
    }

    private static Icon loadModResourceIcon(String resource) {
        String normalized = resource.startsWith("/") ? resource.substring(1) : resource;
        String[] parts = normalized.split("/");
        if (parts.length < 3 || !"assets".equals(parts[0])) return null;
        try {
            var modList = ModList.get();
            if (modList == null) return null;
            var modFile = modList.getModFileById(parts[1]);
            if (modFile == null) return null;
            Path modResource = modFile.getFile().findResource(parts);
            if (!Files.exists(modResource)) return null;
            try (InputStream in = Files.newInputStream(modResource)) {
                return readIcon(in);
            }
        } catch (Throwable t) {
            return null;
        }
    }

    private static Icon readIcon(InputStream in) throws Exception {
        BufferedImage img = ImageIO.read(in);
        if (img == null) return null;
        int w = img.getWidth();
        int h = img.getHeight();
        // getRGB always yields packed 0xAARRGGBB regardless of the source image type.
        int[] argb = img.getRGB(0, 0, w, h, null, 0, w);
        return upscaleSmallIcon(new Icon(argb, w, h));
    }

    private static Icon upscaleSmallIcon(Icon icon) {
        int max = Math.max(icon.width(), icon.height());
        if (max >= MIN_ICON_SIZE) return icon;
        int scale = (MIN_ICON_SIZE + max - 1) / max;
        int w = icon.width();
        int h = icon.height();
        int scaledW = w * scale;
        int scaledH = h * scale;
        int[] scaled = new int[scaledW * scaledH];
        for (int y = 0; y < scaledH; y++) {
            int srcY = y / scale;
            for (int x = 0; x < scaledW; x++) {
                scaled[y * scaledW + x] = icon.argb()[srcY * w + x / scale];
            }
        }
        return new Icon(scaled, scaledW, scaledH);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static void installShutdownHook() {
        if (shutdownHookInstalled) return;
        shutdownHookInstalled = true;
        Runtime.getRuntime().addShutdownHook(new Thread(NotificationUtils::shutdown, "gtonotify-cleanup"));
    }

    private static boolean loadNative() {
        String os = System.getProperty("os.name", "").toLowerCase();
        String resource;
        String suffix;
        if (os.contains("win")) {
            resource = "/natives/gtonotify.dll";
            suffix = ".dll";
        } else if (os.contains("mac") || os.contains("darwin")) {
            resource = "/natives/libgtonotify.dylib";
            suffix = ".dylib";
        } else {
            return false;
        }
        try (InputStream in = NotificationUtils.class.getResourceAsStream(resource)) {
            if (in == null) return false;
            Path tmp = Files.createTempFile("gtonotify", suffix);
            tmp.toFile().deleteOnExit();
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
            System.load(tmp.toAbsolutePath().toString());
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean notifyRenderedStackInternal(String title, String subtitle, String text, Type type, GenericStack renderStack) {
        Icon renderedIcon = renderIcon(renderStack);
        if (renderedIcon != null) {
            return notifyWithIcon(title, subtitle, text, type, renderedIcon);
        }
        return notify(title, subtitle, text, type, DEFAULT_ICON);
    }

    private static Icon renderIcon(GenericStack renderStack) {
        Minecraft minecraft = Minecraft.getInstance();
        if (renderStack == null || renderStack.what() == null) return null;

        RenderTarget fbo = new TextureTarget(RENDER_ICON_SIZE, RENDER_ICON_SIZE, true, Minecraft.ON_OSX);
        try {
            BufferedImage image = captureAeStackIcon(minecraft, renderStack, fbo);
            if (image == null) return null;
            int[] argb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            return upscaleSmallIcon(new Icon(argb, image.getWidth(), image.getHeight()));
        } catch (Throwable t) {
            GTOCore.LOGGER.debug("Failed to render notification icon for stack {}", renderStack, t);
            return null;
        } finally {
            fbo.destroyBuffers();
            minecraft.getMainRenderTarget().bindWrite(true);
        }
    }

    private static BufferedImage captureAeStackIcon(Minecraft minecraft, GenericStack renderStack, RenderTarget fbo) {
        Matrix4f previousProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        boolean pushedModelView = false;
        RenderTarget previousTarget = minecraft.getMainRenderTarget();
        try {
            fbo.setClearColor(0f, 0f, 0f, 0f);
            fbo.clear(Minecraft.ON_OSX);
            fbo.bindWrite(true);

            Matrix4f projection = new Matrix4f().setOrtho(0, RENDER_ICON_SIZE, RENDER_ICON_SIZE, 0, 1000.0f, 21000.0f);
            RenderSystem.setProjectionMatrix(projection, VertexSorting.DISTANCE_TO_ORIGIN);

            modelViewStack.pushPose();
            pushedModelView = true;
            modelViewStack.setIdentity();
            modelViewStack.translate(0, 0, -11000.0f);
            RenderSystem.applyModelViewMatrix();

            Lighting.setupFor3DItems();

            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            GuiGraphics guiGraphics = new GuiGraphics(minecraft, bufferSource);
            float scale = RENDER_ICON_SIZE / 16.0f;
            guiGraphics.pose().scale(scale, scale, scale);
            AEKeyRendering.drawInGui(minecraft, guiGraphics, 0, 0, renderStack.what());
            if (renderStack.amount() > 0) {
                String amountText = renderStack.what().formatAmount(renderStack.amount(), AmountFormat.SLOT);
                StackSizeRenderer.renderSizeLabel(guiGraphics, minecraft.font, 0, 0, amountText, false);
            }
            guiGraphics.flush();

            return readFBOPixels(fbo);
        } catch (Throwable t) {
            GTOCore.LOGGER.debug("Failed to capture AE notification stack {}", renderStack, t);
            return null;
        } finally {
            if (pushedModelView) {
                modelViewStack.popPose();
                RenderSystem.applyModelViewMatrix();
            }
            RenderSystem.setProjectionMatrix(previousProjection, VertexSorting.DISTANCE_TO_ORIGIN);
            previousTarget.bindWrite(true);
        }
    }

    private static BufferedImage readFBOPixels(RenderTarget fbo) {
        try (NativeImage nativeImage = new NativeImage(fbo.width, fbo.height, false)) {
            RenderSystem.bindTexture(fbo.getColorTextureId());
            nativeImage.downloadTexture(0, false);
            nativeImage.flipY();

            BufferedImage image = new BufferedImage(fbo.width, fbo.height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < fbo.height; y++) {
                for (int x = 0; x < fbo.width; x++) {
                    int pixel = nativeImage.getPixelRGBA(x, y);
                    int a = (pixel >> 24) & 0xFF;
                    int b = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int r = pixel & 0xFF;
                    image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }
            return image;
        } catch (Throwable t) {
            GTOCore.LOGGER.debug("Failed to read notification icon FBO", t);
            return null;
        }
    }
}
