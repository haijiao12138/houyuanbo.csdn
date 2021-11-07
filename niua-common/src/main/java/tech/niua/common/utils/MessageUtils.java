package tech.niua.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import tech.niua.common.utils.spring.SpringUtils;

/**
 * @author wangzhen
 * @title: MessageUtils
 * @projectName niua_easy_parent
 * @description: 获取i18n资源文件
 * @date 2020/12/19 下午10:34
 */
public class MessageUtils {

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
