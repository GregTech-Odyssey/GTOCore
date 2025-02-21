package com.gto.gtocore.mixin.curvypipes;

import cyb0124.curvy_pipes.common.CommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommonHandler.class)
public class CommonHandlerMixin {

    @Shadow(remap = false)
    private static void loadConfig(String s) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void loadConfig() {
        loadConfig("""
                ignore_unknown_pipes: true
                pipe_types:
                  - id: tiny_item_pipe
                    name: Tiny Item Pipe
                    texture: curvy_pipes:block/item_pipe
                    diameter: 0.1
                    variant: { Item: { rate: 0.1 } }
                  - { id: small_item_pipe, name: Small Item Pipe, texture: curvy_pipes:block/item_pipe, diameter: 0.2, variant: { Item: { rate: 1. } } }
                  - { id: medium_item_pipe, name: Medium Item Pipe, texture: curvy_pipes:block/item_pipe, diameter: 0.4, variant: { Item: { rate: 10. } } }
                  - { id: large_item_pipe, name: Large Item Pipe, texture: curvy_pipes:block/item_pipe, diameter: 0.8, variant: { Item: { rate: 100. } } }
                  - { id: huge_item_pipe, name: Huge Item Pipe, texture: curvy_pipes:block/item_pipe, diameter: 1.6, variant: { Item: { rate: 1E3 } } }
                  - { id: tiny_fluid_pipe, name: Tiny Fluid Pipe, texture: curvy_pipes:block/fluid_pipe, diameter: 0.1, variant: { Fluid: { rate: 10. } } }
                  - { id: small_fluid_pipe, name: Small Fluid Pipe, texture: curvy_pipes:block/fluid_pipe, diameter: 0.2, variant: { Fluid: { rate: 100. } } }
                  - { id: medium_fluid_pipe, name: Medium Fluid Pipe, texture: curvy_pipes:block/fluid_pipe, diameter: 0.4, variant: { Fluid: { rate: 1E3 } } }
                  - { id: large_fluid_pipe, name: Large Fluid Pipe, texture: curvy_pipes:block/fluid_pipe, diameter: 0.8, variant: { Fluid: { rate: 10E3 } } }
                  - { id: huge_fluid_pipe, name: Huge Fluid Pipe, texture: curvy_pipes:block/fluid_pipe, diameter: 1.6, variant: { Fluid: { rate: 100E3 } } }
                  - { id: tiny_energy_pipe, name: Tiny Energy Pipe, texture: curvy_pipes:block/energy_pipe, diameter: 0.1, variant: { Energy: { rate: 1E3 } } }
                  - { id: small_energy_pipe, name: Small Energy Pipe, texture: curvy_pipes:block/energy_pipe, diameter: 0.2, variant: { Energy: { rate: 10E3 } } }
                  - { id: medium_energy_pipe, name: Medium Energy Pipe, texture: curvy_pipes:block/energy_pipe, diameter: 0.4, variant: { Energy: { rate: 100E3 } } }
                  - { id: large_energy_pipe, name: Large Energy Pipe, texture: curvy_pipes:block/energy_pipe, diameter: 0.8, variant: { Energy: { rate: 1E6 } } }
                  - { id: huge_energy_pipe, name: Huge Energy Pipe, texture: curvy_pipes:block/energy_pipe, diameter: 1.6, variant: { Energy: { rate: 10E6 } } }
                gtceu:
                  eu_cables: OffHand
                  item_pipes: OffHand
                  fluid_pipes: OffHand
                recipes:
                  item_base:
                    result: { count: 1, item: curvy_pipes:small_item_pipe }
                    type: crafting_shaped
                    key: { A: { tag: gt:nuggets/electrum }, B: { tag: gt:nuggets/red_alloy } }
                    pattern: [ 'AAA', 'ABA', 'AAA' ]
                  fluid_base:
                    result: { count: 1, item: curvy_pipes:small_fluid_pipe }
                    type: crafting_shaped
                    key: { A: { tag: gt:nuggets/blue_alloy }, B: { tag: gt:nuggets/red_alloy } }
                    pattern: [ 'AAA', 'ABA', 'AAA' ]
                  energy_base:
                    result: { count: 1, item: curvy_pipes:small_energy_pipe }
                    type: crafting_shaped
                    key: { A: { tag: gt:nuggets/redstone_alloy }, B: { tag: gt:nuggets/red_alloy } }
                    pattern: [ 'AAA', 'ABA', 'AAA' ]
                  item_t2s: { result: { count: 1, item: curvy_pipes:small_item_pipe }, type: crafting_shapeless, ingredients: [ &ti { item: curvy_pipes:tiny_item_pipe }, *ti, *ti, *ti ] }
                  item_s2m: { result: { count: 1, item: curvy_pipes:medium_item_pipe }, type: crafting_shapeless, ingredients: [ &si { item: curvy_pipes:small_item_pipe }, *si, *si, *si ] }
                  item_m2l: { result: { count: 1, item: curvy_pipes:large_item_pipe }, type: crafting_shapeless, ingredients: [ &mi { item: curvy_pipes:medium_item_pipe }, *mi, *mi, *mi ] }
                  item_l2h: { result: { count: 1, item: curvy_pipes:huge_item_pipe }, type: crafting_shapeless, ingredients: [ &li { item: curvy_pipes:large_item_pipe }, *li, *li, *li ] }
                  item_h2l: { result: { count: 4, item: curvy_pipes:large_item_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:huge_item_pipe } ] }
                  item_l2m: { result: { count: 4, item: curvy_pipes:medium_item_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:large_item_pipe } ] }
                  item_m2s: { result: { count: 4, item: curvy_pipes:small_item_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:medium_item_pipe } ] }
                  item_s2t: { result: { count: 4, item: curvy_pipes:tiny_item_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:small_item_pipe } ] }
                  fluid_t2s: { result: { count: 1, item: curvy_pipes:small_fluid_pipe }, type: crafting_shapeless, ingredients: [ &tf { item: curvy_pipes:tiny_fluid_pipe }, *tf, *tf, *tf ] }
                  fluid_s2m: { result: { count: 1, item: curvy_pipes:medium_fluid_pipe }, type: crafting_shapeless, ingredients: [ &sf { item: curvy_pipes:small_fluid_pipe }, *sf, *sf, *sf ] }
                  fluid_m2l: { result: { count: 1, item: curvy_pipes:large_fluid_pipe }, type: crafting_shapeless, ingredients: [ &mf { item: curvy_pipes:medium_fluid_pipe }, *mf, *mf, *mf ] }
                  fluid_l2h: { result: { count: 1, item: curvy_pipes:huge_fluid_pipe }, type: crafting_shapeless, ingredients: [ &lf { item: curvy_pipes:large_fluid_pipe }, *lf, *lf, *lf ] }
                  fluid_h2l: { result: { count: 4, item: curvy_pipes:large_fluid_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:huge_fluid_pipe } ] }
                  fluid_l2m: { result: { count: 4, item: curvy_pipes:medium_fluid_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:large_fluid_pipe } ] }
                  fluid_m2s: { result: { count: 4, item: curvy_pipes:small_fluid_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:medium_fluid_pipe } ] }
                  fluid_s2t: { result: { count: 4, item: curvy_pipes:tiny_fluid_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:small_fluid_pipe } ] }
                  energy_t2s: { result: { count: 1, item: curvy_pipes:small_energy_pipe }, type: crafting_shapeless, ingredients: [ &te { item: curvy_pipes:tiny_energy_pipe }, *te, *te, *te ] }
                  energy_s2m: { result: { count: 1, item: curvy_pipes:medium_energy_pipe }, type: crafting_shapeless, ingredients: [ &se { item: curvy_pipes:small_energy_pipe }, *se, *se, *se ] }
                  energy_m2l: { result: { count: 1, item: curvy_pipes:large_energy_pipe }, type: crafting_shapeless, ingredients: [ &me { item: curvy_pipes:medium_energy_pipe }, *me, *me, *me ] }
                  energy_l2h: { result: { count: 1, item: curvy_pipes:huge_energy_pipe }, type: crafting_shapeless, ingredients: [ &le { item: curvy_pipes:large_energy_pipe }, *le, *le, *le ] }
                  energy_h2l: { result: { count: 4, item: curvy_pipes:large_energy_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:huge_energy_pipe } ] }
                  energy_l2m: { result: { count: 4, item: curvy_pipes:medium_energy_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:large_energy_pipe } ] }
                  energy_m2s: { result: { count: 4, item: curvy_pipes:small_energy_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:medium_energy_pipe } ] }
                  energy_s2t: { result: { count: 4, item: curvy_pipes:tiny_energy_pipe }, type: crafting_shapeless, ingredients: [ { item: curvy_pipes:small_energy_pipe } ] }
                """);
    }
}
