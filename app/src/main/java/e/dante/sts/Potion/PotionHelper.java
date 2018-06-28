package e.dante.sts.Potion;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/*
    This the class that interacts with the dataset to get all the potions.
 */
public class PotionHelper {
    private Callback activity;
    private DatabaseReference mDatabase;

    public PotionHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getPotions(Callback activity) {
        this.activity = activity;

        Query query = mDatabase.child("Potions");
        query.addValueEventListener(new potionValueListener());
    }

    public interface Callback {
        void gotPotions(ArrayList<Potion> potions);

        void gotPotionsError(String message);
    }

    private class potionValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Potion> potions = new ArrayList<>();

            for (DataSnapshot score : dataSnapshot.getChildren()) {
                Potion item = score.getValue(Potion.class);
                potions.add(item);
            }

            activity.gotPotions(potions);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotPotionsError(databaseError.getMessage());
        }
    }
}
