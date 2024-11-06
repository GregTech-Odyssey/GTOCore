package com.gto.gtocore.api.item.tool.ae2.patternTool;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import java.util.*;
import java.util.stream.Stream;

public class ConflictAnalysisManager {

    ////////////////////////////////////////////////
    // 使用配方recipe自动生成的样板对象(工具类)的集
    // 一个Manager所管理的配方是一个机器的类型所能制造的配方
    ////////////////////////////////////////////////
    private final List<ConflictAnalysisObj> conflictAnalysisObjs;

    public ConflictAnalysisManager(List<GTRecipe> recipes) {
        this.conflictAnalysisObjs = new ArrayList<>();
        recipes.forEach(recipe -> conflictAnalysisObjs.add(new ConflictAnalysisObj(recipe)));
    }

    public ConflictAnalysisManager(List<ConflictAnalysisObj> conflictAnalysisObjs, boolean dummy) {
        this.conflictAnalysisObjs = conflictAnalysisObjs;
    }

    public static ConflictAnalysisResult findConflict(ConflictAnalysisObj conflictAnalysisObj, List<ConflictAnalysisObj> conflictAnalysisObjs) {
        boolean conflict_k_14223 = false;
        conflictAnalysisObjs = conflictAnalysisObjs.stream().filter(conflictAnalysisObj1 -> !conflictAnalysisObj1.equals(conflictAnalysisObj)).toList();
        List<String> matchPool = Stream.of(conflictAnalysisObj.inputFluidIds, conflictAnalysisObj.inputItemIds).flatMap(Collection::stream).toList();
        List<String> storagePool = new ArrayList<>();
        conflictAnalysisObjs
                .forEach(conflictAnalysisObj1 -> {
                    storagePool.addAll(conflictAnalysisObj1.inputFluidIds);
                    storagePool.addAll(conflictAnalysisObj1.inputItemIds);
                });
        if (new HashSet<>(storagePool).containsAll(matchPool)) {
            conflict_k_14223 = true;
        }
        return new ConflictAnalysisResult(conflict_k_14223, matchPool, storagePool, conflictAnalysisObj);
    }

    public List<ConflictAnalysisResult> useFindConflictForAll(Number circuit) {
        // initialize
        List<ConflictAnalysisResult> conflictAnalysisResultList = new ArrayList<>();

        // preprocessData
        List<ConflictAnalysisObj> conflictAnalysisObjList = this.conflictAnalysisObjs.stream()
                .filter(conflictAnalysisObj -> (Objects.equals(conflictAnalysisObj.CIRCUIT, circuit) || conflictAnalysisObj.CIRCUIT.equals(0))) // 任何电路的机器都可以运行0号电路
                .toList();

        // useConflictProcessLogic
        conflictAnalysisObjList.forEach(conflictAnalysisObj -> {
            ConflictAnalysisResult conflictAnalysisResult = findConflict(conflictAnalysisObj, conflictAnalysisObjList);
            if (conflictAnalysisResult.conflict) {
                conflictAnalysisResultList.add(conflictAnalysisResult);
            }
        });

        return conflictAnalysisResultList;
    }

    public void printAllObject() {
        this.conflictAnalysisObjs.forEach(conflictAnalysisObj -> GTOCore.LOGGER.info(conflictAnalysisObj.toString()));
    }
}
