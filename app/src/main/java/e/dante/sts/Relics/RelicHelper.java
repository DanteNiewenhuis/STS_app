package e.dante.sts.Relics;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RelicHelper {
    private Callback activity;
    private SingleCallback singleActivity;
    private DatabaseReference mDatabase;
    private String name;

    public RelicHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getRelics(Callback activity) {
        this.activity = activity;

        DatabaseReference reference = mDatabase.child("Relics");
        Query query = reference.orderByChild("Rarity");
        query.addValueEventListener(new relicValueListener());
    }

    public void getSingleRelic(SingleCallback activity, String name) {
        this.singleActivity = activity;
        this.name = name;

        DatabaseReference reference = mDatabase;
        Query query = reference.orderByChild("name");
        query.addValueEventListener(new singleRelicValueListener());
    }

    public interface Callback {
        void gotRelics(ArrayList<Relic> relics);

        void gotRelicsError(String message);
    }

    public interface SingleCallback {
        void gotSingleRelic(Relic relics);

        void gotSingleRelicError(String message);
    }

    private class relicValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Relic> relics = new ArrayList<>();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = mAuth.getCurrentUser();
            for (DataSnapshot dataChild : dataSnapshot.getChildren()) {
                Relic item = dataChild.getValue(Relic.class);
                Log.d("onDataChangedSingle", "name: " + item.getName());

                if (dataChild.hasChild("scores")) {
                    DataSnapshot score = dataChild.child("scores");


                    if ((mUser != null) && (score.hasChild(mUser.getUid()))) {
                        float rating = Float.parseFloat(score.child(mUser.getUid()).getValue().toString());
                        item.setYourScore(rating);
                    }

                    int counter = 0;
                    float total = 0;
                    for (DataSnapshot user_score : score.getChildren()) {
                        total += Float.parseFloat(user_score.getValue().toString());
                        counter++;
                    }

                    item.setAverageScore(total / counter);
                }
                relics.add(item);
            }

            activity.gotRelics(relics);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotRelicsError(databaseError.getMessage());
        }
    }

    private class singleRelicValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = mAuth.getCurrentUser();

            DataSnapshot relicSnapshot = dataSnapshot.child("Relics").child(name);
            DataSnapshot comboSnapshot = dataSnapshot.child("Combos");
            Relic item = relicSnapshot.getValue(Relic.class);
            if (relicSnapshot.hasChild("scores")) {
                DataSnapshot score = relicSnapshot.child("scores");


                if ((mUser != null) && (score.hasChild(mUser.getUid()))) {
                    float rating = Float.parseFloat(score.child(mUser.getUid()).getValue().toString());
                    item.setYourScore(rating);
                }

                int counter = 0;
                float total = 0;
                for (DataSnapshot user_score : score.getChildren()) {
                    total += Float.parseFloat(user_score.getValue().toString());
                    counter++;
                }

                item.setAverageScore(total / counter);
            }


            if (relicSnapshot.hasChild("notes")) {
                Log.d("Detail Notes", "init");
                DataSnapshot notes = relicSnapshot.child("notes");


                if ((mUser != null) && (notes.hasChild(mUser.getUid()))) {
                    String note = (String) notes.child(mUser.getUid()).getValue();
                    Log.d("onDataChange Detail", "note: " + note);
                    item.setYourNote(note);
                    Log.d("onDataChange Detail", "note: " + item.getYourNote());
                }
            }

            ArrayList<String> comboRelics = new ArrayList<>();
            ArrayList<String> comboCards = new ArrayList<>();

            comboSnapshot = comboSnapshot.child("Relics");
            if (comboSnapshot.hasChild(name)) {
                DataSnapshot relicCombo = comboSnapshot.child(name);

                if (relicCombo.hasChild("Relics")) {
                    DataSnapshot relicComboRelics = relicCombo.child("Relics");

                    if ((mUser != null) && (relicComboRelics.hasChild(mUser.getUid()))) {
                        for (DataSnapshot relic : relicComboRelics.child(mUser.getUid()).getChildren()) {
                            comboRelics.add(relic.getKey());
                        }
                    }
                }

                if (relicCombo.hasChild("Cards")) {
                    DataSnapshot relicComboCards = relicCombo.child("Cards");

                    if ((mUser != null) && (relicComboCards.hasChild(mUser.getUid()))) {
                        for (DataSnapshot card : relicComboCards.child(mUser.getUid()).getChildren()) {
                            comboCards.add(card.getKey());
                        }
                    }
                }
            }

            item.setYourComboRelics(comboRelics);
            item.setYourComboCards(comboCards);
            singleActivity.gotSingleRelic(item);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("ERRORORROROORR", "relics");
            singleActivity.gotSingleRelicError(databaseError.getMessage());
        }
    }
}
