package e.dante.sts;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputFragment extends DialogFragment {
    private String name;
    private String oldNote;
    private String type;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    public InputFragment() {
        // Required empty public constructor
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
        oldNote = args.getString("oldNote");
        type = args.getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myView =  inflater.inflate(R.layout.fragment_input, container, false);
        TextView textView = myView.findViewById(R.id.note_input_view);
        textView.setText(oldNote);
        myView.findViewById(R.id.note_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = myView.findViewById(R.id.note_input_view);
                String input = textView.getText().toString();
                mDatabase.child(type).child(name).child("notes").child(mUser.getUid())
                        .setValue(input);
                dismiss();
            }
        });
        return myView;
    }

}
