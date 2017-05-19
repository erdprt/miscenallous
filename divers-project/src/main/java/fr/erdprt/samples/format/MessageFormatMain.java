package fr.erdprt.samples.format;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.text.StrSubstitutor;

public class MessageFormatMain {

	public static void main(String[] args) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("parameter");
		String message = resourceBundle.getString("req.reader.sql.idtech");
		System.out.println("message = " + message);

		String minValue = "\'100\'";
		String maxValue = "\'200\'";
		message = MessageFormat.format(message, minValue, maxValue);
		System.out.println("message = " + message);

		Map<String, String> valuesMap = new HashMap<String, String>();
		valuesMap.put("name", "Dupont");
		valuesMap.put("email", "jean.dupont@gmail.com");
		String templateString = resourceBundle.getString("message.sample1");
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		System.out.println("resolvedString = " + resolvedString);

		templateString = resourceBundle.getString("message.sample2");
		sub = new StrSubstitutor(valuesMap, "#{", "}");
		resolvedString = sub.replace(templateString);
		System.out.println("resolvedString = " + resolvedString);

		templateString = resourceBundle.getString("message.sample3");
		sub = new StrSubstitutor(valuesMap, "#", "#");
		resolvedString = sub.replace(templateString);
		System.out.println("resolvedString = " + resolvedString);

	}

}
