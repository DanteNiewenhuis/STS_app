package e.dante.sts;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingFragment extends DialogFragment {
    private String name;
    private float score;
    private String type;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public RatingFragment() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
        score = args.getFloat("score");
        type = args.getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_rating, container, false);
        RatingBar ratingBar = myView.findViewById(R.id.ratingBar);
        ratingBar.setRating(score);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mDatabase.child("Opinions").child(type).child(name).child(mUser.getUid()).child("score").setValue(v);
                dismiss();
            }
        });
        return myView;
    }

}
