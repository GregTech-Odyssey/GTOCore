package com.gto.gtocore.mixin.gtm.chemical;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.IMaterialProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.MaterialProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MaterialProperties.class)
public interface MaterialPropertiesAccessor {

    @Accessor(value = "propertyMap", remap = false)
    Map<PropertyKey<? extends IMaterialProperty>, IMaterialProperty> getPropertyMap();
}
