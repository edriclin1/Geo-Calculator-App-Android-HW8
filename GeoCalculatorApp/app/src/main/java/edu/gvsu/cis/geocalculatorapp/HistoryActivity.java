package edu.gvsu.cis.geocalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.gvsu.cis.geocalculatorapp.dummy.HistoryContent;

public class HistoryActivity extends AppCompatActivity implements HistoryFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

/*<android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        app:srcCompat="@android:drawable/ic_dialog_email" />*/
    }

    @Override
    public void onListFragmentInteraction(HistoryContent.HistoryItem item) {
        System.out.println("Interact!");
        Intent intent = new Intent();
        String[] vals = {item.origLat, item.origLng, item.destLat, item.destLng};
        intent.putExtra("item", vals);
        setResult(MainActivity.HISTORY_RESULT,intent);
        finish();
    }

}
