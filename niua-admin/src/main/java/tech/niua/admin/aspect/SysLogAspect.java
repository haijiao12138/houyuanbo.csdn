package tech.niua.admin.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.niua.admin.operlog.domain.SysOperLog;
import tech.niua.admin.operlog.service.ISysOperLogService;
import tech.niua.auth.utils.JwtUtils;
import tech.niua.common.utils.IpUtil;
import tech.niua.core.annotation.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author wangzhen
 * @title: SysLogAspect
 * @projectName nuia-easy
 * @description: 系统日志，切面处理类
 * @date 2021/1/29 下午6:52
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private ISysOperLogService sysOperLogService;

    @Resource
    private JwtUtils jwtUtils;

    @Value("${jwt.tokenHead}")
    private String authTokenStart;

    @Autowired
    private HttpServletRequest request;

    @Value("${jwt.header}")
    private String token_header;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(tech.niua.core.annotation.Log)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        System.out.println("切面。。。。。");
        //保存日志
        SysOperLog sysOperLog = new SysOperLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
            String value = log.value();
            sysOperLog.setOperation(value);//保存获取的操作
        }

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysOperLog.setMethod(className + "." + methodName);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        ObjectMapper objectMapper = new ObjectMapper();
        String params = "";
        try{
            //将参数所在的数组转换成json
            params = objectMapper.writeValueAsString(args);
        }catch (Exception e){
            e.printStackTrace();;
        }


        sysOperLog.setParams(params);
        String auth_token = request.getHeader(this.token_header);
        if (StringUtils.isNotEmpty(auth_token) && auth_token.startsWith(authTokenStart)) {
            auth_token = auth_token.substring(authTokenStart.length());
            String username = jwtUtils.getUsernameFromToken(auth_token);
            sysOperLog.setOperName(username);
        } else {
            sysOperLog.setOperName("未授权操作");
        }


        sysOperLog.setIp(IpUtil.getIpAddr(request));

        sysOperLog.setBusinessType(log.businessType().toString());

        sysOperLogService.save(sysOperLog);
    }
}
