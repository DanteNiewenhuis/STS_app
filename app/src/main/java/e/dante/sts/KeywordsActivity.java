package e.dante.sts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class KeywordsActivity extends AppCompatActivity implements KeywordHelper.Callback{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        new KeywordHelper().getKeywords(this);

    }

    @Override
    public void gotKeywords(HashMap<String, String> dataChild) {
        ArrayList<String> dataHeaders = new ArrayList<>(dataChild.keySet());

        ExpandableListView expListView = findViewById(R.id.keywords_list_view);

        ExpandableListAdapter listAdapter = new KeywordListAdapter(this, dataHeaders, dataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void gotKeywordsError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
