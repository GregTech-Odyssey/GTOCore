package com.gtocore.common.machine.tesseract;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import net.minecraft.world.entity.player.Player;

import java.util.List;

@DataGeneratorScanned
public interface ITesseractMarkerInteractable {

    @RegisterLanguage(cn = "高亮显示已绑定的目标", en = "Highlight Bound Targets")
    String HIGHLIGHT_TEXT = "gui.gtceu.machine.tesseract.highlight_targets";
    @RegisterLanguage(cn = "成功将目标写入传送方块", en = "Successfully Written Targets to Tesseract")
    String WRITE_SUCCESS_TEXT = "gui.gtceu.machine.tesseract.write_targets_success";
    @RegisterLanguage(cn = "坐标信息卡不足以存储更多目标", en = "Not Enough Space on Coordinate Card for More Targets")
    String WRITE_FAIL_TEXT = "gui.gtceu.machine.tesseract.write_targets_fail";

    boolean onMarkerInteract(Player player, List<TesseractDirectedTarget> targets);
}
