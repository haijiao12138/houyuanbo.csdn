package tech.niua.gen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.niua.gen.domain.Generator;

import java.util.List;

public interface GeneratorMapper  extends BaseMapper<Generator> {

    List<String> quertyAllTableName();

    String  queryAllById(Long Id);



}
