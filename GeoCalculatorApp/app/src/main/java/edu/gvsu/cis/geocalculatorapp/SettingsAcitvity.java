package edu.gvsu.cis.geocalculatorapp;

/**
 * Settings activity screen for geo calculator app.
 *
 * @author Edric Lin
 * @author Dimitri Haring
 * @version 5/22/18
 *
 */

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsAcitvity extends AppCompatActivity {

    private String distanceUnitsSelection = "";
    private String bearingUnitsSelection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_acitvity);

        FloatingActionButton confirmSettingsButton = findViewById(R.id.confirmSettingsButton);
        confirmSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("distanceUnits", distanceUnitsSelection);
                intent.putExtra("bearingUnits", bearingUnitsSelection);

                setResult(MainActivity.UNITS_SELECTION, intent);

                finish();
            }
        });

        // set up distance units picker
        Spinner distanceUnitsPicker = findViewById(R.id.distanceUnitsPicker);

        ArrayAdapter<CharSequence> distanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.distanceUnitsList, android.R.layout.simple_spinner_item);

        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceUnitsPicker.setAdapter(distanceAdapter);
        distanceUnitsPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distanceUnitsSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // set up bearing units picker
        Spinner bearingUnitsPicker = findViewById(R.id.bearingUnitsPicker);

        ArrayAdapter<CharSequence> bearingAdapter = ArrayAdapter.createFromResource(this,
                R.array.bearingUnitsList, android.R.layout.simple_spinner_item);

        bearingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bearingUnitsPicker.setAdapter(bearingAdapter);
        bearingUnitsPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bearingUnitsSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // select current distance and bearing units on spinner
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            distanceUnitsSelection = extras.getString("distanceUnits");
            bearingUnitsSelection = extras.getString("bearingUnits");

            int distanceUnitsIndex = distanceAdapter.getPosition(distanceUnitsSelection);
            int bearingUnitsIndex = bearingAdapter.getPosition(bearingUnitsSelection);

            distanceUnitsPicker.setSelection(distanceUnitsIndex);
            bearingUnitsPicker.setSelection(bearingUnitsIndex);
        }
    }
}
