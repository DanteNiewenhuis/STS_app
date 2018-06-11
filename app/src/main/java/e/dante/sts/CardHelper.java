package e.dante.sts;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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
    private DatabaseReference mDatabase;

    public CardHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public interface Callback {
        void gotCards(ArrayList<Card> cards);
        void gotCardsError(String message);
    }

    public void getCards(Callback activity) {
        this.activity = activity;

        DatabaseReference reference = mDatabase.child("Cards");
        Query query = reference.orderByChild("color");
        query.addValueEventListener(new cardValueListener());
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

                    if (score.hasChild(mUser.getUid())) {
                        float rating = Float.parseFloat(score.child(mUser.getUid()).getValue().toString());
                        item.setYourScore(rating);
                    }

                    int counter = 0;
                    float total = 0;
                    Log.d("CARDHELPER", "making average");
                    for (DataSnapshot user_score : score.getChildren()) {
                        Log.d("CARDHELPER", "getting value");
                        total += Float.parseFloat(user_score.getValue().toString());
                        counter++;
                    }

                    Log.d("CARDHELPER", "setting average");
                    item.setAverageScore(total/counter);
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
}
