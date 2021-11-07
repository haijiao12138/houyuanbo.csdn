package tech.niua.admin.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.niua.admin.test.domain.Test;
import tech.niua.admin.test.mapper.TestMapper;
import tech.niua.admin.test.service.ITestService;

/**
 * <p>
 *  测试表 服务实现类
 * </p>
 *
 * @author niua
 * @since 2021-01-22 11:30:06
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
