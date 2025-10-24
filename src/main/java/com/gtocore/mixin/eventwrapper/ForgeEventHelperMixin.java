package com.gtocore.mixin.eventwrapper;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

import io.github.lounode.eventwrapper.eventbus.api.EventConverter;
import io.github.lounode.eventwrapper.eventbus.api.EventWrapper;
import io.github.lounode.eventwrapper.eventbus.api.SubscribeEventWrapper;
import io.github.lounode.eventwrapper.forge.ForgeEventHelper;
import io.github.lounode.eventwrapper.forge.event.ForgeEventMappings;
import org.spongepowered.asm.mixin.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// todo 更新后删除
@Mixin(ForgeEventHelper.class)
public class ForgeEventHelperMixin {

    @Shadow(remap = false)
    @Final
    private static Map<Event, EventWrapper> FORGE_EVENT_TRACKER_MAP;
    @Unique
    private static final Map<Class<?>, Field[]> NON_FINAL_FIELD_CACHE = new ConcurrentHashMap<>();

    @Unique
    private static void gtolib$syncEventData(Object from, Object to) {
        if (from == null || to == null) {
            return;
        }

        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();

        Field[] fromFields = gtolib$getFieldsWithoutFinal(fromClass);
        Field[] toFields = gtolib$getFieldsWithoutFinal(toClass);

        for (Field fromField : fromFields) {
            String name = fromField.getName();
            Class<?> type = fromField.getType();

            try {
                for (Field toField : toFields) {
                    if (toField.getType() == type && toField.getName().equals(name)) {
                        Object value = fromField.get(from);
                        toField.set(to, value);
                        break;
                    }
                }
            } catch (IllegalAccessException ignored) {}
        }
    }

    @Unique
    private static Field[] gtolib$getFieldsWithoutFinal(Class<?> clazz) {
        return NON_FINAL_FIELD_CACHE.computeIfAbsent(clazz, c -> {
            List<Field> fieldList = new ArrayList<>(16);
            while (c != null) {
                for (Field field : c.getDeclaredFields()) {
                    if (!Modifier.isFinal(field.getModifiers())) {
                        field.setAccessible(true);
                        fieldList.add(field);
                    }
                }
                c = c.getSuperclass();
            }
            return fieldList.toArray(new Field[0]);
        });
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private void registerListener(Object target, Method method) {
        Class<?>[] params = method.getParameterTypes();
        Class<? extends EventWrapper> wrapperClass = (Class<? extends EventWrapper>) params[0];
        EventConverter converter = ForgeEventMappings.getConverter(wrapperClass);
        if (converter == null) return;
        Class<? extends Event> forgeEventClass = ForgeEventMappings.getForgeEventClass(wrapperClass);
        if (forgeEventClass == null) return;
        SubscribeEventWrapper annotation = method.getAnnotation(SubscribeEventWrapper.class);
        EventPriority priority = annotation != null ? EventPriority.valueOf(annotation.priority().name()) : EventPriority.NORMAL;
        boolean receiveCanceled = annotation != null && annotation.receiveCanceled();
        method.setAccessible(true);
        MinecraftForge.EVENT_BUS.addListener(priority, receiveCanceled, forgeEventClass, event -> {
            try {
                EventWrapper wrapper;
                if ((wrapper = FORGE_EVENT_TRACKER_MAP.get(event)) == null) {
                    wrapper = converter.toWrapper(event);
                } else {
                    gtolib$syncEventData(event, wrapper);
                }
                method.invoke(target, wrapper);
                gtolib$syncEventData(wrapper, event);
            } catch (Exception e) {
                throw new RuntimeException("Event call Error!", e);
            }
        });
    }
}
