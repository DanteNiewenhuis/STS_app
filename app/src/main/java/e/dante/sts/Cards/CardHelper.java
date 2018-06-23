package e.dante.sts.Cards;

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

import e.dante.sts.GlobalFunctions;

public class CardHelper {
    private Callback activity;
    private SingleCallback singleActivity;
    private DatabaseReference mDatabase;
    private String name;
    private GlobalFunctions gFunctions;

    public CardHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        gFunctions = new GlobalFunctions();
    }

    public void getCards(Callback activity) {
        this.activity = activity;

        DatabaseReference reference = mDatabase;
        Query query = reference.orderByChild("name");
        query.addValueEventListener(new cardValueListener());
    }

    public void getSingleCard(SingleCallback activity, String name) {
        this.singleActivity = activity;
        this.name = name;

        DatabaseReference reference = mDatabase;
        Query query = reference.orderByChild("name");
        query.addValueEventListener(new singleCardValueListener());
    }

    public interface Callback {
        void gotCards(ArrayList<Card> cards);

        void gotCardsError(String message);
    }

    public interface SingleCallback {
        void gotSingleCard(Card cards);

        void gotSingleCardError(String message);
    }

    private class cardValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Card> cards = new ArrayList<>();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = mAuth.getCurrentUser();
            DataSnapshot cardsSnapshot = dataSnapshot.child("Cards");
            DataSnapshot opinionsSnapshot = dataSnapshot.child("Opinions").child("Cards");
            for (DataSnapshot dataChild : cardsSnapshot.getChildren()) {
                Card item = dataChild.getValue(Card.class);
                Log.d("onDataChangedSingle", "name: " + item.getName());

                if (opinionsSnapshot.hasChild(gFunctions.name_to_dName(item.getName()))) {
                    DataSnapshot cardOpinionsSnapshot = opinionsSnapshot.child(gFunctions.name_to_dName(item.getName()));

                    int counter = 0;
                    float total = 0;
                    for (DataSnapshot user_opinion : cardOpinionsSnapshot.getChildren()) {
                        if (user_opinion.hasChild("score")) {
                            total += Float.parseFloat(user_opinion.child("score").getValue().toString());
                            counter++;
                        }
                    }

                    if (cardOpinionsSnapshot.hasChild(mUser.getUid())) {
                        cardOpinionsSnapshot = cardOpinionsSnapshot.child(mUser.getUid());

                        if (cardOpinionsSnapshot.hasChild("score")) {
                            item.setYourScore(Float.parseFloat(cardOpinionsSnapshot.child("score").getValue().toString()));
                        }
                    }

                    if (counter == 0) {
                        counter = 1;
                    }
                    item.setAverageScore(total / counter);
                }

                cards.add(item);
            }




            activity.gotCards(cards);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("ERRORORROROORR", "cards");
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

            DataSnapshot opinionSnapshot = dataSnapshot.child("Opinions");
            ArrayList<String> comboCards = new ArrayList<>();
            ArrayList<String> comboRelics = new ArrayList<>();

            if (opinionSnapshot.hasChild("Cards")) {
                opinionSnapshot = opinionSnapshot.child("Cards");

                if (opinionSnapshot.hasChild(gFunctions.name_to_dName(item.getName()))) {
                    opinionSnapshot = opinionSnapshot.child(gFunctions.name_to_dName(item.getName()));
                    Log.d("CardHelper", "name found");

                    int counter = 0;
                    float total = 0;
                    for (DataSnapshot user_opinion : opinionSnapshot.getChildren()) {
                        if (user_opinion.hasChild("score")) {
                            total += Float.parseFloat(user_opinion.child("score").getValue().toString());
                            counter++;
                        }
                    }

                    if (counter == 0) {
                        counter = 1;
                    }
                    item.setAverageScore(total / counter);

                    if ((mUser != null) && (opinionSnapshot.hasChild(mUser.getUid()))) {
                        Log.d("CardHelper", "user found");
                        opinionSnapshot = opinionSnapshot.child(mUser.getUid());

                        if (opinionSnapshot.hasChild("score")) {
                            item.setYourScore(Float.parseFloat(opinionSnapshot.child("score").getValue().toString()));
                        }

                        if (opinionSnapshot.hasChild("note")) {
                            Log.d("CardHelper", "note found");
                            item.setYourNote((String) opinionSnapshot.child("note").getValue());
                        }

                        if (opinionSnapshot.hasChild("Combos")) {
                            DataSnapshot comboSnapshot = opinionSnapshot.child("Combos");

                            if (comboSnapshot.hasChild("Cards")) {
                                Log.d("CardHelper", "combo card found");
                                DataSnapshot comboCardSnapshot = comboSnapshot.child("Cards");
                                for (DataSnapshot comboCard : comboCardSnapshot.getChildren()) {
                                    comboCards.add(comboCard.getKey());
                                }
                            }

                            if (comboSnapshot.hasChild("Relics")) {
                                Log.d("CardHelper", "combo relic found");
                                DataSnapshot comboRelicSnapshot = comboSnapshot.child("Relics");
                                for (DataSnapshot comboRelic : comboRelicSnapshot.getChildren()) {
                                    comboRelics.add(comboRelic.getKey());
                                }
                            }
                        }
                    }
                }
            }

            item.setYourComboCards(comboCards);
            item.setYourComboRelics(comboRelics);
            singleActivity.gotSingleCard(item);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("ERRORORROROORR", "cards");
            singleActivity.gotSingleCardError(databaseError.getMessage());
        }
    }
}
