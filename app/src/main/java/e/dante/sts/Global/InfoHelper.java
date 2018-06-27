package e.dante.sts.Global;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import e.dante.sts.Global.Globals;

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

            ///////////////// CARDS ////////////////////
            ArrayList<String> anyCards = new ArrayList<>();
            ArrayList<String> ironcladCards = new ArrayList<>();
            ArrayList<String> silentCards = new ArrayList<>();
            ArrayList<String> defectCards = new ArrayList<>();

            DataSnapshot cardsSnapshot = dataSnapshot.child("Cards");
            for (DataSnapshot card : cardsSnapshot.getChildren()) {
                switch (card.child("hero").getValue().toString()) {
                    case "Neutral":
                        anyCards.add((String) card.child("name").getValue());
                        break;
                    case "Ironclad":
                        ironcladCards.add((String) card.child("name").getValue());
                        break;
                    case "Silent":
                        silentCards.add((String) card.child("name").getValue());
                        break;
                    case "Defect":
                        defectCards.add((String) card.child("name").getValue());
                        break;
                }
            }

            sharedData.setAnyCards(anyCards);
            sharedData.setIroncladCards(ironcladCards);
            sharedData.setSilentCards(silentCards);
            sharedData.setDefectCards(defectCards);

            ///////////////// RELICS ////////////////////
            ArrayList<String> anyRelics = new ArrayList<>();
            ArrayList<String> ironcladRelics = new ArrayList<>();
            ArrayList<String> silentRelics = new ArrayList<>();
            ArrayList<String> defectRelics = new ArrayList<>();

            DataSnapshot relicsSnapshot = dataSnapshot.child("Relics");
            for (DataSnapshot relic : relicsSnapshot.getChildren()) {
                switch (relic.child("hero").getValue().toString()) {
                    case "Any":
                        anyRelics.add((String) relic.child("name").getValue());
                        break;
                    case "Ironclad":
                        ironcladRelics.add((String) relic.child("name").getValue());
                        break;
                    case "Silent":
                        silentRelics.add((String) relic.child("name").getValue());
                        break;
                    case "Defect":
                        defectRelics.add((String) relic.child("name").getValue());
                        break;
                }
            }

            sharedData.setAnyRelics(anyRelics);
            sharedData.setIroncladRelics(ironcladRelics);
            sharedData.setSilentRelics(silentRelics);
            sharedData.setDefectRelics(defectRelics);

            ///////////////// KEYWORDS ////////////////////
            ArrayList<String> keywords = new ArrayList<>();
            DataSnapshot keywordsSnapshot = dataSnapshot.child("Keywords");
            for (DataSnapshot keyword : keywordsSnapshot.getChildren()) {
                keywords.add((String) keyword.child("name").getValue());
            }

            sharedData.setKeywords(keywords);

            ///////////////// EVENTS ////////////////////
            ArrayList<String> events = new ArrayList<>();
            DataSnapshot eventsSnapshot = dataSnapshot.child("Events");
            for (DataSnapshot event : eventsSnapshot.getChildren()) {
                events.add((String) event.child("name").getValue());
            }

            sharedData.setEvents(events);

            ///////////////// POTIONS ////////////////////
            ArrayList<String> potions = new ArrayList<>();
            DataSnapshot potionsSnapshot = dataSnapshot.child("Potions");
            for (DataSnapshot potion : potionsSnapshot.getChildren()) {
                potions.add((String) potion.child("name").getValue());
            }

            sharedData.setPotions(potions);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
