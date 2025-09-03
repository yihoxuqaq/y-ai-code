package top.yihoxu.yaicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * @author yihoxu
 * @date 2025/9/2  15:48
 * @description HTML代码结构化输出
 */
@Data
@Description("生成HTML代码文件的结果")
public class HtmlCodeResult {

    /**
     * html代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * 代码描述
     */
    @Description("代码描述")
    private String description;
}
