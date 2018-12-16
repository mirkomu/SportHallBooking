package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Map;

import ch.he_arc.ig.techno.group4.sporthallbooking.firebase.Firebase;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class GestionActivity extends AppCompatActivity {

    // Affichage d'une notification rapide de type toast pour l'utilisateur :
    public void diplayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 225);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_gestion);

        //contiens les données de l'application
        final MyApplication mApp = new MyApplication();
        final Intent intent = getIntent();


    TableLayout table = (TableLayout) findViewById(R.id.idTable); // on prend le tableau défini dans le layout
    TableRow row; // création d'un élément : ligne
        TextView tv1, tv2, tv3; // création des cellules


        // pour chaque ligne
        for (Map.Entry<CalendarDay, String> dateBooked : mApp.bookedDays.entrySet()) {
            row = new TableRow(this); // création d'une nouvelle ligne
            tv1 = new TextView(this); // création cellule => date reservation
            //traitement du format des date (Firebase gère pas bien format de date)
            final int year = (dateBooked.getKey().getYear());
            final int MonthCorrected = (dateBooked.getKey().getMonth() + 1);//on ajoute + 1 pour commencé le mois a 1 et pas a 0
            final int day = (dateBooked.getKey().getDay());

            tv1.setText(day + "-" + MonthCorrected + "-" + year); // ajout du texte => day
            tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
            // adaptation de la largeur de colonne à l'écran :
            tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            // idem 2ème cellule => nom utilisateur
            tv2 = new TextView(this);
            tv2.setText(dateBooked.getValue());

            // idem 3ème cellule => lien pour supprimer
            tv3 = new TextView(this);
            if (dateBooked.getValue().equals(intent.getStringExtra(EXTRA_MESSAGE))) {
                tv3.setBottom(1);
                tv3.setText("supprime moi ");
                //TODO ajouter code pour suprimer entrée sur Firebase

                //ceci suprimme tout mes reservation (curent user)
                //   String strChosenDate = day + "-" + MonthCorrected + "-" + year;
                //   Firebase.delete(strChosenDate, intent.getStringExtra(EXTRA_MESSAGE), mApp, getApplicationContext() );
                //    final int id = i;
                final TextView dateCol = tv1;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // faites ici ce que vous voulez
                        String strChosenDate = day + "-" + MonthCorrected + "-" + year;
                        Firebase.delete(strChosenDate, intent.getStringExtra(EXTRA_MESSAGE), mApp, getApplicationContext());
                        //Toast.makeText(getApplicationContext(), String.valueOf(dateCol.getText()), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                tv3.setText("-");
            }

            tv2.setGravity(Gravity.CENTER);
            tv2.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            // ajout des cellules à la ligne
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);

            // ajout de la ligne au tableau
            table.addView(row);
        }
 }
}