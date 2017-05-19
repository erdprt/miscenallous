package fr.erdprt.samples.locale;

import java.util.Locale;

public class LocaleMain {

	public static void main(String[] args) {
		String value	=	"mygod";
		System.out.println(value.toUpperCase(Locale.FRANCE));
		System.out.println(value.toUpperCase(Locale.FRENCH));

	}

}
