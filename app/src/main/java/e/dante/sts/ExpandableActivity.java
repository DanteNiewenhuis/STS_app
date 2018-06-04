package e.dante.sts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableActivity extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> dataHeaders;
    HashMap<String, String> dataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);

        Intent intent = getIntent();
        String type = (String) intent.getSerializableExtra("type");

        dataChild = new DatabaseHelper().get_expendable_data(type);
        dataHeaders = getHeaders(dataChild);

        expListView = findViewById(R.id.exp_list_view);

        listAdapter = new myExpandableListAdapter(this, dataHeaders, dataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private List<String> getHeaders(HashMap<String, String> dataChild) {
        List <String> result = new ArrayList<>();
        for (String key : dataChild.keySet()) {
            result.add(key);
        }

        return result;
    }
}
