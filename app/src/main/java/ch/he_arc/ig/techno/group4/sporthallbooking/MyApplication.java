package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.app.Application;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashMap;

public class MyApplication extends Application {

    public HashMap<CalendarDay, String> bookedDays = new HashMap<>();
    public String userName;

}
