package fr.dsirc.dsn.utils;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumericUtilsTest {
	
	private static final Logger LOGGER	=	LoggerFactory.getLogger(NumericUtilsTest.class);

	@Rule
	public ContiPerfRule ContiPerfRule	=	new ContiPerfRule();

	@PerfTest(invocations=50000, threads=20)
	@Test
	public void cumulerDouble() {
		Double resultat	=	NumericUtils.cumuler(Double.parseDouble("123.34"), Double.parseDouble("126.66"),Double.parseDouble("100.50"));
		//Assert.assertEquals(Double.valueOf(350.5), resultat);
	}

	@PerfTest(invocations=50000, threads=20)
	@Test
	public void cumulerPrimitive() {
		double resultat	=	NumericUtils.cumulerPrimitive(123.34, 126.66, 100.50);
		//LOGGER.debug("resultat={}", resultat);
		//Assert.assertEquals(Double.valueOf(350.5), Double.valueOf(resultat));
	}

}
