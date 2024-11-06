package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PrimitiveOreMachine extends WorkableMultiblockMachine {

    public PrimitiveOreMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    private final List<Material> OVERWORLD_ORE = List.of(GTMaterials.Bentonite,
            GTMaterials.Magnetite,
            GTMaterials.Olivine,
            GTMaterials.GlauconiteSand,
            GTMaterials.Almandine,
            GTMaterials.Pyrope,
            GTMaterials.Sapphire,
            GTMaterials.GreenSapphire,
            GTMaterials.Goethite,
            GTMaterials.YellowLimonite,
            GTMaterials.Hematite,
            GTMaterials.Malachite,
            GTMaterials.Soapstone,
            GTMaterials.Talc,
            GTMaterials.Pentlandite,
            GTMaterials.Grossular,
            GTMaterials.Spessartine,
            GTMaterials.Pyrolusite,
            GTMaterials.Tantalite,
            GTMaterials.Chalcopyrite,
            GTMaterials.Zeolite,
            GTMaterials.Cassiterite,
            GTMaterials.Realgar,
            GTMaterials.Coal,
            GTMaterials.Iron,
            GTMaterials.Pyrite,
            GTMaterials.Copper,
            GTMaterials.VanadiumMagnetite,
            GTMaterials.Gold,
            GTMaterials.Lazurite,
            GTMaterials.Sodalite,
            GTMaterials.Lapis,
            GTMaterials.Calcite,
            GTMaterials.Galena,
            GTMaterials.Silver,
            GTMaterials.Lead,
            GTMaterials.Kyanite,
            GTMaterials.Mica,
            GTMaterials.Pollucite,
            GTMaterials.Tin,
            GTMaterials.GarnetRed,
            GTMaterials.GarnetYellow,
            GTMaterials.Amethyst,
            GTMaterials.Opal,
            GTMaterials.BasalticMineralSand,
            GTMaterials.GraniticMineralSand,
            GTMaterials.FullersEarth,
            GTMaterials.Gypsum,
            GTMaterials.RockSalt,
            GTMaterials.Salt,
            GTMaterials.Lepidolite,
            GTMaterials.Spodumene,
            GTMaterials.Redstone,
            GTMaterials.Ruby,
            GTMaterials.Cinnabar,
            GTMaterials.Apatite,
            GTMaterials.TricalciumPhosphate,
            GTMaterials.CassiteriteSand,
            GTMaterials.GarnetSand,
            GTMaterials.Asbestos,
            GTMaterials.Diatomite,
            GTMaterials.Oilsands,
            GTMaterials.Graphite,
            GTMaterials.Diamond,
            GTMaterials.Garnierite,
            GTMaterials.Nickel,
            GTMaterials.Cobaltite);

    private final List<Material> NETHER_ORE = List.of(GTMaterials.Alunite,
            GTMaterials.Barite,
            GTMaterials.Bastnasite,
            GTMaterials.Beryllium,
            GTMaterials.BlueTopaz,
            GTMaterials.Bornite,
            GTMaterials.CertusQuartz,
            GTMaterials.Chalcocite,
            GTMaterials.Cinnabar,
            GTMaterials.Diatomite,
            GTMaterials.Electrotine,
            GTMaterials.Emerald,
            GTMaterials.Goethite,
            GTMaterials.Grossular,
            GTMaterials.Hematite,
            GTMaterials.Molybdenite,
            GTMaterials.Molybdenum,
            GTMaterials.Monazite,
            GTMaterials.Neodymium,
            GTMaterials.NetherQuartz,
            GTMaterials.Powellite,
            GTMaterials.Pyrite,
            GTMaterials.Pyrolusite,
            GTMaterials.Quartzite,
            GTMaterials.Redstone,
            GTMaterials.Ruby,
            GTMaterials.Saltpeter,
            GTMaterials.Sphalerite,
            GTMaterials.Stibnite,
            GTMaterials.Sulfur,
            GTMaterials.Tantalite,
            GTMaterials.Tetrahedrite,
            GTMaterials.Topaz,
            GTMaterials.Wulfenite,
            GTMaterials.YellowLimonite);

    private final List<Material> END_ORE = List.of(GTMaterials.Aluminium,
            GTMaterials.Alunite,
            GTMaterials.Barite,
            GTMaterials.Bastnasite,
            GTMaterials.Bauxite,
            GTMaterials.Beryllium,
            GTMaterials.BlueTopaz,
            GTMaterials.Bornite,
            GTMaterials.CertusQuartz,
            GTMaterials.Chalcocite,
            GTMaterials.Chromite,
            GTMaterials.Cinnabar,
            GTMaterials.Cooperite,
            GTMaterials.Diatomite,
            GTMaterials.Electrotine,
            GTMaterials.Emerald,
            GTMaterials.Goethite,
            GTMaterials.Grossular,
            GTMaterials.Hematite,
            GTMaterials.Ilmenite,
            GTMaterials.Lithium,
            GTMaterials.Magnetite,
            GTMaterials.Molybdenite,
            GTMaterials.Molybdenum,
            GTMaterials.Monazite,
            GTMaterials.Naquadah,
            GTMaterials.Neodymium,
            GTMaterials.NetherQuartz,
            GTMaterials.Palladium,
            GTMaterials.Pitchblende,
            GTMaterials.Platinum,
            GTMaterials.Plutonium239,
            GTMaterials.Powellite,
            GTMaterials.Pyrite,
            GTMaterials.Pyrolusite,
            GTMaterials.Quartzite,
            GTMaterials.Redstone,
            GTMaterials.Ruby,
            GTMaterials.Saltpeter,
            GTMaterials.Scheelite,
            GTMaterials.Sphalerite,
            GTMaterials.Stibnite,
            GTMaterials.Sulfur,
            GTMaterials.Tantalite,
            GTMaterials.Tetrahedrite,
            GTMaterials.Topaz,
            GTMaterials.Tungstate,
            GTMaterials.Uraninite,
            GTMaterials.VanadiumMagnetite,
            GTMaterials.Wulfenite,
            GTMaterials.YellowLimonite);

    public Material getRandomMaterial(List<Material> list) {
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        return list.get(index);
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (Objects.requireNonNull(getLevel()).dimension() == Level.OVERWORLD) {
            MachineUtil.outputItem(this, ChemicalHelper.get(TagPrefix.rawOre,
                    getRandomMaterial(OVERWORLD_ORE), 64));
        } else if (getLevel().dimension() == Level.NETHER) {
            MachineUtil.outputItem(this, ChemicalHelper.get(TagPrefix.rawOre,
                    getRandomMaterial(NETHER_ORE), 64));
        } else if (getLevel().dimension() == Level.END) {
            MachineUtil.outputItem(this, ChemicalHelper.get(TagPrefix.rawOre,
                    getRandomMaterial(END_ORE), 64));
        }
        return value;
    }
}
