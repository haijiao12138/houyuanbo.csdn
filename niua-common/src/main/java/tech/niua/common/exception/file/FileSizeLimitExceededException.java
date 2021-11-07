package tech.niua.common.exception.file;

/**
 * @author wangzhen
 * @title: FileSizeLimitExceededException
 * @projectName niua_easy_parent
 * @description: 文件名大小限制异常类
 * @date 2020/12/19 下午10:37
 */
public class FileSizeLimitExceededException extends FileException
{
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize)
    {
        super("upload.exceed.maxSize", new Object[] { defaultMaxSize });
    }
}
