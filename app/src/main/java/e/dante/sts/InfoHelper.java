package e.dante.sts;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoHelper {
    private Callback activity;
    private DatabaseReference mDatabase;
    private String type;

    public InfoHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getInfo(Callback activity, String type, String filter) {
        this.activity = activity;
        this.type = type;

        Query query = mDatabase.child(type).child(filter);
        query.addValueEventListener(new SingleKeywordValueListener());
    }

    public void getLists() {
        Query query = mDatabase;
        query.addValueEventListener(new ListValueListener());
    }

    public interface Callback {
        void gotInfo(String name, String type, String des);
        void gotInfoError(String message);
    }

    private class SingleKeywordValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String name = (String) dataSnapshot.child("name").getValue();
            String des = (String) dataSnapshot.child("description").getValue();

            Log.d("SingleKeywordValueListener", "sending now");
            activity.gotInfo(name, type, des);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotInfoError(databaseError.getMessage());
        }
    }

    private class ListValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Globals sharedData = Globals.getInstance();

            ArrayList<String> cards = new ArrayList<>();
            DataSnapshot cardsSnapshot = dataSnapshot.child("Cards");
            for (DataSnapshot card: cardsSnapshot.getChildren()) {
                cards.add((String) card.child("name").getValue());
            }

            Log.d("LisValueListener", "length cards: " + cards.size());
            sharedData.setCards(cards);

            ArrayList<String> relics = new ArrayList<>();
            DataSnapshot relicsSnapshot = dataSnapshot.child("Relics");
            for (DataSnapshot relic: relicsSnapshot.getChildren()) {
                relics.add((String) relic.child("name").getValue());
            }

            Log.d("LisValueListener", "length relics: " + relics.size());
            sharedData.setRelics(relics);

            ArrayList<String> keywords = new ArrayList<>();
            DataSnapshot keywordsSnapshot = dataSnapshot.child("Keywords");
            for (DataSnapshot keyword: keywordsSnapshot.getChildren()) {
                keywords.add((String) keyword.child("name").getValue());
            }

            Log.d("LisValueListener", "length keywords: " + keywords.size());
            sharedData.setKeywords(keywords);

            ArrayList<String> events = new ArrayList<>();
            DataSnapshot eventsSnapshot = dataSnapshot.child("Events");
            for (DataSnapshot event: eventsSnapshot.getChildren()) {
                events.add((String) event.child("name").getValue());
            }

            Log.d("LisValueListener", "length events: " + events.size());
            sharedData.setEvents(events);

            ArrayList<String> potions = new ArrayList<>();
            DataSnapshot potionsSnapshot = dataSnapshot.child("Potions");
            for (DataSnapshot potion: potionsSnapshot.getChildren()) {
                potions.add((String) potion.child("name").getValue());
            }

            Log.d("LisValueListener", "length potions: " + potions.size());
            sharedData.setPotions(potions);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
