package e.dante.sts;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EventsActivity extends Fragment implements EventsHelper.Callback{
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_events, container, false);

        new EventsHelper().getEvents(this);

        return myView;
    }


    @Override
    public void gotEvents(ArrayList<Event> events) {
        ExpandableListView expListView = myView.findViewById(R.id.events_list_view);

        ExpandableListAdapter listAdapter = new EventListAdapter(getContext(), events);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void gotEventsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
