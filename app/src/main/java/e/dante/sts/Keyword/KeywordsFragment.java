package e.dante.sts.Keyword;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

import e.dante.sts.R;

public class KeywordsFragment extends Fragment implements KeywordHelper.Callback {
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_keywords, container, false);

        new KeywordHelper().getKeywords(this);

        return myView;
    }

    @Override
    public void gotKeywords(ArrayList<Keyword> dataChild) {
        ExpandableListView expListView = myView.findViewById(R.id.keywords_list_view);

        ExpandableListAdapter listAdapter = new KeywordListAdapter(getContext(), dataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void gotKeywordsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
