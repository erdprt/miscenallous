package fr.erdprt.samples.atomicinteger;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerMain {

	public static void main(String[] args) {
		System.out.println("start");
		AtomicInteger atomicInteger	=	new AtomicInteger();
		System.out.println("value=" + atomicInteger.get());
		atomicInteger.addAndGet(2);
		System.out.println("value=" + atomicInteger.get());
		atomicInteger.addAndGet(12);
		System.out.println("value=" + atomicInteger.get());

	}

}
