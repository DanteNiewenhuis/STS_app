package e.dante.sts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity implements EventsHelper.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        new EventsHelper().getEvents(this);
    }


    @Override
    public void gotEvents(ArrayList<Event> events) {
        ExpandableListView expListView = findViewById(R.id.events_list_view);

        ExpandableListAdapter listAdapter = new EventListAdapter(this, events);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void gotEventsError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
