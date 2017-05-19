package fr.erdprt.samples.misc;

public class MiscellaneousMain {

	public static void main(String[] args) {

		test(2);
		test(3);
		test(4);
		test(10);
		test(15);
		test(20);
	}

	private static void test(final int oldCapacity) {
		int newCapacity = (oldCapacity >> 1);
		System.out.println("oldCapacity=" + oldCapacity + " newCapacity=" + newCapacity);

	}
}
