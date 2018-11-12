package ch.he_arc.ig.techno.group4.sporthallbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                Toast.makeText(getApplicationContext(), "Réservation pour " + namePerson + " le " + cal.getSelectedDate().getDay() + "." + cal.getSelectedDate().getMonth() + "." +
                        cal.getSelectedDate().getYear(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
