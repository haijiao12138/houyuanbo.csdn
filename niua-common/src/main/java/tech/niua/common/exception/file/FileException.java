package tech.niua.common.exception.file;

import tech.niua.common.exception.BaseException;

/**
 * @author wangzhen
 * @title: FileException
 * @projectName niua_easy_parent
 * @description: 文件信息异常类
 * @date 2020/12/19 下午10:36
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
