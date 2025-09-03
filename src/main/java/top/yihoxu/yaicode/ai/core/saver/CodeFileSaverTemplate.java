package top.yihoxu.yaicode.ai.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import top.yihoxu.yaicode.ai.model.enums.CodeGenTypeEnum;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author yihoxu
 * @date 2025/9/3  14:30
 * @description 抽象代码文件保存器
 */
public abstract class CodeFileSaverTemplate<T> {

    //文件保存根目录
    protected static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";


    /**
     * 模板方法:保存代码的标准流程
     * @param result
     * @return
     */
    public final File saveCode (T result){
        //1、验证输入
        validateInput(result);
        //2、构建唯一目录
        String baseDirPath = buildUniqueDir();
        //3、保存文件（具体实现由子类提供）
        saveFiles(result,baseDirPath);
        //4、返回目录文件对象
        return new File(baseDirPath);
    }

    /**
     * 验证输入参数（可由子类覆盖）
     *
     * @param result
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码对象不能为空");
        }
    }

    /**
     * 构建唯一目录路径
     *
     * @return
     */
    protected final String buildUniqueDir() {
        String codeType = getCodeType().getValue();
        String uniqueDir = StrUtil.format("{}_{}", codeType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDir;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 写入单个文件的工具方法
     * @param dirPath
     * @param fileName
     * @param content
     */
    protected final void writeToFile(String dirPath, String fileName, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + fileName;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }

    /**
     * 获取代码类型(由子类实现)
     *
     * @return
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 保存文件的具体实现(由子类实现)
     *
     * @param result
     * @param baseDirPath
     */
    protected abstract void saveFiles(T result, String baseDirPath);

}
