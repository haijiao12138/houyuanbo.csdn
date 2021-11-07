package tech.niua.gen.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.niua.gen.domain.Generator;
import tech.niua.gen.mapper.GeneratorMapper;
import tech.niua.gen.service.IGeneratorService;

import java.util.List;

/**
*
* description: GeneratorServiceImpl
* @author:
* @date:
*
*/
@Service
public class GeneratorServiceImpl extends ServiceImpl<GeneratorMapper, Generator> implements IGeneratorService {

    @Autowired
    private GeneratorMapper generatorMapper;

    @Override
    public List<String> quertyAllTableName() {
        return generatorMapper.quertyAllTableName();
    }
}
