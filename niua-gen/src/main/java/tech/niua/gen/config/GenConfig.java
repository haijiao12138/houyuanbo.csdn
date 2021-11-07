package tech.niua.gen.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 * 
 * @author niua
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = "classpath:gen.yml")
@Data
public class GenConfig {
    /** 作者 */
    @Value("${author}")
    public String author;

    /** 生成包路径 */
    @Value("${packageName}")
    public String packageName;

    /** 自动去除表前缀，默认是false */
    @Value("${autoRemovePre}")
    public boolean autoRemovePre;

    /** 表前缀(类名不会包含表前缀) */
    @Value("${tablePrefix}")
    public String tablePrefix;
    /** 数据库连接地址 */
    @Value("${url}")
    public String url;
    /** 数据库访问用户名 */
    @Value("${username}")
    public String username;
    /** 数据库访问密码*/
    @Value("${password}")
    public String password;
    /** 数据库驱动*/
    @Value("${driverClassName}")
    public String driverClassName;
    /** 代码生成路径*/
    @Value("${outPath}")
    public String outPath;




}
