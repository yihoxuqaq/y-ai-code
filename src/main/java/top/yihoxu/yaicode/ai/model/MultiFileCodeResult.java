package top.yihoxu.yaicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * @author yihoxu
 * @date 2025/9/2  15:50
 * @description 多文件结构化输出
 */
@Data
@Description("生成多个代码文件的结果")
public class MultiFileCodeResult {

    /**
     * html代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * css代码
     */
    @Description("CSS代码")
    private String CssCode;

    /**
     * js代码
     */
    @Description("JS代码")
    private String jsCode;

    /**
     * 代码描述
     */
    @Description("代码文件描述")
    private String description;
}
