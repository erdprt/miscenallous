package fr.erdprt.samples.loop;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LoopPersonPerfSetMain {

	public static void main(String[] args) {
		Integer count = 10;
		Integer tryCount = 100000;
		Integer modulo = 3;
		// String method = "DEFAULT";
		LoopPersonPerfSetMain instance = new LoopPersonPerfSetMain();

		// instance.runLoop(count, modulo, tryCount, "DEFAULT");
		instance.runLoop(count, modulo, tryCount, "BYINDEX");
		// instance.runLoop(count, modulo, tryCount, "DEFAULT");

	}

	public void runLoop(Integer count, Integer modulo, Integer tryCount, String method) {

		Long start = System.currentTimeMillis();

		Set<Person> liste = generateCollection(count, modulo);

		if ("BYINDEX".equalsIgnoreCase(method)) {
			runLoopByIterator(count, tryCount, liste);
		} else {
			runLoopDefault(count, tryCount, liste);
		}

		Long end = System.currentTimeMillis();

		String unit = "ms";
		Long duration = end - start;
		if (duration > 1000) {
			duration = duration / 1000;
			unit = "s";
		}

		System.out.println("method " + method + ", duration for count " + count + " = " + duration + " " + unit + " on " + tryCount + " try");

	}

	public void runLoopDefault(Integer count, Integer tryCount, Set<Person> collections) {

		for (Integer index0 = 0; index0 < tryCount; index0++) {
			for (Person currentValue : collections) {
				// DO ... NOTHING
				currentValue.getFirstName();
			}
		}
	}

	public void runLoopByIterator(Integer count, Integer tryCount, Set<Person> collections) {

		for (Integer index0 = 0; index0 < tryCount; index0++) {

			for (Iterator<Person> iterator = collections.iterator(); iterator.hasNext();) {
				// DO ... NOTHING
				iterator.next().getFirstName();
			}
		}
	}

	private Set<Person> generateCollection(Integer count, Integer modulo) {
		Set<Person> persons = new HashSet<Person>();

		for (Integer index = 1; index < count; index++) {
			persons.add(new Person(index, "firstName " + (index % modulo), "lasName " + (index % modulo)));
		}
		return persons;
	}
}
