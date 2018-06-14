package e.dante.sts.Keyword;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import e.dante.sts.R;

public class KeywordsFragment extends Fragment implements KeywordHelper.Callback{
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_keywords, container, false);

        new KeywordHelper().getKeywords(this);

        return myView;
    }

    @Override
    public void gotKeywords(HashMap<String, String> dataChild) {
        ArrayList<String> dataHeaders = new ArrayList<>(dataChild.keySet());

        ExpandableListView expListView = myView.findViewById(R.id.keywords_list_view);

        ExpandableListAdapter listAdapter = new KeywordListAdapter(getContext(), dataHeaders, dataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void gotKeywordsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
