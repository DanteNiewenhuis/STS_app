package e.dante.sts.Event;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsHelper {
    private Callback activity;
    private DatabaseReference mDatabase;

    public EventsHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public interface Callback {
        void gotEvents(ArrayList<Event> events);
        void gotEventsError(String message);
    }

    public void getEvents(Callback activity) {
        this.activity = activity;

        Query query = mDatabase.child("Events");
        query.addValueEventListener(new eventsValueListener());
    }

    private class eventsValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Event> events = new ArrayList<>();

            for (DataSnapshot score : dataSnapshot.getChildren()) {
                events.add(score.getValue(Event.class));
            }

            activity.gotEvents(events);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotEventsError(databaseError.getMessage());
        }
    }
}
