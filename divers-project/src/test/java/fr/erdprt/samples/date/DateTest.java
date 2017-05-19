package fr.erdprt.samples.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTest {

	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.FRENCH);
		Date date = Calendar.getInstance().getTime();
		date.setMonth(9);

		System.out.println(dateFormat.format(date));

	}

}
