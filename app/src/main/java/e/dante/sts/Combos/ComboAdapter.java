package e.dante.sts.Combos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import e.dante.sts.R;

public class ComboAdapter extends ArrayAdapter<String> {
    private int resource;
    private List<String> combos;
    private String type1;
    private String type2;
    private String action;
    private String name;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    public ComboAdapter(@NonNull Context context, int resource, @NonNull List<String> objects,
                        String name, String type1, String type2, String action) {
        super(context, resource, objects);
        this.resource = resource;
        this.combos = objects;
        this.type1 = type1;
        this.type2 = type2;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.action = action;
        this.name = name;
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        final String comboName = combos.get(position);

        TextView nameView = convertView.findViewById(R.id.combo_name_view);
        nameView.setText(comboName);

        //TODO make this clickable
//        nameView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gFunctions.getInfo(comboName, type);
//            }
//        });

        convertView.findViewById(R.id.combo_delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("comboadapter", "X clicked");
                Log.d("comboadapter", "name: " + name);
                Log.d("comboadapter", "comboname: " + comboName);
                Log.d("comboadapter", "type1: " + type1);
                Log.d("comboadapter", "type2: " + type2);
                Log.d("comboadapter", "uid: " + mUser.getUid());
                Log.d("comboadapter", "action: " + action);
                mDatabase.child("Opinions").child(type1).child(name).child(mUser.getUid())
                        .child(action).child(type2).child(comboName).setValue(null);
                mDatabase.child("Opinions").child(type2).child(comboName).child(mUser.getUid())
                        .child(action).child(type1).child(name).setValue(null);
                mDatabase.child("Opinions").child("Cards").child("A Thousand Cuts").child(mUser.getUid())
                        .child("Combos").child("Cards").child("Bandage Up").setValue(null);
                mDatabase.child("Opinions").child("Cards").child("Bandage Up").child(mUser.getUid())
                        .child("Combos").child("Cards").child("A Thousand Cuts").setValue(null);
            }
        });
        return convertView;
    }
}
