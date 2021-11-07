package tech.niua.common.exception.file;

/**
 * @author wangzhen
 * @title: FileNameLengthLimitExceededException
 * @projectName niua_easy_parent
 * @description: 文件名称超长限制异常类
 * @date 2020/12/19 下午10:36
 */
public class FileNameLengthLimitExceededException extends FileException
{
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength)
    {
        super("upload.filename.exceed.length", new Object[] { defaultFileNameLength });
    }
}
