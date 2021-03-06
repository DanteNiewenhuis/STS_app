package e.dante.sts.Opinions;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import e.dante.sts.Global.GlobalFunctions;
import e.dante.sts.R;

/*
    create a list of combo cards/relics
 */

public class OpinionAdapter extends ArrayAdapter<String> {
    private int resource;
    private List<String> combos;
    private String type1;
    private String type2;
    private String action;
    private String name;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private GlobalFunctions gFunctions;

    public OpinionAdapter(@NonNull Context context, int resource, @NonNull List<String> objects,
                          String name, String type1, String type2, String action, FragmentManager fragmentManager) {
        super(context, resource, objects);
        this.resource = resource;
        this.combos = objects;
        this.type1 = type1;
        this.type2 = type2;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.action = action;
        this.name = name;
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
        this.gFunctions = new GlobalFunctions(fragmentManager);

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

        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gFunctions.getInfo(comboName, type2);
            }
        });

        // delete the combo/anticombo from the database if the button is clicked
        convertView.findViewById(R.id.combo_delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Opinions").child(type1).child(name).child(mUser.getUid())
                        .child(action).child(type2).child(comboName).setValue(null);
                mDatabase.child("Opinions").child(type2).child(comboName).child(mUser.getUid())
                        .child(action).child(type1).child(name).setValue(null);
            }
        });
        return convertView;
    }
}
