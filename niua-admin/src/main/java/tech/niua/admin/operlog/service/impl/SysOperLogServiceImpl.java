package tech.niua.admin.operlog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.niua.admin.operlog.domain.SysOperLog;
import tech.niua.admin.operlog.mapper.SysOperLogMapper;
import tech.niua.admin.operlog.service.ISysOperLogService;

/**
 * <p>
 *  操作日志记录 服务实现类
 * </p>
 *
 * @author niua
 * @since 2021-01-29 18:43:12
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {

}
