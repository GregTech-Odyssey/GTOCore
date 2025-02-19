package com.gto.gtocore.common.data;

import com.gregtechceu.gtceu.api.sound.SoundEntry;

import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

public interface GTOSoundEntries {

    SoundEntry DTPF = REGISTRATE.sound("dtpf").build();
    SoundEntry FUSIONLOOP = REGISTRATE.sound("fusionloop").build();

    static void init() {}
}
