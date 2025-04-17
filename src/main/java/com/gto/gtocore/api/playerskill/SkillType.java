package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.AttributeRecord;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.network.chat.Component;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.*;

@Builder
@Getter(AccessLevel.PUBLIC)
public class SkillType {

    @NotNull
    private final String id; // 唯一标识符
    @NotNull
    private final String nameTranslateKey;
    @NotNull
    private final Integer levelStepPerVoltage;
    @NotNull
    private final String chineseName;
    @NotNull
    private final String englishName;
    @NotNull
    private final String nbtKey; // 存储数据，不能改
    @NotNull
    private final ToLongFunction<BasicExperienceLevel> nextLevelExperienceFormula;
    @NotNull
    private final Function<PlayerData, BasicExperienceLevel> experienceLevelGetter;

    @Builder.ObtainVia(method = "originGenerateUpgradePackage")
    public Boolean generateUpgradePackage; // 是否生成升级包
    private ToLongBiFunction<Long, Long> upgradePackageBonusFormula;

    @Getter
    @NotNull
    @Singular
    private List<AttributeRecord> attributeRecords; // 属性加成

    @NotNull
    @Builder.Default
    public final Boolean isVisible = true; // 是否可见

    public static boolean originGenerateUpgradePackage() {
        return false;
    }

    public static class SkillTypeBuilder {

        public SkillTypeBuilder upgradePackageBonus(ToLongBiFunction<Long, Long> upgradePackageBonusFormula) {
            this.upgradePackageBonusFormula = upgradePackageBonusFormula;
            this.generateUpgradePackage = true;
            return this;
        }
    }

    public boolean equals(SkillType other) {
        return this.id.equals(other.id);
    }

    public String getName() {
        return Component.translatable(this.nameTranslateKey).getString();
    }

    public BasicExperienceLevel getExperienceLevel(PlayerData playerData) {
        return experienceLevelGetter.apply(playerData);
    }
}
