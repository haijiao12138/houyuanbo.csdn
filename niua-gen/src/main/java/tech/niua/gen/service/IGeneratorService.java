package tech.niua.gen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.niua.gen.domain.Generator;

import java.util.List;


public interface IGeneratorService extends IService<Generator> {

    List<String> quertyAllTableName();
}
