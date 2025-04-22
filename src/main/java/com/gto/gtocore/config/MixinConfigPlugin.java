package com.gto.gtocore.config;

import com.gregtechceu.gtceu.GTCEu;

import com.lowdragmc.lowdraglib.core.mixins.MixinPluginShared;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.embeddedt.embeddium_integrity.MixinTaintDetector;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class MixinConfigPlugin implements IMixinConfigPlugin {

    private static MethodHandle MIXINS_ON_CLASS_INFO;

    private static List<IExtension> EXTENSIONS;

    @Override
    public void onLoad(String mixinPackage) {
        if (GTCEu.isClientSide() && MixinEnvironment.getDefaultEnvironment().getActiveTransformer() instanceof IMixinTransformer transformer && transformer.getExtensions() instanceof Extensions internalExtensions) {
            var instance = new MyIExtension();
            try {
                Field extensionsField = internalExtensions.getClass().getDeclaredField("extensions");
                extensionsField.setAccessible(true);
                EXTENSIONS = ((List<IExtension>) extensionsField.get(internalExtensions));
                EXTENSIONS.add(instance);
                Field activeExtensionsField = internalExtensions.getClass().getDeclaredField("activeExtensions");
                activeExtensionsField.setAccessible(true);
                List<IExtension> newActiveExtensions = new ArrayList<>((List<IExtension>) activeExtensionsField.get(internalExtensions));
                newActiveExtensions.removeIf(extension -> extension instanceof MixinTaintDetector);
                newActiveExtensions.add(instance);
                activeExtensionsField.set(internalExtensions, Collections.unmodifiableList(newActiveExtensions));
            } catch (ReflectiveOperationException | RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        Configurator.setRootLevel(Level.ERROR);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("com.gto.gtocore.mixin.emi")) {
            return MixinPluginShared.IS_EMI_LOADED;
        } else if (mixinClassName.contains("com.gto.gtocore.mixin.jei")) {
            return MixinPluginShared.IS_JEI_LOAD;
        } else if (mixinClassName.equals("com.gto.gtocore.mixin.mc.OreConfigurationMixin")) {
            return GTCEu.isProd();
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private static class MyIExtension implements IExtension {

        @Override
        public boolean checkActive(MixinEnvironment environment) {
            return true;
        }

        @Override
        public void preApply(ITargetClassContext context) {
            if (MIXINS_ON_CLASS_INFO == null) {
                if (MixinEnvironment.getDefaultEnvironment().getActiveTransformer() instanceof IMixinTransformer transformer && transformer.getExtensions() instanceof Extensions internalExtensions) {
                    EXTENSIONS.removeIf(extension -> extension instanceof MixinTaintDetector);
                    try {
                        Field activeExtensionsField = internalExtensions.getClass().getDeclaredField("activeExtensions");
                        activeExtensionsField.setAccessible(true);
                        List<IExtension> newActiveExtensions = new ArrayList<>((List<IExtension>) activeExtensionsField.get(internalExtensions));
                        newActiveExtensions.removeIf(extension -> extension instanceof MixinTaintDetector);
                        activeExtensionsField.set(internalExtensions, Collections.unmodifiableList(newActiveExtensions));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Field field = MixinTaintDetector.class.getDeclaredField("GET_MIXINS_ON_CLASS_INFO");
                    field.setAccessible(true);
                    MIXINS_ON_CLASS_INFO = (MethodHandle) field.get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            var classInfo = context.getClassInfo();
            var name = classInfo.getClassName();
            if (name.startsWith("com.gregtechceu") && !name.equals("com.gregtechceu.gtceu.api.machine.SimpleTieredMachine")) {
                try {
                    for (IMixinInfo iMixinInfo : (Set<IMixinInfo>) MIXINS_ON_CLASS_INFO.invokeExact(classInfo)) {
                        if (iMixinInfo.getConfig().getMixinPackage().equals("com.gto.gtocore.mixin.")) continue;
                        throw new RuntimeException();
                    }
                } catch (Throwable e) {
                    throw new RuntimeException("Mixin usage in this location is prohibited due to potential severe consequences.");
                }
            } else if (name.startsWith("com.gto.gtocore")) throw new RuntimeException("Do not use mixins on core - submit a pull request instead if modifications are needed.");
        }

        @Override
        public void postApply(ITargetClassContext context) {}

        @Override
        public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {}
    }
}
