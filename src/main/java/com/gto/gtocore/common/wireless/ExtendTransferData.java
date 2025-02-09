package com.gto.gtocore.common.wireless;

import com.hepdd.gtmthings.api.misc.ITransferData;

import java.util.UUID;

public record ExtendTransferData(UUID UUID, long Throughput, long loss) implements ITransferData {}
