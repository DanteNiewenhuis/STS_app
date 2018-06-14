package e.dante.sts.Keyword;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class KeywordHelper {
    private Callback activity;
    private DatabaseReference mDatabase;

    public KeywordHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public interface Callback {
        void gotKeywords(HashMap<String, String> keywords);
        void gotKeywordsError(String message);
    }

    public void getKeywords(Callback activity) {
        this.activity = activity;

        Query query = mDatabase.child("Keywords");
        query.addValueEventListener(new keywordsValueListener());
    }

    private class keywordsValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            HashMap<String, String> keywords = new HashMap<>();

            for (DataSnapshot score : dataSnapshot.getChildren()) {
                keywords.put((String) score.child("name").getValue(), (String) score.child("description").getValue());
            }

            activity.gotKeywords(keywords);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotKeywordsError(databaseError.getMessage());
        }
    }
}
