package tech.niua.quartz.domain;

import lombok.Data;
import tech.niua.common.annotation.Excel;

import java.util.List;
import java.util.Map;

@Data
public class QuartzJob {

	@Excel(name = "任务名称")
	private String jobName;
	@Excel(name = "任务分组")
	private String jobGroup;
	@Excel(name = "任务描述")
	private String description;
	@Excel(name = "执行类")
	private String jobClassName;
	@Excel(name = "执行时间")
	private String cronExpression;
	private String triggerName;
	@Excel(name = "任务状态")
	private String triggerState;

	private String oldJobName;//任务名称 用于修改
	private String oldJobGroup;//任务分组 用于修改

	private List<Map<String, Object>> jobDataParam;
	
	public QuartzJob() {
		super();
	}
	public QuartzJob(String jobName, String jobGroup, String description, String jobClassName, String cronExpression, String triggerName) {
        super();
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.jobClassName = jobClassName;
        this.cronExpression = cronExpression;
        this.triggerName = triggerName;
    }
}
