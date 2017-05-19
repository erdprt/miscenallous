package fr.erdprt.testng.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class SampleTest {

	private static final Logger LOG = LoggerFactory.getLogger(SampleTest.class);

	// @AfterMethod()
	public void after() {
		LOG.info("after");
	}

	@BeforeGroups("groupA")
	public void beforeGroupA() {
		LOG.info("before groupA");
	}

	@Test
	public void test1() {
		LOG.info("test1");
	}

	@Test(groups = "groupA")
	public void test2() {
		LOG.info("test2");
	}

	@Test
	public void test3() {
		LOG.info("test3");
	}

}
