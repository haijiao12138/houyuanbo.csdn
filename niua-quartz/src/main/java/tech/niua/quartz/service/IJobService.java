package tech.niua.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.niua.common.model.ResultJson;
import tech.niua.quartz.domain.QuartzJob;



public interface IJobService {

    IPage<QuartzJob> listQuartzJob(String jobName, IPage<QuartzJob> page);

    /**
     * 新增job
     * @param quartz
     * @return
     */
    ResultJson saveJob(QuartzJob quartz);

    /**
     * 触发job
     * @param jobName
     * @param jobGroup
     * @return
     */
    ResultJson triggerJob(String jobName, String jobGroup);

    /**
     * 暂停job
     * @param jobName
     * @param jobGroup
     * @return
     */
    ResultJson pauseJob(String jobName, String jobGroup);

    /**
     * 恢复job
     * @param jobName
     * @param jobGroup
     * @return
     */
    ResultJson resumeJob(String jobName, String jobGroup);

    /**
     * 移除job
     * @param jobName
     * @param jobGroup
     * @return
     */
    ResultJson removeJob(String jobName, String jobGroup);
}