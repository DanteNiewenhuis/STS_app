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

        DatabaseReference reference = mDatabase.child("Cards").child(name);
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
            for (DataSnapshot score : dataSnapshot.getChildren()) {
                Card item = score.getValue(Card.class);

                if (score.hasChild("scores")) {
                    score = score.child("scores");


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
            Card item = dataSnapshot.getValue(Card.class);

            if (dataSnapshot.hasChild("scores")) {
                dataSnapshot = dataSnapshot.child("scores");


                if ((mUser != null) && (dataSnapshot.hasChild(mUser.getUid()))) {
                    float rating = Float.parseFloat(dataSnapshot.child(mUser.getUid()).getValue().toString());
                    item.setYourScore(rating);
                }

                int counter = 0;
                float total = 0;
                for (DataSnapshot user_score : dataSnapshot.getChildren()) {
                    total += Float.parseFloat(user_score.getValue().toString());
                    counter++;
                }

                item.setAverageScore(total / counter);
            }

            singleActivity.gotSingleCard(item);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("ERRORORROROORR", "cards");
            singleActivity.gotSingleCardError(databaseError.getMessage());
        }
    }
}
