package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Variables et éléments UI utilisés dans cette activité
    MaterialCalendarView agenda;
    Button button;
    DatabaseReference db;
    HashMap<CalendarDay, String> bookedDays;
    ValueEventListener postListener;

    // Evénement : Quand l'application s'ouverte; on initialise les éléments
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        agenda = (MaterialCalendarView)findViewById(R.id.calendarView);
        agenda.setDateSelected(new Date(), true);
        button = findViewById(R.id.btnReserve);
        db = FirebaseDatabase.getInstance().getReference();
        bookedDays  = new HashMap<CalendarDay, String>();

        // Evénement : Quand l'application est intialisé; on lis les valeurs de Firebase une première fois
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get dates and use the values to update the UI
                HashMap<CalendarDay, String> InitialBookedDays = new HashMap<CalendarDay, String>();
                int day = 1, month = 1, year = 2000;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        String[] partsDate = snapshot.getKey().toString().split(Pattern.quote("-"));
                        day = Integer.parseInt(partsDate[0]);
                        month = Integer.parseInt(partsDate[1]) - 1;
                        year = Integer.parseInt(partsDate[2]);
                        InitialBookedDays.put(CalendarDay.from(year, month, day), snapshot.getValue().toString());
                        agenda.addDecorator(new EventDecorator(Color.RED, InitialBookedDays.keySet()));
                    } catch (Exception e) {
                        // Elements vides dans la BDD ? -> Pas besoin d'afficher d'erreur
                    }
                }
                bookedDays.putAll(InitialBookedDays);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                diplayToast("Erreur de mise à jour de la base de donnée.");
            }
        });

        // Evénement : Quand le bouton de réservation est cliqué; on réserve la date
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nameEdit = (EditText)findViewById(R.id.editTextName);
                if(nameEdit.getText().length() == 0) {
                    diplayToast("Veuillez indiquer votre nom.");
                } else {
                    String namePerson = nameEdit.getText().toString();
                    MaterialCalendarView cal = (MaterialCalendarView) findViewById(R.id.calendarView);
                    int correctedMonth = cal.getSelectedDate().getMonth() + 1;
                    String strChosenDate = cal.getSelectedDate().getDay() + "-" + correctedMonth + "-" +
                            cal.getSelectedDate().getYear();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String[] partsDate = strChosenDate.split(Pattern.quote("-"));
                    try {
                        int day = 1, month = 1, year = 2000;
                        day = Integer.parseInt(partsDate[0]);
                        year = Integer.parseInt(partsDate[2]);
                        DatabaseReference myRef = database.getReference(strChosenDate);
                        String alreadyExistsName = "";
                        for (Map.Entry<CalendarDay, String> dateBooked : bookedDays.entrySet()) {
                            int MonthCorrected = (dateBooked.getKey().getMonth() + 1);
                            String strDateBooked = dateBooked.getKey().getDay() + "-" + MonthCorrected + "-" + dateBooked.getKey().getYear();
                            if (strChosenDate.equals(strDateBooked)) {
                                alreadyExistsName = dateBooked.getValue();
                                break;
                            }
                        }
                        if (alreadyExistsName == "") {
                            myRef.setValue(namePerson);
                            diplayToast("Réservation pour " + namePerson + " le " + strChosenDate + " effectuée");
                        } else {
                            diplayToast("Erreur, il y a déjà une réservation pour " + alreadyExistsName + " le " + strChosenDate + ". Date non réservée.");
                        }
                    } catch (NumberFormatException e) {
                        diplayToast("Désolé, une erreur de date est survenue.");
                    }
                }
            }
        });

        // Evénement : Quand une donnée est mise à jour dans FireBase; on met à jour notre UI
        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<CalendarDay> updatedBookedDays = new ArrayList<>();
                int day = 1, month = 1, year = 2000;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String[] partsDate = snapshot.getKey().toString().split(Pattern.quote("-"));
                    try {
                        day = Integer.parseInt(partsDate[0]);
                        month = Integer.parseInt(partsDate[1]) - 1;
                        year = Integer.parseInt(partsDate[2]);
                        updatedBookedDays.add(CalendarDay.from(year, month, day));
                        agenda.addDecorator(new EventDecorator(Color.RED, updatedBookedDays));
                        bookedDays.put(CalendarDay.from(year, month, day), snapshot.getValue().toString());
                    } catch (NumberFormatException e) {
                        diplayToast("Désolé, une erreur de date est survenue.");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                diplayToast("Erreur de mise à jour de la base de donnée.");
            }
        };
        db.addValueEventListener(dataListener);
    }

    // Affichage d'une notification rapide de type toast pour l'utilisateur :
    public void diplayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 50);
        toast.show();
    }
}
