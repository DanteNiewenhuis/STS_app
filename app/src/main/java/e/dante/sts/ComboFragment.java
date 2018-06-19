package e.dante.sts;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComboFragment extends DialogFragment {
    private String name;
    private List<String> comboCards;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private String type1;
    private String type2;

    public ComboFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
//        comboCards = (List<String>) args.getSerializable("comboCards");
        comboCards = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        type1 = "Cards";
        type2 = "Cards";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myView = inflater.inflate(R.layout.fragment_combo, container, false);

        //get the spinner from the xml.
        final Spinner dropdown = myView.findViewById(R.id.combo_options_dropdown);
        //create a list of items for the spinner.
        ArrayList<String> items = new ArrayList<>();
        items.add("Bash");
        items.add("Anger");
        items.add("Barricade");
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        myView.findViewById(R.id.combo_option_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner dropdown = myView.findViewById(R.id.combo_options_dropdown);
                String selected = (String) dropdown.getSelectedItem();

                Log.d("ComboFragment", "combocards: " + selected);
                if (!comboCards.contains(selected)) {
                    mDatabase.child("Combos").child(type1).child(name).child(type2).child(mUser.getUid()).child(selected).setValue(1);
                    mDatabase.child("Combos").child(type2).child(selected).child(type1).child(mUser.getUid()).child(name).setValue(1);
                    dismiss();
                }
            }
        });
        return myView;
    }

}
