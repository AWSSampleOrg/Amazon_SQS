package com.example.demo;


import java.util.Date;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.text.SimpleDateFormat;


public class Logger {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String utcNow(){
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime local = now.withZoneSameLocal(ZoneId.systemDefault());
        return formatter.format(Date.from(local.toInstant()));
    }
   
    private static String getStackTrace(Exception e){
        StackTraceElement[] list = e.getStackTrace();
        StringBuilder b = new StringBuilder();
        for(StackTraceElement s : list){
            b.append(s.toString()).append("\t\n");
        }
        return b.toString();
    }

    public static void println(Exception e){
        synchronized(Logger.class){
            String threadName = Thread.currentThread().getName();
            System.out.println("[" + threadName + "]" + utcNow() + " " + getStackTrace(e));
        }
    }

    public static void println(String message){
        synchronized(Logger.class){
            String threadName = Thread.currentThread().getName();
            System.out.println("[" + threadName + "]" + utcNow() + " " + message);
        }
    }
}
