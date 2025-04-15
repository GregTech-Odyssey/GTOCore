package com.gto.gtocore.common.wireless;

import com.gto.gtocore.common.saved.WirelessManaSavaedData;

import com.hepdd.gtmthings.utils.TeamUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.UUID;

@Getter
public final class WirelessManaContainer {

    public static WirelessManaContainer getOrCreateContainer(UUID uuid) {
        return WirelessManaSavaedData.INSTANCE.containerMap.computeIfAbsent(TeamUtil.getTeamUUID(uuid), WirelessManaContainer::new);
    }

    @Setter
    private BigInteger storage;
    private final UUID uuid;

    public WirelessManaContainer(UUID uuid) {
        this.uuid = uuid;
        this.storage = BigInteger.ZERO;
    }

    public WirelessManaContainer(UUID uuid, BigInteger storage) {
        this(uuid);
        this.storage = storage;
    }
}
