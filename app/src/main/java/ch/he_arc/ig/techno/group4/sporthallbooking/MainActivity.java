package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialCalendarView agenda = (MaterialCalendarView)findViewById(R.id.calendarView);
        agenda.setDateSelected(new Date(), true);

        final Button button = findViewById(R.id.btnReserve);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nameEdit = (EditText)findViewById(R.id.editTextName);
                String namePerson = nameEdit.getText().toString();
                MaterialCalendarView cal = (MaterialCalendarView)findViewById(R.id.calendarView);
                String strChosenDate =  cal.getSelectedDate().getDay() + "." + cal.getSelectedDate().getMonth() + "." +
                        cal.getSelectedDate().getYear();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(namePerson);
                myRef.setValue(strChosenDate);
                Toast.makeText(getApplicationContext(), "Réservation pour " + namePerson + " le " + strChosenDate, Toast.LENGTH_LONG).show();

            }
        });
    }
}
