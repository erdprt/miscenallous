package fr.erdprt.samples.biglist;

import java.util.ArrayList;
import java.util.List;

public class BigListMain {

	public static void main(String[] args) {
		System.out.println("starting...");
		Long start	=	Runtime.getRuntime().freeMemory();
		int	count	=	10000000;
		int indexStart	=	100000;
		List<Long> listLong	=	new ArrayList<Long>();
		for (int index=indexStart;index< (count + indexStart);index++) {
			listLong.add(Long.valueOf(index));
			if (index %100000 == 0) {
				System.out.println("index =" + index);
			}
			
		}
		Long end	=	Runtime.getRuntime().freeMemory();
		System.out.println("memory consumed=" + (start-end)/(1064*1064) + " Mo for "+ listLong.size() + " items");
	}

}
