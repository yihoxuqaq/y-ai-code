package top.yihoxu.yaicode.ai.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * @author yihoxu
 * @date 2025/9/2  16:21
 * @description 生成代码类型
 */
@Getter
public enum CodeGenTypeEnum {
    HTML("原生HTML模式", "html"),
    MULTI_FILE("原生多文件模式", "multi_file");

    private final String text;

    private final String value;

    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static CodeGenTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (CodeGenTypeEnum anEnum : CodeGenTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
