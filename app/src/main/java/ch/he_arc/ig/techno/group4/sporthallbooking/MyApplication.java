package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.app.Application;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;
import java.util.HashMap;

public class MyApplication extends Application implements Serializable {

    public static HashMap<CalendarDay, String> bookedDays = new HashMap<>();
    public static String userName;

}
