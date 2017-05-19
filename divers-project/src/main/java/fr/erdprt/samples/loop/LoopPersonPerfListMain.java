package fr.erdprt.samples.loop;

import java.util.ArrayList;
import java.util.List;

public class LoopPersonPerfListMain {

	public static void main(String[] args) throws Exception {
		Integer count = 10;
		Integer tryCount = 10000;
		Integer modulo = 3;
		String method = "BYINDEX";
		// String method = "DEFAULT";
		LoopPersonPerfListMain instance = new LoopPersonPerfListMain();

		instance.runLoop(count, modulo, tryCount, "BYINDEX");
		Thread.sleep(2000);
		instance.runLoop(count, modulo, tryCount, "DEFAULT");
		Thread.sleep(2000);
		instance.runLoop(count, modulo, tryCount, "BYINDEX");

	}

	public void runLoop(Integer count, Integer modulo, Integer tryCount, String method) {

		Long start = 0L;
		Long end = 0L;

		Long duration = 0L;

		List<Person> liste = generateList(count, modulo);

		if ("BYINDEX".equalsIgnoreCase(method)) {
			start = System.currentTimeMillis();
			runLoopByIndex(count, tryCount, liste);
			end = System.currentTimeMillis();
			duration += (end - start);
		} else {
			start = System.currentTimeMillis();
			runLoopDefault(count, tryCount, liste);
			end = System.currentTimeMillis();
			duration += (end - start);
		}

		String unit = "ms";
		if (duration > 1000) {
			duration = duration / 1000;
			unit = "s";
		}

		System.out.println("method " + method + ", duration for count " + count + " = " + duration + " " + unit + " on " + tryCount + " try");
		liste.clear();

	}

	public void runLoopDefault(Integer count, Integer tryCount, List<Person> liste) {

		for (Integer index0 = 0; index0 < tryCount; index0++) {
			for (Person currentValue : liste) {
				// DO ... NOTHING
				currentValue.toString();
			}
		}
	}

	public void runLoopByIndex(Integer count, Integer tryCount, List<Person> liste) {

		for (Integer index0 = 0; index0 < tryCount; index0++) {

			for (Integer index = 0; index < liste.size(); index++) {
				// DO ... NOTHING
				liste.get(index).toString();
			}
		}
	}

	private List<Person> generateList(Integer count, Integer modulo) {
		List<Person> persons = new ArrayList<Person>();

		for (Integer index = 1; index < count; index++) {
			persons.add(new Person(index, "firstName " + (index % modulo), "lasName " + (index % modulo)));
		}
		return persons;
	}
}
