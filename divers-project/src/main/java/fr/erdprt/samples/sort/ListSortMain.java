package fr.erdprt.samples.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ListSortMain {

	public static void main(String[] args) {
		ListSortMain instance 	=	new ListSortMain();
		List<Long> listValues	=	instance.fillList(2000000);
		instance.sortList(listValues);

	}
	
	private List<Long> fillList(Integer count) {
		Set<Long> setValues	=	new HashSet<Long>();
		
		Random random	=	new Random();
		for (Integer index=0;index< count; index++) {
			Long longValue	=	Long.valueOf(random.nextInt(count));
			//System.out.println("longValue=" + longValue);
			setValues.add(longValue);
		}
		return new ArrayList<Long>(setValues);
	}
	
	private void sortList(List<Long> listValues) {
		Long start	=	System.currentTimeMillis();
		Collections.sort(listValues);
		Long end	=	System.currentTimeMillis();
		System.out.println("duration for " + listValues.size() +" items :" + (end -start) + " ms");
		//System.out.println("lst=" + listValues);
	}

}
