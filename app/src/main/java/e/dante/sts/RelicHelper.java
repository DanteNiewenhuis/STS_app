package e.dante.sts;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RelicHelper {
    private Callback activity;
    private DatabaseReference mDatabase;

    public RelicHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public interface Callback {
        void gotRelics(ArrayList<Relic> relics);
        void gotRelicsError(String message);
    }

    public void getRelics(Callback activity) {
        this.activity = activity;

        DatabaseReference reference = mDatabase.child("Relics");
        Query query = reference.orderByChild("Rarity");
        query.addValueEventListener(new relicValueListener());
    }

    private class relicValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Relic> relics = new ArrayList<>();

            for (DataSnapshot score : dataSnapshot.getChildren()) {
                Relic item = score.getValue(Relic.class);
                relics.add(item);
            }

            activity.gotRelics(relics);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotRelicsError(databaseError.getMessage());
        }
    }
}
