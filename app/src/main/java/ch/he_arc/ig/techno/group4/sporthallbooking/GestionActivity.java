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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class GestionActivity extends AppCompatActivity {


    //methode => utils
    // Affichage d'une notification rapide de type toast pour l'utilisateur :
    public void diplayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 225);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_gestion);



        //ajoute du nom
        MyApplication mApp = new MyApplication();
        //     final EditText nameEdit = findViewById(R.id.editTextNameActivity2);

        //  if (!mApp.bookedDays.isEmpty()) {
        //      nameEdit.setText(mApp.bookedDays.get(1));
        diplayToast("ok 1");
        Intent intent = getIntent();
        if (intent != null) {
            diplayToast(intent.getStringExtra(EXTRA_MESSAGE));
        }
        //marche évidamment aussi
        //diplayToast(mApp.userName);

        for (Map.Entry<CalendarDay, String> dateBooked : mApp.bookedDays.entrySet()) {
            diplayToast(dateBooked.getKey().toString());
            diplayToast("ok 2");

            //   nameEdit.setText(dateBooked.getKey().toString());


        }

        diplayToast("ok 3");


/*
            diplayToast(mApp.bookedDays.get(0));
        diplayToast(mApp.bookedDays.get(1));
        diplayToast(mApp.bookedDays.get(2));
        */

        //   }

//editTextName


        ///////////////////////////////////////////////////////////////////////////////////////////


    // données du tableau
    final String[] col1 = {"col1:ligne1", "col1:ligne2", "col1:ligne3", "col1:ligne4", "col1:ligne5"};
    final String[] col2 = {"col2:ligne1", "col2:ligne2", "col2:ligne3", "col2:ligne4", "col2:ligne5"};

    TableLayout table = (TableLayout) findViewById(R.id.idTable); // on prend le tableau défini dans le layout
    TableRow row; // création d'un élément : ligne
        TextView tv1, tv2, tv3; // création des cellules

// pour chaque ligne

        // for(int i=0;i<col1.length;i++) {

        for (Map.Entry<CalendarDay, String> dateBooked : mApp.bookedDays.entrySet()) {
            //         diplayToast(dateBooked.getKey().toString());
            //         diplayToast("ok 2");

            row = new TableRow(this); // création d'une nouvelle ligne
            tv1 = new TextView(this); // création cellule => date reservation
            //traitement du format des date (Firebase gère pas notre format de date)
            int year = (dateBooked.getKey().getYear());
            int MonthCorrected = (dateBooked.getKey().getMonth() + 1);//on ajoute + 1 pour commencé le mois a 1 et pas a 0
            int day = (dateBooked.getKey().getDay());


            tv1.setText(day + "-" + MonthCorrected + "-" + year); // ajout du texte => date
            tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
            // adaptation de la largeur de colonne à l'écran :
            tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            // idem 2ème cellule => nom utilisateur
            tv2 = new TextView(this);
            tv2.setText(dateBooked.getValue().toString());

            // idem 3ème cellule => lien pour supprimer
            tv3 = new TextView(this);
            if (dateBooked.getValue().toString().equals(intent.getStringExtra(EXTRA_MESSAGE))) {
                tv3.setText("supprime moi ");
                //TODO ajouter code pour suprimer entrée sur Firebase
            } else {
                tv3.setText("-");
            }

            final int id = dateBooked.getValue().length();
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // faites ici ce que vous voulez
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    //  Integer j = id;
                    Toast.makeText(context, String.valueOf(id), duration).show();

                    diplayToast("clique ok");                 }
            });
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