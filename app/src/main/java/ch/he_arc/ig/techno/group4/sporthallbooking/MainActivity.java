package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import ch.he_arc.ig.techno.group4.sporthallbooking.firebase.Firebase;
import ch.he_arc.ig.techno.group4.sporthallbooking.persistance.DBOpenHelper;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    MyApplication mApp = new MyApplication();

    // Variables et éléments UI utilisés dans cette activité
    MaterialCalendarView agenda;
    Button button;
    Button buttonGestionReservation;
    DatabaseReference db;
    ValueEventListener postListener;
    // The database
    private SQLiteDatabase dbLocal;
    // The database creator and updater helper
    DBOpenHelper dbLocalOpenHelper;


    /*********************************************************************************/
    /** Managing LifeCycle and database open/close operations ************************/
    /*********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        openLocalDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeLocalDB();
    }

    /**
     * * Open the database* *
     *
     * @throws SQLiteException
     */
    public void openLocalDB() throws SQLiteException {
        try {
            dbLocal = dbLocalOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            dbLocal = dbLocalOpenHelper.getReadableDatabase();
        }
    }

    /**
     * Close Database
     */
    public void closeLocalDB() {
        dbLocal.close();
    }

    /*********************************************************************************/
    /** Methode  personalisé   *******************************************************/
    /*********************************************************************************/
    // Affichage d'une notification rapide de type toast pour l'utilisateur :
    public void diplayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 225);
        toast.show();
    }

    /*********************************************************************************/


    // Evénement : Quand l'application s'ouverte; on initialise les éléments
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        agenda = findViewById(R.id.calendarView);
        agenda.setDateSelected(new Date(), true);
        button = findViewById(R.id.btnReserve);
        db = FirebaseDatabase.getInstance().getReference();
        mApp.bookedDays = new HashMap<>();

        buttonGestionReservation = findViewById(R.id.btnGestReservation);

        //Initalisation de la base de donnée local (SQLITE)
        dbLocalOpenHelper = new DBOpenHelper(this, DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);
        // open the database
        openLocalDB();

        final EditText nameEdit = findViewById(R.id.editTextNameActivity2);

        //recherche des données dans la base local si un nom est trouvé on l'insert dans le champs nom de l'aplication

        //attention le premier enregistrement commence à l'index 1 dans SQLITE
        //on recupère le dernier enregistrement de la bd local
        //TODO le select ne fonctionne pas correctement retoune toujours 1
        SQLiteStatement s = dbLocal.compileStatement("select count(*) " + DBOpenHelper.Constants.MY_TABLE);
        String id = s.simpleQueryForString();

        //diplayToast("id bd : " + id);
        Cursor cursor = dbLocal.rawQuery("select " + DBOpenHelper.Constants.KEY_COL_NAME + " from " + DBOpenHelper.Constants.MY_TABLE + " where id = ?", new String[]{id});
        //TODO remplacer les displayToast par des log
        try {
            if (cursor.moveToFirst()) {
                mApp.userName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.Constants.KEY_COL_NAME));
                //diplayToast("user in the local db " + mApp.userName);
                if (!mApp.userName.isEmpty()) {
                    nameEdit.setText(mApp.userName);
                }
            } else {
                // diplayToast("curseur vide");
            }
        } catch (Exception ex) {
            diplayToast("erreur lecture db local");
        }


        // Evénement : Quand l'application est initalisé; on lis les valeurs de Firebase une première fois
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get dates and use the values to update the UI
                HashMap<CalendarDay, String> InitialBookedDays = new HashMap<>();
                int day, month, year;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        String[] partsDate = snapshot.getKey().split(Pattern.quote("-"));
                        day = Integer.parseInt(partsDate[0]);
                        month = Integer.parseInt(partsDate[1]) - 1;
                        year = Integer.parseInt(partsDate[2]);
                        InitialBookedDays.put(CalendarDay.from(year, month, day), snapshot.getValue().toString());
                        agenda.addDecorator(new EventDecorator(Color.RED, InitialBookedDays.keySet()));
                    } catch (Exception e) {
                        // Elements vides dans la BDD ? -> Pas besoin d'afficher d'erreur
                    }
                }
                mApp.bookedDays.putAll(InitialBookedDays);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                diplayToast("Erreur de mise à jour de la base de donnée.");
            }
        });

        // Evénement : Quand le bouton de réservation est cliqué; on réserve la date
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                MaterialCalendarView cal = (MaterialCalendarView) findViewById(R.id.calendarView);


                if(nameEdit.getText().length() == 0) {
                    diplayToast("Veuillez indiquer votre nom.");
                } else if (cal.getSelectedDate().getDate().compareTo(new Date()) < 0) {
                    diplayToast("La date doit être dans le futur.");
                } else {
                    String namePerson = nameEdit.getText().toString();
                    //Save the user name in the local database
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBOpenHelper.Constants.KEY_COL_NAME, namePerson);
                    // Insert the line in the database
                    long rowId = dbLocal.insert(DBOpenHelper.Constants.MY_TABLE, null, contentValues);

                    //TODO Remplacer par des logs
                    // Test to see if the insertion was ok
                    if (rowId == -1) {
                        diplayToast("Error when creating the User " + rowId);
                    } else {
                        //diplayToast("User " + namePerson + " created and stored in local database, rowId: " + rowId);
                    }


                    int correctedMonth = cal.getSelectedDate().getMonth() + 1;
                    String strChosenDate = cal.getSelectedDate().getDay() + "-" + correctedMonth + "-" +
                            cal.getSelectedDate().getYear();

                    Firebase.add(strChosenDate, namePerson, mApp, getApplicationContext());
                }
            }
        });

        // Evénement : Quand une donnée est mise à jour dans FireBase; on met à jour notre UI
        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<CalendarDay> updatedBookedDays = new ArrayList<>();
                int day, month, year;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String[] partsDate = snapshot.getKey().split(Pattern.quote("-"));
                    try {
                        day = Integer.parseInt(partsDate[0]);
                        month = Integer.parseInt(partsDate[1]) - 1;
                        year = Integer.parseInt(partsDate[2]);
                        updatedBookedDays.add(CalendarDay.from(year, month, day));
                        agenda.addDecorator(new EventDecorator(Color.RED, updatedBookedDays));
                        mApp.bookedDays.put(CalendarDay.from(year, month, day), snapshot.getValue().toString());
                    } catch (NumberFormatException e) {
                        diplayToast("Désolé, une erreur de date est survenue.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                diplayToast("Erreur de mise à jour de la base de donnée  (Firebase).");

            }
        };
        db.addValueEventListener(dataListener);

        agenda.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                for (Map.Entry<CalendarDay, String> dateBooked : mApp.bookedDays.entrySet()) {
                    //int MonthCorrected = (dateBooked.getKey().getMonth() + 1);
                    //String strDateBooked = dateBooked.getKey().getDay() + "-" + MonthCorrected + "-" + dateBooked.getKey().getYear();
                    if (date.getDate().compareTo(dateBooked.getKey().getDate()) == 0) {
                        diplayToast(dateBooked.getValue() + " a déjà réservé le " + dateBooked.getKey().getDate().toString());
                        break;
                    }
                }
            }
        });


        //button gestionReservation (GestionActivity

        // Evénement : Quand le bouton gèrer les réservations est cliqué; ouvre l'activity "GestionActivity
        buttonGestionReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GestionActivity.class);
                String message = MyApplication.userName;
                intent.putExtra(EXTRA_MESSAGE, message); //cette ligne est essanciel même si on utilise directement les objets de la classe MyApplication
                // intent.putExtra("KEY", MyApplication.bookedDays); startActivity(intent);

                startActivity(intent);
            }
        });
    }


}
