package top.yihoxu.yaicode.ai.core;

import top.yihoxu.yaicode.ai.model.HtmlCodeResult;
import top.yihoxu.yaicode.ai.model.MultiFileCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yihoxu
 * @date 2025/9/2  18:57
 * @description 代码解析器
 */
public class CodeParser {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("```css\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private static final Pattern JS_CODE_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);


    /**
     * 解析HTML单文件代码
     *
     * @param codeContent
     * @return
     */
    public static HtmlCodeResult parseHtmlCode(String codeContent) {
        HtmlCodeResult htmlCodeResult = new HtmlCodeResult();
        String htmlCode = extractHtmlCode(codeContent);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            htmlCodeResult.setHtmlCode(htmlCode.trim());
        } else {
            htmlCodeResult.setHtmlCode(htmlCode.trim());
        }
        return htmlCodeResult;

    }

    /**
     * 解析多文件代码
     *
     * @param codeContent
     * @return
     */
    public static MultiFileCodeResult parserMultiFileCode(String codeContent) {
        MultiFileCodeResult multiFileCodeResult = new MultiFileCodeResult();
        String htmlCode = extractCodeByPattern(codeContent, HTML_CODE_PATTERN);
        String cssCode = extractCodeByPattern(codeContent, CSS_CODE_PATTERN);
        String jsCode = extractCodeByPattern(codeContent, JS_CODE_PATTERN);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            multiFileCodeResult.setHtmlCode(htmlCode);
        }
        if (cssCode != null && !cssCode.trim().isEmpty()) {
            multiFileCodeResult.setCssCode(cssCode);
        }
        if (jsCode != null && !jsCode.trim().isEmpty()) {
            multiFileCodeResult.setJsCode(jsCode);
        }
        return multiFileCodeResult;
    }


    /**
     * 提取HTML代码内容
     *
     * @param content
     * @return
     */
    private static String extractHtmlCode(String content) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    /**
     * 根据正则提取代码
     *
     * @param content
     * @param pattern
     * @return
     */
    private static String extractCodeByPattern(String content, Pattern pattern) {
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
