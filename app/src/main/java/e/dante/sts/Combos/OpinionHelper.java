package e.dante.sts.Combos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.dante.sts.Combos.Opinion;

public class OpinionHelper {
    private Callback activity;
    private DatabaseReference mDatabase;
    private String type;
    private String name;
    private FirebaseUser mUser;

    public OpinionHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface Callback {
        void gotOpinions(ArrayList<Opinion> opinions);
        void gotOpinionsError(String message);
    }

    // gets all the opinions about a specific card/relic
    public void getOpinions(Callback activity, String name, String type) {
        this.activity = activity;
        this.name = name;
        this.type = type;

        Query query = mDatabase;
        query.addValueEventListener(new OpinionsValueListener());
    }

    private class OpinionsValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Opinion> opinions = new ArrayList<>();

            DataSnapshot opinionSnapshot = dataSnapshot.child("Opinions");
            if (opinionSnapshot.child(type).hasChild(name)) {
                DataSnapshot comboSnapshot = opinionSnapshot.child(type).child(name);

                for (DataSnapshot playerOpinion : comboSnapshot.getChildren()) {
                    Opinion item = new Opinion();
                    item.setUserId(playerOpinion.getKey());
                    if (playerOpinion.hasChild("note")) {
                        item.setNote((String) playerOpinion.child("note").getValue());
                    } else {
                        item.setNote("No notes");
                    }

                    if (playerOpinion.hasChild("Opinions")) {
                        DataSnapshot playerOpinions = playerOpinion.child("Opinions");
                        ArrayList<String> comboCards = new ArrayList<>();
                        ArrayList<String> comboRelics = new ArrayList<>();

                        if (playerOpinions.hasChild("Cards")) {
                            DataSnapshot cardOpinionSnapshot = playerOpinions.child("Cards");
                            for (DataSnapshot card : cardOpinionSnapshot.getChildren()) {
                                comboCards.add(card.getKey());
                            }
                        }

                        if (playerOpinions.hasChild("Relics")) {
                            DataSnapshot relicOpinionSnapshot = playerOpinions.child("Relics");
                            for (DataSnapshot relic : relicOpinionSnapshot.getChildren()) {
                                comboRelics.add(relic.getKey());
                            }
                        }

                        //TODO add anticombocards
                        item.setComboCards(comboCards);
                        item.setComboRelics(comboRelics);
                    }
                    opinions.add(item);
                }
            }

            activity.gotOpinions(opinions);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            activity.gotOpinionsError(databaseError.getMessage());
        }
    }
}
