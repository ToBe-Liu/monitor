package com.wkyc.monitor.dao;

import com.wkyc.monitor.service.WkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WkServiceTests {

	@Autowired
	WkService wkService;
	@Test
	public void TestDeleteBatch() {

		wkService.failureTransactionSendMail();
	}

}
