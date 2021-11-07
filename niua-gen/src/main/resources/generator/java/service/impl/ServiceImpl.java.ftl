package ${package.ServiceImpl};

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};

/**
 * <p>
 *  ${table.comment} 服务实现类
 * </p>
 *
 * @author niua
 * @since ${.now?string("yyyy-MM-dd HH:mm:ss")}
 */
@Service
public class ${table.serviceImplName} extends ServiceImpl<${table.mapperName}, ${table.entityName}> implements ${table.serviceName} {

}
