package us.chary.pulsenotify;

import android.content.Context;

import java.util.List;

public class CalendarHelper {

    public long getTimeBetweenNowAndNextEvent(Context context){
        CalendarUtil.readCalendarEvent(context);
        long nextEvent = getNextEvent();
        return nextEvent - System.currentTimeMillis();
    }

    private long getNextEvent(){
        List<Long> dates = CalendarUtil.startDates;
        long longestDate = 0;

        for (Long date: dates){
            if(date <= longestDate){
                longestDate = date;
            }
        }

        return longestDate;
    }
}