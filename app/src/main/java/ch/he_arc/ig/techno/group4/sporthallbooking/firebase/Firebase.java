package ch.he_arc.ig.techno.group4.sporthallbooking.firebase;

import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Map;

import ch.he_arc.ig.techno.group4.sporthallbooking.MyApplication;

public class Firebase {


    public static void save(String strChosenDate, String namePerson, MyApplication mApp) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //FirebaseDatabase.getInstance().getReference();

        try {
            DatabaseReference myRef;
            myRef = database.getReference(strChosenDate);
            String alreadyExistsName = "";
            for (Map.Entry<CalendarDay, String> dateBooked : mApp.bookedDays.entrySet()) {
                int MonthCorrected = (dateBooked.getKey().getMonth() + 1);
                String strDateBooked = dateBooked.getKey().getDay() + "-" + MonthCorrected + "-" + dateBooked.getKey().getYear();
                if (strChosenDate.equals(strDateBooked)) {
                    alreadyExistsName = dateBooked.getValue();
                    break;
                }
            }
            if (alreadyExistsName.equals("")) {
                myRef.setValue(namePerson);
                Toast.makeText(mApp, "Réservation pour " + namePerson + " le " + strChosenDate + " effectuée", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(mApp, "Erreur, il y a déjà une réservation pour " + alreadyExistsName + " le " + strChosenDate + ". Date non réservée.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(mApp, "Désolé, une erreur de date est survenue.", Toast.LENGTH_SHORT).show();
        }
    }
}


