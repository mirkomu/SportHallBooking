package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.activity_gestion);

        ///////////////////////////////////////////////////////////////////////////////////////////



    // données du tableau
    final String[] col1 = {"col1:ligne1", "col1:ligne2", "col1:ligne3", "col1:ligne4", "col1:ligne5"};
    final String[] col2 = {"col2:ligne1", "col2:ligne2", "col2:ligne3", "col2:ligne4", "col2:ligne5"};

    TableLayout table = (TableLayout) findViewById(R.id.idTable); // on prend le tableau défini dans le layout
    TableRow row; // création d'un élément : ligne
    TextView tv1, tv2; // création des cellules

// pour chaque ligne

         for(int i=0;i<col1.length;i++) {


             row = new

                     TableRow(this); // création d'une nouvelle ligne

             tv1 = new

                     TextView(this); // création cellule
             tv1.setText(col1[i]); // ajout du texte
             tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
             // adaptation de la largeur de colonne à l'écran :
             tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

             // idem 2ème cellule
             tv2 = new

                     TextView(this);
             tv2.setText(col2[i]);
             //supression ligne
             final int id=i;
             row.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     // faites ici ce que vous voulez

                     diplayToast("clique ok");                 }
             });
             tv2.setGravity(Gravity.CENTER);
             tv2.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

             // ajout des cellules à la ligne
             row.addView(tv1);
             row.addView(tv2);

             // ajout de la ligne au tableau
             table.addView(row);


         }

 }
}