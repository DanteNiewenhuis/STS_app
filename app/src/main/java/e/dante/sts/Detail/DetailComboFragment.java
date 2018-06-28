package e.dante.sts.Detail;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import e.dante.sts.MainActivity;
import e.dante.sts.Opinions.OpinionListAdapter;
import e.dante.sts.Opinions.Opinion;
import e.dante.sts.Opinions.OpinionHelper;
import e.dante.sts.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailComboFragment extends DialogFragment implements OpinionHelper.Callback {
    private String name;
    private String type;
    private View myView;

    public DetailComboFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
        type = args.getString("type");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_combo, container, false);

        new OpinionHelper().getOpinions(this, name, type);
        return myView;
    }

    @Override
    // create the opinions expendable list from the gotten list of opinions
    public void gotOpinions(ArrayList<Opinion> opinions) {
        ExpandableListView listView = myView.findViewById(R.id.combo_exp_list_view);

        ExpandableListAdapter listAdapter = new OpinionListAdapter(getContext(), opinions);

        listView.setAdapter(listAdapter);

    }

    @Override
    public void gotOpinionsError(String message) {
        Toast.makeText(this.getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
