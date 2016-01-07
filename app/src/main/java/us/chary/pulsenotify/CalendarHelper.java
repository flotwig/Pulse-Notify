package us.chary.pulsenotify;

import android.content.Context;

public class CalendarHelper {

    public long getTimeBetweenNowAndNextEvent(Context context){
        CalendarUtil.readCalendarEvent(context);
        long nextEvent = getNextEvent();
        return nextEvent - System.currentTimeMillis();
    }

    private long getNextEvent(){
        return CalendarUtil.startDates.get(0);
    }
}