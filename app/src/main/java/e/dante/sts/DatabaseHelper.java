package e.dante.sts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper {
    private Context context;
    private Callback activity;
    private DatabaseReference mDatabase;

    public DatabaseHelper(Context context) {
        this.context = context;

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public HashMap<String, String> get_expendable_data(String type) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Innate", "Always begin combat with this card in your hard");
        data.put("Exhaust", "Removes the Exhausted card from the deck until end of combat.");


        return data;
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

            for (DataSnapshot score : dataSnapshot.getChildren()) {
                Card item = score.getValue(Card.class);
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
