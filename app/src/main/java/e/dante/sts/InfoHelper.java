package e.dante.sts;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InfoHelper {
    private Callback activity;
    private DatabaseReference mDatabase;
    private String type;

    public InfoHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getInfo(Callback activity, String type, String filter) {
        this.activity = activity;
        this.type = type;

        Query query = mDatabase.child(type).child(filter);
        query.addValueEventListener(new singleKeywordValueListener());
    }

    public interface Callback {
        void gotInfo(String name, String type, String des);

        void gotInfoError(String message);
    }

    private class singleKeywordValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String name = (String) dataSnapshot.child("name").getValue();
            String des = (String) dataSnapshot.child("description").getValue();

            activity.gotInfo(name, type, des);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotInfoError(databaseError.getMessage());
        }
    }
}
