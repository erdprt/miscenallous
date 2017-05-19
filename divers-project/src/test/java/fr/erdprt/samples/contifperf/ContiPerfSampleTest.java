package fr.erdprt.samples.contifperf;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.databene.contiperf.report.CSVInvocationReportModule;
import org.databene.contiperf.report.CSVLatencyReportModule;
import org.databene.contiperf.report.CSVSummaryReportModule;
import org.databene.contiperf.report.HtmlReportModule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContiPerfSampleTest {

	private static final Logger LOGGER	=	LoggerFactory.getLogger(ContiPerfSampleTest.class);
	
	@Rule
	public ContiPerfRule ContiPerfRule	=	new ContiPerfRule(new HtmlReportModule(), 
																new CSVSummaryReportModule(), 
																new CSVInvocationReportModule(),
																new CSVLatencyReportModule());
	
	@PerfTest(invocations=10000)
	@Test
	public void testDoubleAdd() {
		//LOGGER.info("testDoubleAdd");
		//LOGGER.info("run test1");
		Double resultat	=	add(Double.parseDouble("123.34"), Double.parseDouble("126.66"));
		LOGGER.trace("resultat={}", resultat);
	}

	@PerfTest(invocations=10000)
	@Test
	public void testDoublePrimitiveAdd() {
		//LOGGER.info("testDoublePrimitiveAdd");
		Double resultat	=	add(123.34, 126.66);
		LOGGER.trace("resultat={}", resultat);
	}

	private Double add(Double double1, Double double2) {
		return double1 + double2;
	}
	
	private double add(double double1, double double2) {
		return double1 + double2;
	}

}
