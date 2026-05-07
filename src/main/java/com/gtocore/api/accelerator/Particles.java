package com.gtocore.api.accelerator;

import com.gtocore.api.accelerator.particle.ParticleDefinition;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.registry.GTRegistry;

public class Particles {

    public static final GTRegistry.RL<ParticleDefinition> REGISTRY_KEY;

    static {
        REGISTRY_KEY = new GTRegistry.RL<>(GTOCore.id("particles"));
        REGISTRY_KEY.unfreeze();
    }

    private static ParticleDefinition register(String name, double mass, double charge, double width) {
        var definition = new ParticleDefinition(name, mass, charge, width);
        REGISTRY_KEY.register(GTOCore.id(name), definition);
        return definition;
    }

    public static void init() {
        REGISTRY_KEY.freeze();
    }

    // 基本粒子
    public static final ParticleDefinition ELECTRON = register("electron", 0.511, -1, 1);
    public static final ParticleDefinition PROTON = register("proton", 938.272, 1, 1);
    public static final ParticleDefinition NEUTRON = register("neutron", 939.565, 0, 0.1);
    public static final ParticleDefinition PHOTON = register("photon", 0, 0, 0);
    public static final ParticleDefinition GLUON = register("gluon", 0, 0, 0);
    public static final ParticleDefinition W_BOSON = register("W boson", 80.379, 1, 0.1);
    public static final ParticleDefinition Z_BOSON = register("Z boson", 91.1876, 0, 0.1);
    public static final ParticleDefinition HIGGS_BOSON = register("Higgs boson", 125.10, 0, 0.1);
    public static final ParticleDefinition MUON = register("muon", 105.66, -1, 1);

    // 核素
    // α;
    // Ti-50; Ca-48; Cr-54; Fe-58; Ni-64; Zn-70;
    // Tc-98;
    // Pu-244; Am-243; Cm-247; Bk-247; Cf-251; Cf-252; Es-252; Fm-257; Md-258; No-259; Lr-262;
    public static final ParticleDefinition ALPHA = register("alpha", 3727.379, 2, 2);

    public static final ParticleDefinition TI_50 = register("Ti-50", 46548.0, 22, 14);
    public static final ParticleDefinition CA_48 = register("Ca-48", 44798.0, 20, 16);
    public static final ParticleDefinition CR_54 = register("Cr-54", 50391.0, 24, 13.6);
    public static final ParticleDefinition FE_58 = register("Fe-58", 54006.0, 26, 13.2);
    public static final ParticleDefinition NI_64 = register("Ni-64", 59616.0, 28, 12.8);
    public static final ParticleDefinition ZN_70 = register("Zn-70", 65177.0, 30, 12.4);

    public static final ParticleDefinition TC_98 = register("Tc-98", 91300.0, 43, 33.0);

    public static final ParticleDefinition PU_244 = register("Pu-244", 227000.0, 94, 106);
    public static final ParticleDefinition AM_243 = register("Am-243", 227000.0, 95, 106);
    public static final ParticleDefinition CM_247 = register("Cm-247", 247000.0, 96, 106);
    public static final ParticleDefinition BK_247 = register("Bk-247", 247000.0, 97, 106);
    public static final ParticleDefinition CF_251 = register("Cf-251", 251000.0, 98, 106);
    public static final ParticleDefinition CF_252 = register("Cf-252", 252000.0, 98, 106);
    public static final ParticleDefinition ES_252 = register("Es-252", 252000.0, 99, 106);
    public static final ParticleDefinition FM_257 = register("Fm-257", 257000.0, 100, 106);
    public static final ParticleDefinition MD_258 = register("Md-258", 258000.0, 101, 106);
    public static final ParticleDefinition NO_259 = register("No-259", 259000.0, 102, 106);
    public static final ParticleDefinition LR_262 = register("Lr-262", 262000.0, 103, 106);
}
