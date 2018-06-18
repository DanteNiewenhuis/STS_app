package e.dante.sts;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainHelper {
    private Callback activity;
    private DatabaseReference mDatabase;
    private String type;

    public MainHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getList(Callback activity, String type) {
        Log.d("getList", "init");
        this.activity = activity;
        this.type = type;

        Query query = mDatabase.child(type);
        query.addValueEventListener(new ListValueListener());
    }

    public interface Callback {
        void gotList(ArrayList<String> list, String type);

        void gotListError(String message);
    }

    private class ListValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("onDataChange", "init");
            ArrayList<String> list = new ArrayList<>();

            for (DataSnapshot item: dataSnapshot.getChildren()) {
                list.add(item.getKey());
            }


            Log.d("onDataChange", "done");
            activity.gotList(list, type);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotListError(databaseError.getMessage());
        }
    }
}

