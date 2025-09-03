package top.yihoxu.yaicode.ai.core.saver;

import cn.hutool.core.util.StrUtil;
import top.yihoxu.yaicode.ai.model.HtmlCodeResult;
import top.yihoxu.yaicode.ai.model.enums.CodeGenTypeEnum;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;

/**
 * @author yihoxu
 * @date 2025/9/3  14:51
 * @description HTML代码文件保存起
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        //保存html代码
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        //html代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容不能为空");
        }
    }
}
