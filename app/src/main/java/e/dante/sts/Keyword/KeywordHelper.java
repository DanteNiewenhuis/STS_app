package e.dante.sts.Keyword;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KeywordHelper {
    private Callback activity;
    private DatabaseReference mDatabase;

    public KeywordHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getKeywords(Callback activity) {
        this.activity = activity;

        Query query = mDatabase.child("Keywords");
        query.addValueEventListener(new keywordsValueListener());
    }

    public interface Callback {
        void gotKeywords(ArrayList<Keyword> keywords);

        void gotKeywordsError(String message);
    }

    private class keywordsValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Keyword> keywords = new ArrayList<>();
            for (DataSnapshot score : dataSnapshot.getChildren()) {
                keywords.add(score.getValue(Keyword.class));
            }

            activity.gotKeywords(keywords);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotKeywordsError(databaseError.getMessage());
        }
    }
}
