package e.dante.sts.Cards;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import e.dante.sts.Global.GlobalFunctions;

/*
    This is the Fragment that gathers all the cards from the firebase database.
    There is the option to get all the cards in the database or to get a single card.
 */

public class CardHelper {
    private Callback activity;
    private SingleCallback singleActivity;
    private DatabaseReference mDatabase;
    private String name;
    private GlobalFunctions gFunctions;

    // constructor
    public CardHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        gFunctions = new GlobalFunctions();
    }

    // Interfaces
    public interface Callback {
        void gotCards(ArrayList<Card> cards);
        void gotCardsError(String message);
    }

    public interface SingleCallback {
        void gotSingleCard(Card cards);
        void gotSingleCardError(String message);
    }

    // getCards collects all the cards in the database and then calls gotCards
    public void getCards(Callback activity) {
        this.activity = activity;

        DatabaseReference reference = mDatabase;
        Query query = reference.orderByChild("name");
        query.addValueEventListener(new cardValueListener());
    }

    // getSingleCards collects one specific card from the database and then calls gotCards
    public void getSingleCard(SingleCallback activity, String name) {
        this.singleActivity = activity;
        this.name = name;

        DatabaseReference reference = mDatabase;
        Query query = reference.orderByChild("name");
        query.addValueEventListener(new singleCardValueListener());
    }

    // this value listener collects all the cards from the database and returns them when the
    // data is changed.
    private class cardValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Card> cards = new ArrayList<>();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = mAuth.getCurrentUser();

            // make snapshots
            DataSnapshot cardsSnapshot = dataSnapshot.child("Cards");
            DataSnapshot opinionsSnapshot = dataSnapshot.child("Opinions").child("Cards");
            for (DataSnapshot dataChild : cardsSnapshot.getChildren()) {
                Card item = dataChild.getValue(Card.class);

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

                cards.add(item);
            }

            activity.gotCards(cards);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotCardsError(databaseError.getMessage());
        }
    }

    private class singleCardValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = mAuth.getCurrentUser();

            DataSnapshot cardSnapshot = dataSnapshot.child("Cards").child(name);
            Card item = cardSnapshot.getValue(Card.class);

            //create opinion lists
            DataSnapshot opinionSnapshot = dataSnapshot.child("Opinions");
            ArrayList<String> comboCards = new ArrayList<>();
            ArrayList<String> comboRelics = new ArrayList<>();

            ArrayList<String> antiComboCards = new ArrayList<>();
            ArrayList<String> antiComboRelics = new ArrayList<>();

            // get the opinions off the card when there are any
            if (opinionSnapshot.hasChild("Cards")) {
                opinionSnapshot = opinionSnapshot.child("Cards");

                if (opinionSnapshot.hasChild(gFunctions.name_to_dName(item.getName()))) {
                    opinionSnapshot = opinionSnapshot.child(gFunctions.name_to_dName(item.getName()));

                    //calculate the average score
                    int counter = 0;
                    float total = 0;
                    for (DataSnapshot user_opinion : opinionSnapshot.getChildren()) {
                        if (user_opinion.hasChild("score")) {
                            total += Float.parseFloat(user_opinion.child("score").getValue().toString());
                            counter++;
                        }
                    }

                    // change the counter to 1 to prevent devision by 0
                    if (counter == 0) {
                        counter = 1;
                    }
                    item.setAverageScore(total / counter);

                    if ((mUser != null) && (opinionSnapshot.hasChild(mUser.getUid()))) {
                        opinionSnapshot = opinionSnapshot.child(mUser.getUid());

                        // get the score
                        if (opinionSnapshot.hasChild("score")) {
                            item.setYourScore(Float.parseFloat(opinionSnapshot.child("score")
                                    .getValue().toString()));
                        }

                        // get the note
                        if (opinionSnapshot.hasChild("note")) {
                            item.setYourNote((String) opinionSnapshot.child("note").getValue());
                        }

                        // get the combo cards and relics
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

                        // get the anti combo cards and relics
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

            singleActivity.gotSingleCard(item);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            singleActivity.gotSingleCardError(databaseError.getMessage());
        }
    }
}
