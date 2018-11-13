package com.wkyc.monitor.dao;

import com.wkyc.monitor.domain.TccTransactionMail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TccTransactionMailTests {

	@Autowired
	TccTransactionMailDao tccTransactionMailDao;
	@Test
	public void TestDeleteBatch() {

		List<String> globalTxIds = new ArrayList<>(2);
		//globalTxIds.add("1:CAPITAL");
		//globalTxIds.add("2:CAPITAL");
		int i = tccTransactionMailDao.delete(new TccTransactionMail());
		//int i = tccTransactionMailDao.deleteBatch(null,null);
		System.out.println(i);
	}
	@Test
	public void TestInsertBatch() {

		List<TccTransactionMail> tccTransactionMails = new ArrayList<>(2);
		tccTransactionMails.add(new TccTransactionMail("1:CAPITAL"));
		tccTransactionMails.add(new TccTransactionMail("2:CAPITAL"));
		int i = tccTransactionMailDao.insertBatch(tccTransactionMails);
		System.out.println(i);
	}

}
