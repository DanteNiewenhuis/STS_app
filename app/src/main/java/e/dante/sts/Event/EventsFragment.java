package e.dante.sts.Event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

import e.dante.sts.InfoFragment;
import e.dante.sts.InfoHelper;
import e.dante.sts.R;

public class EventsFragment extends Fragment implements EventsHelper.Callback {
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_events, container, false);

        getActivity().setTitle("Events");

        new EventsHelper().getEvents(this);

        return myView;
    }


    @Override
    public void gotEvents(ArrayList<Event> events) {
        ExpandableListView expListView = myView.findViewById(R.id.events_list_view);

        ExpandableListAdapter listAdapter = new EventListAdapter(getContext(),
                getActivity().getSupportFragmentManager(), events);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void gotEventsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
