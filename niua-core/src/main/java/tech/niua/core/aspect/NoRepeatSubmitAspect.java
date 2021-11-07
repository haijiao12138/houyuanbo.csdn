package tech.niua.core.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;
import tech.niua.core.config.RedisLock;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 防止重复提交
 * @author wangzhen
 */
@Component
@Aspect
@Slf4j
public class NoRepeatSubmitAspect {

    @Pointcut("@annotation(tech.niua.core.annotation.NoRepeatSubmit)")
    public void repeatSubmit(){}

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private RedisLock redisLock;
    @Around("repeatSubmit()")
    public Object around(ProceedingJoinPoint joinPoint) {
        log.info("校验重复提交");
        String token = request.getHeader(tokenHeader);
        String key = getKey(token, request.getServletPath());
        String clientId = getClientId();
        boolean isSuccess = redisLock.tryLock(key, clientId, 3);
        // 如果缓存中有这个url视为重复提交
        if (isSuccess) {
            Object result = null;
            try {
                result = joinPoint.proceed();

            } catch (Throwable e) {
                log.error(e.getMessage());
            }
//            finally {
//                redisLock.releaseLock(key, clientId);
//            }
            return result;
        } else {
            log.error("重复提交");
            return ResultJson.failure(ResultCode.REPEAT_SUBMIT);
        }
    }

    private String getKey(String token, String path) {
        return token + path;
    }

    private String getClientId() {
        return UUID.randomUUID().toString();
    }

}
