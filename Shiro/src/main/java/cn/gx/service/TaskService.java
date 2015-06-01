package cn.gx.service;

import java.util.List;

import cn.gx.quartz.job.ScheduleJob;

public interface TaskService {

	void addTask(ScheduleJob job);
	
	void pauseTask(ScheduleJob job);
	
	void resumeTask(ScheduleJob job);
	
	List<ScheduleJob> getAllJob();
	
	List<ScheduleJob> getExecuteJob();
	
	void removeJob(ScheduleJob job);
	
	void triggerJob(ScheduleJob job);
}
