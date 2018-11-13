package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MaterialCalendarView agenda = (MaterialCalendarView)findViewById(R.id.calendarView);
        agenda.setDateSelected(new Date(), true);
        final Button button = findViewById(R.id.btnReserve);
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        final ArrayList<CalendarDay> bookedDays = new ArrayList<>();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get dates and use the values to update the UI
                ArrayList<CalendarDay> InitialBookedDays = new ArrayList<>();
                int day = 1, month = 1, year = 2000;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object sd = snapshot.getValue();
                    String[] partsDate = sd.toString().split(Pattern.quote("."));
                    // TODO : Gérer les exceptions :
                    day = Integer.parseInt(partsDate[0]);
                    month = Integer.parseInt(partsDate[1]) - 1;
                    year = Integer.parseInt(partsDate[2]);
                    InitialBookedDays.add(CalendarDay.from(year, month, day));
                    agenda.addDecorator(new EventDecorator(Color.RED, InitialBookedDays));
                }
                bookedDays.addAll(InitialBookedDays);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting dates failed, log a message
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nameEdit = (EditText)findViewById(R.id.editTextName);
                String namePerson = nameEdit.getText().toString();
                MaterialCalendarView cal = (MaterialCalendarView)findViewById(R.id.calendarView);
                String strChosenDate =  cal.getSelectedDate().getDay() + "." + cal.getSelectedDate().getMonth() + "." +
                        cal.getSelectedDate().getYear();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(namePerson);
                String[] partsDate = strChosenDate.split(Pattern.quote("."));
                // TODO : Gérer les exceptions :
                int day = 1, month = 1, year = 2000;
                day = Integer.parseInt(partsDate[0]);
                month = Integer.parseInt(partsDate[1]) - 1;
                year = Integer.parseInt(partsDate[2]);
                if(!bookedDays.contains(CalendarDay.from(year, month, day))) {
                    myRef.setValue(strChosenDate);
                    Toast.makeText(getApplicationContext(), "Réservation pour " + namePerson + " le " + strChosenDate, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erreur, il y a déjà une réservation pour " + myRef.toString() + " le " + strChosenDate + ". Date non réservée.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get dates and use the values to update the UI
                ArrayList<CalendarDay> updatedBookedDays = new ArrayList<>();
                int day = 1, month = 1, year = 2000;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object sd = snapshot.getValue();
                    String[] partsDate = sd.toString().split(Pattern.quote("."));
                    // TODO : Gérer les exceptions :
                    day = Integer.parseInt(partsDate[0]);
                    month = Integer.parseInt(partsDate[1]) - 1;
                    year = Integer.parseInt(partsDate[2]);
                    updatedBookedDays.add(CalendarDay.from(year, month, day));
                    agenda.addDecorator(new EventDecorator(Color.RED, updatedBookedDays));
                    bookedDays.add(CalendarDay.from(year, month, day));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting dates failed, log a message
            }
        };

        db.addValueEventListener(postListener);
    }
}
