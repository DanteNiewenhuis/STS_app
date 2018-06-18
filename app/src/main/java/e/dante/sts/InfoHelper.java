package e.dante.sts;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoHelper {
    private Callback activity;
    private String input;
    private DatabaseReference mDatabase;
    private String type;
    private int viewId;

    public InfoHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getInfo(Callback activity, String type, String filter) {
        Log.d("getInfo", "init");
        this.activity = activity;
        this.type = type;

        Query query = mDatabase.child(type).child(filter);
        query.addValueEventListener(new SingleKeywordValueListener());
        Log.d("getInfo", "done");
    }

    public void getSpan(Callback activity, int viewId, String input) {
        Log.d("getList", "init");
        this.activity = activity;
        this.input = input;
        this.viewId = viewId;

        Query query = mDatabase.child("Keywords");
        query.addValueEventListener(new ListValueListener());
    }

    public interface Callback {
        void gotInfo(String name, String type, String des);
        void gotInfoError(String message);
        void gotSpan(int viewId, SpannableString ss);
    }

    private class SingleKeywordValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("onDataChange", "init");
            String name = (String) dataSnapshot.child("name").getValue();
            String des = (String) dataSnapshot.child("description").getValue();

            Log.d("onDataChange", "done");
            activity.gotInfo(name, type, des);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotInfoError(databaseError.getMessage());
        }
    }

    private class ListValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("onDataChange", "init");
            SpannableString ss = new SpannableString(input);

            for (DataSnapshot item: dataSnapshot.getChildren()) {
                String word = item.getKey();
                Log.d("onDataChange", "word: " + word);
                for (int index = input.toLowerCase().indexOf(word.toLowerCase());
                     index >= 0;
                     index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1)) {
                    ss.setSpan(makeClickableSpan(word), index, index + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }


            Log.d("onDataChange", "done");
            activity.gotSpan(viewId, ss);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private ClickableSpan makeClickableSpan(final String filter) {
        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                new InfoHelper().getInfo(activity, "Keywords", filter);
            }
        };
    }
}
