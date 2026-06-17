package com.gtocore.api.accelerator;

import com.gtocore.api.accelerator.particle.ParticleDefinition;

import com.gtolib.GTOCore;
import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.registry.GTRegistry;

import com.gto.fastcollection.O2OOpenCacheHashMap;

public class Particles {

    public static final O2OOpenCacheHashMap<String, CNEN> NAME_TO_transkey = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    public static final GTRegistry.RL<ParticleDefinition> REGISTRY_KEY;

    static {
        REGISTRY_KEY = new GTRegistry.RL<>(GTOCore.id("particles"));
        REGISTRY_KEY.unfreeze();
    }

    private static ParticleDefinition register(String name, String cn, double mass, double charge, double width) {
        var definition = new ParticleDefinition(name, mass, charge, width);
        REGISTRY_KEY.register(GTOCore.id(name), definition);
        if (GTCEu.isDataGen()) {
            NAME_TO_transkey.put(definition.translationKey(), new CNEN(name, cn));
        }
        return definition;
    }

    public static void init() {
        REGISTRY_KEY.freeze();
    }

    public static final ParticleDefinition EMPTY = register("empty", "空", 0, 0, 0);

    // 费米子
    public static final ParticleDefinition ELECTRON = register("electron", "电子", 0.511, -1, 1);
    public static final ParticleDefinition MUON = register("muon", "μ子", 105.66, -1, 1);
    public static final ParticleDefinition TAU = register("tau", "τ子", 1776.86, -1, 1);
    public static final ParticleDefinition UP_QUARK = register("up quark", "上夸克", 2.2, 2.0 / 3, 0.1);
    public static final ParticleDefinition DOWN_QUARK = register("down quark", "下夸克", 4.7, -1.0 / 3, 0.1);
    public static final ParticleDefinition CHARM_QUARK = register("charm quark", "粲夸克", 1275, 2.0 / 3, 0.1);
    public static final ParticleDefinition STRANGE_QUARK = register("strange quark", "奇夸克", 96, -1.0 / 3, 0.1);
    public static final ParticleDefinition TOP_QUARK = register("top quark", "顶夸克", 173100, 2.0 / 3, 0.1);
    public static final ParticleDefinition BOTTOM_QUARK = register("bottom quark", "底夸克", 4180, -1.0 / 3, 0.1);
    public static final ParticleDefinition ANTIELECTRON = register("antielectron", "正电子", 0.511, 1, 1);
    public static final ParticleDefinition ANTIMUON = register("antimuon", "反μ子", 105.66, 1, 1);
    public static final ParticleDefinition ANTITAU = register("antitau", "反τ子", 1776.86, 1, 1);
    public static final ParticleDefinition ANTIUP_QUARK = register("antiup quark", "反上夸克", 2.2, -2.0 / 3, 0.1);
    public static final ParticleDefinition ANTIDOWN_QUARK = register("antidown quark", "反下夸克", 4.7, 1.0 / 3, 0.1);
    public static final ParticleDefinition ANTICHARM_QUARK = register("anticharm quark", "反粲夸克", 1275, -2.0 / 3, 0.1);
    public static final ParticleDefinition ANTISTRANGE_QUARK = register("antistrange quark", "反奇夸克", 96, 1.0 / 3, 0.1);
    public static final ParticleDefinition ANTITOP_QUARK = register("antitop quark", "反顶夸克", 173100, -2.0 / 3, 0.1);
    public static final ParticleDefinition ANTIBOTTOM_QUARK = register("antibottom quark", "反底夸克", 4180, 1.0 / 3, 0.1);

    // 玻色子
    public static final ParticleDefinition PHOTON = register("photon", "光子", 0, 0, 0);
    public static final ParticleDefinition GLUON = register("gluon", "胶子", 0, 0, 0);
    public static final ParticleDefinition W_BOSON = register("W boson", "W玻色子", 80.379, 1, 0.1);
    public static final ParticleDefinition Z_BOSON = register("Z boson", "Z玻色子", 91.1876, 0, 0.1);
    public static final ParticleDefinition HIGGS_BOSON = register("Higgs boson", "希格斯玻色子", 125.10, 0, 0.1);

    // 介子
    public static final ParticleDefinition PION = register("pion", "π介子", 139.57, 0, 0.1);
    public static final ParticleDefinition KAON = register("kaon", "K介子", 493.67, 0, 0.1);
    public static final ParticleDefinition ETA = register("eta", "η介子", 547.86, 0, 0.1);
    public static final ParticleDefinition RHO = register("rho", "ρ介子", 775.26, 0, 0.1);
    public static final ParticleDefinition OMEGA = register("omega", "ω介子", 782.65, 0, 0.1);
    public static final ParticleDefinition PHI = register("phi", "φ介子", 1019.46, 0, 0.1);
    public static final ParticleDefinition J_PSI = register("J/psi", "J/ψ介子", 3096.9, 0, 0.1);
    public static final ParticleDefinition UPSILON = register("upsilon", "ϒ介子", 9460.3, 0, 0.1);

    // 重子
    public static final ParticleDefinition PROTON = register("proton", "质子", 938.272, 1, 1);
    public static final ParticleDefinition NEUTRON = register("neutron", "中子", 939.565, 0, 0.1);
    public static final ParticleDefinition ANTIPROTON = register("antiproton", "反质子", 938.272, -1, 1);
    public static final ParticleDefinition ANTINEUTRON = register("antineutron", "反中子", 939.565, 0, 0.1);

    // 核素
    // α;
    // Ti-50; Ca-48; Cr-54; Fe-58; Ni-64; Zn-70;
    // Tc-98;
    // Pu-244; Am-243; Cm-247; Bk-247; Cf-251; Cf-252; Es-252; Fm-257; Md-258; No-259; Lr-262;
    public static final ParticleDefinition ALPHA = register("alpha", "α粒子", 3727.379, 2, 2);

    public static final ParticleDefinition TI_50 = register("Ti-50", "钛-50", 46548.0, 22, 14);
    public static final ParticleDefinition CA_48 = register("Ca-48", "钙-48", 44798.0, 20, 16);
    public static final ParticleDefinition CR_54 = register("Cr-54", "铬-54", 50391.0, 24, 13.6);
    public static final ParticleDefinition FE_58 = register("Fe-58", "铁-58", 54006.0, 26, 13.2);
    public static final ParticleDefinition NI_64 = register("Ni-64", "镍-64", 59616.0, 28, 12.8);
    public static final ParticleDefinition ZN_70 = register("Zn-70", "锌-70", 65177.0, 30, 12.4);

    public static final ParticleDefinition TC_98 = register("Tc-98", "锝-98", 91300.0, 43, 33.0);

    public static final ParticleDefinition PU_244 = register("Pu-244", "钚-244", 227000.0, 94, 106);
    public static final ParticleDefinition AM_243 = register("Am-243", "镅-243", 227000.0, 95, 106);
    public static final ParticleDefinition CM_247 = register("Cm-247", "锔-247", 247000.0, 96, 106);
    public static final ParticleDefinition BK_247 = register("Bk-247", "锫-247", 247000.0, 97, 106);
    public static final ParticleDefinition CF_251 = register("Cf-251", "锎-251", 251000.0, 98, 106);
    public static final ParticleDefinition CF_252 = register("Cf-252", "锎-252", 252000.0, 98, 106);
    public static final ParticleDefinition ES_252 = register("Es-252", "锿-252", 252000.0, 99, 106);
    public static final ParticleDefinition FM_257 = register("Fm-257", "镄-257", 257000.0, 100, 106);
    public static final ParticleDefinition MD_258 = register("Md-258", "钔-258", 258000.0, 101, 106);
    public static final ParticleDefinition NO_259 = register("No-259", "锘-259", 259000.0, 102, 106);
    public static final ParticleDefinition LR_262 = register("Lr-262", "铹-262", 262000.0, 103, 106);
}
