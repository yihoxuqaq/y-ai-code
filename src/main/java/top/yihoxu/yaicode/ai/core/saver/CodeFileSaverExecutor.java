package top.yihoxu.yaicode.ai.core.saver;

import top.yihoxu.yaicode.ai.model.HtmlCodeResult;
import top.yihoxu.yaicode.ai.model.MultiFileCodeResult;
import top.yihoxu.yaicode.ai.model.enums.CodeGenTypeEnum;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;

import java.io.File;

/**
 * @author yihoxu
 * @date 2025/9/3  15:04
 * @description 代码文件保存执行器
 * 根据代码生成类型执行响应的保存逻辑
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();


    /**
     * 执行代码保存
     * @param codeResult
     * @param codeType
     * @return
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeType) {
        return switch (codeType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型" + codeType);

        };
    }


}
