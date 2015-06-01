package cn.gx.service.impl;

import java.util.List;

import cn.gx.quartz.factory.QuartzJobFactory;
import cn.gx.quartz.factory.ScheduleJobOperation;
import cn.gx.quartz.job.ScheduleJob;
import cn.gx.service.TaskService;

public class TaskServiceImpl implements TaskService {

	public void addTask(ScheduleJob job) {
		ScheduleJobOperation.addJob(job, QuartzJobFactory.class);
	}

	public void pauseTask(ScheduleJob job) {
		ScheduleJobOperation.puaseJob(job);
	}

	public List<ScheduleJob> getAllJob() {
		
		return ScheduleJobOperation.getAllJob();
	}

	public List<ScheduleJob> getExecuteJob() {
		
		return ScheduleJobOperation.getExecuteJob();
	}

	public void removeJob(ScheduleJob job) {
		ScheduleJobOperation.removeJob(job);
	}

	public void triggerJob(ScheduleJob job) {
		ScheduleJobOperation.triggerjob(job);
	}

	public void resumeTask(ScheduleJob job) {
		ScheduleJobOperation.removeJob(job);
	}

}
