package com.wkyc.monitor.schedule.job.tcc;

import com.wkyc.monitor.service.TccFailureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *  事务失败发送邮件定时任务
 * @author ZhangHailiang
 */
@Component("tccTransactionFailureJob")
public class TccTransactionFailureJob {

	/**审核日志*/
	Logger _logger = LoggerFactory.getLogger(this.getClass());
	/**控制并发*/
	private volatile boolean isRunning = false;

	@Autowired
	private TccFailureService tccFailureService;

	public void execute() {
		if (isRunning) {
			_logger.info("[TccTransactionFailureJob] 本任务正在执行中，稍后再试！");
			return;
		}
		isRunning = true;
		try {
			tccFailureService.failureTransactionSendMail();
		} catch (Exception e) {
			e.printStackTrace();
			_logger.info("[TccTransactionFailureJob] 执行任务出错:" + e);
		} finally {
			isRunning = false;
			_logger.info("[TccTransactionFailureJob] 当次任务执行结束！");
		}
	}
}
