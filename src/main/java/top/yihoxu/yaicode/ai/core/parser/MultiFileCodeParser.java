package top.yihoxu.yaicode.ai.core.parser;

import top.yihoxu.yaicode.ai.model.MultiFileCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yihoxu
 * @date 2025/9/3  14:12
 * @description 多文件代码解析
 */
public class MultiFileCodeParser implements CodeParser<MultiFileCodeResult> {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("```css\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private static final Pattern JS_CODE_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);


    /**
     * 解析代码
     * @param content
     * @return
     */
    @Override
    public MultiFileCodeResult parserCode(String content) {
        MultiFileCodeResult multiFileCodeResult = new MultiFileCodeResult();
        //解析代码
        String htmlCode = extractCodeByPattern(content, HTML_CODE_PATTERN);
        String cssCode = extractCodeByPattern(content, CSS_CODE_PATTERN);
        String jsCode = extractCodeByPattern(content, JS_CODE_PATTERN);
        if (htmlCode!=null&&!htmlCode.trim().isEmpty()){
            multiFileCodeResult.setHtmlCode(htmlCode.trim());
        }
        if (cssCode!=null&&!cssCode.trim().isEmpty()){
            multiFileCodeResult.setCssCode(cssCode.trim());
        }
        if (jsCode!=null&&!jsCode.trim().isEmpty()){
            multiFileCodeResult.setJsCode(jsCode.trim());
        }
        return multiFileCodeResult;
    }


    /**
     * 根据正则解析代码
     *
     * @param content
     * @param pattern
     * @return
     */
    private String extractCodeByPattern(String content, Pattern pattern) {
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
}
