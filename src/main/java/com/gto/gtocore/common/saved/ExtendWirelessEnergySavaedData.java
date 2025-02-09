package com.gto.gtocore.common.saved;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;
import com.gto.gtocore.utils.GTOUtils;

import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.data.WirelessEnergySavaedData;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

public final class ExtendWirelessEnergySavaedData extends WirelessEnergySavaedData {

    public ExtendWirelessEnergySavaedData() {}

    public ExtendWirelessEnergySavaedData(CompoundTag tag) {
        super(tag);
    }

    @Override
    protected WirelessEnergyContainer readTag(CompoundTag engTag) {
        UUID uuid = engTag.getUUID(GTOValues.WIRELESS_ENERGY_UUID);
        String en = engTag.getString(GTOValues.WIRELESS_ENERGY_STORAGE);
        String ca = engTag.getString(GTOValues.WIRELESS_ENERGY_CAPACITY);
        BigInteger energy = new BigInteger(en.isEmpty() ? "0" : en);
        BigInteger capacity = new BigInteger(ca.isEmpty() ? "0" : ca);
        long rate = engTag.getLong(GTOValues.WIRELESS_ENERGY_RATE);
        int loss = engTag.getInt(GTOValues.WIRELESS_ENERGY_LOSS);
        GlobalPos bindPos = GTOUtils.readGlobalPos(engTag.getString(GTOValues.WIRELESS_ENERGY_DIMENSION), engTag.getLong(GTOValues.WIRELESS_ENERGY_POS));
        return new ExtendWirelessEnergyContainer(uuid, energy, rate, bindPos, capacity, loss);
    }

    @Override
    protected CompoundTag toTag(WirelessEnergyContainer c) {
        CompoundTag engTag = new CompoundTag();
        if (c instanceof ExtendWirelessEnergyContainer container) {
            BigInteger storage = container.getStorage();
            if (!Objects.equals(storage, BigInteger.ZERO)) {
                engTag.putString(GTOValues.WIRELESS_ENERGY_STORAGE, storage.toString());
            }
            BigInteger capacity = container.getCapacity();
            if (!Objects.equals(capacity, BigInteger.ZERO)) {
                engTag.putString(GTOValues.WIRELESS_ENERGY_CAPACITY, capacity.toString());
            }
            long rate = container.getRate();
            if (rate != 0) {
                engTag.putLong(GTOValues.WIRELESS_ENERGY_RATE, rate);
            }
            int loss = container.getLoss();
            if (loss != 0) {
                engTag.putInt(GTOValues.WIRELESS_ENERGY_LOSS, loss);
            }
            GlobalPos bindPos = container.getBindPos();
            if (bindPos != null) {
                engTag.putString(GTOValues.WIRELESS_ENERGY_DIMENSION, bindPos.dimension().location().toString());
                engTag.putLong(GTOValues.WIRELESS_ENERGY_POS, bindPos.pos().asLong());
            }
            if (!engTag.isEmpty()) {
                engTag.putUUID(GTOValues.WIRELESS_ENERGY_UUID, container.getUuid());
            }
        }
        return engTag;
    }
}
