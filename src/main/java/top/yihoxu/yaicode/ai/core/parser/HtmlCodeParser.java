package top.yihoxu.yaicode.ai.core.parser;

import top.yihoxu.yaicode.ai.model.HtmlCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yihoxu
 * @date 2025/9/3  14:06
 * @description HTML代码解析
 */
public class HtmlCodeParser implements CodeParser<HtmlCodeResult> {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    /**
     * 解析HTML代码
     * @param content
     * @return
     */
    @Override
    public HtmlCodeResult parserCode(String content) {
        HtmlCodeResult htmlCodeResult = new HtmlCodeResult();
        //解析代码
        String result = extractHtmlCode(content);
        if (result != null && !result.trim().isEmpty()) {
            htmlCodeResult.setHtmlCode(result.trim());
        } else {
            htmlCodeResult.setHtmlCode(content.trim());
        }
        return htmlCodeResult;
    }


    /**
     * 解析HTML代码
     *
     * @param content
     * @return
     */
    private  String extractHtmlCode(String content) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
