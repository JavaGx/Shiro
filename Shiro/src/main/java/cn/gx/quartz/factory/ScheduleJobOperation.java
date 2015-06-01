package cn.gx.quartz.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import cn.gx.quartz.job.ScheduleJob;


public class ScheduleJobOperation {
	
	@Resource
	private static SchedulerFactoryBean schedulerFactory;
	
	private static Scheduler scheduler;
	
	static{
		scheduler= schedulerFactory.getScheduler();
	}
	/**
	 * 添加任务
	 * @param scheduleJobs
	 */
	public static void addJob(ScheduleJob scheduleJob,Class<? extends Job> jobClass){
		String jobName=scheduleJob.getJobName();
		String jobGroup=scheduleJob.getJobGroup();
		
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName,jobGroup );
		
		try {
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			
			if(trigger==null){
				
				 JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(jobName, jobGroup).build();
				 
				 jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
				 
				 CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
				 	
				 trigger=TriggerBuilder.newTrigger().withIdentity(jobName,jobGroup)
				 	.withSchedule(cronScheduleBuilder).build();
				 
				 scheduler.scheduleJob(trigger);
			}else{
				CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
			 	
				trigger=TriggerBuilder.newTrigger().withIdentity(jobName,jobGroup)
				 	.withSchedule(cronScheduleBuilder).build();
				
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
		
		
	
	/**
	 * 所有的任务
	 * @return
	 */
	public static List<ScheduleJob> getAllJob(){
		GroupMatcher<JobKey> groupMatcher = GroupMatcher.anyJobGroup();
		
		List<ScheduleJob> jobs=new ArrayList<ScheduleJob>();
		
		try {
			Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
			
			for(JobKey jobKey:jobKeys){
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				
				for(Trigger trigger:triggers){
					TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					
					ScheduleJob job=new ScheduleJob();
					
					job.setJobName(jobKey.getName());
					job.setJobGroup(jobKey.getGroup());
					job.setDesc("触发器："+trigger.getKey());
					job.setJobStatus(triggerState.name());
					
					if(trigger instanceof CronTrigger){
						CronTrigger cronTrigger=(CronTrigger) trigger;
						
						job.setCronExpression(cronTrigger.getCronExpression());
					}
					
					jobs.add(job);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return jobs;
	}
	/**
	 * 获取正在运行的任务
	 * @return
	 */
	public static List<ScheduleJob> getExecuteJob(){
		List<ScheduleJob> jobs=new ArrayList<ScheduleJob>();
		try {
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			
			for(JobExecutionContext context:executingJobs){
				JobDetail jobDetail = context.getJobDetail();
				
				JobKey jobKey = jobDetail.getKey();
				
				Trigger trigger = context.getTrigger();
				
				ScheduleJob job=new ScheduleJob();
				TriggerState state = scheduler.getTriggerState(trigger.getKey());
				
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				job.setDesc("触发器："+trigger.getKey());
				job.setJobStatus(state.name());
				
				if(trigger instanceof CronTrigger){
					CronTrigger cronTrigger=(CronTrigger) trigger;
					
					job.setCronExpression(cronTrigger.getCronExpression());
				}
				jobs.add(job);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return jobs;
	}
	/**
	 * 暂停任务
	 * @param scheduleJob
	 */
	public static void puaseJob(ScheduleJob scheduleJob){
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 恢复任务
	 * @param scheduleJob
	 */
	public static void resumeJob(ScheduleJob scheduleJob){
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 重新运行一次任务
	 * @param scheduleJob
	 */
	public static void triggerjob(ScheduleJob scheduleJob){
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除任务
	 * @param scheduleJob
	 */
	public static void removeJob(ScheduleJob scheduleJob){
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
