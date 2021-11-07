package tech.niua.gen.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.niua.gen.config.GenConfig;
import tech.niua.gen.domain.Generator;
import tech.niua.gen.model.MybatisFieldModel;

import java.io.File;
import java.util.*;
/**
 * @className: AutoGeneratorUtils
 * @description:
 * @author: xiaoDong
 * @date: 2020/12/17
 * wangzhen
 * @modifyDate 2021/1/16
 */
@Component
public class AutoGeneratorUtils {
    @Autowired
    private GenConfig genConfig;


    /**
     * @Method: createCode
     * @Param: [tableName, moduleName]
     * @Return: void
     * @Description:
     * @Author: xiaoDong
     * @Date: 2020/12/21
     *
     */
    public void createCode(Generator generator){
        AutoGenerator mpg = new AutoGenerator();

        //数据库连接配置
        String url = genConfig.getUrl();
        String driverName =genConfig.getDriverClassName();
        String userName = genConfig.getUsername();
        String password = genConfig.getPassword();
        //自定义器代码生成输出路径
        String outPutDir = genConfig.getOutPath();

        //mp自动生成器package输出路径
        String parentDir = genConfig.getPackageName();

        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(outPutDir + "/src/main/java");
        gc.setAuthor(genConfig.getAuthor());
        gc.setOpen(false);
        gc.setFileOverride(true);

        mpg.setGlobalConfig(gc);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driverName);
        dsc.setUsername(userName);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        PackageConfig pc = new PackageConfig();
        String moduleName = generator.getModuleName();
        String tableName = generator.getTableName();
        pc.setModuleName(moduleName);
        pc.setParent(parentDir);
        pc.setEntity("domain");
        Map pathInfo = new HashMap<>();
        String controllerPath = outPutDir  + moduleName + File.separator + "controller" ;
        String servicePath = outPutDir  + moduleName + File.separator + "service";
        String serviceImplPath = outPutDir  + moduleName + File.separator + "service" +  File.separator + "impl" ;
        String entityPath = outPutDir  + moduleName + File.separator + "domain";
        String mapperPath = outPutDir  + moduleName + File.separator + "mapper";
        String xmlPath = outPutDir  + moduleName + File.separator + "xml";
        String vuePath = outPutDir  + moduleName + File.separator + "vue";



        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                //组装属性,适配xml
                List<TableInfo> listTableInfo = mpg.getConfig().getTableInfoList();
                List<MybatisFieldModel> mybatisFields = new ArrayList<>();
                for (TableInfo tableInfo : listTableInfo) {
                    if (!tableInfo.getName().equals(tableName)) {
                        continue;
                    }
                    List<TableField> fields = new ArrayList<>();
                    fields.addAll(tableInfo.getCommonFields());
                    fields.addAll(tableInfo.getFields());
                    for (TableField field : fields) {
                        String type = field.getType();
                        if ("TEXT".equalsIgnoreCase(type)) {
                            type = "VARCHAR";
                        }
                        if (type.contains("(")) {
                            type = type.substring(0, type.indexOf("(")).toUpperCase();
                        }

                        if ("INT".equalsIgnoreCase(type)) {
                            type = "INTEGER";
                        }

                        if ("DATETIME".equalsIgnoreCase(type)) {
                            type = "TIMESTAMP";
                        }

                        MybatisFieldModel mybatisModel =
                                new MybatisFieldModel(field.getName(), type.toUpperCase(), field.getPropertyName(), field.isKeyFlag());
                        mybatisFields.add(mybatisModel);
                    }
                }
                map.put("mybatisFields", mybatisFields);
                this.setMap(map);
            }
        };




        List<FileOutConfig> focList = new ArrayList<>();

        // 自定义vue界面文件存放的路径
        focList.add(new FileOutConfig("/generator/vue/Page.vue.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String tableName = tableInfo.getEntityName().substring(0, 1).toLowerCase() + tableInfo.getEntityName().substring(1);
                String entityFile = vuePath + File.separator + tableName + File.separator + tableName.substring(0, 1).toUpperCase() + tableName.substring(1) + ".vue";
                return entityFile;
            }
        });


        pathInfo.put(ConstVal.CONTROLLER_PATH, controllerPath);
        pathInfo.put(ConstVal.SERVICE_PATH, servicePath);
        pathInfo.put(ConstVal.SERVICE_IMPL_PATH, serviceImplPath);
        pathInfo.put(ConstVal.ENTITY_PATH, entityPath);
        pathInfo.put(ConstVal.MAPPER_PATH, mapperPath);
        pathInfo.put(ConstVal.XML_PATH, xmlPath);
        pc.setPathInfo(pathInfo);

        mpg.setPackageInfo(pc);


        injectionConfig.setFileOutConfigList(focList);
        mpg.setCfg(injectionConfig);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController("/generator/java/controller/controller.java");
        templateConfig.setService("/generator/java/service/IService.java");
        templateConfig.setServiceImpl("/generator/java/service/impl/ServiceImpl.java");
        templateConfig.setEntity("/generator/java/domain/entity.java");
        templateConfig.setMapper("/generator/java/mapper/Mapper.java");
        templateConfig.setXml("/generator/xml/mapper.xml");
        mpg.setTemplate(templateConfig);

        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(false);
        strategy.setInclude(tableName);
//        strategy.setControllerMappingHyphenStyle(true);
        if(generator.getIgnoreFlag() == 1){ // 忽略数据库前缀
            strategy.setTablePrefix(generator.getIgnorePrefix());
        }


        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}