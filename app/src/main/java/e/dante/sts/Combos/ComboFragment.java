package e.dante.sts.Combos;


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

import e.dante.sts.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComboFragment extends DialogFragment implements ComboHelper.ListCallback{
    private String name;
    private List<String> comboCards;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private String type;

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
        type = args.getString("type");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myView = inflater.inflate(R.layout.fragment_combo, container, false);

        new ComboHelper().getComboList(this, name, type);
        return myView;
    }

    @Override
    public void gotComboList(ArrayList<Combo> combos) {

    }
}
