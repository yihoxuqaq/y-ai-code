package top.yihoxu.yaicode.ai.core.parser;

import top.yihoxu.yaicode.ai.model.enums.CodeGenTypeEnum;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;

/**
 * @author yihoxu
 * @date 2025/9/3  14:18
 * @description 代码解析执行器
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();

    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();


    /**
     * 根据代码类型，执行响应的代码解析器
     *
     * @param content
     * @param codeType
     * @return
     */
    public static Object executeParser(String content, CodeGenTypeEnum codeType) {
        return switch (codeType) {
            case HTML -> htmlCodeParser.parserCode(content);
            case MULTI_FILE -> multiFileCodeParser.parserCode(content);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型:" + codeType);
        };

    }
}
