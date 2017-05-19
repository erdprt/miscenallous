package fr.erdprt.samples.loop;

import java.util.ArrayList;
import java.util.List;

public class LoopPerfMain {

    public static void main(String[] args) {
        Integer count		=	100;
        Integer tryCount	=	20000;
        String method		=	"BYINDEX";
        //String method		=	"DEFAULT";
        LoopPerfMain instance	=	new LoopPerfMain();

        instance.runLoop(count, tryCount, method);
    }

    public long runLoop(Integer count, Integer tryCount, String method) {

        //List<String> liste	=	createList(count);
        List<String> liste	=	null;
        List<Long> durations	=	new ArrayList<Long>();
        List<Long> memories	=	new ArrayList<Long>();

        for (Integer index=0;index< tryCount;index++) {
            Long start	=		System.nanoTime();
            Long starMemory=	Runtime.getRuntime().freeMemory();

            if ("BYINDEX".equals(method)) {
                runLoopDefault(count, liste);
            } else if ("DEFAULT".equals(method)) {
                runLoopByIndex(count, liste);
            }
            Long end	=	System.nanoTime();
            durations.add((end -start));
            Long endMemory=	Runtime.getRuntime().freeMemory();
            durations.add((end -start));
            memories.add((starMemory - endMemory));
        }
        Long finalDuration	=	0L;
        for (Long durationCurrent:durations) {
            finalDuration += durationCurrent;
        }
        finalDuration	=	finalDuration/ durations.size();
        System.out.println("method " + method + ", duration for count=" + count + "=" + finalDuration + " nano sec on " + tryCount + " try");

        Long finalMemory	=	0L;
        for (Long memoryCurrent:memories) {
            finalMemory += memoryCurrent;
        }
        finalMemory	=	(finalMemory/ memories.size()/ (1024L));
        System.out.println("method " + method + ", memory for count=" + count + "=" + finalMemory + " Ko on " + tryCount + " try");

        if (liste!=null && liste.size()> 0) {
        	liste.clear();
        }
        return finalDuration;
    }

    public void runLoopDefault(Integer count, List<String> liste) {

        boolean clear	=	false;
        if (liste==null) {
            liste	=	createList(count);
            clear	=	true;
        }
        for (String currentValue: liste) {
            // DO ... NOTHING
            currentValue.toString();
        }
        if (clear) {
            //liste.clear();
        }

    }

    public void runLoopByIndex(Integer count, List<String> liste) {

        boolean clear	=	false;
        if (liste==null) {
            liste	=	createList(count);
            clear	=	true;
        }

        for (Integer index=0;index<count;index++) {
            // DO ... NOTHING
            liste.get(index).toString();
        }
        if (clear) {
           //liste.clear();
        }

    }

    private List<String> createList(Integer count) {
        List<String> list	=	new ArrayList<String>();

        for (Integer index=0;index< count; index++) {
            list.add("value" + index);
        }

        return list;
    }

}
