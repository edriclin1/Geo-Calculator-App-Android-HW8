package edu.gvsu.cis.geocalculatorapp;

/**
 * Main activity screen for geo calculator app.
 *
 * @author Edric Lin
 * @author Dimitri Haring
 * @version 5/22/18
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String distanceUnits = "Kilometers";
    private String bearingUnits = "Degrees";

    public static final int UNITS_SELECTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText p1LatField = findViewById(R.id.p1LatField);
        EditText p1LongField = findViewById(R.id.p1LongField);
        EditText p2LatField = findViewById(R.id.p2LatField);
        EditText p2LongField = findViewById(R.id.p2LongField);

        Button calculateButton = findViewById(R.id.calculateButton);
        Button clearButton = findViewById(R.id.clearButton);
//        Button settingsButton = findViewById(R.id.settingsButton);

        TextView distanceLabel = findViewById(R.id.distanceLabel);
        TextView bearingLabel = findViewById(R.id.bearingLabel);


        ConstraintLayout layout = findViewById(R.id.MainActivityLayout);
        layout.setOnTouchListener((v, ev) -> {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
        });

        calculateButton.setOnClickListener(v -> {

            // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            // get latitudes and longitudes from fields
            String p1LatStr = p1LatField.getText().toString();
            String p1LongStr = p1LongField.getText().toString();
            String p2LatStr = p2LatField.getText().toString();
            String p2LongStr = p2LongField.getText().toString();

            // check if any fields are empty
            if (p1LatStr.length() == 0 || p1LongStr.length() == 0 ||
                    p2LatStr.length() == 0 || p2LongStr.length() == 0) {
                Toast.makeText(this, "Oops! You forgot to fill out some fields.", Toast.LENGTH_SHORT).show();
            }

            // else proceed with calculations
            else {

                // create two points
                Location p1 = new Location("");
                Location p2 = new Location("");

                // set corresponding latitude and longitudes
                p1.setLatitude(Double.parseDouble(p1LatStr));
                p1.setLongitude(Double.parseDouble(p1LongStr));
                p2.setLatitude(Double.parseDouble(p2LatStr));
                p2.setLongitude(Double.parseDouble(p2LongStr));

                // calculate distance in km and bearing in degrees from p1 to p2
                float distance = p1.distanceTo(p2) / 1000;
                float bearing = p1.bearingTo(p2);

                // convert to miles and/or mils if needed
                if (this.distanceUnits.equals("Miles")) {
                    distance = distance * 0.621371f;
                }

                if (this.bearingUnits.equals("Mils")) {
                    bearing = bearing * 17.777777777778f;
                }

                // add calculation to distance and bearing labels
                distanceLabel.setText(getString(R.string.emptyDistance) + " " + String.format("%.2f", distance) +
                        " " + this.distanceUnits);
                bearingLabel.setText(getString(R.string.emptyBearing) + " " + String.format("%.2f", bearing) +
                        " " + this.bearingUnits);
            }
        });

        clearButton.setOnClickListener(v -> {

            // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            p1LatField.setText("");
            p1LongField.setText("");
            p2LatField.setText("");
            p2LongField.setText("");

            distanceLabel.setText(getString(R.string.emptyDistance));
            bearingLabel.setText(getString(R.string.emptyBearing));
        });

/*        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsAcitvity.class);
            startActivityForResult(intent, UNITS_SELECTION);
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == UNITS_SELECTION) {
            this.distanceUnits = data.getStringExtra("distanceUnits");
            this.bearingUnits = data.getStringExtra("bearingUnits");

            TextView distanceLabel = findViewById(R.id.distanceLabel);
            TextView bearingLabel = findViewById(R.id.bearingLabel);
            Button calculateButton = findViewById(R.id.calculateButton);

            if (distanceLabel.getText().length() > 9) {
                calculateButton.performClick();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsAcitvity.class);
        intent.putExtra("distanceUnits", this.distanceUnits);
        intent.putExtra("bearingUnits", this.bearingUnits);
        startActivityForResult(intent, UNITS_SELECTION);
        return super.onOptionsItemSelected(item);
    }
}
