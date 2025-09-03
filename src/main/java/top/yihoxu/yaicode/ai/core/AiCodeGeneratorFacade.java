package top.yihoxu.yaicode.ai.core;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import top.yihoxu.yaicode.ai.AiCodeGeneratorService;
import top.yihoxu.yaicode.ai.core.parser.CodeParserExecutor;
import top.yihoxu.yaicode.ai.core.saver.CodeFileSaverExecutor;
import top.yihoxu.yaicode.ai.model.HtmlCodeResult;
import top.yihoxu.yaicode.ai.model.MultiFileCodeResult;
import top.yihoxu.yaicode.ai.model.enums.CodeGenTypeEnum;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;

import java.io.File;

/**
 * @author yihoxu
 * @date 2025/9/2  18:38
 * @description ai生成代码并保存
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;


    /**
     * 统一入口:根据类型生成并保存代码
     *
     * @param userMessage
     * @param codeType
     * @return
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeType) {
        if (codeType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeType) {
            case HTML -> {
                //  generateAndSaveHtmlCode(userMessage);
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(htmlCodeResult, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                //  generateAndSaveMultiFileCode(userMessage);
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE);
            }
            default -> {
                String errorMessage = "不支持的生成类型:" + codeType.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口:根据类型生成并保存代码(流式)
     *
     * @param userMessage
     * @param codeType
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeType) {
        if (codeType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeType) {
            case HTML -> {
                // generateAndSaveHtmlCodeStream(userMessage);
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                // generateAndSaveMultiFileCodeStream(userMessage);
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE);
            }
            default -> {
                String errorMessage = "不支持的生成类型:" + codeType.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }


    /**
     * 生成原生HTML代码并保存
     *
     * @param userMessage
     * @return
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
    }

    /**
     * 生成原生多文件HTML代码并保存
     *
     * @param userMessage
     * @return
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
    }

    /**
     * 生成原生HTML代码并保存(流式)
     *
     * @param userMessage
     * @return
     */
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream
                .doOnNext(chunk -> {
                    //收集流式响应
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    try {
                        //收集完成
                        String completeHtmlCode = codeBuilder.toString();
                        //解析代码
                        HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeHtmlCode);
                        //保存代码
                        File saveDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                        log.info("保存成功,路径为:" + saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败:{}", e.getMessage());
                    }

                });
    }

    /**
     * 生成原生多文件HTML代码并保存(流失)
     *
     * @param userMessage
     * @return
     */
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        StringBuilder codeBuilder = new StringBuilder();

        return codeStream.doOnNext(chunk -> {
                    //收集流式
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    try {
                        //完成收集
                        String completeMultiFileCode = codeBuilder.toString();
                        //解析代码
                        MultiFileCodeResult multiFileCodeResult = CodeParser.parserMultiFileCode(completeMultiFileCode);
                        //保存代码
                        File saveDir = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
                        log.info("保存成功,路径为:" + saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败:{}", e.getMessage());
                    }
                });
    }

    /**
     * 通用流式代码处理方法
     *
     * @param codeStream
     * @param codeType
     * @return
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeType) {
        StringBuilder codeBuilder = new StringBuilder();

        return codeStream.doOnNext(chunk -> {
                    //收集流式代码
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    try {
                        //完成收集
                        String codeComplete = codeBuilder.toString();
                        //代码解析器解析代码
                        Object result = CodeParserExecutor.executeParser(codeComplete, codeType);
                        //代码保存器保存代码
                        File saveDir = CodeFileSaverExecutor.executeSaver(result, codeType);
                        log.info("保存成功,路径为:" + saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败:{}", e.getMessage());
                    }
                });


    }
}
