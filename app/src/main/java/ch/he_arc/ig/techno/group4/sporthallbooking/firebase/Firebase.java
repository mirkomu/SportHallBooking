package ch.he_arc.ig.techno.group4.sporthallbooking.firebase;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Map;

import ch.he_arc.ig.techno.group4.sporthallbooking.MyApplication;

public class Firebase {

    public static Toast diplayToast (Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 225);
        toast.show();
        return toast;
    }

    public static void add(String strChosenDate, String namePerson, MyApplication mApp, Context contextActivity) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                diplayToast(contextActivity, "Réservation pour " + namePerson + " le " + strChosenDate + " effectuée");

            } else {
                diplayToast(contextActivity, "Erreur, il y a déjà une réservation pour " + alreadyExistsName + " le " + strChosenDate + ". Date non réservée.");
            }
        } catch (NumberFormatException e) {
            diplayToast(contextActivity, "Désolé, une erreur de date est survenue.");
        }
    }

    public static void delete(String strChosenDate, String namePerson, MyApplication mApp, Context contextActivity) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

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
            //on verifie si la personne qui supprime est la même que celle qui sera suprimé sur Firebase
            if (alreadyExistsName.equals(namePerson)) {
                myRef.removeValue();
                diplayToast(contextActivity, "Supression pour " + namePerson + " le " + strChosenDate + " effectuée");

            } else {
                diplayToast(contextActivity, "Erreur, il y a pas de  réservation pour " + alreadyExistsName + " le " + strChosenDate + " .");
            }
        } catch (NumberFormatException e) {
            diplayToast(contextActivity, "Désolé, une erreur de date est survenue.");
        }
    }
}


