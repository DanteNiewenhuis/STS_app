package e.dante.sts.Opinions;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
            DataSnapshot accountSnapshot = dataSnapshot.child("Accounts");

            if (opinionSnapshot.child(type).hasChild(name)) {
                DataSnapshot comboSnapshot = opinionSnapshot.child(type).child(name);

                for (DataSnapshot playerOpinion : comboSnapshot.getChildren()) {
                    Opinion item = new Opinion();
                    ArrayList<String> comboCards = new ArrayList<>();
                    ArrayList<String> comboRelics = new ArrayList<>();
                    ArrayList<String> antiComboCards = new ArrayList<>();
                    ArrayList<String> antiComboRelics = new ArrayList<>();

                    String uId = playerOpinion.getKey();
                    item.setUserName(accountSnapshot.child(uId).child("Display").getValue().toString());

                    if (playerOpinion.hasChild("score")) {
                        item.setScore(playerOpinion.child("score").getValue().toString());
                    }

                    if (playerOpinion.hasChild("note")) {
                        item.setNote(playerOpinion.child("note").getValue().toString());
                    }

                    if (playerOpinion.hasChild("Combos")) {
                        DataSnapshot ComboDataSnapshot = playerOpinion.child("Combos");
                        if (ComboDataSnapshot.hasChild("Cards")) {
                            for (DataSnapshot comboCard: ComboDataSnapshot.child("Cards").getChildren()) {
                                comboCards.add(comboCard.getKey());
                            }
                        }

                        if (ComboDataSnapshot.hasChild("Relics")) {
                            for (DataSnapshot comboRelic: ComboDataSnapshot.child("Relics").getChildren()) {
                                comboRelics.add(comboRelic.getKey());
                            }
                        }
                    }

                    if (playerOpinion.hasChild("Anti_Combos")) {
                        DataSnapshot antiComboDataSnapshot = playerOpinion.child("Anti_Combos");
                        if (antiComboDataSnapshot.hasChild("Cards")) {
                            for (DataSnapshot antiComboCard: antiComboDataSnapshot.child("Cards").getChildren()) {
                                antiComboCards.add(antiComboCard.getKey());
                            }
                        }

                        if (antiComboDataSnapshot.hasChild("Relics")) {
                            for (DataSnapshot antiComboRelic: antiComboDataSnapshot.child("Relics").getChildren()) {
                                antiComboRelics.add(antiComboRelic.getKey());
                            }
                        }
                    }

                    item.setComboCards(comboCards);
                    item.setComboRelics(comboRelics);
                    item.setAntiComboCards(antiComboCards);
                    item.setAntiComboRelics(antiComboRelics);

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
