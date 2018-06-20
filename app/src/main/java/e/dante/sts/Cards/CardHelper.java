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

public class CardHelper {
    private Callback activity;
    private SingleCallback singleActivity;
    private DatabaseReference mDatabase;
    private String name;

    public CardHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getCards(Callback activity) {
        this.activity = activity;

        DatabaseReference reference = mDatabase.child("Cards");
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
            for (DataSnapshot dataChild : dataSnapshot.getChildren()) {
                Card item = dataChild.getValue(Card.class);
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
            DataSnapshot comboSnapshot = dataSnapshot.child("Combos");
            Card item = cardSnapshot.getValue(Card.class);
            if (cardSnapshot.hasChild("scores")) {
                DataSnapshot score = cardSnapshot.child("scores");


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


            if (cardSnapshot.hasChild("notes")) {
                Log.d("Detail Notes", "init");
                DataSnapshot notes = cardSnapshot.child("notes");


                if ((mUser != null) && (notes.hasChild(mUser.getUid()))) {
                    String note = (String) notes.child(mUser.getUid()).getValue();
                    Log.d("onDataChange Detail", "note: " + note);
                    item.setYourNote(note);
                    Log.d("onDataChange Detail", "note: " + item.getYourNote());
                }
            }

            ArrayList<String> comboCards = new ArrayList<>();
            ArrayList<String> comboRelics = new ArrayList<>();
            comboSnapshot = comboSnapshot.child("Cards");
            if (comboSnapshot.hasChild(name)) {
                DataSnapshot cardCombo = comboSnapshot.child(name);
                if (cardCombo.hasChild("Cards")) {
                    DataSnapshot cardComboCards = cardCombo.child("Cards");

                    if ((mUser != null) && (cardComboCards.hasChild(mUser.getUid()))) {
                        Log.d("CardHelper", "got combo card in UID");
                        for (DataSnapshot card : cardComboCards.child(mUser.getUid()).getChildren()) {
                            Log.d("CardHelper", "got combo card" + card.getKey());
                            comboCards.add(card.getKey());
                        }
                    }
                }
                if (cardCombo.hasChild("Relics")) {
                    DataSnapshot cardComboRelics = cardCombo.child("Relics");

                    if ((mUser != null) && (cardComboRelics.hasChild(mUser.getUid()))) {
                        Log.d("CardHelper", "got combo card in UID");
                        for (DataSnapshot relic : cardComboRelics.child(mUser.getUid()).getChildren()) {
                            Log.d("CardHelper", "got combo card" + relic.getKey());
                            comboRelics.add(relic.getKey());
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
