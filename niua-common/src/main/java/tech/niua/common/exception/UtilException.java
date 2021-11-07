package tech.niua.common.exception;

/**
 * @author wangzhen
 * @title: UtilException
 * @projectName niua_easy_parent
 * @description: 工具类异常
 * @date 2020/12/19 下午10:42
 */
public class UtilException extends RuntimeException
{
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e)
    {
        super(e.getMessage(), e);
    }

    public UtilException(String message)
    {
        super(message);
    }

    public UtilException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
