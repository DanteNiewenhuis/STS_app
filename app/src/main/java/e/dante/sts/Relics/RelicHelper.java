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

import e.dante.sts.Cards.Card;
import e.dante.sts.Global.GlobalFunctions;
/*
    This the class that interacts with the dataset to get all the Relics.
 */
public class RelicHelper {
    private Callback activity;
    private SingleCallback singleActivity;
    private DatabaseReference mDatabase;
    private String name;
    private GlobalFunctions gFunctions;

    public RelicHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        gFunctions = new GlobalFunctions();
    }

    public void getRelics(Callback activity) {
        this.activity = activity;

        DatabaseReference reference = mDatabase;
        Query query = reference.orderByChild("name");
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

            // make snapshots
            DataSnapshot relicsSnapshot = dataSnapshot.child("Relics");
            DataSnapshot opinionsSnapshot = dataSnapshot.child("Opinions").child("Relics");
            for (DataSnapshot dataChild : relicsSnapshot.getChildren()) {
                Relic item = dataChild.getValue(Relic.class);

                // get the data of the cards if it is present
                if (opinionsSnapshot.hasChild(gFunctions.name_to_dName(item.getName()))) {
                    DataSnapshot cardOpinionsSnapshot = opinionsSnapshot.child(gFunctions
                            .name_to_dName(item.getName()));

                    // calculate the average score for the card
                    int counter = 0;
                    float total = 0;
                    for (DataSnapshot user_opinion : cardOpinionsSnapshot.getChildren()) {
                        if (user_opinion.hasChild("score")) {
                            total += Float.parseFloat(user_opinion.child("score").getValue().toString());
                            counter++;
                        }
                    }


                    item.setVoteCount(counter);
                    // set the counter to one if no votes have been collected to prevent deviding by 0
                    if (counter == 0) {
                        counter = 1;
                    }
                    item.setAverageScore(total / counter);

                    // get the personal score if it is present
                    if ((mUser != null) && (cardOpinionsSnapshot.hasChild(mUser.getUid()))) {
                        cardOpinionsSnapshot = cardOpinionsSnapshot.child(mUser.getUid());

                        if (cardOpinionsSnapshot.hasChild("score")) {
                            item.setYourScore(Float.parseFloat(cardOpinionsSnapshot.child("score")
                                    .getValue().toString()));
                        }
                    }
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

            DataSnapshot cardSnapshot = dataSnapshot.child("Relics").child(name);
            Relic item = cardSnapshot.getValue(Relic.class);

            DataSnapshot opinionSnapshot = dataSnapshot.child("Opinions");
            ArrayList<String> comboCards = new ArrayList<>();
            ArrayList<String> comboRelics = new ArrayList<>();

            ArrayList<String> antiComboCards = new ArrayList<>();
            ArrayList<String> antiComboRelics = new ArrayList<>();

            if (opinionSnapshot.hasChild("Relics")) {
                opinionSnapshot = opinionSnapshot.child("Relics");

                if (opinionSnapshot.hasChild(gFunctions.name_to_dName(item.getName()))) {
                    opinionSnapshot = opinionSnapshot.child(gFunctions.name_to_dName(item.getName()));

                    int counter = 0;
                    float total = 0;
                    for (DataSnapshot user_opinion : opinionSnapshot.getChildren()) {
                        if (user_opinion.hasChild("score")) {
                            total += Float.parseFloat(user_opinion.child("score").getValue().toString());
                            counter++;
                        }
                    }

                    item.setVoteCount(counter);
                    if (counter == 0) {
                        counter = 1;
                    }
                    item.setAverageScore(total / counter);

                    if ((mUser != null) && (opinionSnapshot.hasChild(mUser.getUid()))) {
                        opinionSnapshot = opinionSnapshot.child(mUser.getUid());

                        if (opinionSnapshot.hasChild("score")) {
                            item.setYourScore(Float.parseFloat(opinionSnapshot.child("score")
                                    .getValue().toString()));
                        }

                        if (opinionSnapshot.hasChild("note")) {
                            item.setYourNote((String) opinionSnapshot.child("note").getValue());
                        }

                        if (opinionSnapshot.hasChild("Combos")) {
                            DataSnapshot comboSnapshot = opinionSnapshot.child("Combos");

                            if (comboSnapshot.hasChild("Cards")) {
                                DataSnapshot comboCardSnapshot = comboSnapshot.child("Cards");
                                for (DataSnapshot comboCard : comboCardSnapshot.getChildren()) {
                                    comboCards.add(comboCard.getKey());
                                }
                            }

                            if (comboSnapshot.hasChild("Relics")) {
                                DataSnapshot comboRelicSnapshot = comboSnapshot.child("Relics");
                                for (DataSnapshot comboRelic : comboRelicSnapshot.getChildren()) {
                                    comboRelics.add(comboRelic.getKey());
                                }
                            }
                        }

                        if (opinionSnapshot.hasChild("Anti_Combos")) {
                            DataSnapshot antiComboSnapshot = opinionSnapshot.child("Anti_Combos");

                            if (antiComboSnapshot.hasChild("Cards")) {
                                DataSnapshot antiComboCardSnapshot = antiComboSnapshot.child("Cards");
                                for (DataSnapshot antiComboCard : antiComboCardSnapshot.getChildren()) {
                                    antiComboCards.add(antiComboCard.getKey());
                                }
                            }

                            if (antiComboSnapshot.hasChild("Relics")) {
                                DataSnapshot antiComboRelicSnapshot = antiComboSnapshot.child("Relics");
                                for (DataSnapshot antiComboRelic : antiComboRelicSnapshot.getChildren()) {
                                    antiComboRelics.add(antiComboRelic.getKey());
                                }
                            }
                        }
                    }
                }
            }

            item.setYourComboCards(comboCards);
            item.setYourComboRelics(comboRelics);

            item.setYourAntiComboCards(antiComboCards);
            item.setYourAntiComboRelics(antiComboRelics);

            singleActivity.gotSingleRelic(item);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            singleActivity.gotSingleRelicError(databaseError.getMessage());
        }
    }
}
