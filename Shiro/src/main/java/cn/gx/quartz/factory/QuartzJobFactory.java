package cn.gx.quartz.factory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.gx.quartz.job.ScheduleJob;

/**
 * 任务运行工厂类
 * @author Administrator
 *
 */
public class QuartzJobFactory implements Job {
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		System.out.println(scheduleJob.getJobName());
	}

}
