package top.yihoxu.yaicode.ai.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.yihoxu.yaicode.ai.model.HtmlCodeResult;
import top.yihoxu.yaicode.ai.model.MultiFileCodeResult;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yihoxu
 * @date 2025/9/2  19:13
 * @description todo
 */
class CodeParserTest {

    @Test
    void parseHtmlCode() {
        String codeContent = """
                随便写一段描述：
                ```html
                <!DOCTYPE html>
                <html>
                <head>
                    <title>测试页面</title>
                </head>
                <body>
                    <h1>Hello World!</h1>
                </body>
                </html>
                ```
                随便写一段描述
                """;
        HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(codeContent);
        Assertions.assertNotNull(htmlCodeResult);
    }

    @Test
    void parserMultiFileCode() {
        String codeContent = """
                创建一个完整的网页：
                ```html
                <!DOCTYPE html>
                <html>
                <head>
                    <title>多文件示例</title>
                    <link rel="stylesheet" href="style.css">
                </head>
                <body>
                    <h1>欢迎使用</h1>
                    <script src="script.js"></script>
                </body>
                </html>
                ```
                ```css
                h1 {
                    color: blue;
                    text-align: center;
                }
                ```
                ```js
                console.log('页面加载完成');
                ```
                文件创建完成！
                """;
        MultiFileCodeResult multiFileCodeResult = CodeParser.parserMultiFileCode(codeContent);
        Assertions.assertNotNull(multiFileCodeResult);
    }
}