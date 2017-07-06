package com.revature.caliber.services;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:integration-test.xml" })
public class ReportingServiceTest {

	private static Logger log = Logger.getLogger(ReportingServiceTest.class);
	@SuppressWarnings("unused")
	@Autowired
	private ReportingService reportingService;

	private static int runs = 10;
	private static long nano = 1000000000L;
	Calendar startDate = Calendar.getInstance();

	@Test
	@Ignore
	public void benchmarkTest() {
		long serialRunTimeSystem = serialMethodTest();
		long concurrentRunTimeSystem = concurrentMethod();

		log.info("Serial System Time         : " + ((double) serialRunTimeSystem / nano) + " seconds.");
		log.info("Concurrent System Time     : " + ((double) concurrentRunTimeSystem / nano) + " seconds.");
		double percentageImprovement = (double) (serialRunTimeSystem - concurrentRunTimeSystem) / serialRunTimeSystem
				* 100;
		log.info("Concurrent Speed Improvement: " + percentageImprovement + "%");
	}

	public long serialMethodTest() {
		startDate.set(2017, Calendar.MARCH, 01);
		long startTimeNano = System.nanoTime();
		
		long serialRunTimeSystem = System.nanoTime() - startTimeNano;
		serialRunTimeSystem /= runs;
		return serialRunTimeSystem;
	}

	public long concurrentMethod() {
		long startTimeNano = System.nanoTime();
		
		long concurrentRunTimeSystem = System.nanoTime() - startTimeNano;
		concurrentRunTimeSystem /= runs;
		return concurrentRunTimeSystem;
	}
	
}