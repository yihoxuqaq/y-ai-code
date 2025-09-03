package top.yihoxu.yaicode.ai.core.parser;

/**
 * @author yihoxu
 * @date 2025/9/3  14:05
 * @description 代码解析结构
 */
public interface CodeParser<T> {

    T parserCode(String content);
}
