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

public class ComboHelper {

    private Callback activity;
    private ListCallback listActivity;
    private DatabaseReference mDatabase;
    private String type;
    private String type1;
    private String type2;
    private String name;
    private FirebaseUser mUser;

    public ComboHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface Callback {
        void gotCombos(String name, String type1, String type2, List<String> combos);
    }

    public interface ListCallback {
        void gotComboList(ArrayList<Combo> combos);
    }

    public void getCombos(Callback activity, String name, String type1, String type2) {
        this.activity = activity;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;

        Query query = mDatabase;
        query.addValueEventListener(new CombosValueListener());
    }

    public void getComboList(ListCallback activity, String name, String type) {
        this.listActivity = activity;
        this.name = name;
        this.type = type;

        Log.d("getComboList", "name: " + name);
        Log.d("getComboList", "type: " + type);
        Query query = mDatabase;
        query.addValueEventListener(new ComboListValueListener());
    }

    private class CombosValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<String> combos = new ArrayList<>();

            if (mUser != null) {
                DataSnapshot userCombos = dataSnapshot.child(mUser.getUid());

                for (DataSnapshot combo: userCombos.getChildren()) {
                    if (Integer.parseInt(combo.getValue().toString()) == 1) {
                        combos.add(combo.getKey());
                    }
                }
            }

            activity.gotCombos(name, type1, type2, combos);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private class ComboListValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Combo> combos = new ArrayList<>();

            DataSnapshot opinionSnapshot = dataSnapshot.child("Opinions");
            if(opinionSnapshot.child(type).hasChild(name)) {
                DataSnapshot comboSnapshot = opinionSnapshot.child(type).child(name);

                Log.d("getComboList", "got type_name");
                for (DataSnapshot playerOpinion: comboSnapshot.getChildren()) {
                    Combo item = new Combo();
                    item.setUserId(playerOpinion.getKey());
                    Log.d("getComboList", "got player: " + playerOpinion.getKey());
                    if (playerOpinion.hasChild("note")) {
                        Log.d("getComboList", "got note");
                        item.setNote((String) playerOpinion.child("note").getValue());
                    }
                    else {
                        item.setNote("No notes");
                    }

                    if (playerOpinion.hasChild("Combos")) {
                        Log.d("getComboList", "got Combos");
                        DataSnapshot playerCombos = playerOpinion.child("Combos");
                        ArrayList<String> comboCards = new ArrayList<>();
                        ArrayList<String> comboRelics = new ArrayList<>();

                        if (playerCombos.hasChild("Cards")) {
                            Log.d("getComboList", "got Cards");
                            DataSnapshot cardComboSnapshot = playerCombos.child("Cards");
                            for (DataSnapshot card: cardComboSnapshot.getChildren()) {
                                comboCards.add(card.getKey());
                            }
                        }

                        if (playerCombos.hasChild("Relics")) {
                            Log.d("getComboList", "got Relics");
                            DataSnapshot relicComboSnapshot = playerCombos.child("Relics");
                            for (DataSnapshot relic: relicComboSnapshot.getChildren()) {
                                comboRelics.add(relic.getKey());
                            }
                        }

                        item.setComboCards(comboCards);
                        item.setComboRelics(comboRelics);
                    }
                    combos.add(item);
                }
            }

            listActivity.gotComboList(combos);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
