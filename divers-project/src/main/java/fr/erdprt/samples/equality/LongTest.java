package fr.erdprt.samples.equality;

public class LongTest {

	public static void main(String[] args) {

		test(123456L, 123456L);
		test(Long.valueOf(123456), Long.valueOf(123456));
		test(new Long(123456), new Long(123456));

		test2(123456L, 123456L);
		test2(Long.valueOf(123456), Long.valueOf(123456));
		test2(new Long(123456), new Long(123456));

	}

	private static void test(final Long longValue1, final Long longValue2) {
		System.out.println(longValue1 + "=" + longValue2 + "?:" + (longValue1 == longValue2));
	}

	private static void test2(final Long longValue1, final Long longValue2) {
		System.out.println(longValue1 + "=" + longValue2 + "?:" + (longValue1.equals(longValue2)));
	}

}
