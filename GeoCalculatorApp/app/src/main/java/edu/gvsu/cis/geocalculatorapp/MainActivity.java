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

import org.joda.time.DateTime;

import edu.gvsu.cis.geocalculatorapp.dummy.HistoryContent;

public class MainActivity extends AppCompatActivity {

    private String distanceUnits = "Kilometers";
    private String bearingUnits = "Degrees";

    public static final int SETTINGS_RESULT = 1;
    public static final int HISTORY_RESULT = 2;

    EditText p1LatField = null;
    EditText p1LongField = null;
    EditText p2LatField = null;
    EditText p2LongField = null;

    Button calculateButton = null;
    Button clearButton = null;
//        Button settingsButton = findViewById(R.id.settingsButton);

    TextView distanceLabel = null;
    TextView bearingLabel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1LatField = findViewById(R.id.p1LatField);
        p1LongField = findViewById(R.id.p1LongField);
        p2LatField = findViewById(R.id.p2LatField);
        p2LongField = findViewById(R.id.p2LongField);

        calculateButton = findViewById(R.id.calculateButton);
        clearButton = findViewById(R.id.clearButton);
//        settingsButton = findViewById(R.id.settingsButton);

        distanceLabel = findViewById(R.id.distanceLabel);
        bearingLabel = findViewById(R.id.bearingLabel);


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

                // remember the calculation.
                HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(p1LatStr, p1LongStr, p2LatStr, p2LongStr, DateTime.now());
                HistoryContent.addItem(item);
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
        if (resultCode == SETTINGS_RESULT) {
            this.distanceUnits = data.getStringExtra("distanceUnits");
            this.bearingUnits = data.getStringExtra("bearingUnits");

            if (this.distanceLabel.getText().length() > 9) {
                this.calculateButton.performClick();
            }
        } else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");

            this.p1LatField.setText(vals[0]);
            this.p1LongField.setText(vals[1]);
            this.p2LatField.setText(vals[2]);
            this.p2LongField.setText(vals[3]);

            this.calculateButton.performClick();
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
/*        Intent intent = new Intent(MainActivity.this, SettingsAcitvity.class);
        intent.putExtra("distanceUnits", this.distanceUnits);
        intent.putExtra("bearingUnits", this.bearingUnits);
        startActivityForResult(intent, UNITS_SELECTION);
        return super.onOptionsItemSelected(item);*/

        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsAcitvity.class);
            startActivityForResult(intent, SETTINGS_RESULT);
            return true;
        } else if (item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT);
            return true;
        }
        return false;
    }
}
