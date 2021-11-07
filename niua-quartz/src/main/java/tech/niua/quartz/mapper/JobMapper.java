package tech.niua.quartz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.niua.quartz.domain.QuartzJob;



@Mapper
public interface JobMapper {

    IPage<QuartzJob> listJob(@Param("jobName") String jobName, IPage<QuartzJob> page);
}
